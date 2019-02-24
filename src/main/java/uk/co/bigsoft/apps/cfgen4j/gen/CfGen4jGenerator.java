package uk.co.bigsoft.apps.cfgen4j.gen;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeSet;

import javax.lang.model.element.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.contentful.java.cma.model.CMAArray;
import com.contentful.java.cma.model.CMAContentType;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import uk.co.bigsoft.apps.cfgen4j.cf.CfCdaType;
import uk.co.bigsoft.apps.cfgen4j.cf.ContentfulService;
import uk.co.bigsoft.apps.cfgen4j.cf.ContentfulServiceFactory;
import uk.co.bigsoft.apps.cfgen4j.cfg.Cfg;
import uk.co.bigsoft.apps.cfgen4j.gen.files.CfGen4jFileGenerator;
import uk.co.bigsoft.apps.cfgen4j.gen.files.FileGenerators;
import uk.co.bigsoft.apps.cfgen4j.gen.ref.JavaClassInfo;
import uk.co.bigsoft.apps.cfgen4j.gen.ref.Refs;

public class CfGen4jGenerator implements Runnable {

	private static Logger L = LoggerFactory.getLogger(CfGen4jGenerator.class);

	private JavaFileFactory javaFileFactory = new JavaFileFactory();
	private PoetFactory poetFactory = new PoetFactory();
	private FileGenerators fileGenerators = new FileGenerators();
	private ContentfulServiceFactory contentfulServiceFactory = new ContentfulServiceFactory();
	private JavaFileHandler fileHandler = new JavaFileHandler();
	private Cfg cfg = null;

	public CfGen4jGenerator(Cfg cfg) {
		this.cfg = cfg;
	}

	@Override
	public void run() {
		L.info("========================================");
		L.info("Date               : {}", new Date().toString());
		L.info("Space Id           : {}", cfg.getSpaceId());
		L.info("Package            : {}", cfg.getPackageName());
		L.info("cfg.getFolder()    : {}", cfg.getFolder());
		L.info("Token              : {}", cfg.getAccessToken());
		L.info("Config");
		L.info("   add-settings    : {}", cfg.getAddSetters());
		L.info("   use-date        : {}", cfg.getUseDateType());
		L.info("   cache-file      : {}", cfg.getCacheFile());
		L.info("   core-endpoint   : {}", cfg.getCoreEndpoint());
		L.info("   upload-endpoint : {}", cfg.getUploadEndpoint());
		L.info("----------------------------------------");

		ContentfulService contentfulService = contentfulServiceFactory.create(cfg);

		long start = System.currentTimeMillis();
		generate(contentfulService);
		long end = System.currentTimeMillis();

		L.info("Duration : {} ms", end - start);

	}

	/**
	 * Fetch content types from the given space and generate corresponding model
	 * classes.
	 *
	 * @param        cfg.getSpaceId() space id
	 * @param pkg    package name for generated classes
	 * @param path   package source root
	 * @param client management api client instance
	 */
	private void generate(ContentfulService client) {

		try {

			L.info("Loading content types");
			CMAArray<CMAContentType> contentTypes = client.fetchAllContentTypes(cfg.getSpaceId());

			L.info("Set up forward references");
			Refs refs = new Refs(cfg.getPackageName());
			refs.addForwardReferences(contentTypes);

			// <<cfId, Class Builder>>
			HashMap<String, Builder> allBuilders = new HashMap<>();

			BetterGenerator generator = new BetterGenerator(cfg, refs);

			// Create classes
			L.info("Creating class builders");
			for (CMAContentType contentType : contentTypes.getItems()) {
				String id = contentType.getId();
				L.debug("    {}", id);
				Builder classBuilder = generator.create(contentType);
				allBuilders.put(id, classBuilder);
			}

			InterfaceMarkers markers = generator.getInterfaceMarkers();

			L.info("Creating factory discriminators");
			SupportClass supportClass = new SupportClass(refs);
			supportClass.createMethodCfId2JavaClass();
			supportClass.createMethodGetIds();

			for (String interfaceName : markers.getInterfaceNames()) {
				ClassName className = ClassName.get(cfg.getPackageName(), interfaceName);
				supportClass.createMethodInterfaceDescriminator(className);
			}
			supportClass.save(fileHandler, cfg.getFolder(), cfg.getPackageName());

			// Write interface files
			L.info("Writing interfaces");
			for (String interfaceName : markers.getInterfaceNames()) {
				Builder builder = TypeSpec.interfaceBuilder(interfaceName);
				TypeSpec.interfaceBuilder(interfaceName).addModifiers(Modifier.PUBLIC);
				JavaFile javaFile = javaFileFactory.create(cfg.getPackageName(), builder.build());
				L.debug("    {}", interfaceName);
				fileHandler.write(javaFile, cfg.getFolder());
			}

			// Add interfaces to classes
			for (Entry<String, TreeSet<String>> mappings : markers.getClassInterfaces().entrySet()) {
				String cfId = mappings.getKey();
				TreeSet<String> interfaceNames = mappings.getValue();

				Builder builder = allBuilders.get(cfId);
				for (String interfaceName : interfaceNames) {
					ClassName className = ClassName.get(cfg.getPackageName(), interfaceName);
					builder.addSuperinterface(className);
				}
			}

			// Add constructor bodies
			L.info("Adding constructors");
			for (Entry<String, Builder> entry : allBuilders.entrySet()) {
				JavaClassInfo info = refs.get(entry.getKey());
				Builder classBuilder = entry.getValue();

				MethodSpec constructor = poetFactory.constructor(info.getConstructorBody(), CfCdaType.CDAEntry,
						"entry");
				classBuilder.addMethod(constructor);
			}

			// Write classes
			L.info("Writing classes");
			for (Entry<String, Builder> entry : allBuilders.entrySet()) {
				JavaClassInfo info = refs.get(entry.getKey());
				Builder builder = entry.getValue();

				JavaFile javaFile = javaFileFactory.create(cfg.getPackageName(), builder.build());
				L.debug("    {}", info.getClassName());
				fileHandler.write(javaFile, cfg.getFolder());
			}

			// Create support classes
			L.info("Copying support classes:");

			List<CfGen4jFileGenerator> fileGens = fileGenerators.getFileGenerators();
			for (CfGen4jFileGenerator fileGen : fileGens) {
				String name = fileGen.getClass().getSimpleName().replaceAll("Generator", "");
				L.debug("    {}", name);
				fileGen.save(fileHandler, cfg.getPackageName(), cfg.getFolder());
			}

			L.info("Done :)");

		} catch (Exception e) {
			e.printStackTrace();
			L.error("Failed :(");
		}
	}

}

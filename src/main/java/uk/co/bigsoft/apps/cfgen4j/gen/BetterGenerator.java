package uk.co.bigsoft.apps.cfgen4j.gen;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.lang.model.element.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.contentful.java.cma.Constants.CMAFieldType;
import com.contentful.java.cma.model.CMAContentType;
import com.contentful.java.cma.model.CMAField;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import uk.co.bigsoft.apps.cfgen4j.cf.CfCdaType;
import uk.co.bigsoft.apps.cfgen4j.cfg.Cfg;
import uk.co.bigsoft.apps.cfgen4j.exceptions.CfGen4jException;
import uk.co.bigsoft.apps.cfgen4j.gen.ref.JavaClassInfo;
import uk.co.bigsoft.apps.cfgen4j.gen.ref.Refs;

class BetterGenerator {

	private static Logger L = LoggerFactory.getLogger(BetterGenerator.class);

	// TODO remove CF_ASSET_CAST
	private static final String CF_ASSET_CAST = "(com.contentful.java.cda.CDAAsset)";
	private static final ClassName UNKNOWN_TYPE = ClassName.get(Object.class);

	private PoetFactory poetFactory = new PoetFactory();
	private NameConverter nameConverter = new NameConverter();

	// new java interface name, list of cf contentType ids
	private InterfaceMarkers interfaceMarkers = new InterfaceMarkers();

	private Refs refs;
	private Cfg cfg;

	BetterGenerator(Cfg cfg, Refs refs) {
		this.cfg = cfg;
		this.refs = refs;
	}

	TypeSpec.Builder create(CMAContentType contentType) {
		JavaClassInfo info = refs.get(contentType.getId());
		ClassName interfaceName = ClassName.get(refs.getPackageName(), "CfGen4jType");
		TypeSpec.Builder classBuilder = TypeSpec //
				.classBuilder(info.getClassName()) //
				.addSuperinterface(interfaceName) //
				.addModifiers(Modifier.PUBLIC);

		for (CMAField cfField : contentType.getFields()) {
			if (cfField.isDisabled() || cfField.isOmitted()) {
				L.debug("Skipping: {}", cfField.getId());
				continue;
			}

			FieldSpec fieldSpec = createFieldSpec(info, cfField);

			classBuilder //
					.addField(fieldSpec) //
					.addMethod(poetFactory.fieldGetter(fieldSpec));

			if (cfg.getAddSetters()) {
				classBuilder.addMethod(poetFactory.fieldSetter(fieldSpec));
			}
		}

		return classBuilder;
	}

	InterfaceMarkers getInterfaceMarkers() {
		return interfaceMarkers;
	}

	FieldSpec createFieldSpec(JavaClassInfo info, CMAField cfField) {
		String fieldName = nameConverter.generateJavaFieldName(cfField.getId());
		CMAFieldType fieldType = cfField.getType();

		L.debug("   {}.{} -> {}.{}", info.getId(), cfField.getId(), info.getClassName(), fieldName);

		if (fieldType == CMAFieldType.Array) {
			return createArrayFieldSpec(info, cfField, fieldName);
		}

		if (fieldType == CMAFieldType.Link) {
			return createLinkFieldSpec(info, cfField, fieldName);

		}

		return createPrimitive(fieldType, info, cfField, fieldName);
	}

	FieldSpec createPrimitive(CMAFieldType fieldType, JavaClassInfo info, CMAField cfField, String javaFieldName) {

		String entryGetField = "entry.getField(\"" + cfField.getId() + "\")";
		CodeBlock block = CodeBlock.builder() //
				.addStatement("this." + javaFieldName + " = " + entryGetField) //
				.build();
		info.addConstructorBody(block);
		return poetFactory.fieldBuilder(ClassName.get(classForFieldType(fieldType)), javaFieldName);

	}

	FieldSpec createArrayFieldSpec(JavaClassInfo info, CMAField cfField, String javaFieldName) {

		Map<?, ?> arrayItems = cfField.getArrayItems();
		String cfFieldName = cfField.getId();
		ClassName typeName = UNKNOWN_TYPE;

		if ("Link".equals(arrayItems.get("type"))) {

			String linkType = (String) arrayItems.get("linkType");
			String entryGetField = "entry.getField(\"" + cfFieldName + "\")";
			String newText = "";

			L.trace("arrayItems.get('linkType')={} cfField.getLinkType()={}", linkType, cfField.getLinkType());

			if ("Asset".equals(linkType)) {

				typeName = ClassName.get(refs.getPackageName(), "CfGen4jAsset");
				newText = "new CfGen4jAsset(" + CF_ASSET_CAST + entryGetField + ")";

			} else {
				if ("Entry".equals(linkType)) {

					// no inspection unchecked
					TreeSet<String> linkContentType = extractLinkedContentTypes(arrayItems.get("validations"));

					if (linkContentType.size() == 1) {
						String id = linkContentType.first();
						JavaClassInfo fieldType = refs.get(id);
						newText = "new " + fieldType.getClassName() + "(single)";
					}
					if (linkContentType.size() != 0) {
						String interfaceName = info.getClassName() + "_" + javaFieldName;

						interfaceMarkers.add(interfaceName, linkContentType);
						// This field can be lots of types so create interface marker
						typeName = ClassName.get(refs.getPackageName(), interfaceName);
						newText = "CfGen4jUtils.load_" + interfaceName + "(single)";
					}
				} else {
					throw new CfGen4jException("Contentful inconsistant: Unknown array link type: " + info.getId() + "."
							+ javaFieldName + " : " + linkType);
				}
			}

			CodeBlock body = CodeBlock.builder() //
					.beginControlFlow("if (" + entryGetField + " != null)") //
					.addStatement("this." + javaFieldName + " = new $T<>()", ArrayList.class)
					.addStatement("List<$T> array = " + entryGetField, CfCdaType.CDAEntry) //
					.beginControlFlow("for ($T single : array)", CfCdaType.CDAEntry) //
					.addStatement("this." + javaFieldName + ".add(" + newText + ")") //
					.endControlFlow() //
					.endControlFlow() //
					.build();

			info.addConstructorBody(body);
			return poetFactory.fieldBuilder(poetFactory.parameterizedList(typeName), javaFieldName);
		}

		return poetFactory.fieldBuilder(ClassName.get(List.class), javaFieldName);
	}

	FieldSpec createLinkFieldSpec(JavaClassInfo info, CMAField cfField, String javaFieldName) {

		String cfFieldName = cfField.getId();
		TypeName typeName = UNKNOWN_TYPE;
		String entryGetField = "entry.getField(\"" + cfFieldName + "\")";
		CodeBlock line;

		if ("Asset".equals(cfField.getLinkType())) {

			typeName = ClassName.get(refs.getPackageName(), "CfGen4jAsset");

			line = CodeBlock.builder() //
					.addStatement("this." + javaFieldName + " = new CfGen4jAsset(($T) " + entryGetField + ")",
							CfCdaType.CDAAsset) //
					.build();

		} else {
			if ("Entry".equals(cfField.getLinkType())) {

				TreeSet<String> linkContentType = extractLinkedContentTypes(cfField.getValidations());

				if (linkContentType.size() != 1) {
					throw new CfGen4jException("Contentful inconsistant: should be only one link type on an entry: "
							+ info.getId() + "." + javaFieldName + " : " + cfField.getLinkType());
				}
				String id = linkContentType.first();
				JavaClassInfo fieldInfo = refs.get(id);

				typeName = ClassName.get(refs.getPackageName(), fieldInfo.getClassName());
				line = CodeBlock.builder().addStatement(
						"this." + javaFieldName + " = new " + fieldInfo.getClassName() + "(($T) " + entryGetField + ")",
						CfCdaType.CDAEntry).build();

			} else {
				throw new CfGen4jException("Contentful inconsistant: Unknown field link type: " + info.getId() + "."
						+ javaFieldName + " : " + cfField.getLinkType());
			}

		}

		CodeBlock body = CodeBlock.builder() //
				.beginControlFlow("if (" + entryGetField + " != null)") //
				.add(line) //
				.endControlFlow().build();

		info.addConstructorBody(body);
		return poetFactory.fieldBuilder(typeName, javaFieldName);
	}

	@SuppressWarnings("unchecked")
	TreeSet<String> extractLinkedContentTypes(Object validationsObject) {
		List<Map<String, List<String>>> validations = (List<Map<String, List<String>>>) validationsObject;
		TreeSet<String> set = new TreeSet<>();
		if (validations != null) {
			for (Map<String, List<String>> validation : validations) {
				List<String> linkContentTypes = validation.get("linkContentType");
				if (linkContentTypes == null) {
					continue;
				}
				set.addAll(linkContentTypes);
				break;
			}
		}
		return set;
	}

	Class<?> classForFieldType(CMAFieldType fieldType) {
		switch (fieldType) {
		case Boolean:
			return Boolean.class;
		case Date:
			if (cfg.getUseDateType()) {
				// "2015-11-06T09:45:27"
				// ISO 8601
				return Date.class;
			} else {
				return String.class;
			}
		case Integer:
			return Integer.class;
		case Location:
			return Map.class;
		case Number:
			return Double.class;
		case Object:
			return Map.class;
		case Symbol:
			return String.class;
		case Text:
		default:
			return String.class;
		}
	}

}

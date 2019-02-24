package uk.co.bigsoft.apps.cfgen4j.args;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import uk.co.bigsoft.apps.cfgen4j.cfg.Cfg;

public class ArgParser {
	private static final String SPACE_ID = "spaceid";
	private static final String PACKAGE = "package";
	private static final String FOLDER = "folder";
	private static final String TOKEN = "token";
	private static final String EP_CORE = "ep-core";
	private static final String EP_UPLOAD = "ep-upload";
	private static final String VERSION = "version";
	private static final String CACHE_FILE = "cache-file";
	// TODO: --debug ?

	public ArgParser() {
		//
	}

	public Cfg getArgs(String[] args) {

		Cfg cfg = new Cfg();

		ArgumentParser parser = ArgumentParsers.newFor("cfgen4j").build().version(cfg.getVersion())
				.defaultHelp(true)
				.description("Inspects Contentful schema and generates Java classes " + cfg.getVersion());
		parser.addArgument("-s", "--" + SPACE_ID).help("Contentful space id").nargs("?").required(true);
		parser.addArgument("-p", "--" + PACKAGE).help("Java package name").nargs("?").required(true);
		parser.addArgument("-f", "--" + FOLDER).help("Path to the package root").nargs("?").required(true);
		parser.addArgument("-t", "--" + TOKEN).help("Contentful app access token").nargs("?").required(true);
		parser.addArgument("-ec", "--" + EP_CORE).help("Change core endpoint").nargs("?");
		parser.addArgument("-eu", "--" + EP_UPLOAD).help("Change upload endpoint").nargs("?");
		parser.addArgument("-cf", "--" + CACHE_FILE).help("File to cache contentful net request").nargs("?");
		parser.addArgument("-v", "--" + VERSION).help("Version").action(Arguments.version());

		try {
			String x;
			Namespace res = parser.parseArgs(args);

			cfg.setSpaceId(res.getString(SPACE_ID));
			cfg.setPackageName(res.getString(PACKAGE));
			cfg.setFolder(res.getString(FOLDER));
			cfg.setAppAccessToken(res.getString(TOKEN));
			cfg.setCacheFile(res.getString(replace(CACHE_FILE)));

			x = res.getString(EP_CORE);
			if (x != null) {
				cfg.setCoreEndpoint(replace(x));
			}
			x = res.getString(EP_UPLOAD);
			if (x != null) {
				cfg.setUploadEndpoint(replace(x));
			}

			return cfg;
		} catch (ArgumentParserException e) {
			parser.handleError(e);
			System.exit(1);
		}
		// Never reached
		return null;
	}

	private String replace(String s) {
		return s.replace('-', '_');
	}

}

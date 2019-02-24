package uk.co.bigsoft.apps.cfgen4j;

import uk.co.bigsoft.apps.cfgen4j.args.ArgParser;
import uk.co.bigsoft.apps.cfgen4j.cfg.Cfg;
import uk.co.bigsoft.apps.cfgen4j.gen.CfGen4jGenerator;

public class Main {

	public static void main(String[] args) {

		Cfg cfg = new ArgParser().getArgs(args);
		CfGen4jGenerator runner = new CfGen4jGenerator(cfg);
		runner.run();
	}
}

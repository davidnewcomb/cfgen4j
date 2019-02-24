package uk.co.bigsoft.apps.cfgen4j;

import uk.co.bigsoft.apps.cfgen4j.cfg.Cfg;
import uk.co.bigsoft.apps.cfgen4j.gen.CfGen4jGenerator;

public class CfGen4jComponent extends Cfg implements Runnable {

	public CfGen4jComponent() {
		//
	}

	@Override
	public void run() {
		new CfGen4jGenerator(this).run();
	}

}

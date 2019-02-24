package uk.co.bigsoft.apps.cfgen4j.args;

import org.junit.Assert;
import org.junit.Test;

import uk.co.bigsoft.apps.cfgen4j.cfg.Cfg;

public class ArgParserTest {

	private ArgParser subject = new ArgParser();

	@Test
	public void minimumCommandLineOptions() {
		Cfg c = subject.getArgs(new String[] { "-s", "_space_", "-p", "_package_", "-t", "_token_", "-f", "_folder_" });
		Assert.assertEquals(c.getSpaceId(), "_space_");
		Assert.assertEquals(c.getPackageName(), "_package_");
		Assert.assertEquals(c.getAccessToken(), "_token_");
		Assert.assertEquals(c.getFolder(), "_folder_");
	}

}

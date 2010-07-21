package tinytools.jsync;

import uk.co.flamingpenguin.jewel.cli.Option;

public interface Options
{
	@Option
	String getMasterDir();
	
	@Option
	String getSlaveDir();
	
	@Option(description = "if provided, only a shell script with the given name is generated")
	String getShellScriptFile();
	boolean isShellScriptFile();
	
	@Option(description = "deletes files on slave that have been deleted on master. thus the two dirs really will be synchronized")
	boolean isDeleteOnSlave();
	
	@Option(helpRequest = true, description = "display help", shortName = "h")
	boolean isHelp();
	
	@Option(description = "does not perform any filesystem changes, only prints the modifications")
	boolean isOnlyListModified();
	
	@Option(description = "be more verbose", shortName = "v")
	boolean isVerbose();
}

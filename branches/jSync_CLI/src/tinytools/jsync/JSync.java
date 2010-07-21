package tinytools.jsync;

import java.io.IOException;

import uk.co.flamingpenguin.jewel.cli.ArgumentValidationException;
import uk.co.flamingpenguin.jewel.cli.CliFactory;

public class JSync
{
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws Exception
	{
		/* parse command line options */
		Options options = null;
		try
		{
			options = CliFactory.parseArguments(Options.class, args);
		}
		catch (ArgumentValidationException ex)
		{
			printHelp(ex);
		}

		FileFinder master = new FileFinder(options.getMasterDir(), options);
		master.indexFiles();

		FileFinder slave = new FileFinder(options.getSlaveDir(), options);
		slave.indexFiles();

		master.compareToSlave(slave);
		
		ISynchronizer synchronizer;
		
		/* should we only generate a shell script? */
		if (options.isShellScriptFile())
		{
			synchronizer = new ShellScriptGenerator();
		}
		else
		{
			synchronizer = new JavaSynchronizer();
		}
		
		synchronizer.setOptions(options);
		synchronizer.setChanges(master.getChanges());
		synchronizer.setMasterDir(master.getPath());
		synchronizer.setSlaveDir(slave.getPath());
		
		synchronizer.synchronize();
	}
	
	private static void printHelp(ArgumentValidationException ex)
	{
		System.err.println(ex.getMessage());
		System.err.flush();
		System.out.println();
		System.out.println("Use --help for help");
		System.out.flush();
		System.exit(1);
	}
}
package tinytools.jsync;

import java.io.File;
import java.io.IOException;
import java.util.List;

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

		if(!options.isDeleteOnSlave())
		{
			if(master.getChanges().deletedFiles != null)
			{
				List<File> deletedFiles = master.getChanges().deletedFiles;
				System.out.println(deletedFiles.size() + " files have been deleted on master. This changes will be ignored. Use --deleteOnSlave to do a full sync.");
				master.getChanges().deletedFiles.clear();
			}
		}
		
		/* should we only generate a shell script? */
		if (options.isShellScriptFile())
		{
			generateShellScript(options, master, slave);
			System.exit(0);
		}
		

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

	private static void generateShellScript(Options options, FileFinder master,
			FileFinder slave) throws Exception
	{
		File shellScriptFile = new File(options.getShellScriptFile());
		System.out
				.println("Shell script with sync commands will be written to: "
						+ shellScriptFile.getAbsolutePath());

		ShellScriptGenerator generator = new ShellScriptGenerator(options);
		generator.setChanges(master.getChanges());
		generator.setMasterDir(master.getPath());
		generator.setSlaveDir(slave.getPath());
		generator.synchronize();
	}
}
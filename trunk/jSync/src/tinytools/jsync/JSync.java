package tinytools.jsync;

import java.io.File;
import java.io.IOException;

public class JSync 
{
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws Exception 
	{
		if(args.length < 2)
		{
			System.out.println("Usage: java -jar jSync.jar <Master-Directory> <Slave-Directory> <shellScriptName>");
			System.exit(0);
		}
		
		File shellScriptFile = new File("runSync.sh");
		if(args.length >= 3)
		{
			shellScriptFile  = new File(args[2]);
		}
		
		FileFinder master = new FileFinder(args[0]);
		master.indexFiles();
		
		FileFinder slave = new FileFinder(args[1]);
		slave.indexFiles();

		master.compareToSlave(slave);
		
		ShellScriptGenerator generator = new ShellScriptGenerator(shellScriptFile);
		generator.setChanges(master.getChanges());
		generator.setMasterDir(master.getPath());
		generator.setSlaveDir(slave.getPath());
		generator.synchronize();
	}
}
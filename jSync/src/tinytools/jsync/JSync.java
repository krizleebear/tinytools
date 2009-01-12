package tinytools.jsync;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class JSync {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException 
	{
		if(args.length < 2)
		{
			System.out.println("Usage: java -jar jSync.jar <Master-Directory> <Slave-Directory>");
			System.exit(0);
		}
		
		FileFinder master = new FileFinder(args[0]);
		master.indexFiles();
		
		FileFinder slave = new FileFinder(args[1]);
		slave.indexFiles();

		master.compareToSlave(slave);
		
		ShellScriptGenerator generator = new ShellScriptGenerator(master.getChanges(), master.getPath(), slave.getPath());
		String shellScript = generator.generate();
		
		PrintWriter pw = new PrintWriter(new File("runSync.sh"));
		pw.print(shellScript);
		pw.flush();
		pw.close();
	}
}
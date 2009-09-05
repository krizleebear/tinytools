package fileCounter;

import java.io.File;

public class Main
{
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		File root = null;

		if (args.length < 1)
		{
			root = new File("./");
		}
		else
		{
			root = new File(args[0]);
		}

		FileCounter fc = new FileCounter(root);
		fc.run();
	}
}

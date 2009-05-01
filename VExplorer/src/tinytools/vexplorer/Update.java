package tinytools.vexplorer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

public class Update
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Update update = new Update();
		try
		{
			update.start();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public int getVersionNumber()
	{
		
		
		return 0;
	}

	public void start() throws IOException
	{
		URL url = new URL("http://tinytools.googlecode.com/svn/");
		URLConnection conn = url.openConnection();
		//conn.setDoOutput(true);
		
		OutputStream out = conn.getOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));

		String request = "<?xml version=\"1.0\" encoding=\"utf-8\"?><propfind xmlns=\"DAV:\"><prop><version-controlled-configuration xmlns=\"DAV:\"/><resourcetype xmlns=\"DAV:\"/><baseline-relative-path xmlns=\"http://subversion.tigris.org/xmlns/dav/\"/><repository-uuid xmlns=\"http://subversion.tigris.org/xmlns/dav/\"/></prop></propfind>";

		writer.write(request);
		writer.close();

		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

		String decodedString;

		while ((decodedString = in.readLine()) != null)
		{
			System.out.println(decodedString);
		}
		in.close();
	}

}

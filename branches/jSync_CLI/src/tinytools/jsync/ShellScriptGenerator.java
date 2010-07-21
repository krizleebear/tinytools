package tinytools.jsync;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;

public class ShellScriptGenerator extends SynchronizerBase 
{
	private File shellScriptFile;
	private StringBuilder sb = new StringBuilder();
	private HashSet<String> createdDirectories = new HashSet<String>();
	
	public ShellScriptGenerator()
	{
		this.shellScriptFile = new File(options.getShellScriptFile());
	}
	
	@Override
	protected void pre() throws Exception
	{
		sb = new StringBuilder();
		appendLine("#!/bin/sh");
	}
	
	@Override
	protected void post() throws Exception
	{
		PrintWriter pw = new PrintWriter(shellScriptFile);
		pw.print(sb.toString());
		pw.flush();
		pw.close();
		
		sb = new StringBuilder();
	}
	
	@Override
	protected void handleAddedFiles(List<File> added) throws Exception
	{
		for(File f : changes.addedFiles)
		{
			File destFile = new File(slaveDir, f.getAbsolutePath().substring(masterDir.getAbsolutePath().length()));
			
			checkPathTo(sb, destFile);
			
			sb.append("cp -pv \"");
			sb.append(escape(f.getAbsolutePath()));
			sb.append("\" \"");
			
			sb.append(escape(destFile.getAbsolutePath()));
			
			sb.append("\"\n");
		}
	}

	@Override
	protected void handleDeletedFiles(List<File> deleted) throws Exception
	{
		for(File f : changes.deletedFiles)
		{
			sb.append("rm \"");
			sb.append(escape(f.getAbsolutePath()));
			sb.append("\"\n");
		}
	}

	@Override
	protected void handleModifiedFiles(List<File> modified) throws Exception
	{
		for(File f : changes.modifiedFiles)
		{
			File destFile = new File(slaveDir, f.getAbsolutePath().substring(masterDir.getAbsolutePath().length()));
			
			checkPathTo(sb, destFile);
			
			sb.append("cp -pv \"");
			sb.append(escape(f.getAbsolutePath()));
			sb.append("\" \"");
			
			sb.append(escape(destFile.getAbsolutePath()));
			
			sb.append("\"\n");
		}
	}

	private void appendLine(String msg)
	{
		sb.append(msg);
		sb.append("\n");
	}
	
	private String escape(String path)
	{
		return path.replace("`", "\\`").replace("\"", "\\\"");
	}
	
	private void checkPathTo(StringBuilder sb, File destFile) 
	{
		File destParent = destFile.getParentFile();
		String destPath = destParent.getAbsolutePath();

		if(destParent.exists()) //dest path already exists
			return;
		
		if(createdDirectories.contains(destPath)) //path was already created by our script
			return;
		
		sb.append("mkdir -p \"");
		sb.append(destPath);
		sb.append("\"\n");
		
		createdDirectories.add(destPath); //remember created path
	}
}

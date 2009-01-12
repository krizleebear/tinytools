package tinytools.jsync;

import java.io.File;
import java.util.HashSet;
import java.util.Hashtable;

public class ShellScriptGenerator {

	private ChangeSet changes;
	File masterDir, slaveDir;
	
	public ShellScriptGenerator(ChangeSet changes, File masterDir, File slaveDir)
	{
		this.masterDir = masterDir;
		this.slaveDir = slaveDir;
		this.changes = changes;
	}
	
	public String generate()
	{
		StringBuilder sb = new StringBuilder();
		
		for(File f : changes.addedFiles)
		{
			appendCopy(sb, f);
		}
		
		for(File f : changes.deletedFiles)
		{
			appendDelete(sb, f);
		}
		
		for(File f : changes.modifiedFiles)
		{
			appendCopy(sb, f);
		}
		
//		for(File f : changes.equalFiles)
//		{
//			
//		}
		
		return sb.toString();
	}
	
	private void appendCopy(StringBuilder sb, File srcFile)
	{
		File destFile = new File(slaveDir, srcFile.getAbsolutePath().substring(masterDir.getAbsolutePath().length()));
		
		checkPathTo(sb, destFile);
		
		sb.append("cp -pv \"");
		sb.append(escape(srcFile.getAbsolutePath()));
		sb.append("\" \"");
		
		sb.append(escape(destFile.getAbsolutePath()));
		
		sb.append("\"\n");
	}
	
	private String escape(String path)
	{
		return path.replace("`", "\\`").replace("\"", "\\\"");
	}
	
	HashSet<String> createdDirectories = new HashSet<String>();
	
	private void checkPathTo(StringBuilder sb, File destFile) 
	{
		File destParent = destFile.getParentFile();
		String destPath = destParent.getAbsolutePath();

		if(destParent.exists()) //Zielpfad existiert bereits
			return;
		
		if(createdDirectories.contains(destPath)) //Pfad wird von uns bereits im Shellskript angelegt
			return;
		
		sb.append("mkdir -p \"");
		sb.append(destPath);
		sb.append("\"\n");
		
		createdDirectories.add(destPath); //Verzeichnis merken
	}

	private void appendDelete(StringBuilder sb, File f)
	{
		sb.append("rm \"");
		sb.append(escape(f.getAbsolutePath()));
		sb.append("\"\n");
	}
}

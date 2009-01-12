package tinytools.jsync;

import java.io.File;

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
	
	private void appendCopy(StringBuilder sb, File f)
	{
		sb.append("cp -p \"");
		sb.append(f.getAbsolutePath());
		sb.append("\" \"");
		
		sb.append(slaveDir.getAbsolutePath());
		sb.append(f.getAbsolutePath().substring(masterDir.getAbsolutePath().length()));
		
		sb.append("\"\n");
	}
	
	private void appendDelete(StringBuilder sb, File f)
	{
		sb.append("rm \"");
		sb.append(f.getAbsolutePath());
		sb.append("\"\n");
	}
}

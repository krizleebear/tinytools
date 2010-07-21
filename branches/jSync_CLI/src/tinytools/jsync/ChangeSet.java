package tinytools.jsync;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChangeSet {

	public List<File> deletedFiles = new ArrayList<File>();
	public List<File> addedFiles = new ArrayList<File>();
	public List<File> modifiedFiles = new ArrayList<File>();
	public List<File> equalFiles = new ArrayList<File>();
	
}

package eu.seelenoede.getyourfilesoutthere;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

public class SettingsFilter implements FileFilter {
	
	ArrayList<String> extensions;

	SettingsFilter() {
		extensions = new ArrayList<String>();
	}
	
	@Override
	public boolean accept(File pathname) {
		String filePath = pathname.getAbsolutePath();
		String fileExtension = filePath.substring(filePath.lastIndexOf('.') + 1);
		
		if(extensions.contains(fileExtension))
			return true;
		return false;
	}

}

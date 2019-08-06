package eu.seelenoede.getyourfilesoutthere;

import java.io.File;
import java.io.FileFilter;

public class SettingsFilter implements FileFilter {

	@Override
	public boolean accept(File pathname) {
		String filePath = pathname.getAbsolutePath();
		String fileExtension = filePath.substring(filePath.lastIndexOf('.') + 1);
		
		if(MainForm.extensions.contains(fileExtension))
			return true;
		return false;
	}

}

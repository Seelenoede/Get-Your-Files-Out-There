package eu.seelenoede.getyourfilesoutthere;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Engine {
	
	private SettingsFilter filter;
	
	public Engine() {
		
	}
	
	/**
	 * Extract files from the directories
	 * 
	 * @author Seelenoede
	 */
	protected void extractFiles(File[] dirs)	{

		for(File dir:dirs) {
			try {
				String parentDir = dir.getCanonicalFile().getParent();

				File[] files;
				if(filter.extensions.size() == 1 && filter.extensions.get(0).equals(""))
					files = dir.listFiles();
				else				
					files = dir.listFiles(filter);
				
				for(File file:files) {
					Path target = FileSystems.getDefault().getPath(parentDir + "\\" + file.getName());

					Files.move(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
				}
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected void setExtensionSettings(String extensionString) {
		String[] extensions = extensionString.split(",");
		if (extensions.length == 0)
			return;
		for(String extension:extensions) {
			filter.extensions.add(extension.trim());
		}
	}
}

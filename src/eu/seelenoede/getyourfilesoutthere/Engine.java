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
		filter = new SettingsFilter();
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
				if(filter.extensions.size() == 0)
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
		if(extensionString.equals("")) {
			return;
		}
		String[] extensions = extensionString.split(",");
		for(String extension:extensions) {
			filter.extensions.add(extension.trim());
		}
	}
}

package eu.seelenoede.getyourfilesoutthere;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Main extends Application {
	
	private boolean alreadyRunning;
	private File[] dirs;
	private Engine engine;
	
	@FXML Label lblDone;
	@FXML TextField textField_dirs;
	@FXML TextField textField_extensions;

	@FXML
	public void initialize()  {
		engine = new Engine();
		alreadyRunning = false;
	}
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
		
		Scene scene = new Scene(root);
		primaryStage.setTitle("Get Your Files Out There");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	@FXML
	private void handleBrowseButtonAction(ActionEvent event) {
		selectDirs();
	}
	
	@FXML
	private void handleStartButtonAction(ActionEvent event) {
		Thread noBlock = new Thread() {
			public void run() {
				engine.setExtensionSettings(textField_extensions.getText());
				resetElements();
				engine.extractFiles(dirs);
				alreadyRunning = false;
				lblDone.setVisible(true);
			}
		};
		if ((!alreadyRunning) & (dirs!=null)) {
			noBlock.start();
			alreadyRunning = true;
		}	
	}
	
	private void resetElements() {
		lblDone.setVisible(false);
	}
	
	private void selectDirs() {
		if(alreadyRunning){
			return;
		}
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setMultiSelectionEnabled(true);
		int val = chooser.showOpenDialog(null);

		if (val == JFileChooser.APPROVE_OPTION) {
			dirs = chooser.getSelectedFiles();
			String selectedDirs = "";
			for (File dir:dirs)
			{
				selectedDirs = selectedDirs + "\"" + dir.getAbsolutePath() + "\"; ";
			}
			textField_dirs.setText(selectedDirs);
		}
	}
	
	
}

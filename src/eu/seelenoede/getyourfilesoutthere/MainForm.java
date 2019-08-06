package eu.seelenoede.getyourfilesoutthere;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTextField;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

import javax.swing.JFileChooser;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;

public class MainForm {

	private JFrame frmGetYourFiles;
	private JTextField textField;
	private JLabel lblPath;
	private boolean alreadyRunning;
	private JPanel panel_progress;
	private JButton btnBrowse;
	private File[] dirs;
	private JLabel lblDone;
	static ArrayList<String> extensions;
	private SettingsFilter filter;
	private JPanel panel;
	private JPanel panel_settings;
	private JLabel lblSettings;
	private JTextField textField_extensions;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					MainForm window = new MainForm();
					window.frmGetYourFiles.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainForm() {
		filter = new SettingsFilter();
		extensions = new ArrayList<String>();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		alreadyRunning = false;
		frmGetYourFiles = new JFrame("Get Your Files Out There");
		frmGetYourFiles.setTitle("Get Your Files Out There");
		frmGetYourFiles.getContentPane().setBackground(Color.LIGHT_GRAY);
		frmGetYourFiles.setResizable(false);
		frmGetYourFiles.setBackground(Color.WHITE);
		frmGetYourFiles.setBounds(100, 100, 489, 150);
		frmGetYourFiles.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmGetYourFiles.setLocationRelativeTo(null);
		frmGetYourFiles.getContentPane().setLayout(new BorderLayout(0, 0));

		panel = new JPanel();
		frmGetYourFiles.getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JPanel panel_path = new JPanel();
		panel.add(panel_path);
		panel_path.setBackground(Color.WHITE);

		lblPath = new JLabel("Path: ");
		panel_path.add(lblPath);

		textField = new JTextField();
		textField.setEditable(false);
		panel_path.add(textField);
		textField.setColumns(30);

		btnBrowse = new JButton("Browse");
		btnBrowse.setBackground(Color.LIGHT_GRAY);
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
					textField.setText(selectedDirs);
				}
			}
		});
		panel_path.add(btnBrowse);

		panel_settings = new JPanel();
		panel_settings.setBackground(Color.WHITE);
		panel.add(panel_settings);
		panel_settings.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		lblSettings = new JLabel("Extensions: ");
		panel_settings.add(lblSettings);

		textField_extensions = new JTextField();
		panel_settings.add(textField_extensions);
		textField_extensions.setColumns(35);

		JButton btnStart = new JButton("Start");
		frmGetYourFiles.getContentPane().add(btnStart, BorderLayout.SOUTH);
		btnStart.setSelected(true);
		btnStart.setBackground(Color.LIGHT_GRAY);

		frmGetYourFiles.getRootPane().setDefaultButton(btnStart);

		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Thread noBlock = new Thread() {
					public void run() {
						setExtensionSettings();
						resetElements();
						extractFiles();
					}
				};
				if ((!alreadyRunning) & (dirs!=null)) {
					noBlock.start();
					alreadyRunning = true;
				}

			}
		});

		panel_progress = new JPanel();
		panel_progress.setBackground(Color.WHITE);
		frmGetYourFiles.getContentPane().add(panel_progress, BorderLayout.CENTER);

		lblDone = new JLabel("");
		panel_progress.add(lblDone);
	}

	private void extractFiles()	{

		for(File dir:dirs) {
			try {
				String parentDir = dir.getCanonicalFile().getParent();

				File[] files;
				if(MainForm.extensions.size() == 1 && MainForm.extensions.get(0).equals(""))
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
		lblDone.setText("Done");
		alreadyRunning = false;
	}

	private void resetElements() {
		//lblExtract.setText("");
		//lblZip.setText("");
		lblDone.setText("");

		//progressBar_Extraction.setValue(0);
		//progressBar_Zip.setValue(0);
	}

	private void setExtensionSettings() {
		String[] extensions = textField_extensions.getText().split(",");
		if (extensions.length == 0)
			return;
		for(String extension:extensions) {
			MainForm.extensions.add(extension.trim());
		}
	}

}
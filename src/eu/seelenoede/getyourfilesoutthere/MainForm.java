package eu.seelenoede.getyourfilesoutthere;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

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

public class MainForm {

	private JFrame frmGetYourFiles;
	private JTextField textField;
	private JLabel lblPath;
	private boolean alreadyRunning;
	private JPanel panel_progress;
	private JButton btnBrowse;
	private File[] dirs;
	private JLabel lblDone;

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

		JPanel panel_path = new JPanel();
		panel_path.setBackground(Color.WHITE);
		frmGetYourFiles.getContentPane().add(panel_path, BorderLayout.NORTH);

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

		JButton btnStart = new JButton("Start");
		btnStart.setSelected(true);
		btnStart.setBackground(Color.LIGHT_GRAY);

		frmGetYourFiles.getRootPane().setDefaultButton(btnStart);
		
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Thread noBlock = new Thread() {
					public void run() {
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

		frmGetYourFiles.getContentPane().add(btnStart, BorderLayout.SOUTH);

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
				files = dir.listFiles();
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
	
}
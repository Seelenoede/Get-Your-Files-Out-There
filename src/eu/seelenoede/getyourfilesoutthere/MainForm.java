package eu.seelenoede.getyourfilesoutthere;

import java.io.File;

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
	private JTextField textField_extensions;
	private JButton btnBrowse;
	private JButton btnStart;
	private JLabel lblDone;

	private boolean alreadyRunning;
	private File[] dirs;
	private Engine engine;
	
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
		engine = new Engine();
		initialize();
		initActions();
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

		JPanel panel = new JPanel();
		frmGetYourFiles.getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JPanel panel_path = new JPanel();
		panel.add(panel_path);
		panel_path.setBackground(Color.WHITE);

		JLabel lblPath = new JLabel("Path: ");
		panel_path.add(lblPath);

		textField = new JTextField();
		textField.setEditable(false);
		panel_path.add(textField);
		textField.setColumns(30);

		btnBrowse = new JButton("Browse");
		btnBrowse.setBackground(Color.LIGHT_GRAY);
		panel_path.add(btnBrowse);

		JPanel panel_settings = new JPanel();
		panel_settings.setBackground(Color.WHITE);
		panel.add(panel_settings);
		panel_settings.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblSettings = new JLabel("Extensions: ");
		panel_settings.add(lblSettings);

		textField_extensions = new JTextField();
		panel_settings.add(textField_extensions);
		textField_extensions.setColumns(35);

		btnStart = new JButton("Start");
		frmGetYourFiles.getContentPane().add(btnStart, BorderLayout.SOUTH);
		btnStart.setSelected(true);
		btnStart.setBackground(Color.LIGHT_GRAY);

		frmGetYourFiles.getRootPane().setDefaultButton(btnStart);

		JPanel panel_progress = new JPanel();
		panel_progress.setBackground(Color.WHITE);
		frmGetYourFiles.getContentPane().add(panel_progress, BorderLayout.CENTER);

		lblDone = new JLabel("");
		panel_progress.add(lblDone);
	}
	
	private void initActions() {
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
		
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Thread noBlock = new Thread() {
					public void run() {
						engine.setExtensionSettings(textField_extensions.getText());
						resetElements();
						engine.extractFiles(dirs);
					}
				};
				if ((!alreadyRunning) & (dirs!=null)) {
					noBlock.start();
					alreadyRunning = true;
				}

			}
		});
	}
	
	private void resetElements() {
		lblDone.setText("");
	}
}
package partD;

import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.trees.J48;

import com.esotericsoftware.tablelayout.swing.Table;

public class LearnerGUI extends JFrame implements ActionListener, TextListener, Runnable {
	private static final long serialVersionUID = 3907389239044543752L;
	
	private Learner learner;
	private TextField textField;
	private JComboBox<String> box;
	private boolean processing = false;
	private Label predictionLabel;
	private Label infoLabel;
	
	private boolean textChanged;
	
	public LearnerGUI(Learner learner, String[] classes) throws Exception {
		super("AI Learner");
		this.learner = learner;
		
		Table table = new Table();
		getContentPane().add(table);
		
		//text entry, left side of GUI
		Table textTable = new Table();
		table.addCell(textTable).expand().fill();
		textTable.addCell(new Label("Enter new text to train:")).left().row();
		textField = new TextField();
		textField.addTextListener(this);
		textTable.addCell(textField).expand().fill();
		
		//Update part, right side of GUI
		Table updateTable = new Table();
		table.addCell(updateTable);
		
		infoLabel = new Label();
		updateTable.addCell(infoLabel).row();
		
		predictionLabel = new Label();
		updateTable.addCell(predictionLabel).row();
		
		box = new JComboBox<String>();
		for (int i = 0; i < classes.length; i++) {
			box.addItem(classes[i]);
		}
		updateTable.addCell(box).fill().row();
		
		JButton button = new JButton("Update data");
		button.addActionListener(this);
		updateTable.addCell(button);
		
//		table.debug();
		
		setSize(400, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	@Override
	public void run() {
		classifyText();
		
		while (textChanged) {
			textChanged = false;
			classifyText();
		}
		
		processing = false;
		infoLabel.setText("Done");
		validate();
	}
	
	private void classifyText() {
		String input = textField.getText();
		
		if (input.length() != 0) {
			predictionLabel.setText("Prediction: " + learner.getClass(input));
			validate();
		}
		
//		System.out.println(learner.getInstances());
	}
	
	@Override
	public void textValueChanged(TextEvent e) {
		if (learner.hasData()) {
			if (!processing) {
				processing = true;
				new Thread(this).start();
				infoLabel.setText("Processing");
				validate();
			} else {
				//text changed while classifying
				textChanged = true;
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			learner.classify(textField.getText(), box.getSelectedItem().toString());
		} catch (Exception e1) {
			e1.printStackTrace();
			//don't care too much if one entry fails
		}
		textField.setText(null);
		box.setSelectedIndex(0);
	}
	
	private static String[] getClassesFromUser() {
		String input = JOptionPane.showInputDialog("Enter the classes you want to train for, separated by comma");
		
		return input.split("\\s*,\\s*");
	}
	
	public static void main(String[] args) throws Exception {
		Classifier c = getUserClassifier();
		
		String[] options = { "None", "Language", "Spam" };
		int option = JOptionPane.showOptionDialog(null, "Select data to start with", "Select preset",
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
		
		String[] classes;
		switch (option) {
		case 0:
			classes = getClassesFromUser();
			new LearnerGUI(new Learner(c, classes), classes);
			break;
		case 1: {
			classes = new String[] { "English", "French" };
			Learner learner = new Learner(c, classes);
			fillFileData(learner, "data/language/english", "English");
			fillFileData(learner, "data/language/french", "French");
			
			new LearnerGUI(learner, classes);
			break;
		}
		case 2: {
			classes = new String[] { "Spam", "Ham" };
			Learner learner = new Learner(c, classes);
			fillFileData(learner, "data/spam/spam", "Spam");
			fillFileData(learner, "data/spam/ham", "Ham");
			
			new LearnerGUI(learner, classes);
			break;
		}
		default:
			break;
		}
	}
	
	private static Classifier getUserClassifier() {
		String[] options = { "Naive Bayes", "Naive Bayes (multinominal)", "J48" };
		int option = JOptionPane.showOptionDialog(null, "Select classifier to work with", "Select classifier",
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
		
		switch (option) {
		case 0:
			return new NaiveBayes();
		case 1:
			return new NaiveBayesMultinomial();
		case 2:
			return new J48();
		default:
			return null;
		}
	}
	
	private static void fillFileData(Learner learner, String path, String classification) {
		File folder = new File(path);
		
		for (File f : folder.listFiles()) {
			if (!f.isDirectory()) {
				try {
					String input = new String(Files.readAllBytes(f.toPath()));
//					System.out.println(input);
					learner.classify(input, classification);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

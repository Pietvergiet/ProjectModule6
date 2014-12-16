package nineb.ai;

import java.awt.Label;

import javax.swing.JFrame;

import com.esotericsoftware.tablelayout.swing.Table;

public class LearnerGUI {
	public static void main(String[] args) {
		JFrame frame = new JFrame("AI Learner");
		Table table = new Table();
		frame.getContentPane().add(table);
		
		table.addCell(new Label("This is label"));
		table.addCell(new Label("This is label"));
		table.addCell(new Label("This is label")).row();
		table.addCell(new Label("This is label"));
		table.addCell(new Label("This is label")).colspan(2);
		
//		table.debug();
		
		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

package exercise01;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class SuperComputerGUI extends JPanel implements ActionListener {
	private static final long serialVersionUID = 4998293627753886206L;
	private JLabel statusLabel;
	private JLabel resultLabel;
	private final TheSupercomputer theSupercomputer;

	public SuperComputerGUI(TheSupercomputer theSupercomputer) {
		this.theSupercomputer = theSupercomputer;

		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(layout);

		addImg();
		addLabel("Confused? google the result.");
		addStartButton();

		statusLabel = addLabel("Status: ?");
		resultLabel = addLabel("Result: ?");
		add(Box.createRigidArea(new Dimension(0, 5)));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	    new Thread(() -> {
	    	String result = theSupercomputer.calculateUltimateAnswerToTheUltimateQuestionOfLifeTheUniverseAndEverything();	
	    	SwingUtilities.invokeLater(() -> {
	    		resultLabel.setText("Result: " + result);
	      });
	  }).start();
	}

	private void addStartButton() {
		add(Box.createRigidArea(new Dimension(0, 5)));
		JButton startButton = new JButton("Start");
		startButton.setToolTipText("Starts the calculation to find the answer to the Ultimate Question of Life, the Universe, and Everything");
		startButton.addActionListener(this);
		add(startButton);
	}

	private JLabel addLabel(String text) {
		add(Box.createRigidArea(new Dimension(0, 5)));
		JLabel label = new JLabel(text);
		add(label);
		return label;
	}

	private void addImg() {
		java.net.URL imgURL = getClass().getResource("supercomputer.jpg");
		if (imgURL != null) {
			add(new JLabel(new ImageIcon(imgURL)));
		}
	}

	private static void createAndShowGUI(TheSupercomputer theSupercomputer) {
		JFrame frame = new JFrame("The Supercomputer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SuperComputerGUI contentPane = new SuperComputerGUI(theSupercomputer);
		frame.setContentPane(contentPane);
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		final TheSupercomputer theSupercomputer = new TheSupercomputer();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI(theSupercomputer);
			}
		});
	}
}
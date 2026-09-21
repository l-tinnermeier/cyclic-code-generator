import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.CardLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.JButton;

public class MainDisplay {

	private JFrame frame;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainDisplay window = new MainDisplay();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainDisplay() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Cyclic Code Generator");
		frame.setBounds(100, 100, 1366, 768);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new CardLayout(0, 0));
		
		JPanel encodePanelDisp = new JPanel();
		frame.getContentPane().add(encodePanelDisp, "name_293514128711250");
		encodePanelDisp.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(210, 224, 228));
		panel.setBounds(62, 103, 1241, 571);
		encodePanelDisp.add(panel);
		panel.setLayout(null);
		
		JLabel enterMessageLbl = new JLabel("Enter an 11-bit message:");
		enterMessageLbl.setBounds(6, 485, 195, 63);
		panel.add(enterMessageLbl);
		enterMessageLbl.setFont(new Font("Neutraface 2 Text", Font.PLAIN, 20));
		
		textField = new JTextField();
		textField.setBounds(6, 530, 195, 35);
		panel.add(textField);
		textField.setColumns(10);
		
		JButton enterErrorBtn_1 = new JButton("0");
		enterErrorBtn_1.setFont(new Font("MesloLGL Nerd Font Mono", Font.BOLD, 13));
		enterErrorBtn_1.setBounds(1040, 530, 35, 35);
		panel.add(enterErrorBtn_1);
		
		JLabel enterErrorLbl = new JLabel("Enter an error:");
		enterErrorLbl.setHorizontalAlignment(SwingConstants.LEFT);
		enterErrorLbl.setFont(new Font("Neutraface 2 Text", Font.PLAIN, 20));
		enterErrorLbl.setBounds(1040, 472, 195, 63);
		panel.add(enterErrorLbl);
		
		JButton btnNewButton = new JButton(">");
		btnNewButton.setFont(new Font("Neutraface 2 Text", Font.BOLD, 40));
		btnNewButton.setBounds(1040, 6, 90, 90);
		panel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton(">>>");
		btnNewButton_1.setFont(new Font("Neutraface 2 Text", Font.BOLD, 40));
		btnNewButton_1.setBounds(1142, 6, 90, 90);
		panel.add(btnNewButton_1);
		
		JLabel adv = new JLabel("Advance 1 Step");
		adv.setFont(new Font("Neutraface 2 Text", Font.PLAIN, 8));
		adv.setBounds(1050, 108, 61, 16);
		panel.add(adv);
		
		JLabel lblFastForward = new JLabel("Fast Forward");
		lblFastForward.setFont(new Font("Neutraface 2 Text", Font.PLAIN, 8));
		lblFastForward.setBounds(1152, 108, 61, 16);
		panel.add(lblFastForward);
		
		JLabel titleLbl = new JLabel("Cyclic Code Generator");
		titleLbl.setHorizontalAlignment(SwingConstants.CENTER);
		titleLbl.setFont(new Font("Neutraface 2 Text", Font.BOLD, 36));
		titleLbl.setBounds(238, 21, 889, 63);
		encodePanelDisp.add(titleLbl);
	}
}

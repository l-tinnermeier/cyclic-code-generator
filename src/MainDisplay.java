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

    private static JButton bit1Btn;
    private static JButton bit2Btn;
    private static JButton bit3Btn;
    private static JButton bit4Btn;
    private static JButton bit5Btn;
    private static JButton bit6Btn;
    private static JButton bit7Btn;
    private static JButton bit8Btn;
    private static JButton bit9Btn;
    private static JButton bit10Btn;
    private static JButton bit11Btn;
    private static JButton encodeBtn;

    private static JLabel invalidMsgDisp;

    public static JButton[] messageButtons = new JButton[11];

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {

            public void run() {

                try {
                    MainDisplay window = new MainDisplay();
                    frame.setVisible(true);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MainDisplay() {
        initialize();
        addFunctionality();
    }

    private void initialize() {

        frame = new JFrame("Cyclic Code Simulator");
        frame.setBounds(100, 100, 1366, 768);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JPanel messagePanelDisp = new JPanel();
        messagePanelDisp.setBounds(0, 0, 1350, 729);
        messagePanelDisp.setLayout(null);
        frame.getContentPane().add(messagePanelDisp);

        // Title
        JLabel titleDisp = new JLabel("Cyclic Code Simulator!");
        titleDisp.setHorizontalAlignment(SwingConstants.CENTER);
        titleDisp.setFont(new Font("MesloLGLDZ Nerd Font", Font.BOLD, 26));
        titleDisp.setBounds(378, 25, 609, 80);
        messagePanelDisp.add(titleDisp);

        // Enter message label
        JLabel enterMsgLabel = new JLabel("Please enter an 11-bit message:");
        enterMsgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        enterMsgLabel.setFont(new Font("MesloLGLDZ Nerd Font", Font.PLAIN, 20));
        enterMsgLabel.setBounds(378, 120, 609, 50);
        messagePanelDisp.add(enterMsgLabel);

        // Text field
        enterMsgTxtField = new JTextField();
        enterMsgTxtField.setHorizontalAlignment(SwingConstants.CENTER);
        enterMsgTxtField.setFont(new Font("MesloLGLDZ Nerd Font", Font.BOLD, 20));
        enterMsgTxtField.setBounds(508, 180, 350, 50);
        enterMsgTxtField.setColumns(11);
        messagePanelDisp.add(enterMsgTxtField);

        // Invalid message display
        invalidMsgDisp = new JLabel("");
        invalidMsgDisp.setHorizontalAlignment(SwingConstants.CENTER);
        invalidMsgDisp.setFont(new Font("MesloLGLDZ Nerd Font", Font.BOLD, 16));
        invalidMsgDisp.setBounds(378, 230, 609, 30);
        messagePanelDisp.add(invalidMsgDisp);

        // Message bits label
        JLabel messageBitsLbl = new JLabel("11-bit Message");
        messageBitsLbl.setHorizontalAlignment(SwingConstants.CENTER);
        messageBitsLbl.setFont(new Font("MesloLGLDZ Nerd Font", Font.PLAIN, 18));
        messageBitsLbl.setBounds(378, 270, 609, 40);
        messagePanelDisp.add(messageBitsLbl);

        // Panel for 11 bits
        JPanel messageButtonDisp = new JPanel();
        messageButtonDisp.setBounds(210, 310, 930, 90);
        messageButtonDisp.setLayout(null);
        messagePanelDisp.add(messageButtonDisp);

        bit1Btn = new JButton("-");
        bit1Btn.setBounds(10, 15, 65, 60);
        messageButtonDisp.add(bit1Btn);

        bit2Btn = new JButton("-");
        bit2Btn.setBounds(90, 15, 65, 60);
        messageButtonDisp.add(bit2Btn);

        bit3Btn = new JButton("-");
        bit3Btn.setBounds(170, 15, 65, 60);
        messageButtonDisp.add(bit3Btn);

        bit4Btn = new JButton("-");
        bit4Btn.setBounds(250, 15, 65, 60);
        messageButtonDisp.add(bit4Btn);

        bit5Btn = new JButton("-");
        bit5Btn.setBounds(330, 15, 65, 60);
        messageButtonDisp.add(bit5Btn);

        bit6Btn = new JButton("-");
        bit6Btn.setBounds(410, 15, 65, 60);
        messageButtonDisp.add(bit6Btn);

        bit7Btn = new JButton("-");
        bit7Btn.setBounds(490, 15, 65, 60);
        messageButtonDisp.add(bit7Btn);

        bit8Btn = new JButton("-");
        bit8Btn.setBounds(570, 15, 65, 60);
        messageButtonDisp.add(bit8Btn);

        bit9Btn = new JButton("-");
        bit9Btn.setBounds(650, 15, 65, 60);
        messageButtonDisp.add(bit9Btn);

        bit10Btn = new JButton("-");
        bit10Btn.setBounds(730, 15, 65, 60);
        messageButtonDisp.add(bit10Btn);

        bit11Btn = new JButton("-");
        bit11Btn.setBounds(810, 15, 65, 60);
        messageButtonDisp.add(bit11Btn);

        // Button array
        messageButtons[0] = bit1Btn;
        messageButtons[1] = bit2Btn;
        messageButtons[2] = bit3Btn;
        messageButtons[3] = bit4Btn;
        messageButtons[4] = bit5Btn;
        messageButtons[5] = bit6Btn;
        messageButtons[6] = bit7Btn;
        messageButtons[7] = bit8Btn;
        messageButtons[8] = bit9Btn;
        messageButtons[9] = bit10Btn;
        messageButtons[10] = bit11Btn;

        // Display only
        for (JButton button : messageButtons) {
            button.setFont(new Font("MesloLGLDZ Nerd Font", Font.BOLD, 20));
            button.setEnabled(false);
        }

        // Encode button
        encodeBtn = new JButton("ENCODE");
        encodeBtn.setFont(new Font("MesloLGLDZ Nerd Font", Font.BOLD, 18));
        encodeBtn.setBounds(583, 440, 200, 60);
        encodeBtn.setEnabled(false);
        messagePanelDisp.add(encodeBtn);
    }

    private void addFunctionality() {

        enterMsgTxtField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                String input = enterMsgTxtField.getText();

                if (MessageInput.validMessage(input)) {

                    invalidMsgDisp.setText("");

                    for (int i = 0; i < 11; i++) {
                        messageButtons[i].setText(input.charAt(i) + "");
                    }

                    encodeBtn.setEnabled(true);

                } else {

                    invalidMsgDisp.setText("Invalid Message - Enter exactly 11 bits (0 or 1)");

                    for (int i = 0; i < 11; i++) {
                        messageButtons[i].setText("-");
                    }

                    encodeBtn.setEnabled(false);
                }
            }
        });
    }

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize_2() {
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

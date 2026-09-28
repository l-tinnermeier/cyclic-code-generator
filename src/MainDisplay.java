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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainDisplay {

	private static JFrame frame;

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
    private static JButton advanceBtn;
    private static JButton fastForwardBtn;
    
    private static JTextField enterMsgTxtField;

    private static JLabel invalidMsgDisp;

    public static JButton[] messageButtons = new JButton[11];
    
    public EncoderSimPanel encoderSimDisp;
    public DecoderSimPanel decoderSimDisp;

    private JButton showDecoderBtn;
    private JButton backToEncoderBtn;
    private JButton decoderStepBtn;
    private JButton decoderFastForwardBtn;
    private JLabel poly_lbl_x_2;
    private JLabel poly_lbl_x_3;
    private JLabel poly_lbl_x_4;
    private JLabel poly_ex_lbl_2;
    private JLabel poly_ex_lbl_3;
    private JLabel poly_ex_lbl_4;
    private JLabel r0_label;
    private JLabel r1_label;
    private JLabel r2_label;
    private JLabel r3_label;
    private JLabel xor_1_label;
    private JLabel xor_2_label;
    private JLabel r0_status_disp;
    private JLabel r1_status_disp;
    private JLabel r2_status_disp;
    private JLabel r3_status_disp;
    private JLabel bitstream_disp;

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
        initializeMessagePanel();
        initializeEncodingPanel();
        initializeDecodingPanel();
        addFunctionality();
    }

    private void initializeMessagePanel() {

        frame = new JFrame("Cyclic Code Simulator");
        frame.setBounds(100, 100, 1366, 768);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new CardLayout(0, 0));

        JPanel messagePanelDisp = new JPanel();
        messagePanelDisp.setLayout(null);
        frame.getContentPane().add(messagePanelDisp, "name_9840094793041");

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
        //encodeBtn.setEnabled(false);
        messagePanelDisp.add(encodeBtn);
    }


	/**
	 * Initialize the contents of the frame.
	 */
	private void initializeEncodingPanel() {
		JPanel encodePanelDisp = new JPanel();
		frame.getContentPane().add(encodePanelDisp, "name_293514128711250");
		encodePanelDisp.setLayout(null);

        showDecoderBtn = new JButton("Open Decoder");
        showDecoderBtn.setBounds(62, 32, 165, 42);
        encodePanelDisp.add(showDecoderBtn);
		
		encoderSimDisp = new EncoderSimPanel();
		encoderSimDisp.setBackground(new Color(210, 224, 228));
		encoderSimDisp.setBounds(60, 78, 1241, 571);
		encodePanelDisp.add(encoderSimDisp);
		encoderSimDisp.setLayout(null);
		
		advanceBtn = new JButton(">");
		advanceBtn.setFont(new Font("Neutraface 2 Text", Font.BOLD, 40));
		advanceBtn.setBounds(1040, 6, 90, 90);
		encoderSimDisp.add(advanceBtn);
		
		fastForwardBtn = new JButton(">>>");
		fastForwardBtn.setFont(new Font("Neutraface 2 Text", Font.BOLD, 40));
		fastForwardBtn.setBounds(1142, 6, 90, 90);
		encoderSimDisp.add(fastForwardBtn);
		
		JLabel adv = new JLabel("Advance 1 Step");
		adv.setHorizontalAlignment(SwingConstants.CENTER);
		adv.setFont(new Font("Neutraface 2 Text", Font.PLAIN, 12));
		adv.setBounds(1040, 107, 90, 16);
		encoderSimDisp.add(adv);
		
		JLabel lblFastForward = new JLabel("Fast Forward");
		lblFastForward.setHorizontalAlignment(SwingConstants.CENTER);
		lblFastForward.setFont(new Font("Neutraface 2 Text", Font.PLAIN, 12));
		lblFastForward.setBounds(1142, 108, 90, 16);
		encoderSimDisp.add(lblFastForward);
		
		encoderSimDisp.r0Lbl = new JLabel("R0 = 0");
		encoderSimDisp.r0Lbl.setFont(new Font("Dialog", Font.BOLD, 22));
		encoderSimDisp.r0Lbl.setBounds(105, 403, 120, 35);
		encoderSimDisp.add(encoderSimDisp.r0Lbl);
		
		encoderSimDisp.r1Lbl = new JLabel("R1 = 0");
		encoderSimDisp.r1Lbl.setFont(new Font("Dialog", Font.BOLD, 22));
		encoderSimDisp.r1Lbl.setBounds(305, 403, 120, 35);
		encoderSimDisp.add(encoderSimDisp.r1Lbl);
		
		encoderSimDisp.r2Lbl = new JLabel("R2 = 0");
		encoderSimDisp.r2Lbl.setFont(new Font("Dialog", Font.BOLD, 22));
		encoderSimDisp.r2Lbl.setBounds(505, 403, 120, 35);
		encoderSimDisp.add(encoderSimDisp.r2Lbl);
		
		encoderSimDisp.xor0Lbl = new JLabel("XOR0");
		encoderSimDisp.xor0Lbl.setFont(new Font("Dialog", Font.BOLD, 22));
		encoderSimDisp.xor0Lbl.setBounds(650, 403, 120, 35);
		encoderSimDisp.add(encoderSimDisp.xor0Lbl);
		
		encoderSimDisp.r3Lbl = new JLabel("R3 = 0");
		encoderSimDisp.r3Lbl.setFont(new Font("Dialog", Font.BOLD, 22));
		encoderSimDisp.r3Lbl.setBounds(785, 403, 120, 35);
		encoderSimDisp.add(encoderSimDisp.r3Lbl);
		
		encoderSimDisp.xor1Lbl = new JLabel("XOR1");
		encoderSimDisp.xor1Lbl.setFont(new Font("Dialog", Font.BOLD, 22));
		encoderSimDisp.xor1Lbl.setBounds(930, 403, 120, 35);
		encoderSimDisp.add(encoderSimDisp.xor1Lbl);
		
		JLabel titleLbl = new JLabel("Cyclic Code Generator");
		titleLbl.setHorizontalAlignment(SwingConstants.CENTER);
		titleLbl.setFont(new Font("Neutraface 2 Text", Font.BOLD, 36));
		titleLbl.setBounds(238, 21, 889, 63);
		encodePanelDisp.add(titleLbl);
	}
	
    private void initializeDecodingPanel() {
        JPanel decodePanelDisp = new JPanel();
        frame.getContentPane().add(decodePanelDisp, "decoding");
        decodePanelDisp.setLayout(null);

        decoderSimDisp = new DecoderSimPanel();
        decoderSimDisp.setBackground(new Color(210, 224, 228));
        decoderSimDisp.setBounds(62, 103, 1241, 571);
        decoderSimDisp.setLayout(null);
        decodePanelDisp.add(decoderSimDisp);

        decoderStepBtn = new JButton(">");
        decoderStepBtn.setFont(new Font("Neutraface 2 Text", Font.BOLD, 40));
        decoderStepBtn.setBounds(1040, 6, 90, 90);
        decoderSimDisp.add(decoderStepBtn);

        decoderFastForwardBtn = new JButton(">>>");
        decoderFastForwardBtn.setFont(new Font("Neutraface 2 Text", Font.BOLD, 28));
        decoderFastForwardBtn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        decoderFastForwardBtn.setBounds(1142, 6, 90, 90);
        decoderSimDisp.add(decoderFastForwardBtn);

        JLabel advanceLabel = new JLabel("Advance 1 Step");
        advanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        advanceLabel.setFont(new Font("Neutraface 2 Text", Font.PLAIN, 12));
        advanceLabel.setBounds(1040, 107, 90, 16);
        decoderSimDisp.add(advanceLabel);

        JLabel fastForwardLabel = new JLabel("Fast Forward");
        fastForwardLabel.setHorizontalAlignment(SwingConstants.CENTER);
        fastForwardLabel.setFont(new Font("Neutraface 2 Text", Font.PLAIN, 12));
        fastForwardLabel.setBounds(1142, 108, 90, 16);
        decoderSimDisp.add(fastForwardLabel);
        
        JLabel poly_lbl_1 = new JLabel("1");
        poly_lbl_1.setHorizontalAlignment(SwingConstants.CENTER);
        poly_lbl_1.setFont(new Font("MesloLGM Nerd Font", Font.PLAIN, 20));
        poly_lbl_1.setBounds(109, 44, 62, 52);
        decoderSimDisp.add(poly_lbl_1);
        
        JLabel poly_lbl_x = new JLabel("x");
        poly_lbl_x.setHorizontalAlignment(SwingConstants.CENTER);
        poly_lbl_x.setFont(new Font("MesloLGM Nerd Font", Font.PLAIN, 20));
        poly_lbl_x.setBounds(344, 44, 62, 52);
        decoderSimDisp.add(poly_lbl_x);
        
        poly_lbl_x_2 = new JLabel("x");
        poly_lbl_x_2.setHorizontalAlignment(SwingConstants.CENTER);
        poly_lbl_x_2.setFont(new Font("MesloLGM Nerd Font", Font.PLAIN, 20));
        poly_lbl_x_2.setBounds(542, 44, 62, 52);
        decoderSimDisp.add(poly_lbl_x_2);
        
        poly_lbl_x_3 = new JLabel("x");
        poly_lbl_x_3.setHorizontalAlignment(SwingConstants.CENTER);
        poly_lbl_x_3.setFont(new Font("MesloLGM Nerd Font", Font.PLAIN, 20));
        poly_lbl_x_3.setBounds(791, 44, 62, 52);
        decoderSimDisp.add(poly_lbl_x_3);
        
        poly_lbl_x_4 = new JLabel("x");
        poly_lbl_x_4.setHorizontalAlignment(SwingConstants.CENTER);
        poly_lbl_x_4.setFont(new Font("MesloLGM Nerd Font", Font.PLAIN, 20));
        poly_lbl_x_4.setBounds(925, 44, 62, 52);
        decoderSimDisp.add(poly_lbl_x_4);
        
        poly_ex_lbl_2 = new JLabel("2");
        poly_ex_lbl_2.setHorizontalAlignment(SwingConstants.CENTER);
        poly_ex_lbl_2.setFont(new Font("MesloLGM Nerd Font", Font.PLAIN, 16));
        poly_ex_lbl_2.setBounds(587, 44, 17, 16);
        decoderSimDisp.add(poly_ex_lbl_2);
        
        poly_ex_lbl_3 = new JLabel("3");
        poly_ex_lbl_3.setHorizontalAlignment(SwingConstants.CENTER);
        poly_ex_lbl_3.setFont(new Font("MesloLGM Nerd Font", Font.PLAIN, 16));
        poly_ex_lbl_3.setBounds(836, 45, 17, 16);
        decoderSimDisp.add(poly_ex_lbl_3);
        
        poly_ex_lbl_4 = new JLabel("4");
        poly_ex_lbl_4.setHorizontalAlignment(SwingConstants.CENTER);
        poly_ex_lbl_4.setFont(new Font("MesloLGM Nerd Font", Font.PLAIN, 16));
        poly_ex_lbl_4.setBounds(970, 45, 17, 16);
        decoderSimDisp.add(poly_ex_lbl_4);
        
        r0_label = new JLabel("Register 0");
        r0_label.setHorizontalAlignment(SwingConstants.CENTER);
        r0_label.setFont(new Font("MesloLGM Nerd Font", Font.BOLD, 13));
        r0_label.setBounds(235, 274, 90, 16);
        decoderSimDisp.add(r0_label);
        
        r1_label = new JLabel("Register 1");
        r1_label.setHorizontalAlignment(SwingConstants.CENTER);
        r1_label.setFont(new Font("MesloLGM Nerd Font", Font.BOLD, 13));
        r1_label.setBounds(433, 274, 90, 16);
        decoderSimDisp.add(r1_label);
        
        r2_label = new JLabel("Register 2");
        r2_label.setHorizontalAlignment(SwingConstants.CENTER);
        r2_label.setFont(new Font("MesloLGM Nerd Font", Font.BOLD, 13));
        r2_label.setBounds(632, 274, 90, 16);
        decoderSimDisp.add(r2_label);
        
        r3_label = new JLabel("Register 3");
        r3_label.setHorizontalAlignment(SwingConstants.CENTER);
        r3_label.setFont(new Font("MesloLGM Nerd Font", Font.BOLD, 13));
        r3_label.setBounds(912, 274, 90, 16);
        decoderSimDisp.add(r3_label);
        
        xor_1_label = new JLabel("XOR");
        xor_1_label.setHorizontalAlignment(SwingConstants.CENTER);
        xor_1_label.setFont(new Font("MesloLGM Nerd Font", Font.BOLD, 13));
        xor_1_label.setBounds(94, 274, 90, 16);
        decoderSimDisp.add(xor_1_label);
        
        xor_2_label = new JLabel("XOR");
        xor_2_label.setHorizontalAlignment(SwingConstants.CENTER);
        xor_2_label.setFont(new Font("MesloLGM Nerd Font", Font.BOLD, 13));
        xor_2_label.setBounds(775, 274, 90, 16);
        decoderSimDisp.add(xor_2_label);
        
        r0_status_disp = new JLabel("holds");
        r0_status_disp.setHorizontalAlignment(SwingConstants.CENTER);
        r0_status_disp.setFont(new Font("MesloLGM Nerd Font", Font.BOLD, 16));
        r0_status_disp.setBounds(235, 410, 90, 16);
        decoderSimDisp.add(r0_status_disp);
        
        r1_status_disp = new JLabel("holds");
        r1_status_disp.setHorizontalAlignment(SwingConstants.CENTER);
        r1_status_disp.setFont(new Font("MesloLGM Nerd Font", Font.BOLD, 16));
        r1_status_disp.setBounds(433, 411, 90, 16);
        decoderSimDisp.add(r1_status_disp);
        
        r2_status_disp = new JLabel("holds");
        r2_status_disp.setHorizontalAlignment(SwingConstants.CENTER);
        r2_status_disp.setFont(new Font("MesloLGM Nerd Font", Font.BOLD, 16));
        r2_status_disp.setBounds(632, 411, 90, 16);
        decoderSimDisp.add(r2_status_disp);
        
        r3_status_disp = new JLabel("holds");
        r3_status_disp.setHorizontalAlignment(SwingConstants.CENTER);
        r3_status_disp.setFont(new Font("MesloLGM Nerd Font", Font.BOLD, 16));
        r3_status_disp.setBounds(912, 411, 90, 16);
        decoderSimDisp.add(r3_status_disp);
        
        bitstream_disp = new JLabel("10000110101");
        bitstream_disp.setHorizontalAlignment(SwingConstants.CENTER);
        bitstream_disp.setFont(new Font("MesloLGM Nerd Font", Font.BOLD, 16));
        bitstream_disp.setBounds(11, 530, 160, 16);
        decoderSimDisp.add(bitstream_disp);

        JLabel titleLabel = new JLabel("Cyclic Code Decoder");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Neutraface 2 Text", Font.BOLD, 36));
        titleLabel.setBounds(238, 21, 889, 63);
        decodePanelDisp.add(titleLabel);

        backToEncoderBtn = new JButton("Back to Encoder");
        backToEncoderBtn.setBounds(62, 32, 165, 42);
        decodePanelDisp.add(backToEncoderBtn);
    }

    private void addFunctionality() {
        showDecoderBtn.addActionListener(e -> {
            CardLayout cards = (CardLayout) frame.getContentPane().getLayout();
            cards.show(frame.getContentPane(), "decoding");
        });

        backToEncoderBtn.addActionListener(e -> {
            CardLayout cards = (CardLayout) frame.getContentPane().getLayout();
            cards.show(frame.getContentPane(), "name_293514128711250");
        });

        // Attach decoder clock-step and fast-forward logic here when ready.

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

	                invalidMsgDisp.setText(
	                    "Invalid Message - Enter exactly 11 bits (0 or 1)"
	                );

	                for (int i = 0; i < 11; i++) {
	                    messageButtons[i].setText("-");
	                }

	                encodeBtn.setEnabled(false);
	            }
	        }
	    });


	    // ENCODE BUTTON
	    encodeBtn.addActionListener(e -> {

	        encoderSimDisp.startEncoding();

	        CardLayout cards =
	            (CardLayout) frame.getContentPane().getLayout();

	        cards.show(
	            frame.getContentPane(),
	            "name_293514128711250"
	        );
	    });


	    advanceBtn.addActionListener(e -> {

	        encoderSimDisp.advanceOneStep();

	    });


	    fastForwardBtn.addActionListener(e -> {

	        encoderSimDisp.fastForward();

	    });

	}
}

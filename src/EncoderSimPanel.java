import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 * Encoder appearance and encoding logic.
 * Generator polynomial: g(x) = x^4 + x^3 + 1
 */

public class EncoderSimPanel extends CircuitPanel {

    private static final long serialVersionUID = 1L;

    private int clock = 0;

    private int r0 = 0;
    private int r1 = 0;
    private int r2 = 0;
    private int r3 = 0;

    private int currentInput = 0;
    private int feedback = 0;

    private JLabel messageLbl;
    private JLabel inputLbl;
    private JLabel feedbackLbl;

    private JLabel r0Lbl;
    private JLabel r1Lbl;
    private JLabel r2Lbl;
    private JLabel r3Lbl;

    private JLabel xor0Lbl;
    private JLabel xor1Lbl;

    private JLabel clockLbl;

    private JLabel checkBitsLbl;
    private JLabel codewordLbl;


    public EncoderSimPanel() {

        super(
            new CircuitElement("R0", ComponentType.REGISTER),
            new CircuitElement("R1", ComponentType.REGISTER),
            new CircuitElement("R2", ComponentType.REGISTER),
            new CircuitElement("XOR0", ComponentType.XOR),
            new CircuitElement("R3", ComponentType.REGISTER),
            new CircuitElement("XOR1", ComponentType.XOR)
        );

        setFeedback("XOR1", 100, "R0", "XOR0");

        setLayout(null);

        createLabels();
    }

    private void createLabels() {

        messageLbl = new JLabel("Message: -----------");
        messageLbl.setFont(new Font("Arial", Font.BOLD, 16));
        messageLbl.setBounds(20, 10, 350, 30);
        add(messageLbl);


        inputLbl = new JLabel("Current Input: -");
        inputLbl.setFont(new Font("Arial", Font.BOLD, 16));
        inputLbl.setBounds(20, 40, 300, 30);
        add(inputLbl);


        feedbackLbl = new JLabel("Feedback: -");
        feedbackLbl.setFont(new Font("Arial", Font.BOLD, 16));
        feedbackLbl.setBounds(20, 70, 300, 30);
        add(feedbackLbl);


        r0Lbl = new JLabel("R0 = 0", SwingConstants.CENTER);
        r0Lbl.setFont(new Font("Arial", Font.BOLD, 16));
        r0Lbl.setBounds(70, 440, 120, 30);
        add(r0Lbl);


        r1Lbl = new JLabel("R1 = 0", SwingConstants.CENTER);
        r1Lbl.setFont(new Font("Arial", Font.BOLD, 16));
        r1Lbl.setBounds(275, 440, 120, 30);
        add(r1Lbl);


        r2Lbl = new JLabel("R2 = 0", SwingConstants.CENTER);
        r2Lbl.setFont(new Font("Arial", Font.BOLD, 16));
        r2Lbl.setBounds(480, 440, 120, 30);
        add(r2Lbl);


        xor0Lbl = new JLabel("XOR0", SwingConstants.CENTER);
        xor0Lbl.setFont(new Font("Arial", Font.BOLD, 16));
        xor0Lbl.setBounds(650, 440, 120, 30);
        add(xor0Lbl);


        r3Lbl = new JLabel("R3 = 0", SwingConstants.CENTER);
        r3Lbl.setFont(new Font("Arial", Font.BOLD, 16));
        r3Lbl.setBounds(800, 440, 120, 30);
        add(r3Lbl);


        xor1Lbl = new JLabel("XOR1", SwingConstants.CENTER);
        xor1Lbl.setFont(new Font("Arial", Font.BOLD, 16));
        xor1Lbl.setBounds(950, 440, 120, 30);
        add(xor1Lbl);


        clockLbl = new JLabel(
            "Clock 0/11",
            SwingConstants.CENTER
        );

        clockLbl.setFont(
            new Font("Arial", Font.BOLD, 20)
        );

        clockLbl.setBounds(475, 490, 250, 30);

        add(clockLbl);


        checkBitsLbl = new JLabel("");

        checkBitsLbl.setFont(
            new Font("Arial", Font.BOLD, 16)
        );

        checkBitsLbl.setBounds(
            20, 525, 350, 30
        );

        add(checkBitsLbl);

        codewordLbl = new JLabel("");

        codewordLbl.setFont(
            new Font("Arial", Font.BOLD, 16)
        );

        codewordLbl.setBounds(
            700, 525, 500, 30
        );

        add(codewordLbl);
    }


    public void startEncoding() {

        clock = 0;

        r0 = 0;
        r1 = 0;
        r2 = 0;
        r3 = 0;

        currentInput = 0;
        feedback = 0;

        resetState();
       
        messageLbl.setText(
            "Message: " + MessageInput.message
        );

        inputLbl.setText(
            "Current Input: -"
        );

        feedbackLbl.setText(
            "Feedback: -"
        );

        clockLbl.setText(
            "Clock 0/11"
        );

        checkBitsLbl.setText("");

        codewordLbl.setText("");

        updateRegisterLabels();

        repaint();
    }


    public void advanceOneStep() {

        // Make sure we have an 11-bit message
        if (MessageInput.message.length() != 11) {
            return;
        }

        if (clock >= 11) {
            return;
        }

  
        currentInput =
            MessageInput.message.charAt(clock) - '0';


        int oldR0 = r0;
        int oldR1 = r1;
        int oldR2 = r2;
        int oldR3 = r3;


        // =================================================
        // g(x) = x^4 + x^3 + 1
        // =================================================

        // Feedback is current input XOR last register
        feedback = currentInput ^ oldR3;


        r0 = feedback;

        r1 = oldR0;

        r2 = oldR1;

        r3 = oldR2 ^ feedback;


        clock++;

        inputLbl.setText(
            "Current Input: " + currentInput
        );

        feedbackLbl.setText(
            "Feedback: " + feedback
        );

        clockLbl.setText(
            "Clock " + clock + "/11"
        );

        updateRegisterLabels();


        if (clock == 11) {

            showFinalResult();
        }

        repaint();
    }


    public void fastForward() {

        if (MessageInput.message.length() != 11) {
            return;
        }
        
        Timer timer = new Timer(455, null);

        timer.addActionListener(e -> {
        if (clock < 11) {

            advanceOneStep();
        }
        
        if(clock >= 11) {
        	timer.stop();
        }
    });
    
    timer.start();
}


    private void updateRegisterLabels() {
    	
    	r0Lbl.setText("R0 = " + r0);
        r1Lbl.setText("R1 = " + r1);
        r2Lbl.setText("R2 = " + r2);
        r3Lbl.setText("R3 = " + r3);
        
        setBit("R0", r0);
        setBit("R1", r1);
        setBit("R2", r2);
        setBit("R3", r3);

        setBit("XOR0", feedback);
        setBit("XOR1", feedback);
    }
      

    private void showFinalResult() {

        checkBitsLbl.setText(
            "Check Bits: " + getCheckBits()
        );

        codewordLbl.setText(
            "15-bit Codeword: " + getCodeword()
        );
    }


    public String getCheckBits() {

        return "" + r3 + r2 + r1 + r0;
    }


    public String getCodeword() {

        return MessageInput.message + getCheckBits();
    }


    public boolean isFinished() {

        return clock == 11;
    }


    public int getClock() {

        return clock;
    }
}
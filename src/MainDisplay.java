import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/** All screen construction lives in its own initialize...Panel method. */
public class MainDisplay {
    private final JFrame frame = new JFrame("Cyclic Code Simulator");
    private final CardLayout cards = new CardLayout();
    private final JPanel screens = new JPanel(cards);
    private final Font normal = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
    public EncoderSimPanel encoderSimDisp;
    public DecoderSimPanel decoderSimDisp;
    private JTextField messageInput;
    private JLabel inputError, encoderStatus, encoderResult, decoderStatus, decoderResult;
    private JLabel receivedResult, correctedResult, recoveredResult, injectionStatus, testSummary;
    private JLabel encoderBits, decoderBits;
    private JButton encoderStep, decoderStep, encoderPlay, decoderPlay, channelButton;
    private final JButton[] errorBits = new JButton[15];
    private final JButton[] messageBits = new JButton[11];
    private int errorPosition = -1;
    private JPanel testResults;
    private JLabel transmittedBits;
    private DefaultTableModel tests;
    private String transmitted = "", received = "";
    private Timer encoderTimer, decoderTimer;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new MainDisplay().frame.setVisible(true));
    }
    public MainDisplay() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(screens);
        frame.setSize(1366, 900);
        frame.setMinimumSize(new Dimension(1100, 800));
        frame.setLocationByPlatform(true);
        initializeMessagePanel();
        initializeEncodingPanel();
        initializeErrorInjectionPanel();
        initializeDecodingPanel();
        encoderTimer = new Timer(650, e -> advanceEncoder());
        decoderTimer = new Timer(650, e -> advanceDecoder());
    }
    private JPanel page(String title, String name) {
        JPanel page = new JPanel(new BorderLayout(12, 12));
        page.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));
        JLabel heading = new JLabel(title, SwingConstants.CENTER);
        heading.setFont(normal.deriveFont(Font.BOLD, 28));
        page.add(heading, BorderLayout.NORTH);
        screens.add(page, name);
        return page;
    }
    private JPanel column() { JPanel p = new JPanel(); p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS)); return p; }
    private JLabel label(String text) { JLabel l = new JLabel(text) { public Dimension getMaximumSize() { return new Dimension(Integer.MAX_VALUE, getPreferredSize().height); } }; l.setFont(normal); l.setAlignmentX(Component.LEFT_ALIGNMENT); return l; }
    private JButton button(String text, Runnable action) {
        JButton b = new JButton(text); b.setFont(normal); b.addActionListener(e -> action.run()); return b;
    }
    private JPanel row(Component... children) { JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6)); p.setAlignmentX(Component.LEFT_ALIGNMENT); for(Component c:children)p.add(c); return p; }
    private void stopTimers() { if(encoderTimer!=null)encoderTimer.stop(); if(decoderTimer!=null)decoderTimer.stop(); }
    private void show(String name) { stopTimers(); encoderPlay.setText("Auto play"); decoderPlay.setText("Auto play"); cards.show(screens, name); }

    private JPanel centeredRow(Component... children) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 6));
        for (Component child : children) panel.add(child);
        return panel;
    }

    private void initializeMessagePanel() {
        JPanel page = page("Cyclic Code Simulator!", "message");
        JPanel body = column();
        body.add(Box.createVerticalStrut(25));
        body.add(centeredRow(label("Please enter an 11-bit message:")));
        messageInput = new JTextField(18);
        messageInput.setHorizontalAlignment(JTextField.CENTER);
        messageInput.setFont(normal.deriveFont(Font.BOLD, 20f));
        body.add(centeredRow(messageInput));
        inputError = label(" ");
        inputError.setForeground(new Color(160, 35, 35));
        body.add(centeredRow(inputError));
        body.add(Box.createVerticalStrut(20));
        body.add(centeredRow(label("11-bit Message")));
        JPanel bits = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 6));
        for (int i = 0; i < messageBits.length; i++) {
            messageBits[i] = new JButton("-");
            messageBits[i].setFont(normal.deriveFont(Font.BOLD, 20f));
            messageBits[i].setPreferredSize(new Dimension(65, 60));
            messageBits[i].setEnabled(false);
            bits.add(messageBits[i]);
        }
        body.add(bits);
        body.add(Box.createVerticalStrut(25));
        JButton encode = button("ENCODE", this::startEncoding);
        encode.setPreferredSize(new Dimension(200, 60));
        body.add(centeredRow(encode));
        messageInput.addActionListener(e -> previewMessage());
        messageInput.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { previewMessage(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { previewMessage(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { previewMessage(); }
        });
        JPanel content = new JPanel(new BorderLayout());
        content.add(body, BorderLayout.NORTH);
        page.add(content, BorderLayout.CENTER);
    }

    private void previewMessage() {
        String value = messageInput.getText();
        boolean valid = value.matches("[01]{11}");
        for (int i = 0; i < messageBits.length; i++) {
            messageBits[i].setText(valid ? String.valueOf(value.charAt(i)) : "-");
        }
        inputError.setText(value.isEmpty() || valid ? " " : "Enter exactly 11 bits (0 or 1).");
    }

    private void initializeEncodingPanel() {
        JPanel page = page("Encoder", "encoding");
        JPanel body = new JPanel(new BorderLayout(8, 8));
        encoderSimDisp = new EncoderSimPanel();
        encoderBits = label("Message: -"); body.add(encoderBits, BorderLayout.NORTH);
        body.add(encoderSimDisp, BorderLayout.CENTER);
        JPanel bottom = column();
        encoderStatus = label(" "); encoderResult = label(" "); bottom.add(encoderStatus); bottom.add(encoderResult);
        encoderStep = button("Follow signals", () -> {encoderTimer.stop(); advanceEncoder();});
        encoderPlay = button("Auto play", () -> {if(encoderTimer.isRunning())encoderTimer.stop();else encoderTimer.start(); encoderPlay.setText(encoderTimer.isRunning()?"Pause":"Auto play");});
        channelButton = button("Transmission / error →", this::openChannel);
        bottom.add(row(button("New message", () -> show("message")), encoderStep, encoderPlay,
                button("Restart", () -> {stopTimers();encoderSimDisp.startEncoding(encoderSimDisp.getMessage());updateEncoder();}), channelButton));
        body.add(bottom, BorderLayout.SOUTH); page.add(body, BorderLayout.CENTER);
    }
    private void initializeErrorInjectionPanel() {
        JPanel page = page("Introduce an Error", "channel");
        JPanel body = new JPanel(new BorderLayout(8, 20));
        JPanel top = column();
        top.add(Box.createVerticalStrut(25));
        transmittedBits = label(" ");
        top.add(centeredRow(transmittedBits));
        top.add(Box.createVerticalStrut(20));
        top.add(centeredRow(label("Click a bit to flip it. One error at a time.")));
        JPanel bits = new JPanel(new GridLayout(1, 15, 8, 0));
        for (int i = 0; i < errorBits.length; i++) {
            final int position = 14 - i;
            JButton bit = button("0", () -> {
                errorPosition = errorPosition == position ? -1 : position;
                updateReceived();
            });
            bit.setFont(normal.deriveFont(Font.BOLD, 22f));
            bit.setPreferredSize(new Dimension(54, 60));
            bit.setToolTipText("Flip bit " + position);
            errorBits[i] = bit;
            JPanel cell = new JPanel(new BorderLayout(0, 6));
            JLabel index = new JLabel(Integer.toString(position), SwingConstants.CENTER);
            cell.add(index, BorderLayout.NORTH);
            cell.add(bit, BorderLayout.CENTER);
            bits.add(cell);
        }
        top.add(centeredRow(bits));
        injectionStatus = label("No error");
        top.add(centeredRow(injectionStatus));
        top.add(Box.createVerticalStrut(20));
        top.add(centeredRow(button("Back", () -> show("encoding")),
                button("Reset", () -> { errorPosition = -1; updateReceived(); }),
                button("DECODE", this::startDecoding)));
        top.add(centeredRow(button("Test all errors", () -> {
            runAutomaticTests();
            testResults.setVisible(true);
            page.revalidate();
        })));
        body.add(top, BorderLayout.NORTH);
        tests = new DefaultTableModel(new String[]{"Error bit", "Syndrome R3..R0", "Detected bit", "Corrected word", "Message", "Result"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tests);
        table.setRowHeight(24);
        table.setFont(normal.deriveFont(14f));
        table.getColumnModel().getColumn(3).setPreferredWidth(210);
        table.getColumnModel().getColumn(4).setPreferredWidth(170);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setColumnHeaderView(table.getTableHeader());
        testResults = new JPanel(new BorderLayout(8, 8));
        testResults.add(scroll, BorderLayout.CENTER);
        testSummary = label(" ");
        testResults.add(centeredRow(testSummary, button("Hide results", () -> {
            testResults.setVisible(false);
            page.revalidate();
        })), BorderLayout.SOUTH);
        testResults.setVisible(false);
        body.add(testResults, BorderLayout.CENTER);
        page.add(body, BorderLayout.CENTER);
    }
    private void initializeDecodingPanel() {
        JPanel page = page("Decoder and correction", "decoding");
        JPanel body = new JPanel(new BorderLayout(8, 8));
        decoderBits = label("Received word: -");body.add(decoderBits,BorderLayout.NORTH);
        decoderSimDisp = new DecoderSimPanel();body.add(decoderSimDisp,BorderLayout.CENTER);
        JPanel bottom = column();
        decoderStatus=label(" ");decoderResult=label(" ");receivedResult=label(" ");correctedResult=label(" ");recoveredResult=label(" ");
        bottom.add(decoderStatus);bottom.add(decoderResult);bottom.add(receivedResult);bottom.add(correctedResult);bottom.add(recoveredResult);
        decoderStep=button("Follow signals",()->{decoderTimer.stop();advanceDecoder();});
        decoderPlay=button("Auto play",()->{if(decoderTimer.isRunning())decoderTimer.stop();else decoderTimer.start();decoderPlay.setText(decoderTimer.isRunning()?"Pause":"Auto play");});
        bottom.add(row(button("← Change error",()->show("channel")),decoderStep,decoderPlay,
                button("Restart decoder",this::startDecoding),button("New message",()->show("message"))));
        body.add(bottom,BorderLayout.SOUTH);page.add(body,BorderLayout.CENTER);
    }
    private void startEncoding() {
        String value=messageInput.getText();
        if(!MessageInput.validMessage(value)){inputError.setText("Enter exactly 11 bits, using only 0 and 1.");return;}
        inputError.setText(" ");stopTimers();encoderSimDisp.startEncoding(value);updateEncoder();show("encoding");
    }
    private void advanceEncoder() {
        if(encoderSimDisp.isFinished()){encoderTimer.stop();return;}
        if(encoderSimDisp.hasPreview())encoderSimDisp.commitNext();else encoderSimDisp.prepareNext();
        if(encoderSimDisp.isFinished())encoderTimer.stop();updateEncoder();
    }
    private void updateEncoder() {
        int clock=encoderSimDisp.getClock();boolean done=encoderSimDisp.isFinished();
        encoderBits.setText(bitLine("Message · rightmost bit first",encoderSimDisp.getMessage(),done?-1:10-clock,-1));
        encoderStatus.setText("Clock " + clock + "/11 · " + encoderSimDisp.getCalculations());
        encoderResult.setText(done?"Check bits (R0 R1 R2 R3): " + encoderSimDisp.getCheckBits() + "     Codeword = check bits + message: " + encoderSimDisp.getCodeword():
                encoderSimDisp.hasPreview()?"Next values are ready. The registers have not changed yet.":"Registers hold their current values until the next clock edge.");
        encoderStep.setText(encoderSimDisp.hasPreview()?"Clock registers":"Follow signals");
        encoderStep.setEnabled(!done);encoderPlay.setEnabled(!done);channelButton.setEnabled(done);
        encoderPlay.setText(encoderTimer!=null&&encoderTimer.isRunning()?"Pause":"Auto play");
    }
    private void openChannel() {
        if(!encoderSimDisp.isFinished())return;
        transmitted=encoderSimDisp.getCodeword();tests.setRowCount(0);
        testResults.setVisible(false);
        errorPosition = -1;updateReceived();show("channel");
    }
    private void updateReceived() {
        if(transmitted.isEmpty())return;
        int position=errorPosition;
        char[] bits=transmitted.toCharArray();if(position>=0)bits[14-position]=bits[14-position]=='0'?'1':'0';received=new String(bits);
        transmittedBits.setText("Encoded message: " + transmitted);
        for (int i = 0; i < errorBits.length; i++) {
            boolean changed = position >= 0 && i == 14 - position;
            errorBits[i].setText(String.valueOf(received.charAt(i)));
            errorBits[i].setForeground(changed ? new Color(175, 45, 25) : UIManager.getColor("Button.foreground"));
            errorBits[i].setBorder(changed
                    ? BorderFactory.createLineBorder(new Color(175, 45, 25), 2)
                    : UIManager.getBorder("Button.border"));
            errorBits[i].getAccessibleContext().setAccessibleName("Bit " + (14 - i) + ": " + received.charAt(i) + (changed ? ", flipped" : ""));
        }
        injectionStatus.setText(position < 0 ? "No error" : "Bit " + position + " flipped");
    }
    private void startDecoding() {
        if(received.isEmpty())return;
        stopTimers();DecoderLogic.loadCodeword(received);decoderSimDisp.resetState();updateDecoder();show("decoding");
    }
    private void advanceDecoder() {
        if(DecoderLogic.isFinished()){decoderTimer.stop();return;}
        if(DecoderLogic.hasPreview())DecoderLogic.commitNext();else DecoderLogic.prepareNext();
        if(DecoderLogic.isFinished())decoderTimer.stop();updateDecoder();
    }
    private void updateDecoder() {
        int[] r=DecoderLogic.getRegisters();for(int i=0;i<4;i++)decoderSimDisp.setBit("R"+i,r[i]);
        decoderSimDisp.setBit("XOR0",DecoderLogic.getLastLeftXor());decoderSimDisp.setBit("XOR1",DecoderLogic.getLastRightXor());
        decoderSimDisp.setPreview(DecoderLogic.getNextRegisters(),DecoderLogic.getLastInput(),DecoderLogic.getLastFeedback(),
                DecoderLogic.getLastInput()+" ⊕ "+DecoderLogic.getLastFeedback()+" = "+DecoderLogic.getLastLeftXor(),
                DecoderLogic.getLastR2()+" ⊕ "+DecoderLogic.getLastFeedback()+" = "+DecoderLogic.getLastRightXor(),"XOR0","XOR1");
        boolean done=DecoderLogic.isFinished();int clock=DecoderLogic.getClock();
        decoderBits.setText(bitLine("Received · rightmost bit first",received,done?-1:14-clock,-1));
        decoderStatus.setText("Clock "+clock+"/15 · "+(clock==0&&!DecoderLogic.hasPreview()?"Registers hold zero. Follow the first input bit.":DecoderLogic.getCalculations()));
        decoderStep.setText(DecoderLogic.hasPreview()?"Clock registers":"Follow signals");decoderStep.setEnabled(!done);decoderPlay.setEnabled(!done);
        decoderPlay.setText(decoderTimer!=null&&decoderTimer.isRunning()?"Pause":"Auto play");
        if(done){
            int position=DecoderLogic.getErrorPosition();String corrected=DecoderLogic.getCorrectedCodeword();
            String syndrome=String.format("%4s",Integer.toBinaryString(DecoderLogic.getSyndrome())).replace(' ','0');
            decoderResult.setText("Syndrome R3 R2 R1 R0: "+syndrome+(position<0?" · No error detected":" · Error at bit "+position+" (0 = rightmost)"));
            receivedResult.setText(bitLine("Before correction",received,-1,position<0?-1:14-position));
            correctedResult.setText(bitLine("Corrected codeword",corrected,-1,position<0?-1:14-position));
            recoveredResult.setText("Recovered message: "+DecoderLogic.getRecoveredMessage()+" · "+(DecoderLogic.getRecoveredMessage().equals(encoderSimDisp.getMessage())?"Matches original":"Does not match original")
                    +" · Recheck syndrome: "+(DecoderLogic.calculateSyndrome(Integer.parseInt(corrected,2))==0?"0000":"nonzero"));
        }else{
            decoderResult.setText(DecoderLogic.hasPreview()?"Both XOR outputs are ready. Clock all four registers together.":"Stored bits stay fixed while the next input is evaluated.");
            receivedResult.setText(" ");correctedResult.setText(" ");recoveredResult.setText(" ");
        }
    }
    private void runAutomaticTests() {
        if(transmitted.isEmpty())return;tests.setRowCount(0);int pass=0;
        for(int position=-1;position<15;position++){
            int word=Integer.parseInt(transmitted,2);if(position>=0)word^=1<<position;
            String damaged=String.format("%15s",Integer.toBinaryString(word)).replace(' ','0');
            int syndrome=DecoderLogic.calculateSyndrome(word);int located=DecoderLogic.SYNDROME_TABLE[syndrome];
            String corrected=DecoderLogic.correctWord(damaged),recovered=corrected.substring(4);
            boolean ok=located==position&&corrected.equals(transmitted)&&recovered.equals(encoderSimDisp.getMessage())&&DecoderLogic.calculateSyndrome(Integer.parseInt(corrected,2))==0;
            if(ok)pass++;
            tests.addRow(new Object[]{position<0?"None":position,String.format("%4s",Integer.toBinaryString(syndrome)).replace(' ','0'),located<0?"None":located,corrected,recovered,ok?"PASS":"FAIL"});
        }
        testSummary.setText(pass+"/16 passed");
    }
    /** Active bit is underlined; the flipped/corrected bit is colored and bracketed. */
    private String bitLine(String title,String bits,int active,int changed) {
        StringBuilder html=new StringBuilder("<html>").append(title).append(": <font face='monospace'>");
        for(int i=0;i<bits.length();i++){
            if(i==changed)html.append("<font color='#a33a16'>[");if(i==active)html.append("<u><b>");
            html.append(bits.charAt(i));
            if(i==active)html.append("</b></u>");if(i==changed)html.append("]</font>");html.append(' ');
        }
        return html.append("</font></html>").toString();
    }
}

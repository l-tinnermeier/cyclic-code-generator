/** LSB-first encoder with a separate combinational preview and clock edge. */
public class EncoderSimPanel extends CircuitPanel {
    private static final long serialVersionUID = 1L;
    private String message = "";
    private int clock;
    private int[] registers = new int[4];
    private int[] pending;
    private int incoming, feedback, oldR2, oldR3;

    public EncoderSimPanel() {
        super(new CircuitElement("R0", ComponentType.REGISTER),
              new CircuitElement("R1", ComponentType.REGISTER),
              new CircuitElement("R2", ComponentType.REGISTER),
              new CircuitElement("XOR0", ComponentType.XOR),
              new CircuitElement("R3", ComponentType.REGISTER),
              new CircuitElement("XOR1", ComponentType.XOR));
        setFeedback("XOR1", 70, "R0", "XOR0");
        setInputGate("XOR1");
    }

    public void startEncoding() { startEncoding(MessageInput.message); }
    public void startEncoding(String value) {
        if (value == null || !value.matches("[01]{11}"))
            throw new IllegalArgumentException("Enter exactly 11 binary digits");
        message = value;
        clock = 0;
        registers = new int[4];
        pending = null;
        incoming = feedback = oldR2 = oldR3 = 0;
        resetState();
        refreshDrawing();
    }

    public void prepareNext() {
        if (message.isEmpty() || isFinished() || pending != null) return;
        incoming = message.charAt(10 - clock) - '0';
        oldR2 = registers[2]; oldR3 = registers[3];
        feedback = incoming ^ oldR3;
        pending = new int[] {feedback, registers[0], registers[1], oldR2 ^ feedback};
        refreshDrawing();
    }

    public void commitNext() {
        if (pending == null) return;
        registers = pending;
        pending = null;
        clock++;
        refreshDrawing();
    }

    /** One full hardware clock for tests or non-interactive callers. */
    public void advanceOneStep() { prepareNext(); commitNext(); }
    public boolean hasPreview() { return pending != null; }
    public boolean isFinished() { return !message.isEmpty() && clock == 11; }
    public int getClock() { return clock; }
    public String getMessage() { return message; }
    public int[] getRegisters() { return registers.clone(); }
    public String getCheckBits() {
        if (!isFinished()) throw new IllegalStateException("Complete all 11 clocks first");
        return "" + registers[0] + registers[1] + registers[2] + registers[3];
    }
    public String getCodeword() { return getCheckBits() + message; }
    public String getCalculations() {
        if (clock == 0 && pending == null) return "Registers hold 0. Follow the signals to calculate their next values.";
        return "Input " + incoming + " XOR old R3 " + oldR3 + " = " + feedback
             + "     |     Old R2 " + oldR2 + " XOR feedback " + feedback + " = " + (oldR2 ^ feedback);
    }
    private void refreshDrawing() {
        for (int i = 0; i < 4; i++) setBit("R" + i, registers[i]);
        setBit("XOR0", oldR2 ^ feedback);
        setBit("XOR1", feedback);
        setPreview(pending, incoming, feedback,
                incoming + " ⊕ " + oldR3 + " = " + feedback,
                oldR2 + " ⊕ " + feedback + " = " + (oldR2 ^ feedback), "XOR1", "XOR0");
        if (pending == null) clearPreview();
    }
}

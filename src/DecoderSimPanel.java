/** Decoder circuit: XOR -> R0 -> R1 -> R2 -> XOR -> R3, with register feedback. */
public class DecoderSimPanel extends CircuitPanel {
    private static final long serialVersionUID = 1L;

    public DecoderSimPanel() {
        super(
            new CircuitElement("XOR0", ComponentType.XOR),
            new CircuitElement("R0", ComponentType.REGISTER),
            new CircuitElement("R1", ComponentType.REGISTER),
            new CircuitElement("R2", ComponentType.REGISTER),
            new CircuitElement("XOR1", ComponentType.XOR),
            new CircuitElement("R3", ComponentType.REGISTER)
        );
        setFeedback("R3", 100, "XOR0", "XOR1");
    }
}

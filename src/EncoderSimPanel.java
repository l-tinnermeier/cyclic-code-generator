/** Encoder appearance; encoding logic can update elements by their stable IDs. */
public class EncoderSimPanel extends CircuitPanel {
    private static final long serialVersionUID = 1L;

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
    }
}

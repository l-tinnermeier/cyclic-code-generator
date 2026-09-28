import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Shape;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.swing.JPanel;

/** Shared circuit drawing. Call state setters on Swing's event-dispatch thread. */
public class CircuitPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public enum ComponentType { REGISTER, XOR }
    public enum Port { LEFT, RIGHT, TOP, BOTTOM }

    /** Layout specification; each panel keeps its own copy and display state. */
    public static final class CircuitElement {
        private final String id;
        private final ComponentType type;
        private final Rectangle2D.Double bounds = new Rectangle2D.Double();
        private int bit;
        private Color color;

        public CircuitElement(String id, ComponentType type) {
            this.id = Objects.requireNonNull(id, "id");
            this.type = Objects.requireNonNull(type, "type");
        }
    }

    private static final class Connection {
        final String from, to;
        final Port fromPort, toPort;
        final Double viaY;
        final boolean arrow;

        Connection(String from, Port fromPort, String to, Port toPort,
                   Double viaY, boolean arrow) {
            this.from = from;
            this.fromPort = fromPort;
            this.to = to;
            this.toPort = toPort;
            this.viaY = viaY;
            this.arrow = arrow;
        }
    }

    private static final Color ONE_COLOR = new Color(70, 170, 100);
    private final Map<String, CircuitElement> elements = new LinkedHashMap<>();
    private final List<Connection> connections = new ArrayList<>();
    private String[] feedbackTargets = new String[0];
    private String feedbackSource;
    private double feedbackY;
    private String inputGate;
    private int[] nextRegisters;
    private int incoming, feedback;
    private String firstExpression = "", secondExpression = "", firstGate = "", secondGate = "";

    public final void setInputGate(String id) { element(id); inputGate = id; }
    public final void setPreview(int[] next, int bit, int feedbackBit,
            String expressionA, String expressionB, String gateA, String gateB) {
        nextRegisters = next == null ? null : next.clone();
        incoming = bit; feedback = feedbackBit;
        firstExpression = expressionA; secondExpression = expressionB;
        firstGate = gateA; secondGate = gateB;
        repaint();
    }
    public final void clearPreview() { nextRegisters = null; repaint(); }
    private static Color signalColor(int bit) { return bit == 1 ? new Color(26, 132, 96) : new Color(110, 119, 125); }
    private static void centered(Graphics2D g, String value, double x, double y) {
        g.drawString(value, (float)(x - g.getFontMetrics().stringWidth(value) / 2.0), (float)y);
    }

    public CircuitPanel(CircuitElement... layout) {
        setBackground(new Color(245, 249, 250));
        setPreferredSize(new Dimension(1100, 380));
        double x = 100;
        CircuitElement previous = null;
        for (CircuitElement specification : layout) {
            CircuitElement element = new CircuitElement(specification.id, specification.type);
            if (elements.containsKey(element.id)) {
                throw new IllegalArgumentException("Duplicate element: " + element.id);
            }
            if (previous != null) {
                x += previous.type == ComponentType.REGISTER
                        && element.type == ComponentType.REGISTER ? 100 : 50;
            }
            double size = element.type == ComponentType.REGISTER ? 100 : 80;
            element.bounds.setRect(x, 200 - size / 2, size, size);
            elements.put(element.id, element);
            if (previous != null) {
                connect(previous.id, Port.RIGHT, element.id, Port.LEFT, true);
            }
            previous = element;
            x += size;
        }
    }

    /** Connect two named shape edges directly. */
    public final void connect(String from, Port fromPort, String to, Port toPort, boolean arrow) {
        addConnection(from, fromPort, to, toPort, null, arrow);
    }

    /** Connect via a horizontal lane, with vertical segments at each end. */
    public final void connectViaY(String from, Port fromPort, String to, Port toPort,
                                  double laneY, boolean arrow) {
        if (!Double.isFinite(laneY)) throw new IllegalArgumentException("Invalid lane Y");
        addConnection(from, fromPort, to, toPort, laneY, arrow);
    }

    private void addConnection(String from, Port fromPort, String to, Port toPort,
                               Double viaY, boolean arrow) {
        element(from);
        element(to);
        connections.add(new Connection(from, Objects.requireNonNull(fromPort),
                to, Objects.requireNonNull(toPort), viaY, arrow));
        repaint();
    }

    /** One shared top feedback bus; arrows enter each target's top edge. */
    public final void setFeedback(String source, double laneY, String... targets) {
        element(source);
        if (!Double.isFinite(laneY)) throw new IllegalArgumentException("Invalid lane Y");
        for (String target : targets) element(target);
        feedbackSource = source;
        feedbackY = laneY;
        feedbackTargets = targets.clone();
        repaint();
    }

    public final void setBit(String id, int bit) {
        checkBit(bit);
        CircuitElement element = element(id);
        element.bit = bit;
        element.color = null;
        repaint();
    }

    /** Validate all updates first, then update the display with one repaint. */
    public final void setState(Map<String, Integer> state) {
        Map<String, Integer> updates = new LinkedHashMap<>(state);
        updates.forEach((id, bit) -> { element(id); checkBit(bit); });
        updates.forEach((id, bit) -> {
            CircuitElement element = element(id);
            element.bit = bit;
            element.color = null;
        });
        repaint();
    }

    /** Override a component's fill; its next bit update restores bit-based coloring. */
    public final void setElementColor(String id, Color color) {
        element(id).color = Objects.requireNonNull(color, "color");
        repaint();
    }

    public final int getBit(String id) { return element(id).bit; }

    /** Defensive copy, in the 1100 by 380 logical drawing coordinates; callers cannot accidentally move a shape. */
    public final Rectangle2D.Double getElementBounds(String id) {
        return (Rectangle2D.Double) element(id).bounds.clone();
    }

    public final void resetState() {
        for (CircuitElement element : elements.values()) {
            element.bit = 0;
            element.color = null;
        }
        repaint();
    }

    private CircuitElement element(String id) {
        CircuitElement element = elements.get(id);
        if (element == null) throw new IllegalArgumentException("Unknown element: " + id);
        return element;
    }

    private static void checkBit(int bit) {
        if (bit != 0 && bit != 1) throw new IllegalArgumentException("Bit must be 0 or 1");
    }

    private Point2D.Double port(String id, Port port) {
        Rectangle2D.Double b = element(id).bounds;
        switch (port) {
            case LEFT: return new Point2D.Double(b.getMinX(), b.getCenterY());
            case RIGHT: return new Point2D.Double(b.getMaxX(), b.getCenterY());
            case TOP: return new Point2D.Double(b.getCenterX(), b.getMinY());
            case BOTTOM: return new Point2D.Double(b.getCenterX(), b.getMaxY());
            default: throw new AssertionError(port);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            double scale = Math.min(getWidth() / 1100.0, getHeight() / 380.0);
            g2.translate((getWidth() - 1100 * scale) / 2, (getHeight() - 380 * scale) / 2);
            g2.scale(scale, scale);
            g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
            g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
            g2.setColor(Color.DARK_GRAY);
            for (Connection connection : connections) {
                g2.setColor(nextRegisters == null ? Color.GRAY : signalColor(element(connection.from).bit));
                Point2D.Double start = port(connection.from, connection.fromPort);
                Point2D.Double end = port(connection.to, connection.toPort);
                if (connection.viaY != null) {
                    line(g2, start.x, start.y, start.x, connection.viaY, false);
                    line(g2, start.x, connection.viaY, end.x, connection.viaY, false);
                    start = new Point2D.Double(end.x, connection.viaY);
                }
                line(g2, start.x, start.y, end.x, end.y, connection.arrow);
            }
            if (feedbackSource != null && feedbackTargets.length > 0) {
                g2.setColor(nextRegisters == null ? Color.GRAY : signalColor(feedback));
                Point2D.Double source = port(feedbackSource, Port.TOP);
                double minX = source.x, maxX = source.x;
                for (String id : feedbackTargets) {
                    double x = port(id, Port.TOP).x;
                    minX = Math.min(minX, x);
                    maxX = Math.max(maxX, x);
                }
                line(g2, source.x, source.y, source.x, feedbackY, false);
                line(g2, minX, feedbackY, maxX, feedbackY, false);
                for (String id : feedbackTargets) {
                    Point2D.Double target = port(id, Port.TOP);
                    line(g2, target.x, feedbackY, target.x, target.y, true);
                    if (target.x > minX && target.x < maxX) {
                        g2.fill(new Ellipse2D.Double(target.x - 5, feedbackY - 5, 10, 10));
                    }
                }
            }
            if (inputGate != null) {
                Point2D.Double p = port(inputGate, Port.BOTTOM);
                g2.setColor(nextRegisters == null ? Color.GRAY : signalColor(incoming));
                line(g2, p.x, p.y + 70, p.x, p.y, true);
                g2.setColor(Color.DARK_GRAY);
                centered(g2, nextRegisters == null ? "Input" : "Input " + incoming, p.x, p.y + 94);
            }
            g2.setColor(Color.DARK_GRAY);
            centered(g2, "g(x) = 1 + x³ + x⁴   •   LSB first", 550, 24);
            centered(g2, nextRegisters == null ? "Feedback bus" : "Feedback = " + feedback, 550, 52);
            for (CircuitElement element : elements.values()) {
                Rectangle2D.Double b = element.bounds;
                boolean register = element.type == ComponentType.REGISTER;
                Shape shape = register ? b : new Ellipse2D.Double(b.x, b.y, b.width, b.height);
                g2.setColor(element.color != null ? element.color
                        : element.bit == 1 && (register || nextRegisters != null) ? new Color(205, 237, 223) : Color.WHITE);
                g2.fill(shape);
                g2.setColor(new Color(77, 95, 105));
                g2.draw(shape);
                g2.setColor(Color.DARK_GRAY);
                g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
                centered(g2, register ? "holds " + element.bit : "⊕", b.getCenterX(), b.getCenterY() + 8);
                g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
                centered(g2, element.id, b.getCenterX(), b.getMinY() - 16);
                if (register) {
                    int index = Integer.parseInt(element.id.substring(1));
                    centered(g2, nextRegisters == null ? "" : "next " + nextRegisters[index],
                            b.getCenterX(), b.getMaxY() + 27);
                } else if (nextRegisters != null) {
                    String expression = element.id.equals(firstGate) ? firstExpression
                            : element.id.equals(secondGate) ? secondExpression : "";
                    centered(g2, expression, b.getCenterX(), b.getMinY() - 40);
                }
            }
            g2.setColor(Color.DARK_GRAY);
            g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
            centered(g2, "Boxes hold bits until the clock. XOR gives 1 when its two inputs differ.", 550, 353);
            centered(g2, "The 1 and x³ terms select the feedback destinations; degree 4 requires four registers.", 550, 375);

        } finally {
            g2.dispose();
        }
    }

    private static void line(Graphics2D g, double x1, double y1, double x2, double y2,
                             boolean arrow) {
        g.draw(new Line2D.Double(x1, y1, x2, y2));
        if (!arrow || (x1 == x2 && y1 == y2)) return;
        double angle = Math.atan2(y2 - y1, x2 - x1);
        for (double offset : new double[] {-Math.PI / 6, Math.PI / 6}) {
            g.draw(new Line2D.Double(x2, y2,
                    x2 - 10 * Math.cos(angle + offset), y2 - 10 * Math.sin(angle + offset)));
        }
    }
}

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
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

    public CircuitPanel(CircuitElement... layout) {
        setBackground(new Color(210, 224, 228));
        setPreferredSize(new Dimension(1241, 571));
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
            element.bounds.setRect(x, 350 - size / 2, size, size);
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

    /** Defensive copy, in panel coordinates; callers cannot accidentally move a shape. */
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
            g2.setStroke(new BasicStroke(4.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
            g2.setColor(Color.DARK_GRAY);
            for (Connection connection : connections) {
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
                        g2.fill(new Ellipse2D.Double(target.x - 10, feedbackY - 10, 20, 20));
                    }
                }
            }
            for (CircuitElement element : elements.values()) {
                g2.setColor(element.color != null ? element.color
                        : element.bit == 1 ? ONE_COLOR : Color.DARK_GRAY);
                Rectangle2D.Double b = element.bounds;
                if (element.type == ComponentType.REGISTER) g2.fill(b);
                else g2.fill(new Ellipse2D.Double(b.x, b.y, b.width, b.height));
            }
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

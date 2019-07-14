package gprocx.step;

//import com.sun.org.apache.xerces.internal.impl.xpath.XPath;
import gprocx.core.*;
import gprocx.mainUI.XFrame;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.UUID;

public class Pipeline {

    private XFrame frame;

    // variables for the pipeline
    private String type;
    private String documentation;
    private ArrayList<Port> inputs;
    private ArrayList<Port> outputs;
    private ArrayList<StepOption> options;

    protected ArrayList<Pipeline> children;
    protected ArrayList<QName> qnames;
    private ArrayList<Pipe> pipes;
    private boolean isAtomic = true;
    private boolean isBrief = false;
    protected int retract = 0;
    protected UUID uuid;

    // variables for drawing
    protected RoundRectangle2D shape;
    protected int x, y, w, h;
    private Rectangle2D inShape;
    private Rectangle2D outShape;

    public Pipeline(XFrame frame, String type) {
        this.frame = frame;

        this.shape = new RoundRectangle2D.Double(0, 0, 0, 0, 15, 15);
        this.qnames = new ArrayList<QName>();
        this.children = new ArrayList<Pipeline>();
        this.pipes = new ArrayList<Pipe>();
        this.type = type;
        this.uuid = UUID.randomUUID();

        this.inputs = new ArrayList<Port>();
        this.outputs = new ArrayList<Port>();
        this.options = new ArrayList<StepOption>();

        this.setPipeline();
    }

    public void setPipeline() {

        StepInfo.setPipelineInfo(frame, this);

        this.qnames.add(new QName("", "name", ""));
        this.qnames.add(new QName("", "use-when", ""));
        this.qnames.add(new QName("", "xml:id", ""));
        this.qnames.add(new QName("", "xml:base", ""));
        for (StepOption option : this.getOptions()) {
            this.qnames.add(new QName("", option.getName(), option.getSelect()));
        }

        //this.type.setX(this.x - this.metrics.computeStringWidth(this.type.getText()) / 2);
        //this.type.setY(this.y - this.metrics.getXheight() / 2 + metrics.getAscent() / 2);

        this.frame.updateInfo();
    }

    public void setIsAtomic(boolean atomic) {
        isAtomic = atomic;
    }

    public boolean isAtomic() {
        return isAtomic;
    }

    public String toString() {
        String code = "";

        // print the retract
        for (int i = 0; i < this.retract; i++) {
            code += "    ";
        }

        code += "<" + getType();

        for (QName qname : qnames) {
            if (!qname.getValue().equals("")) {
                code += " " + qname;
            }
        }

        if (this.isAtomic && this.isBrief) {
            code += "/>\n";
        } else {
            code += ">\n";

            for (Port inPort : this.inputs) {
                for (int i = 0; i < this.retract + 1; i++) {
                    code += "    ";
                }
                code += inPort.toString();
            }

            if (!isAtomic) {
                for (Port outPort : this.outputs) {
                    for (int i = 0; i < this.retract + 1; i++) {
                        code += "    ";
                    }
                    code += outPort.toString();
                }
                code += "\n";
            }

            // print children pipelines
            for (Pipeline pipeline : this.children) {
                code += pipeline.toString();
            }


            for (int i = 0; i < this.retract; i++) {
                code += "    ";
            }
            code += "</" + this.type + ">\n";
        }

        return code;
    }

    public ArrayList<StepOption> getOptions() {
        return options;
    }

    public ArrayList<Port> getInputs() {
        return inputs;
    }

    public ArrayList<Port> getOutputs() {
        return outputs;
    }

    public ArrayList<Pipe> getPipes() {
        return pipes;
    }

    public Port findInPort(String name) {
        for (Port port : this.inputs) {
            if (port.getPort().equals(name)) {
                return port;
            }
        }
        return null;
    }

    public Port findOutPort(String name) {
        for (Port port : this.outputs) {
            if (port.getPort().equals(name)) {
                return port;
            }
        }
        return null;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public void addInput(Port input) {
        input.setRetract(this.retract + 1);
        this.inputs.add(input);
    }

    public void addOption(StepOption option) {
        this.options.add(option);
    }

    public void addOutput(Port output) {
        output.setRetract(this.retract + 1);
        this.outputs.add(output);
    }

    public void addPipe(Pipe pipe) {
        for (Pipeline child : this.children) {
            // make sure the pipe matches the pipeline
            if (child != pipe.getToPipeline()) {
                continue;
            }
            // match the port name with the pipe
            for (Port inport : child.getInputs()) {
                if (inport.getPort().equals(pipe.getToPort().getPort())) {
                    inport.addPipes(pipe);
                    break;
                }
            }
        }

        this.pipes.add(pipe);
        this.frame.updateInfo();
    }

    public String getType() { return type; }

    public void updatePipes() {
        for (Pipe pipe : this.pipes) {
            pipe.updatePipe();
        }
    }

    public boolean setElementValue(String key, String value, String uri) {
        for (QName element : qnames) {
            if (element.getLexical() == key && element.getUri() == uri) {
                element.setValue(value);
                return true;
            }
        }

        // if not find the element
        return false;
    }

    public void addQName(QName q) {
        this.qnames.add(q);
    }

    public ArrayList<QName> getQName() {
        return this.qnames;
    }

    public String getName() {
        for (QName qname : this.qnames) {
            if (qname.getLexical().equals("name")) {
                return qname.getValue();
            }
        }
        return "";
    }

    public void addChildren(Pipeline step) {
        step.setRetract(this.retract + 1);
        this.children.add(step);
    }

    public void setRetract(int retract) {
        this.retract = retract;
    }

    public int getRetract() {
        return retract;
    }

    // functions for drawing
    public void setShape(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        this.shape = new RoundRectangle2D.Double(x - w / 2, y - h / 2,
                w, h, 10, 10);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void drawSelf(Graphics2D g2, FontMetrics metrics) {
        g2.draw(shape);

        //Font sansbold14 = new Font("SansSerif", Font.BOLD, 24);
        int xText = this.x - metrics.stringWidth(this.type) / 2;
        int yText = this.y - metrics.getHeight() / 2 + metrics.getAscent();

        g2.drawString(this.type, xText, yText);
    }

    public void drawChildren(Graphics2D g2, FontMetrics metrics) {
        for (Pipeline child : children) {
            child.drawSelf(g2, metrics);
        }
    }

    public void drawPipes(Graphics2D g2) {
        for (Pipe pipe : pipes) {
            pipe.draw(g2);
        }
    }

    public void drawPorts(Graphics2D g2, FontMetrics metrics) {
        int inX = 0;
        int inY = 0;
        int inW = Toolkit.getDefaultToolkit().getScreenSize().width/10;
        int inH = Toolkit.getDefaultToolkit().getScreenSize().height/20;
        int outX = inX;
        int outY = Toolkit.getDefaultToolkit().getScreenSize().height - Toolkit.getDefaultToolkit().getScreenSize().height/5;
        int outW = inW;
        int outH = inH;
        g2.draw(new Rectangle2D.Double(inX, inY, inW, inH));
        g2.draw(new Rectangle2D.Double(outX, outY, outW, outH));

        int xText1 = inX + inW / 2 - metrics.stringWidth("Input ports") / 2;
        int yText1 = inY + inH / 2 - metrics.getHeight() / 2 + metrics.getAscent();
        g2.drawString("Input ports", xText1, yText1);

        int xText2 = outX + outW / 2 - metrics.stringWidth("Output ports") / 2;
        int yText2 = outY + outH / 2 - metrics.getHeight() / 2 + metrics.getAscent();
        g2.drawString("Output ports", xText2, yText2);
    }

    public boolean contains(Point2D p) {
        if (Math.abs(p.getX() - this.x) <= this.w / 2) {
            if (Math.abs(p.getY() - this.y) <= this.h / 2) {
                return true;
            }
        }
        return false;
    }

    public Pipeline findChild(Point2D p) {
        for (Pipeline child : children) {
            if (child.contains(p)) {
                return child;
            }
        }
        return null;
    }

    public void updateQName(QName qname) {
        for (QName q : this.qnames) {
            if (q.getUri() == qname.getUri() && q.getLexical() == qname.getLexical()) {
                q.setValue(qname.getValue());
                return;
            }
        }
    }
}

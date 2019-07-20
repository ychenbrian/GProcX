package gprocx.step;

//import com.sun.org.apache.xerces.internal.impl.xpath.XPath;
import com.xml_project.morganaxproc.XProcInterfaceException;
import gprocx.core.*;
import gprocx.mainUI.XFrame;
import nu.xom.Element;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class GProcXPipeline implements Comparable<GProcXPipeline> {

    private XFrame frame;

    // variables for the pipeline
    private QName type;
    private String documentation;
    private ArrayList<GProcXPort> inputs = new ArrayList<GProcXPort>();
    private ArrayList<GProcXPort> outputs = new ArrayList<GProcXPort>();
    private ArrayList<GProcXOption> options = new ArrayList<GProcXOption>();

    private GProcXPipeline parent = null;
    private ArrayList<GProcXPipeline> children = new ArrayList<GProcXPipeline>();
    private ArrayList<QName> qnames = new ArrayList<QName>();
    private ArrayList<QName> namespaces = new ArrayList<QName>();
    private ArrayList<GProcXPipe> pipes = new ArrayList<GProcXPipe>();
    private GProcXPipe outPipe;
    private boolean isAtomic = true;
    private UUID uuid;

    // variables for drawing
    private RoundRectangle2D.Double shape = new RoundRectangle2D.Double(0, 0, 0, 0, 15, 15);
    private int x, y, w, h;
    private Rectangle2D inShape;
    private Rectangle2D outShape;

    public GProcXPipeline(XFrame frame, String type) throws XProcInterfaceException {
        this.frame = frame;

        this.shape = new RoundRectangle2D.Double(0, 0, 0, 0, 15, 15);
        this.qnames = new ArrayList<QName>();
        this.children = new ArrayList<GProcXPipeline>();
        this.pipes = new ArrayList<GProcXPipe>();
        this.type = new QName(type);
        this.uuid = UUID.randomUUID();

        this.inputs = new ArrayList<GProcXPort>();
        this.outputs = new ArrayList<GProcXPort>();
        this.options = new ArrayList<GProcXOption>();

        this.setPipeline();
    }

    public GProcXPipeline(XFrame frame, Element element) throws XProcInterfaceException {
        this.frame = frame;

        this.shape = new RoundRectangle2D.Double(0, 0, 0, 0, 15, 15);
        this.type = new QName(element.getQualifiedName());
        this.uuid = UUID.randomUUID();


        this.setPipeline();
    }

    public GProcXPipeline(XFrame frame) {
        this.frame = frame;
        this.uuid = UUID.randomUUID();
    }

    public void setPipeline() throws XProcInterfaceException {

        StepInfo.setPipelineInfo(this);

        this.qnames.add(new QName("", "name", ""));
        this.qnames.add(new QName("", "use-when", ""));
        this.qnames.add(new QName("", "xml:id", ""));
        this.qnames.add(new QName("", "xml:base", ""));
        for (GProcXOption option : this.getOptions()) {
            this.qnames.add(new QName("", option.getName(), option.getSelect()));
        }

        this.frame.updateInfo();
    }

    public void setIsAtomic(boolean atomic) {
        isAtomic = atomic;
    }

    public boolean isAtomic() {
        return StepInfo.isAtomic(this.getType());
    }

    public boolean isBrief() {
        for (GProcXPort input : inputs) {
            if (!input.isBasic()) {
                return false;
            }
        }
        for (GProcXPort output : outputs) {
            if (!output.isBasic()) {
                return false;
            }
        }
        return true;
    }

    public void updateInfo() {
        this.sortChildren();

        for (int i = 0; i < this.getChildren().size(); i++) {
            if (i == 0) {
                GProcXPipe pipe = getDefaultInPipe(this.getChildren().get(0));
                if (pipe == null) {
                    continue;
                }
                pipe.setFromPipeline(this, true);
                pipe.setFromPort(getPrimaryInport(this));
            } else {
                GProcXPipe pipe = getDefaultInPipe(this.getChildren().get(i));
                if (pipe == null) {
                    continue;
                }
                pipe.setFromPipeline(this.getChildren().get(i - 1), false);
                pipe.setFromPort(getPrimaryOutport(this.getChildren().get(i - 1)));
            }
        }

        if (this.outPipe == null) {
            this.outPipe = new GProcXPipe(this.frame);
            this.outPipe.setDefault(true);
            this.outPipe.setToPipeline(this, true);
            this.outPipe.setToPort(getPrimaryOutport(this));
        }
        if (!this.getChildren().isEmpty()) {
            this.outPipe.setFromPipeline(this.getChildren().get(this.getChildren().size() - 1), false);
            this.outPipe.setFromPort(getPrimaryInport(this.getChildren().get(this.getChildren().size() - 1)));
        }
    }

    public static GProcXPipe getDefaultInPipe(GProcXPort port) {
        if (port != null) {
            for (GProcXPipe pipe : port.getPipes()) {
                if (pipe.isDefault()) {
                    return pipe;
                }
            }
        }
        return null;
    }

    public static GProcXPipe getDefaultInPipe(GProcXPipeline pipeline) {
        if (pipeline != null) {
            GProcXPort primary = getPrimaryInport(pipeline);
            return getDefaultInPipe(primary);
        }
        return null;
    }

    public void setParent(GProcXPipeline parent) {
        this.parent = parent;
    }

    public GProcXPipeline getParent() {
        return parent;
    }

    public ArrayList<GProcXOption> getOptions() {
        return options;
    }

    public ArrayList<GProcXPort> getInputs() {
        return inputs;
    }

    public ArrayList<GProcXPort> getOutputs() {
        return outputs;
    }

    public ArrayList<GProcXPipe> getPipes() {
        return pipes;
    }

    public GProcXPort findInPort(String name) {
        for (GProcXPort port : this.inputs) {
            if (port.getPort().equals(name)) {
                return port;
            }
        }
        return null;
    }

    public GProcXPort findOutPort(String name) {
        for (GProcXPort port : this.outputs) {
            if (port.getPort().equals(name)) {
                return port;
            }
        }
        return null;
    }

    public XFrame getFrame() {
        return frame;
    }

    public String getDocumentation() {
        return documentation;
    }

    public GProcXPipeline findPipeline(String name) {
        if (this.getName().equals(name)) {
            return this;
        }
        for (GProcXPipeline child : this.children) {
            if (child.getName().equals(name)) {
                return child;
            }
        }
        return null;
    }

    public GProcXPort findPort(String port) {
        for (GProcXPort input : this.inputs) {
            if (input.getPort().equals(port)) {
                return input;
            }
        }
        for (GProcXPort output : this.outputs) {
            if (output.getPort().equals(port)) {
                return output;
            }
        }
        return null;
    }

    public void setType(String type) {
        this.type = new QName(type);
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public void addInput(GProcXPort input) {
        for (GProcXPort in : this.getInputs()) {
            if (in.getPort().equals(input.getPort())) {
                this.getInputs().remove(in);
                this.getInputs().add(input);
                return;
            }
        }
        this.getInputs().add(input);
    }

    public void addOption(GProcXOption option) {
        this.options.add(option);
    }

    public void addOutput(GProcXPort output) {
        for (GProcXPort out : this.getOutputs()) {
            if (out.getPort().equals(output.getPort())) {
                this.getOutputs().remove(out);
                this.getOutputs().add(output);
                return;
            }
        }
        this.getOutputs().add(output);
    }

    public void addPipe(GProcXPipe pipe) {
        if (hasPipe(pipe)) {
            return;
        }

        // set the main out pipe
        if (pipe.getToPipeline() == this) {
            for (GProcXPort outport : this.getOutputs()) {
                if (outport.getPort().equals(pipe.getToPort().getPort())) {
                    outport.addPipe(pipe);
                    break;
                }
            }
        } else {
            for (GProcXPipeline child : this.getChildren()) {
                // make sure the pipe matches the pipeline
                if (child != pipe.getToPipeline()) {
                    continue;
                }
                // match the port name with the pipe
                if (child.getInputs().size() != 0) {
                    for (GProcXPort inport : child.getInputs()) {

                        if (inport.getPort().equals(pipe.getToPort().getPort())) {
                            inport.addPipe(pipe);
                            break;
                        }
                    }
                }
            }
        }

        this.pipes.add(pipe);
        this.frame.updateInfo();
    }

    public String getType() { return type.getUriLexical(); }

    public static GProcXPort getPrimaryInport(GProcXPipeline pipeline) {
        if (pipeline != null) {
            for (GProcXPort inport : pipeline.getInputs()) {
                if (inport.isPrimary()) {
                    return inport;
                }
            }
        }
        return null;
    }

    public static GProcXPort getPrimaryOutport(GProcXPipeline pipeline) {
        if (pipeline != null) {
            for (GProcXPort outport : pipeline.getOutputs()) {
                if (outport.isPrimary()) {
                    return outport;
                }
            }
        }
        return null;
    }

    // sort the child pipelines, and return the first child
    // if there is no child, return self
    public GProcXPipeline sortChildren() {
        if (this.getChildren().size() == 0) {
            return this;
        } else {
            Collections.sort(this.getChildren());
        }
        return this.getChildren().get(0);
    }

    public int compareTo(GProcXPipeline pipeline) {
        if (this.getY() < pipeline.getY()) {
            return -1;
        } else if (this.getY() > pipeline.getY()) {
            return 1;
        }
        return 0;
    }

    public void updatePipes() {
        for (GProcXPipe pipe : this.pipes) {
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

    public void addNamespace(QName ns) {
        this.namespaces.add(ns);
    }

    public ArrayList<QName> getQNames() {
        return this.qnames;
    }

    public ArrayList<QName> getNamespaces() {
        return namespaces;
    }

    public boolean hasDefineNS(QName ns) {
        if (this.getParent() != null) {
            if (this.getParent().hasDefineNS(ns)) {
                return true;
            }
            for (QName namespace : this.getParent().getNamespaces()) {
                if (namespace.getLexical().equals(ns.getLexical())) {
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<GProcXPipeline> getChildren() {
        return children;
    }

    public String getName() {
        for (QName qname : this.qnames) {
            if (qname.getLexical().equals("name")) {
                return qname.getValue();
            }
        }
        return "";
    }

    public void addChildren(GProcXPipeline step) {
        step.setParent(this);

        GProcXPipe pipe = new GProcXPipe(this.getFrame());
        this.children.add(step);

        pipe.setToPipeline(step, false);
        pipe.setToPort(getPrimaryInport(step));
        pipe.setDefault(true);
        this.addPipe(pipe);
    }

    public boolean hasPipe(GProcXPipe pipe) {
        for (GProcXPipe oldPipe : this.getPipes()) {
            if (oldPipe.equals(pipe)) {
                return true;
            }
        }
        return false;
    }

    // functions for drawing
    public void setShape(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        this.shape.x = x - w / 2;
        this.shape.y = y - h / 2;
        this.shape.width = w;
        this.shape.height = h;

        if (this.getParent() != null) {
            this.getParent().updatePipes();
        }

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
        int xText = this.x - metrics.stringWidth(this.getType()) / 2;
        int yText = this.y - metrics.getHeight() / 2 + metrics.getAscent();

        g2.drawString(this.getType(), xText, yText);
    }

    public void drawChildren(Graphics2D g2, FontMetrics metrics) {
        for (GProcXPipeline child : children) {
            child.drawSelf(g2, metrics);
        }
    }

    public void drawPipes(Graphics2D g2) {
        for (GProcXPipe pipe : pipes) {
            pipe.draw(g2);
        }
        if (this.outPipe.isValid()) {
            this.outPipe.draw(g2);
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

    public GProcXPipeline findChild(Point2D p) {
        for (GProcXPipeline child : children) {
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

    public String toString(int retract) {
        String code = "";

        // print the retract
        for (int i = 0; i < retract; i++) {
            code += "    ";
        }

        code += "<" + getType();

        for (QName namespace : this.namespaces) {
            if (!hasDefineNS(namespace)) {
                if (!namespace.getValue().equals("")) {
                    code += " " + namespace;
                }
            }
        }

        for (QName qname : this.qnames) {
            if (!qname.getValue().equals("")) {
                code += " " + qname;
            }
        }


        if (this.isAtomic && this.isBrief()) {
                    code += "/>\n";
        } else {
            code += ">\n";

            for (GProcXPort inPort : this.inputs) {
                code += inPort.toString(retract + 1);
            }

            if (!isAtomic) {
                for (GProcXPort outPort : this.outputs) {
                    code += outPort.toString(retract + 1);
                }
                code += "\n";
            }

            // print children pipelines
            for (GProcXPipeline pipeline : this.children) {
                code += pipeline.toString(retract + 1);
            }


            for (int i = 0; i < retract; i++) {
                code += "    ";
            }
            code += "</" + this.getType() + ">\n";
        }

        return code;
    }
}

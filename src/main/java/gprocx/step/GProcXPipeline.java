package gprocx.step;

import com.xml_project.morganaxproc.XProcInterfaceException;
import gprocx.core.*;
import gprocx.mainUI.XFrame;
import nu.xom.Element;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class GProcXPipeline implements Comparable<GProcXPipeline>, Serializable {
    // variables for the pipeline
    private QName type;
    private ArrayList<GProcXPort> inputs = new ArrayList<GProcXPort>();
    private ArrayList<GProcXPort> outputs = new ArrayList<GProcXPort>();

    private GProcXPipeline parent = null;
    private ArrayList<GProcXPipeline> children = new ArrayList<GProcXPipeline>();
    private ArrayList<GProcXDoc> docs = new ArrayList<GProcXDoc>();
    private ArrayList<QName> qnames = new ArrayList<QName>();
    private ArrayList<QName> namespaces = new ArrayList<QName>();
    private ArrayList<GProcXPipe> pipes = new ArrayList<GProcXPipe>();
    private GProcXPipe outPipe;
    private boolean isAtomic = true;
    private boolean isBuildin = true;
    private boolean outPipeFlag = true;
    private UUID uuid = null;

    // variables for drawing
    private RoundRectangle2D.Double shape = new RoundRectangle2D.Double(0, 0, 0, 0, 15, 15);
    private int x, y, w, h;

    public GProcXPipeline(XFrame frame, String type) throws XProcInterfaceException {
        this.shape = new RoundRectangle2D.Double(0, 0, 0, 0, 15, 15);
        this.type = new QName(type);
        this.uuid = UUID.randomUUID();

        StepInfo.setPipelineInfo(frame, this);
        frame.updateInfo();
    }

    public GProcXPipeline(XFrame frame, Element element) throws XProcInterfaceException {
        this.shape = new RoundRectangle2D.Double(0, 0, 0, 0, 15, 15);
        this.type = new QName(element.getQualifiedName());
        this.uuid = UUID.randomUUID();

        StepInfo.setPipelineInfo(frame, this);
        frame.updateInfo();
    }

    public GProcXPipeline() {
        this.uuid = UUID.randomUUID();
    }

    public void setAtomic(boolean atomic) {
        isAtomic = atomic;
    }

    public boolean isAtomic() {
        return this.isAtomic;
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
                if (getPrimaryInport(this) == null) {
                    continue;
                }
                pipe.setFromPipeline(this, true);
                pipe.setFromPort(getPrimaryInport(this));
            } else {
                GProcXPipe pipe = getDefaultInPipe(this.getChildren().get(i));
                if (pipe == null) {
                    continue;
                }
                if (getPrimaryOutport(this.getChildren().get(i - 1)) == null) {
                    continue;
                }
                pipe.setFromPipeline(this.getChildren().get(i - 1), false);
                pipe.setFromPort(getPrimaryOutport(this.getChildren().get(i - 1)));
            }
        }

        if (this.outPipe == null && this.outPipeFlag == true) {
            if (getPrimaryOutport(this) != null) {
                this.outPipe = new GProcXPipe();
                this.outPipe.setDefault(true);
                this.outPipe.setToPipeline(this, true);
                this.outPipe.setToPort(getPrimaryOutport(this));
            } else {
                return;
            }
        }
        if (this.outPipe != null) {
            if (!this.getChildren().isEmpty()) {
                if (getPrimaryOutport(this.getChildren().get(this.getChildren().size() - 1)) != null) {
                    this.outPipe.setFromPipeline(this.getChildren().get(this.getChildren().size() - 1), false);
                    this.outPipe.setFromPort(getPrimaryOutport(this.getChildren().get(this.getChildren().size() - 1)));
                }
            }
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

    public GProcXPipe getOutPipe() {
        return this.outPipe;
    }

    public void setOutPipe(GProcXPipe outPipe) {
        this.outPipe = outPipe;
    }

    public void setOutPipeFlag(boolean outPipeFlag) {
        this.outPipeFlag = outPipeFlag;
    }

    public ArrayList<GProcXDoc> getDocs() {
        return docs;
    }

    public void addDoc(GProcXDoc newDoc) {
        this.getDocs().add(newDoc);
    }

    public void addOption(GProcXOption option) {
        QName newQ = new QName(option.getName(), option.getSelect());
        newQ.setRequired(option.isRequired());
        this.addQName(newQ);
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

    public void addPipe(XFrame frame, GProcXPipe pipe) {
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
        frame.updateInfo();
    }

    public boolean isBuildin() {
        return isBuildin;
    }

    public void setBuildin(boolean buildin) {
        isBuildin = buildin;
    }

    public String getType() { return type.getLexical(); }

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

    public void deleteChild(GProcXPipeline child) {
        ArrayList<GProcXPipe> pipeWL = new ArrayList<GProcXPipe>();

        for (GProcXPipe pipe : this.getPipes()) {
            if (pipe.getFromPipeline().getUUID() == child.getUUID()) {
                pipe.setFromPipeline(null, false);
                pipe.setFromPort(null);
            }
            if (pipe.getToPipeline().getUUID() == child.getUUID()) {
                pipeWL.add(pipe);
            }
        }
        for (GProcXPipe pipe : pipeWL) {
            this.getPipes().remove(pipe);
        }
        if (this.outPipe.getFromPipeline().getUUID() == child.getUUID()) {
            this.outPipe = null;
        }

        this.getChildren().remove(child);
    }

    public void addQName(QName q) {
        for (QName qname : this.getQNames()) {
            if (qname.getLexical().equals(q.getLexical())) {
                this.getQNames().remove(qname);
                break;
            }
        }
        this.getQNames().add(q);
    }

    public void addNamespace(QName newNS) {
        for (QName ns : this.getNamespaces()) {
            if (ns.getLexical().equals(newNS.getLexical())) {
                this.getNamespaces().remove(ns);
                break;
            }
        }
        this.namespaces.add(newNS);
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

    public void addChildren(XFrame frame, GProcXPipeline step) {
        step.setParent(this);

        GProcXPipe pipe = new GProcXPipe();
        this.children.add(step);

        pipe.setToPipeline(step, false);
        pipe.setToPort(getPrimaryInport(step));
        pipe.setDefault(true);
        this.addPipe(frame, pipe);
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

    public void drawSelf(XFrame frame, Graphics2D g2, FontMetrics metrics) {
        //Font sansbold14 = new Font("SansSerif", Font.BOLD, 24);
        int xText = this.x - metrics.stringWidth(this.getType()) / 2;
        int yText = this.y - metrics.getHeight() / 2 + metrics.getAscent();

        if (frame.getSelectedPipeline().getUUID() == this.getUUID()) {
            g2.setColor(new Color(255,0,0));
            g2.draw(shape);
            g2.drawString(this.getType(), xText, yText);
            g2.setColor(new Color(0,0,0));
        } else {
            //g2.setColor(new Color(0,0,0));
            g2.draw(shape);
            g2.drawString(this.getType(), xText, yText);
        }
    }

    public void drawChildren(XFrame frame, Graphics2D g2, FontMetrics metrics) {
        for (GProcXPipeline child : children) {
            child.drawSelf(frame, g2, metrics);
        }
    }

    public void drawPipes(Graphics2D g2) {
        for (GProcXPipe pipe : pipes) {
            pipe.draw(g2);
        }
        if (this.outPipe != null) {
            if (this.outPipe.isValid()) {
                this.outPipe.draw(g2);
            }
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

            if (!this.isBuildin()) {
                for (GProcXDoc doc : this.docs) {
                    code += doc.toString(retract + 1);
                }
            }

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

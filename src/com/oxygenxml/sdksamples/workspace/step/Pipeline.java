package com.oxygenxml.sdksamples.workspace.step;

//import com.sun.org.apache.xerces.internal.impl.xpath.XPath;
import com.oxygenxml.sdksamples.workspace.core.*;
import com.oxygenxml.sdksamples.workspace.mainUI.XFrame;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.UUID;

public class Pipeline {

    private XFrame frame;

    private String documentation;
    private ArrayList<Port> inputs;
    private ArrayList<Port> outputs;
    private ArrayList<StepOption> options;

    // variables for the pipeline
    protected ArrayList<Pipeline> children;
    protected ArrayList<QName> qnames;
    private boolean atomic = false;
    protected String type = null;
    protected int retract = 0;
    protected UUID uuid;

    // variables for drawing
    protected RoundRectangle2D shape;
    protected int x, y, w, h;
    FontMetrics metrics;

    public Pipeline(XFrame frame, String type) {
        this.frame = frame;

        this.shape = new RoundRectangle2D.Double(0, 0, 0, 0, 15, 15);
        this.qnames = new ArrayList<QName>();
        this.children = new ArrayList<Pipeline>();
        this.type = type;
        this.metrics = null;
        this.uuid = UUID.randomUUID();

        this.setPipeline(type);
    }

    public void setPipeline(String type) {
        Node fNode = this.frame.getAtomicInfo(type);

        this.inputs = new ArrayList<Port>();
        this.outputs = new ArrayList<Port>();
        this.options = new ArrayList<StepOption>();

        NodeList cList = fNode.getChildNodes();
        Node cNode;
        for (int i = 0; i < cList.getLength(); i++) {
            cNode = cList.item(i);
            if (cNode.getNodeType() == Node.ELEMENT_NODE) {
                if (cNode.getNodeName().equals("p:input")) {
                    inputs.add(new InPort(cNode));
                } else if (cNode.getNodeName().equals("p:output")) {
                    outputs.add(new OutPort(cNode));
                } else if (cNode.getNodeName().equals("p:option")) {
                    options.add(new StepOption(cNode));
                } else if (cNode.getNodeName().equals("p:documentation")) {
                    this.documentation = cNode.getTextContent();
                }
            }
        }

        this.qnames.add(new QName("", "name", ""));
        this.qnames.add(new QName("", "use-when", ""));
        this.qnames.add(new QName("", "xml:id", ""));
        this.qnames.add(new QName("", "xml:base", ""));
        for (StepOption option : this.options) {
            this.qnames.add(new QName("", option.getName(), option.getSelect()));
        }


        //this.type.setX(this.x - this.metrics.computeStringWidth(this.type.getText()) / 2);
        //this.type.setY(this.y - this.metrics.getXheight() / 2 + metrics.getAscent() / 2);

        this.frame.updateInfo();
    }

    public Pipeline(ArrayList<QName> elements) {
        this.qnames = elements;
    }

    public String generateCode() {
        String code = "";
        //System.out.println();

        // print the retract
        for (int i = 0; i < this.retract; i++) {
            code += "    ";
        }

        code += "<" + getType();

        for (QName q : qnames) {

            if (q.getValue() != null) {
                code += " ";
                if (q.getUri() != "") {
                    code += q.getUri() + ":";
                }
                code += q.getLexical() + "=\"";
                code += q.getValue() + "\"";
            }
        }

        if (!children.isEmpty()) {
            code += ">\n";
            for (Pipeline child : children) {
                code += child.generateCode();
            }
            // print the retract
            for (int i = 0; i < this.retract; i++) {
                code += "    ";
            }
            code += "</" + this.type + ">\n";
        } else {
            code += "/>\n";
        }

        return code;
    }

    public String getType() { return type; }

    public void setType(String type) {
        this.type = type;
    }

    public String getDocumentation() {
        return this.documentation;
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

    public ArrayList<Port> getInputs() {
        return inputs;
    }

    public ArrayList<Port> getOutputs() {
        return outputs;
    }

    public ArrayList<StepOption> getOptions() {
        return options;
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

    public void drawSelf(Graphics2D g2, Font font) {
        g2.draw(shape);

        //Font sansbold14 = new Font("SansSerif", Font.BOLD, 24);
        this.metrics = g2.getFontMetrics(font);
        int xText = this.x - metrics.stringWidth(this.type) / 2;
        int yText = this.y - metrics.getHeight() / 2 + metrics.getAscent();

        g2.drawString(this.type, xText, yText);
    }

    public void drawChildren(Graphics2D g2, Font font) {
        for (Pipeline child : children) {
            child.drawSelf(g2, font);
        }
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

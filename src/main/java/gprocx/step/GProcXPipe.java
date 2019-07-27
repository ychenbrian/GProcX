package gprocx.step;

import gprocx.core.GProcXPort;
import gprocx.core.QName;
import gprocx.mainUI.XFrame;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

public class GProcXPipe {

    private XFrame frame;

    private GProcXPort parent;
    private ArrayList<QName> qnames = new ArrayList<QName>();
    private ArrayList<QName> namespaces = new ArrayList<QName>();
    private GProcXPipeline fromPipeline = null;
    private GProcXPort fromPort = null;
    private GProcXPipeline toPipeline = null;
    private GProcXPort toPort = null;
    private boolean isFromMain = false;
    private boolean isToMain = false;
    private boolean isDefault = false;

    // variables for drawing
    private int startX, startY;
    private int endX, endY;

    public GProcXPipe(XFrame frame) {
        this.frame = frame;

        this.startX = 0;
        this.startY = 0;
        this.endX = 0;
        this.endY = 0;
    }

    public void draw(Graphics2D g2) {
        if (!this.isValid()) {
            return;
        }

        double H = 10;
        double L = 4;
        int x3 = 0;
        int y3 = 0;
        int x4 = 0;
        int y4 = 0;
        double awrad = Math.atan(L / H);
        double arraow_len = Math.sqrt(L * L + H * H);
        double[] arrXY_1 = rotateVec(endX - startX, endY - startY, awrad, true, arraow_len);
        double[] arrXY_2 = rotateVec(endX - startX, endY - startY, -awrad, true, arraow_len);
        double x_3 = endX - arrXY_1[0];
        double y_3 = endY - arrXY_1[1];
        double x_4 = endX - arrXY_2[0];
        double y_4 = endY - arrXY_2[1];

        Double X3 = new Double(x_3);
        x3 = X3.intValue();
        Double Y3 = new Double(y_3);
        y3 = Y3.intValue();
        Double X4 = new Double(x_4);
        x4 = X4.intValue();
        Double Y4 = new Double(y_4);
        y4 = Y4.intValue();

        // draw the line
        if (this.isDefault()) {
            g2.drawLine(startX, startY, endX, endY);
        } else {
            g2.setColor(new Color(255,0,0));
            g2.drawLine(startX, startY, endX, endY);
            g2.setColor(new Color(0,0,0));
        }
        GeneralPath triangle = new GeneralPath();
        triangle.moveTo(endX, endY);
        triangle.lineTo(x3, y3);
        triangle.lineTo(x4, y4);
        triangle.closePath();
        // filled arrow
        g2.fill(triangle);
        // unfilled arrow
        //g2.draw(triangle);
    }

    public boolean isValid() {
        if (this.getFromPipeline() == null) {
            return false;
        }
        if (this.getFromPort() == null) {
            return false;
        }
        if (this.getToPipeline() == null) {
            return false;
        }
        if (this.getToPort() == null) {
            return false;
        }
        return true;
    }

    public void setStartXY(GProcXPipeline pipeline) {
        this.startX = pipeline.getX();
        this.startY = pipeline.getY() + pipeline.getH() / 2;
    }

    public void setEndXY(GProcXPipeline pipeline) {
        this.endX = pipeline.getX();
        this.endY = pipeline.getY() - pipeline.getH() / 2;
    }

    public void updatePipe() {
        if (this.fromPipeline == null) {
            return;
        }
        if (this.toPipeline == null) {
            return;
        }
        if (!isFromMain) {
            this.startX = this.fromPipeline.getX();
            this.startY = this.fromPipeline.getY() + this.fromPipeline.getH() / 2;
        }

        if (!isToMain) {
            this.endX = this.toPipeline.getX();
            this.endY = this.toPipeline.getY() - this.toPipeline.getH() / 2;;
        }
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public void addQName(QName qname) {
        this.qnames.add(qname);
    }

    public ArrayList<QName> getQNames() {
        return this.qnames;
    }

    public void setParent(GProcXPort parent) {
        this.parent = parent;
    }

    public GProcXPort getParent() {
        return parent;
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

    public ArrayList<QName> getNamespaces() {
        return namespaces;
    }

    public void addNamespace(QName ns) {
        this.namespaces.add(ns);
    }

    public GProcXPort getFromPort() {
        return fromPort;
    }

    public GProcXPort getToPort() {
        return toPort;
    }

    public GProcXPipeline getFromPipeline() {
        return fromPipeline;
    }

    public GProcXPipeline getToPipeline() {
        return toPipeline;
    }

    public boolean isFromMain() {
        return isFromMain;
    }

    public boolean isToMain() {
        return isToMain;
    }

    public void setFromPort(GProcXPort fromPort) {
        if (fromPort == null) {
            return;
        }
        this.fromPort = fromPort;
    }

    public void setFromPipeline(GProcXPipeline fromPipeline, boolean isMain) {
        if (fromPipeline == null) {
            return;
        }
        this.fromPipeline = fromPipeline;
        this.isFromMain = isMain;
        if (isMain) {
            this.startX = Toolkit.getDefaultToolkit().getScreenSize().width/20;
            this.startY = Toolkit.getDefaultToolkit().getScreenSize().height/20;
        } else {
            this.setStartXY(this.fromPipeline);
        }
    }

    public void setToPort(GProcXPort toPort) {
        if (toPort == null) {
            return;
        }
        this.toPort = toPort;
    }

    public void setToPipeline(GProcXPipeline toPipeline, boolean isMain) {
        if (toPipeline == null) {
            return;
        }
        this.toPipeline = toPipeline;
        this.isToMain = isMain;
        if (isMain) {
            this.endX = Toolkit.getDefaultToolkit().getScreenSize().width/20;
            this.endY = Toolkit.getDefaultToolkit().getScreenSize().height - Toolkit.getDefaultToolkit().getScreenSize().height/5;
        } else {
            this.setEndXY(toPipeline);
        }
    }

    public boolean equals(GProcXPipe pipe) {
        if (this.getFromPipeline() != pipe.getFromPipeline()) {
            return false;
        }
        if (this.getFromPort() != pipe.getFromPort()) {
            return false;
        }
        if (this.getToPipeline() != pipe.getToPipeline()) {
            return false;
        }
        if (this.getToPort() != pipe.getToPort()) {
            return false;
        }

        return true;
    }

    public String toString(int retract) {

        // <p:pipe step="myStep" port="result"/>
        String code = "";
        for (int i = 0; i < retract; i++) {
            code += "    ";
        }
        code += "<p:pipe";
        for (QName namespace : this.getNamespaces()) {
            if (!hasDefineNS(namespace)) {
                if (!namespace.getValue().equals("")) {
                    code += " " + namespace;
                }
            }
        }

        code += " port=\"" + fromPort.getPort() +
        "\" step=\"" + fromPipeline.getName() + "\"";

        for (QName qname : getQNames()) {
            if (!qname.getValue().equals("")) {
                code += " " + qname.toString();
            }
        }

        code += "/>\n";

        return code;
    }

    private double[] rotateVec(int px, int py, double ang,
                                     boolean isChLen, double newLen) {

        double mathstr[] = new double[2];
        double vx = px * Math.cos(ang) - py * Math.sin(ang);
        double vy = px * Math.sin(ang) + py * Math.cos(ang);
        if (isChLen) {
            double d = Math.sqrt(vx * vx + vy * vy);
            vx = vx / d * newLen;
            vy = vy / d * newLen;
            mathstr[0] = vx;
            mathstr[1] = vy;
        }
        return mathstr;
    }

}


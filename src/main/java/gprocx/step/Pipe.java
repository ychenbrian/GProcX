package gprocx.step;

import gprocx.core.Port;
import gprocx.mainUI.XFrame;

import java.awt.*;
import java.awt.geom.GeneralPath;

public class Pipe {

    private XFrame frame;

    private Pipeline fromPipeline = null;
    private Port fromPort = null;
    private Pipeline toPipeline = null;
    private Port toPort = null;
    private boolean isFromMain = false;
    private boolean isToMain = false;

    // variables for drawing
    private int startX, startY;
    private int endX, endY;

    public Pipe(XFrame frame) {
        this.frame = frame;

        this.startX = 0;
        this.startY = 0;
        this.endX = 0;
        this.endY = 0;
    }

    public void draw(Graphics2D g2) {
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
        g2.drawLine(startX, startY, endX, endY);
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

    public void setStartXY(Pipeline pipeline) {
        this.startX = pipeline.getX();
        this.startY = pipeline.getY() + pipeline.getH() / 2;
    }

    public void setEndXY(Pipeline pipeline) {
        this.endX = pipeline.getX();
        this.endY = pipeline.getY() - pipeline.getH() / 2;
    }

    public void updatePipe() {
        if (!isFromMain) {
            this.startX = this.fromPipeline.getX();
            this.startY = this.fromPipeline.getY() + this.fromPipeline.getH() / 2;
        }

        if (!isToMain) {
            this.endX = this.toPipeline.getX();
            this.endY = this.toPipeline.getY() - this.toPipeline.getH() / 2;;
        }
    }

    public Port getFromPort() {
        return fromPort;
    }

    public Port getToPort() {
        return toPort;
    }

    public Pipeline getFromPipeline() {
        return fromPipeline;
    }

    public Pipeline getToPipeline() {
        return toPipeline;
    }

    public boolean isFromMain() {
        return isFromMain;
    }

    public boolean isToMain() {
        return isToMain;
    }

    public void setFromPort(Port fromPort) {
        this.fromPort = fromPort;
    }

    public void setFromPipeline(Pipeline fromPipeline, boolean isMain) {
        this.fromPipeline = fromPipeline;
        this.isFromMain = isMain;
        if (isMain) {
            this.startX = Toolkit.getDefaultToolkit().getScreenSize().width/20;
            this.startY = Toolkit.getDefaultToolkit().getScreenSize().height/20;
        } else {
            this.setStartXY(this.fromPipeline);
        }
    }

    public void setToPort(Port toPort) {
        this.toPort = toPort;
    }

    public void setToPipeline(Pipeline toPipeline, boolean isMain) {
        this.toPipeline = toPipeline;
        this.isToMain = isMain;
        if (isMain) {
            this.endX = Toolkit.getDefaultToolkit().getScreenSize().width/20;
            this.endY = Toolkit.getDefaultToolkit().getScreenSize().height - Toolkit.getDefaultToolkit().getScreenSize().height/5;
        } else {
            this.setEndXY(toPipeline);
        }
    }

    public String toString() {
        // <p:pipe step="myStep" port="result"/>
        String code = "";
        code += "<p:pipe step=\"" + fromPipeline.getName();
        code += "\" port=\"" + fromPort.getPort() + "\"/>\n";

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


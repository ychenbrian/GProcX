package com.oxygenxml.sdksamples.workspace.mainUI;

import com.oxygenxml.sdksamples.workspace.step.Pipeline;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.UUID;

public class XPanel extends JPanel {

    private ArrayList<Pipeline> pipelines;
    private Pipeline mainPipeline;
    private Pipeline current;
    private XFrame frame;

    private MouseHandler mouseHandler;
    private MouseMotionHandler mouseMotionHandler;

    public XPanel(XFrame frame, Pipeline mainPipeline) {
        this.frame = frame;
        this.pipelines = new ArrayList<Pipeline>();
        this.mainPipeline = mainPipeline;
        this.current = null;

        this.mouseHandler = new MouseHandler();
        this.mouseMotionHandler = new MouseMotionHandler();
        this.addMouseListener(this.mouseHandler);
        this.addMouseMotionListener(this.mouseMotionHandler);


    }

    public void updateInfo() {

    }

    public Pipeline findStep(Point2D p) {
        return mainPipeline.findChild(p);
    }

    public String getType() {
        return this.mainPipeline.getType();
    }

    public UUID getUUID() {
        return this.mainPipeline.getUUID();
    }

    public Pipeline getMainPipeline() {
        return mainPipeline;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // set the stroke of round rectangle
        g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,10.0f));

        // set the font
        Font sansbold14 = new Font("SansSerif", 0, 12);
        g2.setFont(sansbold14);

        this.mainPipeline.drawChildren(g2, sansbold14);
    }

    public Pipeline getCurrent() {
        return this.current;
    }

    private class MouseHandler extends MouseAdapter {

        public void mousePressed(MouseEvent event) {

            if (!frame.isDrawStepActive()) {
                return;
            }

            int xStart = event.getX();
            int yStart = event.getY();

            current = new Pipeline(frame, "p:filter");
            current.setShape(xStart, yStart, (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 15,
                    (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 18);
            frame.addPipeline(current);

            mainPipeline.addChildren(current);
            mouseMotionHandler.clear();
            repaint();
            frame.setDrawStepActive(false);
        }

        public void mouseClicked(MouseEvent event) {
            // remove the current square if double clicked
            if (!frame.isDrawStepActive()) {
                Pipeline selected = findStep(event.getPoint());
                if (selected != null && event.getClickCount() == 1) {
                    frame.setSelectedPipeline(selected);
                    frame.updateInfo();
                } else {
                    frame.openTab(selected.getUUID());
                }
            }
        }
    }

    private class MouseMotionHandler implements MouseMotionListener {

        private int xDiff, yDiff;

        public MouseMotionHandler() {
            xDiff = 0;
            yDiff = 0;
        }

        public void clear() {
            xDiff = 0;
            yDiff = 0;
        }

        public void mouseMoved(MouseEvent event) {

            // set the mouse cursor to cross hairs if it is inside a rectangle
            current = findStep(event.getPoint());
            if (current == null) {
                setCursor(Cursor.getDefaultCursor());
            } else {
                setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                this.xDiff = event.getX() - current.getX();
                this.yDiff = event.getY() - current.getY();
            }
        }

        public void mouseDragged(MouseEvent event) {

            if (current != null) {
                // drag the current rectangle to center it at (x, y)
                current.setShape(event.getX() - xDiff, event.getY() - yDiff, current.getW(), current.getH());
                repaint();
            }
        }
    }
}

package gprocx.mainUI;

import gprocx.step.GProcXPipe;
import gprocx.step.GProcXStep;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.UUID;

public class XPanel extends JPanel {

    private GProcXStep mainPipeline;
    private GProcXStep current;
    private XFrame frame;
    private GProcXPipe newPipe;

    private boolean existCurrent;

    private MouseHandler mouseHandler;
    private MouseMotionHandler mouseMotionHandler;

    public XPanel(XFrame frame, GProcXStep mainPipeline) {
        this.frame = frame;
        this.mainPipeline = mainPipeline;
        this.current = null;
        this.existCurrent = true;

        this.mouseHandler = new MouseHandler();
        this.mouseMotionHandler = new MouseMotionHandler();
        this.addMouseListener(this.mouseHandler);
        this.addMouseMotionListener(this.mouseMotionHandler);
    }

    public void updateInfo() {

    }

    public GProcXStep findPipeline(Point2D p) {
        return mainPipeline.findChild(p);
    }

    public boolean selectInPort(Point2D p) {
        if (p.getX() > 0 && p.getX() < Toolkit.getDefaultToolkit().getScreenSize().width/10) {
            if (p.getY() > 0 && p.getY() < Toolkit.getDefaultToolkit().getScreenSize().height/20) {
                return true;
            }
        }
        return false;
    }

    public boolean selectOutPort(Point2D p) {
        if (p.getX() > 0 && p.getX() < Toolkit.getDefaultToolkit().getScreenSize().width/10) {
            if (p.getY() > Toolkit.getDefaultToolkit().getScreenSize().height - Toolkit.getDefaultToolkit().getScreenSize().height/5
                    && p.getY() < Toolkit.getDefaultToolkit().getScreenSize().height - Toolkit.getDefaultToolkit().getScreenSize().height/5 + Toolkit.getDefaultToolkit().getScreenSize().height/20) {
                return true;
            }
        }
        return false;
    }

    public String getType() {
        return this.mainPipeline.getType();
    }

    public UUID getUUID() {
        return this.mainPipeline.getUUID();
    }

    public GProcXStep getMainPipeline() {
        return mainPipeline;
    }

    public void setMainPipeline(GProcXStep mainPipeline) {
        this.mainPipeline = mainPipeline;
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // set the stroke of round rectangle
        g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));

        // set the font
        Font sansbold16 = new Font("SansSerif", 0, 16);
        FontMetrics metrics = g2.getFontMetrics(sansbold16);
        g2.setFont(sansbold16);

        this.mainPipeline.drawChildren(frame, g2, metrics);
        this.mainPipeline.drawPipes(g2);
        this.mainPipeline.drawPorts(g2, metrics);
    }

    public GProcXStep getCurrent() {
        return this.current;
    }

    private class MouseHandler extends MouseAdapter {

        public void mousePressed(MouseEvent event) {

            if (frame.isDrawStepActive()) {
                mouseMotionHandler.clear();

                frame.setDrawStepActive(false);
                frame.setSelectedStep(current);
                existCurrent = true;
            } else if (frame.isDrawPipe01Active() && !frame.isDrawPipe02Active()) {
                GProcXStep selected = findPipeline(event.getPoint());

                // if select the main in port of the parent
                if (selected == null && selectInPort(event.getPoint())) {
                    newPipe = new GProcXPipe();
                    newPipe.setFromStep(mainPipeline, true);

                    Object[] selectionValues = new Object[mainPipeline.getInputs().size()];
                    for (int i = 0; i < mainPipeline.getInputs().size(); i++) {
                        selectionValues[i] = mainPipeline.getInputs().get(i).getPort();
                    }

                    Object inputContent = JOptionPane.showInputDialog(
                            null,
                            "Please select the port:",
                            "GProcXPort",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            selectionValues,
                            selectionValues[0]
                    );

                    newPipe.setFromPort(mainPipeline.findInPort((String) inputContent));
                    frame.setDrawPipe01Active(false);
                    frame.setDrawPipe02Active(true);
                } else if (selected != null) {

                    newPipe = new GProcXPipe();
                    newPipe.setFromStep(selected, false);

                    Object[] selectionValues = new Object[selected.getOutputs().size()];
                    for (int i = 0; i < selected.getOutputs().size(); i++) {
                        selectionValues[i] = selected.getOutputs().get(i).getPort();
                    }

                    if (selectionValues.length == 0) {
                        frame.showErrorMessage("Cannot find available ports, fail to draw the pipe.");
                        frame.setDrawPipe01Active(false);
                    } else {
                        Object inputContent = JOptionPane.showInputDialog(
                                null,
                                "Please select the port:",
                                "GProcXPort",
                                JOptionPane.PLAIN_MESSAGE,
                                null,
                                selectionValues,
                                selectionValues[0]
                        );

                        newPipe.setFromPort(selected.findOutPort((String) inputContent));

                        frame.setDrawPipe01Active(false);
                        frame.setDrawPipe02Active(true);
                    }
                }
            } else if (!frame.isDrawPipe01Active() && frame.isDrawPipe02Active()) {
                GProcXStep selected = findPipeline(event.getPoint());

                // if select the main out port of the parent
                if (selected == null && selectOutPort(event.getPoint())) {
                    newPipe.setToStep(mainPipeline, true);

                    Object[] selectionValues = new Object[mainPipeline.getOutputs().size()];
                    for (int i = 0; i < mainPipeline.getOutputs().size(); i++) {
                        selectionValues[i] = mainPipeline.getOutputs().get(i).getPort();
                    }

                    Object inputContent = JOptionPane.showInputDialog(
                            null,
                            "Please select the port:",
                            "GProcXPort",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            selectionValues,
                            selectionValues[0]
                    );

                    newPipe.setToPort(mainPipeline.findOutPort((String) inputContent));
                    newPipe.setDefault(false);
                    mainPipeline.addPipe(frame, newPipe);
                    frame.setDrawPipe02Active(false);

                } else if (selected != null) {

                    newPipe.setToStep(selected, false);

                    Object[] selectionValues = new Object[selected.getInputs().size()];
                    for (int i = 0; i < selected.getInputs().size(); i++) {
                        selectionValues[i] = selected.getInputs().get(i).getPort();
                    }

                    if (selectionValues.length == 0) {
                        frame.showErrorMessage("Cannot find available ports, fail to draw the pipe.");
                        frame.setDrawPipe02Active(false);
                    } else {
                        Object inputContent = JOptionPane.showInputDialog(
                                null,
                                "Please select the port:",
                                "GProcXPort",
                                JOptionPane.PLAIN_MESSAGE,
                                null,
                                selectionValues,
                                selectionValues[0]
                        );

                        newPipe.setToPort(selected.findInPort((String) inputContent));
                        newPipe.setDefault(false);
                        mainPipeline.addPipe(frame, newPipe);
                        frame.setDrawPipe02Active(false);
                    }
                }
            }
            repaint();
        }

        public void mouseClicked(MouseEvent event) {
            // remove the current square if double clicked
            if (!frame.isDrawStepActive()) {
                GProcXStep selected = findPipeline(event.getPoint());
                if (selected == null) {
                    if (event.getClickCount() == 1) {
                        frame.setSelectedStep(mainPipeline);
                    }
                    return;
                }
                if (event.getClickCount() == 1) {
                    frame.setSelectedStep(selected);
                } else if (event.getClickCount() == 2) {
                    if (selected.isAtomic()) {
                        frame.showErrorMessage("This is an atomic step.");
                    } else {
                        frame.openTab(selected.getUUID());
                    }
                }
            }
            repaint();
        }

        public void mouseReleased(MouseEvent e) {
            if (e.isMetaDown()) {
                if (findPipeline(e.getPoint()) != null) {
                    stepPopMenu(e.getComponent(), e.getPoint());
                } else {
                    generalPopMenu(e.getComponent(), e.getPoint());
                }
            }
            repaint();
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

            if (!frame.isDrawStepActive()) {
                current = findPipeline(event.getPoint());
                if (current == null) {
                    if (selectInPort(event.getPoint()) || selectOutPort(event.getPoint())) {
                        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                    } else {
                        setCursor(Cursor.getDefaultCursor());
                    }
                } else { // add the pipe
                    if (!frame.isDrawPipe01Active() && !frame.isDrawPipe02Active()) {
                        setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                    } else {
                        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                    }
                    this.xDiff = event.getX() - current.getX();
                    this.yDiff = event.getY() - current.getY();
                }


            } else { // add the step
                if (existCurrent) {
                    //current = new GProcXStep(frame, frame.getNewStep());
                    current = frame.getNewStep();

                    frame.addStep(current);
                    mainPipeline.addChildren(frame, current);

                    existCurrent = false;
                }


                current.setShape(event.getX(), event.getY(),
                        (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 15,
                        (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 18);
                frame.updateSequence();

            }
            repaint();
        }

        public void mouseDragged(MouseEvent event) {

            if (!frame.isDrawStepActive()) {
                if (current != null) {
                    // drag the current rectangle to center it at (x, y)
                    current.setShape(event.getX() - xDiff, event.getY() - yDiff, current.getW(), current.getH());
                    mainPipeline.updatePipes();
                    if (frame.getSelectedStep().getUUID() != current.getUUID()) {
                        frame.setSelectedStep(current);
                    }

                    frame.updateSequence();

                }
            }

            repaint();
        }
    }

    public void stepPopMenu(Component invoker, Point2D p) {

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem cutMenuItem = new JMenuItem("Cut"); popupMenu.add(cutMenuItem);
        JMenuItem copyMenuItem = new JMenuItem("Copy"); popupMenu.add(copyMenuItem);
        JMenuItem deleteMenuItem = new JMenuItem("Delete"); popupMenu.add(deleteMenuItem);

        cutMenuItem.addActionListener(new XMenuBar.CutMenu(this.frame));
        copyMenuItem.addActionListener(new XMenuBar.CopyMenu(this.frame));
        deleteMenuItem.addActionListener(new DeletePopMenu(this.frame, findPipeline(p), this));

        popupMenu.show(invoker, (int)p.getX(), (int)p.getY());
    }

    public void generalPopMenu(Component invoker, Point2D p) {

        JPopupMenu popupMenu = new JPopupMenu();

        JMenu addMenu = new JMenu("Add"); popupMenu.add(addMenu);
        JMenuItem pipelineMenuItem = new JMenuItem("Pipeline"); addMenu.add(pipelineMenuItem);
        JMenuItem atomicMenuItem = new JMenuItem("Atomic step"); addMenu.add(atomicMenuItem);
        JMenuItem otherMenuItem = new JMenuItem("Other step"); addMenu.add(otherMenuItem);
        JMenuItem pipeMenuItem = new JMenuItem("Pipe"); addMenu.add(pipeMenuItem);

        JMenuItem pasteMenuItem = new JMenuItem("Paste"); popupMenu.add(pasteMenuItem);
        JMenuItem closeMenuItem = new JMenuItem("Close tab"); popupMenu.add(closeMenuItem);


        pipelineMenuItem.addActionListener(new XMenuBar.PipelineMenu(this.frame));
        atomicMenuItem.addActionListener(new XMenuBar.AtomicMenu(this.frame));
        otherMenuItem.addActionListener(new XMenuBar.OtherStepMenu(this.frame));
        pipeMenuItem.addActionListener(new XMenuBar.PipeMenu(this.frame));

        pasteMenuItem.addActionListener(new XMenuBar.PasteMenu(this.frame));
        closeMenuItem.addActionListener(new XMenuBar.CloseTabMenu(this.frame));

        popupMenu.show(invoker, (int)p.getX(), (int)p.getY());
    }

    public static class DeletePopMenu implements ActionListener {

        GProcXStep select;
        XFrame frame;
        XPanel panel;

        public DeletePopMenu(XFrame frame, GProcXStep select, XPanel panel) {
            this.frame = frame;
            this.panel = panel;
            this.select = select;
        }
        public void actionPerformed(ActionEvent e) {
            int result = JOptionPane.showConfirmDialog(
                    null,
                    "Remove this step or pipeline?",
                    "Warning",
                    JOptionPane.YES_NO_CANCEL_OPTION
            );

            if (result == 0) {
                if (this.select != null) {
                    this.frame.getFigureTabs().removeStep(this.select.getUUID());
                    this.frame.getMainStep().deleteChild(this.select);
                }
                this.frame.setSelectedStep(this.frame.getMainStep());
            }
        }
    }
}

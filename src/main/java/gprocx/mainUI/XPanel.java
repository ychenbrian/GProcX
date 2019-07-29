package gprocx.mainUI;

import com.xml_project.morganaxproc.XProcInterfaceException;
import gprocx.step.GProcXPipe;
import gprocx.step.GProcXPipeline;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.UUID;

public class XPanel extends JPanel {

    private GProcXPipeline mainPipeline;
    private GProcXPipeline current;
    private XFrame frame;
    private GProcXPipe newPipe;

    private boolean existCurrent;

    private MouseHandler mouseHandler;
    private MouseMotionHandler mouseMotionHandler;

    public XPanel(XFrame frame, GProcXPipeline mainPipeline) {
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

    public GProcXPipeline findPipeline(Point2D p) {
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

    public GProcXPipeline getMainPipeline() {
        return mainPipeline;
    }

    public void setMainPipeline(GProcXPipeline mainPipeline) {
        this.mainPipeline = mainPipeline;
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // set the stroke of round rectangle
        g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f));

        // set the font
        Font sansbold14 = new Font("SansSerif", 0, 14);
        FontMetrics metrics = g2.getFontMetrics(sansbold14);
        g2.setFont(sansbold14);

        this.mainPipeline.drawChildren(g2, metrics);
        this.mainPipeline.drawPipes(g2);
        this.mainPipeline.drawPorts(g2, metrics);
    }

    public GProcXPipeline getCurrent() {
        return this.current;
    }

    private class MouseHandler extends MouseAdapter {

        public void mousePressed(MouseEvent event) {

            if (frame.isDrawStepActive()) {
                mouseMotionHandler.clear();

                frame.setDrawStepActive(false);
                frame.setSelectedPipeline(current);
                existCurrent = true;
            } else if (frame.isDrawPipe01Active() && !frame.isDrawPipe02Active()) {
                GProcXPipeline selected = findPipeline(event.getPoint());

                // if select the main in port of the parent
                if (selected == null && selectInPort(event.getPoint())) {
                    newPipe = new GProcXPipe(frame);
                    newPipe.setFromPipeline(mainPipeline, true);

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

                    newPipe = new GProcXPipe(frame);
                    newPipe.setFromPipeline(selected, false);

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
                GProcXPipeline selected = findPipeline(event.getPoint());

                // if select the main out port of the parent
                if (selected == null && selectOutPort(event.getPoint())) {
                    newPipe.setToPipeline(mainPipeline, true);

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
                    mainPipeline.addPipe(newPipe);
                    frame.setDrawPipe02Active(false);

                } else if (selected != null) {

                    newPipe.setToPipeline(selected, false);

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
                        mainPipeline.addPipe(newPipe);
                        frame.setDrawPipe02Active(false);
                    }
                }
            }
            repaint();
        }

        public void mouseClicked(MouseEvent event) {
            // remove the current square if double clicked
            if (!frame.isDrawStepActive()) {
                GProcXPipeline selected = findPipeline(event.getPoint());
                if (selected == null && event.getClickCount() == 1) {
                    frame.setSelectedPipeline(mainPipeline);
                    frame.updateInfo();
                    return;
                }
                if (event.getClickCount() == 1) {
                    frame.setSelectedPipeline(selected);
                    frame.updateInfo();
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
                } else {
                    if (!frame.isDrawPipe01Active() && !frame.isDrawPipe02Active()) {
                        setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                    } else {
                        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                    }
                    this.xDiff = event.getX() - current.getX();
                    this.yDiff = event.getY() - current.getY();
                }


            } else {
                // if the drawing is activated
                if (existCurrent) {
                    try {
                        current = new GProcXPipeline(frame, frame.getNewStep());
                    } catch (XProcInterfaceException e) {
                        e.printStackTrace();
                    }
                    frame.addPipeline(current);
                    mainPipeline.addChildren(current);

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
                    if (frame.getSelectedPipeline().getUUID() != current.getUUID()) {
                        frame.setSelectedPipeline(current);
                    }

                    frame.updateSequence();

                }
            }

            repaint();
        }
    }

    public void stepPopMenu(Component invoker, Point2D p) {

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteMenuItem = new JMenuItem("Delete");

        popupMenu.add(deleteMenuItem);

        deleteMenuItem.addActionListener(new DeletePopMenu(findPipeline(p)));

        popupMenu.show(invoker, (int)p.getX(), (int)p.getY());
    }

    public void generalPopMenu(Component invoker, Point2D p) {

        JPopupMenu popupMenu = new JPopupMenu();
        JMenu addMenu = new JMenu("Add");
        JMenuItem atomicMenuItem = new JMenuItem("Atomic step");
        JMenuItem pipeMenuItem = new JMenuItem("Pipe");
        addMenu.add(atomicMenuItem);
        addMenu.add(pipeMenuItem);

        JMenuItem closeMenuItem = new JMenuItem("Close tab");

        popupMenu.add(addMenu);
        popupMenu.add(closeMenuItem);

        atomicMenuItem.addActionListener(new XMenuBar.AtomicMenu(this.frame));
        pipeMenuItem.addActionListener(new XMenuBar.PipeMenu(this.frame));
        closeMenuItem.addActionListener(new XToolbar.CloseActionListener(this.frame));

        popupMenu.show(invoker, (int)p.getX(), (int)p.getY());
    }

    private class DeletePopMenu implements ActionListener {

        GProcXPipeline select;

        public DeletePopMenu(GProcXPipeline select) {
            this.select = select;
        }
        public void actionPerformed(ActionEvent e) {
            if (select != null) {
                mainPipeline.deleteChild(select);
            }
            repaint();
            frame.setSelectedPipeline(frame.getMainPipeline());
            frame.updateInfo();
        }
    }
}

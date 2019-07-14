package gprocx.mainUI;

import gprocx.step.StepInfo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class XMenuBar extends JMenuBar {

    private XFrame frame;

    public XMenuBar(XFrame frame) {
        this.frame = frame;

        JMenu fileMenu = new JMenu("File");
        JMenuItem newMenuItem = new JMenuItem("New"); fileMenu.add(newMenuItem);
        JMenuItem openMenuItem = new JMenuItem("Open"); fileMenu.add(openMenuItem);
        fileMenu.addSeparator();
        JMenuItem exitMenuItem = new JMenuItem("Exit"); fileMenu.add(exitMenuItem);

        JMenu editMenu = new JMenu("Edit");

        JMenu insertMenu = new JMenu("Insert");
        JMenuItem pipelineMenuItem = new JMenuItem("Pipeline"); insertMenu.add(pipelineMenuItem);
        JMenuItem atomicMenuItem = new JMenuItem("Atomic step"); insertMenu.add(atomicMenuItem);
        JMenuItem pipeMenuItem = new JMenuItem("Pipe"); insertMenu.add(pipeMenuItem);

        atomicMenuItem.addActionListener(new AtomicMenu());
        pipeMenuItem.addActionListener(new PipeMenu());

        this.add(fileMenu);
        this.add(editMenu);
        this.add(insertMenu);
    }
    
    private class AtomicMenu implements ActionListener {
    	
    	public void actionPerformed(ActionEvent e) {
            Object[] selectionValues = StepInfo.getStepTypes();

            Object inputContent = JOptionPane.showInputDialog(
                    null,
                    "Choose the atomic step:",
                    "Atomic step",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    selectionValues,
                    selectionValues[0]
            );
            if (inputContent != null) {
                frame.setNewStep((String) inputContent);
                frame.setDrawStepActive(true);
            }

        }
    }

    private class PipeMenu implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            frame.setDrawPipe01Active(true);
        }
    }
}

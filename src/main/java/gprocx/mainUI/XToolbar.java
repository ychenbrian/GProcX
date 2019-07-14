package gprocx.mainUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class XToolbar extends JToolBar {

    private XFrame frame;

    public XToolbar(XFrame frame) {
        this.frame = frame;

        JButton newButton = new JButton("+");
        JButton closeButton = new JButton("x");

        closeButton.addActionListener(new CloseActionListener());

        this.add(newButton);
        this.add(closeButton);
    }
    
    private class CloseActionListener implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
            frame.removeCurrentTab();
            frame.setSelectedPipeline(frame.getCurrentPipeline());
        }
    }
}

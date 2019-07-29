package gprocx.mainUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class XToolbar extends JToolBar {

    private XFrame frame;

    public XToolbar(XFrame frame) {
        this.frame = frame;

        JButton newButton = new JButton("+");
        JButton closeButton = new JButton("Close current tab");

        closeButton.addActionListener(new CloseActionListener(this.frame));

        this.add(newButton);
        this.add(closeButton);
    }
    
    public static class CloseActionListener implements ActionListener {
        XFrame frame;

        public CloseActionListener(XFrame frame) {
            this.frame = frame;
        }
    	public void actionPerformed(ActionEvent e) {
            this.frame.removeCurrentTab();
            this.frame.setSelectedPipeline(this.frame.getCurrentPipeline());
        }
    }
}

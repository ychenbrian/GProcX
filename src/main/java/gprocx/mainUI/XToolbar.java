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

        this.add(newButton);
        this.add(closeButton);
    }
    
    
}

package gprocx.mainUI;

import gprocx.step.GProcXPipeline;

import javax.swing.*;
import java.util.ArrayList;
import java.util.UUID;

public class XFigureTabs extends JTabbedPane {

    private XFrame frame;
    private ArrayList<XPanel> panels;
    private ArrayList<XPanel> tabs;

    public XFigureTabs(int location, XFrame frame, GProcXPipeline mainPipeline) {
        super(location);
        this.frame = frame;
        this.panels = new ArrayList<XPanel>();
        this.tabs = new ArrayList<XPanel>();

        GProcXPipeline initPipeline = mainPipeline;
        this.addPipeline(initPipeline);
        this.openTab(initPipeline.getUUID());
        //this.addMouseListener(new MouseHandler());
    }

    public void updateInfo() {
        this.getCurrentTab().updateInfo();
    }

    public void addPipeline(GProcXPipeline pipeline) {
        panels.add(new XPanel(frame, pipeline));
    }

    public void newProgram(GProcXPipeline pipeline) {
        this.panels.clear();
        for (int i = 0; i < tabs.size(); i++) {
            this.removeCurrentTab();
        }
        this.tabs.clear();

        this.addPipeline(pipeline);
        this.openTab(pipeline.getUUID());
    }

    public int findTab(UUID uuid) {
        for (int i = 0; i < tabs.size(); i++) {
            if (tabs.get(i).getUUID() == uuid) {
                return i;
            }
        }
        return -1;
    }

    public void addTab(UUID uuid) {
        for (XPanel panel : panels) {
            if (panel.getUUID() == uuid) {
                this.add(panel.getType(), panel);
                this.tabs.add(panel);
                return;
            }
        }
    }

    public void openTab(UUID uuid) {
        int index = this.findTab(uuid);

        if (index >= 0) {   // if already exist the tab
            this.setSelectedIndex(index);
            this.frame.setSelectedPipeline(this.getCurrentTab().getMainPipeline());
            this.frame.setMainPipeline(this.getCurrentTab().getMainPipeline());
            this.frame.updateInfo();
        } else {
            this.addTab(uuid);
            this.setSelectedIndex(this.getTabCount() - 1);
            this.frame.setSelectedPipeline(this.getCurrentTab().getMainPipeline());
            this.frame.setMainPipeline(this.getCurrentTab().getMainPipeline());
            this.frame.updateInfo();
        }
    }

    public void removeCurrentTab() {
        if (this.tabs.size() == 1) {
            JOptionPane.showMessageDialog(
                    null,
                    "You cannot close the last tab.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        int index = this.getSelectedIndex();
        this.remove(index);
        this.tabs.remove(index);

        this.setSelectedIndex(0);
        this.frame.setSelectedPipeline(this.getCurrentTab().getMainPipeline());
        this.frame.setMainPipeline(this.getCurrentTab().getMainPipeline());
        this.frame.updateInfo();
    }

    public XPanel getCurrentTab() {
        return this.tabs.get(this.getSelectedIndex());
    }

    public GProcXPipeline getCurrentStep() {
        return this.tabs.get(this.getSelectedIndex()).getMainPipeline();
    }
}

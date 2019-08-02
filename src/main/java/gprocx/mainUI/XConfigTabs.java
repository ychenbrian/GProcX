package gprocx.mainUI;

import gprocx.core.*;
import gprocx.step.GProcXDoc;
import gprocx.step.GProcXPipe;
import gprocx.step.GProcXPipeline;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class XConfigTabs extends JTabbedPane {

    private XFrame frame;

    private JPanel specPanel;
    private JPanel ioPanel;
    private JPanel pipeChildPanel;

    // variables for specification panel
    private JPanel basicPanel;
    private JPanel nsPanel;
    private JPanel requiredElePanel;
    private JPanel otherElePanel;
    // for io panel
    private JPanel inputPanel;
    private JPanel outputPanel;
    // for pipe child panel
    private JPanel pipePanel;
    private JPanel childPanel;


    public XConfigTabs(XFrame frame) {
        this.frame = frame;

        setSpecPanel();
        setIOPanel();
        setPipeChildPanel();

        this.addTab("Specification", this.specPanel);
        this.addTab("I/O ports", this.ioPanel);
        this.addTab("Pipe & Subpipeline", this.pipeChildPanel);
    }

    public void setSpecPanel() {

        // main specific panel
        this.specPanel = new JPanel();
        this.specPanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/5,
                Toolkit.getDefaultToolkit().getScreenSize().height));


        // information panel
        this.basicPanel = new JPanel();
        this.basicPanel.setBorder(new TitledBorder("Basic information"));
        this.basicPanel.setLayout(new GridLayout(10,1));
        this.basicPanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/5,
                Toolkit.getDefaultToolkit().getScreenSize().height/5));

        // namespaces
        this.nsPanel = new JPanel();
        this.nsPanel.setBorder(new TitledBorder("Namespaces"));
        this.nsPanel.setLayout(new GridLayout(5,1));
        this.nsPanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/5,
                Toolkit.getDefaultToolkit().getScreenSize().height/10));

        // required QNames
        this.requiredElePanel = new JPanel();
        this.requiredElePanel.setBorder(new TitledBorder("Required elements"));
        this.requiredElePanel.setLayout(new GridLayout(5,1));
        this.requiredElePanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/5,
                Toolkit.getDefaultToolkit().getScreenSize().height/10));

        // none required QNames
        this.otherElePanel = new JPanel();
        this.otherElePanel.setBorder(new TitledBorder("Other elements"));
        this.otherElePanel.setLayout(new GridLayout(35,1));
        this.otherElePanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/5,
                Toolkit.getDefaultToolkit().getScreenSize().height*5/10));


        this.specPanel.add(this.basicPanel);
        this.specPanel.add(this.nsPanel);
        this.specPanel.add(this.requiredElePanel);
        this.specPanel.add(this.otherElePanel);
    }

    public void setIOPanel() {
        this.ioPanel = new JPanel();
        this.ioPanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/5,
                Toolkit.getDefaultToolkit().getScreenSize().height));


        // I/O panels of the specific panel
        this.inputPanel = new JPanel();
        this.inputPanel.setBorder(new TitledBorder("Input port"));
        this.inputPanel.setLayout(new GridLayout(30,1));
        this.inputPanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/5,
                Toolkit.getDefaultToolkit().getScreenSize().height/2));

        this.outputPanel = new JPanel();
        this.outputPanel.setBorder(new TitledBorder("Output port"));
        this.outputPanel.setLayout(new GridLayout(30,1));
        this.outputPanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/5,
                Toolkit.getDefaultToolkit().getScreenSize().height/2));


        this.ioPanel.add(this.inputPanel);
        this.ioPanel.add(this.outputPanel);
    }

    public void setPipeChildPanel() {
        this.pipeChildPanel = new JPanel();
        this.pipeChildPanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/5,
                Toolkit.getDefaultToolkit().getScreenSize().height));

        // panel for pipes
        this.pipePanel = new JPanel();
        this.pipePanel.setBorder(new TitledBorder("Pipe"));
        this.pipePanel.setLayout(new GridLayout(30,1));
        this.pipePanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/5,
                Toolkit.getDefaultToolkit().getScreenSize().height/2));

        // panel for subpipelines
        this.childPanel = new JPanel();
        this.childPanel.setBorder(new TitledBorder("Subpipeline"));
        this.childPanel.setLayout(new GridLayout(30,1));
        this.childPanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/5,
                Toolkit.getDefaultToolkit().getScreenSize().height/2));


        this.pipeChildPanel.add(this.pipePanel);
        this.pipeChildPanel.add(this.childPanel);
    }

    public void updateInfo() {
        if (this.frame.getSelectedPipeline() != null) {
            GProcXPipeline selected = this.frame.getSelectedPipeline();

            //this.enterType.setText(selected.getType());

            // set docs
            this.basicPanel.removeAll();
            for (GProcXDoc doc : selected.getDocs()) {
                this.basicPanel.add(this.newDocBox(doc), BorderLayout.WEST);
                JLabel tempDoc = this.createJLabelWithWrapWidth(40, new JLabel(doc.getContent()));
                this.basicPanel.add(tempDoc);

                this.basicPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
            }
            JButton addDocButtion = new JButton("New info");
            addDocButtion.addActionListener(new AddDocListener());
            this.basicPanel.add(addDocButtion, BorderLayout.SOUTH);


            // set namespaces
            this.nsPanel.removeAll();
            for (QName ns : selected.getNamespaces()) {
                this.nsPanel.add(this.newNamespaceBox(ns), BorderLayout.WEST);
            }

            JButton addNSButton = new JButton("Add");
            addNSButton.addActionListener(new AddNSListener());
            this.nsPanel.add(addNSButton, BorderLayout.SOUTH);


            // set qnames
            this.requiredElePanel.removeAll();
            for (QName qname : selected.getQNames()) {
                if (qname.isRequired()) {
                    this.requiredElePanel.add(this.newElementBox(selected.getQNames(), qname), BorderLayout.WEST);
                }
            }
            if (!selected.isBuildin()) {
                JButton addElementButton = new JButton("Add");
                addElementButton.addActionListener(new AddElementListener());
                this.requiredElePanel.add(addElementButton, BorderLayout.SOUTH);
            }

            this.otherElePanel.removeAll();
            for (QName qname : selected.getQNames()) {
                if (!qname.isRequired()) {
                    this.otherElePanel.add(this.newElementBox(selected.getQNames(), qname), BorderLayout.WEST);
                }
            }
            if (!selected.isBuildin()) {
                JButton addElementButton = new JButton("Add");
                addElementButton.addActionListener(new AddElementListener());
                this.otherElePanel.add(addElementButton, BorderLayout.SOUTH);
            }


            // set inputs
            this.inputPanel.removeAll();
            this.inputPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
            for (GProcXPort port : selected.getInputs()) {
                this.inputPanel.add(this.newInBox01(port));
                this.inputPanel.add(this.newInBox02(port));
                this.inputPanel.add(this.newInBox03(port));

                for (IOSource source : port.getSources()) {
                    Box newBox = Box.createHorizontalBox();
                    newBox.add(Box.createHorizontalStrut(10));
                    newBox.add(new DeleteSourceButton(port, source, "Delete source"));
                    newBox.add(Box.createHorizontalStrut(30));
                    newBox.add(new JLabel("Source type: " + source.getSourceType()));
                    this.inputPanel.add(newBox);

                    for (QName qname : source.getQNames()) {
                        this.inputPanel.add(this.newElementBox(source.getQNames(), qname));
                    }
                }

                this.inputPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
            }
            if (!selected.isBuildin()) {
                JButton addPortButtion = new JButton("New port");
                addPortButtion.addActionListener(new AddInPortListener());
                this.inputPanel.add(addPortButtion, BorderLayout.SOUTH);
            }


            // set outputs
            this.outputPanel.removeAll();
            this.outputPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
            for (GProcXPort port : selected.getOutputs()) {
                this.outputPanel.add(this.newOutBox01(port));
                this.outputPanel.add(this.newInBox02(port));
                this.outputPanel.add(this.newInBox03(port));

                for (IOSource source : port.getSources()) {
                    Box newBox = Box.createHorizontalBox();
                    newBox.add(Box.createHorizontalStrut(10));
                    newBox.add(new DeleteSourceButton(port, source, "Delete source"));
                    newBox.add(Box.createHorizontalStrut(30));
                    newBox.add(new JLabel("Source type: " + source.getSourceType()));
                    this.outputPanel.add(newBox);

                    for (QName qname : source.getQNames()) {
                        this.outputPanel.add(this.newElementBox(source.getQNames(), qname));
                    }
                }

                this.outputPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
            }
            if (!selected.isBuildin()) {
                JButton addPortButtion = new JButton("New port");
                addPortButtion.addActionListener(new AddOutPortListener());
                this.outputPanel.add(addPortButtion, BorderLayout.SOUTH);
            }


            // set pipes
            this.pipePanel.removeAll();
            for (GProcXPipe pipe : selected.getPipes()) {
                if (pipe.isValid()) {
                    this.pipePanel.add(this.newPipeBox(pipe), BorderLayout.WEST);
                }
            }
            if (frame.getMainPipeline().getOutPipe() != null) {
                if (frame.getMainPipeline().getOutPipe().isValid()) {
                    this.pipePanel.add(this.newPipeBox(frame.getMainPipeline().getOutPipe()));
                }
            }
            JButton addPipeButton = new JButton("New pipe");
            addPipeButton.addActionListener(new XMenuBar.PipeMenu(this.frame));
            this.pipePanel.add(addPipeButton, BorderLayout.SOUTH);


            // set subpipelines
            this.childPanel.removeAll();
            for (GProcXPipeline child : selected.getChildren()) {
                this.childPanel.add(this.newSubpipelineBox(child), BorderLayout.WEST);
            }


            this.repaint();
        }
    }

    private Box newDocBox(GProcXDoc doc) {
        Box newBox = Box.createHorizontalBox();
        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(new EditDocButton(doc, "Edit"), BorderLayout.WEST);
        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(new DeleteDocButton(doc, "Delete"));
        newBox.add(Box.createHorizontalStrut(30));
        newBox.add(new JLabel(doc.getType()));
        return newBox;
    }

    private Box newNamespaceBox(QName qname) {
        Box newBox = Box.createHorizontalBox();
        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(new EditElementButton(qname,"Edit"), BorderLayout.WEST);
        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(new DeleteNSButton(qname,"Delete"));
        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(new JLabel(qname.toString()));
        return newBox;
    }

    private Box newElementBox(ArrayList<QName> qnames, QName qname) {
        Box newBox = Box.createHorizontalBox();
        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(new EditElementButton(qname,"Edit"), BorderLayout.WEST);
        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(new DeleteElementButton(qnames, qname,"Delete"));
        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(new JLabel(qname.toString()));
        return newBox;
    }

    private Box newInBox01(GProcXPort port) {
        Box newBox = Box.createHorizontalBox();

        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(new AddSourceButton(port,"Insert source"));
        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(new DeleteInPortButton(port,"Delete port"));
        newBox.add(Box.createHorizontalStrut(30));
        newBox.add(new JLabel(port.getPort()));

        return newBox;
    }

    private Box newOutBox01(GProcXPort port) {
        Box newBox = Box.createHorizontalBox();

        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(new DeleteOutPortButton(port,"Delete port"));
        newBox.add(Box.createHorizontalStrut(30));
        newBox.add(new JLabel(port.getPort()));

        return newBox;
    }

    private Box newInBox02(final GProcXPort port) {
        Box newBox = Box.createHorizontalBox();

        JRadioButton trueButton = new JRadioButton("True");
        JRadioButton falseButton = new JRadioButton("False");
        ButtonGroup primaryGroup = new ButtonGroup();
        primaryGroup.add(trueButton);
        primaryGroup.add(falseButton);

        if (!frame.getSelectedPipeline().isAtomic()) {
            trueButton.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    port.setPrimary(true);
                }
            });
            falseButton.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    port.setPrimary(false);
                }
            });
        }

        // update the status
        if (port.isPrimary()) {
            trueButton.setSelected(true);
            falseButton.setSelected(false);
        } else {
            trueButton.setSelected(false);
            falseButton.setSelected(true);
        }

        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(new JLabel("Primary"));
        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(trueButton);
        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(falseButton);

        return newBox;
    }

    private Box newInBox03(final GProcXPort port) {
        Box newBox = Box.createHorizontalBox();

        JRadioButton trueButton = new JRadioButton("True");
        JRadioButton falseButton = new JRadioButton("False");
        ButtonGroup primaryGroup = new ButtonGroup();
        primaryGroup.add(trueButton);
        primaryGroup.add(falseButton);

        if (!frame.getSelectedPipeline().isAtomic()) {
            trueButton.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    port.setSequence(true);
                }
            });
            falseButton.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    port.setSequence(false);
                }
            });
        }

        // update the selection status
        if (port.isSequence()) {
            trueButton.setSelected(true);
            falseButton.setSelected(false);
        } else {
            trueButton.setSelected(false);
            falseButton.setSelected(true);
        }

        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(new JLabel("Sequence"));
        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(trueButton);
        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(falseButton);

        return newBox;
    }

    private Box newPipeBox(GProcXPipe pipe) {
        Box newBox = Box.createHorizontalBox();
        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(new DeletePipeButton(pipe, "Delete"));
        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(new JLabel(pipe.getInfo()));
        return newBox;
    }

    private Box newSubpipelineBox(GProcXPipeline child) {
        Box newBox = Box.createHorizontalBox();
        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(new DeleteSubpipelineButton(child, "Delete"));
        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(new JLabel(child.getType() + " (" + child.getName() + ")"));
        return newBox;
    }

    private JLabel createJLabelWithWrapWidth(int width, JLabel label){
        if (width <= 0 || label == null){
            return label;
        }
        String text = label.getText();
        if (!text.startsWith("<html>")){
            StringBuilder strBuilder = new StringBuilder("<html>");
            strBuilder.append(text);
            strBuilder.append("</html>");
            text = strBuilder.toString();
        }
        label.setText(text);
        View labelView = BasicHTML.createHTMLView(label, label.getText());
        labelView.setSize(width, 100);
        label.setPreferredSize(new Dimension(width, (int) labelView.getMinimumSpan(View.Y_AXIS)));
        return label;
    }

    private class EditDocButton extends JButton {
        public EditDocButton(final GProcXDoc doc, String text) {
            super(text);

            this.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    String inputContent = JOptionPane.showInputDialog(
                            null,
                            doc.getType(),
                            doc.getContent()
                    );
                    if (inputContent != null) {
                        doc.setContent(inputContent);
                        frame.updateInfo();
                    }
                }
            });
        }
    }

    private class EditElementButton extends JButton {

        public EditElementButton(final QName qname, String text) {
            super(text);

            this.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    String inputContent = JOptionPane.showInputDialog(
                            null,
                            qname.getLexical() + "=",
                            qname.getValue()
                    );
                    if (inputContent != null) {
                        qname.setValue(inputContent);
                        frame.updateInfo();
                    }
                }
            });
        }
    }

    private class DeleteDocButton extends JButton {
        public DeleteDocButton(final GProcXDoc doc, String text) {
            super(text);

            this.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (frame.getSelectedPipeline() != null) {
                        if (frame.getSelectedPipeline().isBuildin()) {
                            frame.showWarningMessage("You cannot delete a QName of a build-in step, if you do not need this, you can just leave it blank.");
                        } else {
                            int result = JOptionPane.showConfirmDialog(
                                    null,
                                    "Remove this doc?",
                                    "Warning",
                                    JOptionPane.YES_NO_CANCEL_OPTION
                            );

                            if (result == 0) {
                                frame.getSelectedPipeline().getDocs().remove(doc);
                                frame.updateInfo();
                            }
                        }
                    }
                }
            });
        }
    }

    private class DeleteElementButton extends JButton {

        public DeleteElementButton(final ArrayList<QName> qnames, final QName qname, String text) {
            super(text);

            this.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    if (frame.getSelectedPipeline() != null) {
                        if (frame.getSelectedPipeline().isBuildin()) {
                            frame.showWarningMessage("You cannot delete a QName of a build-in step, if you do not need this, you can just leave it blank.");
                        } else {
                            int result = JOptionPane.showConfirmDialog(
                                    null,
                                    "Remove this element?",
                                    "Warning",
                                    JOptionPane.YES_NO_CANCEL_OPTION
                            );

                            if (result == 0) {
                                qnames.remove(qname);
                                frame.updateInfo();
                            }
                        }
                    }
                }
            });
        }
    }

    private class DeleteNSButton extends JButton {

        public DeleteNSButton(final QName ns, String text) {
            super(text);

            this.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    if (frame.getSelectedPipeline() != null) {

                        int result = JOptionPane.showConfirmDialog(
                                null,
                                "Remove this namespace?",
                                "Warning",
                                JOptionPane.YES_NO_CANCEL_OPTION
                        );

                        if (result == 0) {
                            frame.getSelectedPipeline().getNamespaces().remove(ns);
                            frame.updateInfo();
                        }
                    }
                }
            });
        }
    }

    private class DeleteSourceButton extends JButton {

        public DeleteSourceButton(final GProcXPort port, final IOSource source, String text) {
            super(text);

            this.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    int result = JOptionPane.showConfirmDialog(
                            null,
                            "Remove this source?",
                            "Warning",
                            JOptionPane.YES_NO_CANCEL_OPTION
                    );

                    if (result == 0) {
                        port.getSources().remove(source);
                        frame.updateInfo();
                    }
                }
            });
        }
    }

    private class DeleteInPortButton extends JButton {

        public DeleteInPortButton(final GProcXPort port, String text) {
            super(text);

            this.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    if (frame.getSelectedPipeline() != null) {
                        if (frame.getSelectedPipeline().isBuildin()) {
                            frame.showWarningMessage("You cannot delete a port of a build-in step, if you do not need this, you can just leave it blank.");
                        } else {
                            int result = JOptionPane.showConfirmDialog(
                                    null,
                                    "Remove this element?",
                                    "Warning",
                                    JOptionPane.YES_NO_CANCEL_OPTION
                            );

                            if (result == 0) {
                                frame.getSelectedPipeline().getInputs().remove(port);
                                frame.updateInfo();
                            }
                        }
                    }

                }
            });
        }
    }

    private class DeleteOutPortButton extends JButton {

        public DeleteOutPortButton(final GProcXPort port, String text) {
            super(text);

            this.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    if (frame.getSelectedPipeline() != null) {
                        if (frame.getSelectedPipeline().isBuildin()) {
                            frame.showWarningMessage("You cannot delete a QName of a build-in step, if you do not need this, you can just leave it blank.");
                        } else {
                            int result = JOptionPane.showConfirmDialog(
                                    null,
                                    "Remove this element?",
                                    "Warning",
                                    JOptionPane.YES_NO_CANCEL_OPTION
                            );

                            if (result == 0) {
                                frame.getSelectedPipeline().getOutputs().remove(port);
                                frame.updateInfo();
                            }
                        }
                    }

                }
            });
        }
    }

    private class DeletePipeButton extends JButton {
        public DeletePipeButton(final GProcXPipe pipe, String text) {
            super(text);

            this.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    if (frame.getSelectedPipeline() != null) {

                        int result = JOptionPane.showConfirmDialog(
                                null,
                                "Remove this pipe?",
                                "Warning",
                                JOptionPane.YES_NO_CANCEL_OPTION
                        );

                        if (result == 0) {
                            if (pipe == frame.getSelectedPipeline().getOutPipe()) {
                                frame.getSelectedPipeline().setOutPipe(null);
                                frame.getSelectedPipeline().setOutPipeFlag(false);
                            } else {
                                frame.getSelectedPipeline().getPipes().remove(pipe);
                            }
                            frame.updateInfo();
                        }
                    }

                }
            });
        }
    }

    private class DeleteSubpipelineButton extends JButton {

        public DeleteSubpipelineButton(final GProcXPipeline child, String text) {
            super(text);

            this.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    if (frame.getSelectedPipeline() != null) {

                        int result = JOptionPane.showConfirmDialog(
                                null,
                                "Remove this subpipeline?",
                                "Warning",
                                JOptionPane.YES_NO_CANCEL_OPTION
                        );

                        if (result == 0) {
                            if (child != null) {
                                frame.getSelectedPipeline().deleteChild(child);
                            }
                            frame.getCurrentPanel().repaint();
                            frame.setSelectedPipeline(frame.getMainPipeline());
                        }
                    }
                }
            });
        }
    }

    private class AddSourceButton extends JButton {

        public AddSourceButton(final GProcXPort port, String text) {
            super(text);

            final Object[] selectionValues = new Object[]{"p:document", "p:data", "p:inline", "p:empty"};
            this.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    Object inputContent = JOptionPane.showInputDialog(
                            null,
                            "Please select the type of source:",
                            "Source",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            selectionValues,
                            selectionValues[0]
                    );

                    if (inputContent != null) {
                        port.addSource(new IOSource((String)inputContent));
                        frame.updateInfo();
                    }
                }
            });
        }
    }

    private class AddDocListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (frame.getSelectedPipeline() != null) {

                if (frame.getSelectedPipeline().isBuildin()) {
                    frame.showWarningMessage("You cannot add a user-defined info to a build-in step.");
                } else {
                    String inputContent = JOptionPane.showInputDialog(
                            null,
                            "Please enter the type:",
                            "p:documentation"
                    );

                    if (inputContent != null) {
                        frame.getSelectedPipeline().addDoc(new GProcXDoc(inputContent, "empty"));
                        frame.updateInfo();
                    }
                }
            }
        }
    }

    private class AddElementListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (frame.getSelectedPipeline() != null) {

                if (frame.getSelectedPipeline().isBuildin()) {
                    frame.showWarningMessage("You cannot add a user-defined QName to a build-in step.");
                } else {
                    String inputContent = JOptionPane.showInputDialog(
                            null,
                            "Please enter the lexical:",
                            "untitled"
                    );

                    if (inputContent != null) {
                        frame.getSelectedPipeline().addQName(new QName(inputContent, ""));
                        frame.updateInfo();
                    }
                }
            }
        }
    }

    private class AddNSListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (frame.getSelectedPipeline() != null) {
                String inputContent = JOptionPane.showInputDialog(
                        null,
                        "Please enter the namespace:",
                        "xmlns:"
                );

                if (inputContent != null) {
                    frame.getSelectedPipeline().addNamespace(new QName(inputContent, ""));
                    frame.updateInfo();
                }
            }
        }
    }

    private class AddInPortListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (frame.getSelectedPipeline() != null) {

                if (frame.getSelectedPipeline().isBuildin()) {
                    frame.showWarningMessage("You cannot add a user-defined port to a build-in step.");
                } else {
                    String inputContent = JOptionPane.showInputDialog(
                            null,
                            "Please enter the port:",
                            "untitled"
                    );

                    if (inputContent != null) {
                        frame.getSelectedPipeline().getInputs().add(new InPort(frame.getSelectedPipeline(), inputContent, false, false, "not specified"));
                        frame.updateInfo();
                    }
                }
            }
        }
    }

    private class AddOutPortListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (frame.getSelectedPipeline() != null) {

                if (frame.getSelectedPipeline().isBuildin()) {
                    frame.showWarningMessage("You cannot add a user-defined port to a build-in step.");
                } else {
                    String inputContent = JOptionPane.showInputDialog(
                            null,
                            "Please enter the port:",
                            "untitled"
                    );

                    if (inputContent != null) {
                        frame.getSelectedPipeline().getOutputs().add(new InPort(frame.getSelectedPipeline(), inputContent, false, false, "not specified"));
                        frame.updateInfo();
                    }
                }
            }
        }
    }
}

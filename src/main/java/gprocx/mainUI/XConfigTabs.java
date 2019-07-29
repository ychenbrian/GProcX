package gprocx.mainUI;

import gprocx.core.*;
import gprocx.step.GProcXDoc;
import gprocx.step.GProcXPipeline;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class XConfigTabs extends JTabbedPane {

    private XFrame frame;

    private JPanel specPanel;
    private JPanel ioPanel;

    // variables for specification panel
    private JLabel enterType;
    private JTextArea document;
    private ArrayList<Box> docBox;
    private ArrayList<Box> elementBox;
    private JPanel basicPanel;
    private JPanel elementPanel;
    private JPanel inputPanel;
    private JPanel outputPanel;

    private final String[] ioPortCol = {"type", "port", "primary", "sequence", "kind"};
    String[][] ioPortInfo;
    private final String[] optionCol = {"name", "required", "select"};
    String[][] optionInfo;
    JTable optionTable;

    public XConfigTabs(XFrame frame) {
        this.frame = frame;

        //this.setTabPlacement(JTabbedPane.LEFT);

        setSpecPanel();
        setIOPanel();

        this.addTab("Specification", this.specPanel);
        this.addTab("Inputs / Outputs", this.ioPanel);
    }

    public void setSpecPanel() {

        // main specific panel
        this.specPanel = new JPanel();
        this.specPanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/5,
                Toolkit.getDefaultToolkit().getScreenSize().height));


        // two child panels of the specific panel
        this.basicPanel = new JPanel();
        this.basicPanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/5,
                Toolkit.getDefaultToolkit().getScreenSize().height/5));
        this.elementPanel = new JPanel();
        this.elementPanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/5,
                Toolkit.getDefaultToolkit().getScreenSize().height/2));


        TitledBorder basicInfo = new TitledBorder("Basic information");
        TitledBorder elementInfo = new TitledBorder("Elements");
        this.basicPanel.setBorder(basicInfo);
        this.elementPanel.setBorder(elementInfo);

        this.enterType = new JLabel("p:declare-step");
        this.basicPanel.setLayout(new GridLayout(10,1));
        this.docBox = new ArrayList<Box>();

        this.elementPanel.setLayout(new GridLayout(30,1));
        this.elementBox = new ArrayList<Box>();

        this.specPanel.add(this.basicPanel);
        this.specPanel.add(this.elementPanel);
    }

    public void setIOPanel() {
        this.ioPanel = new JPanel();
        this.ioPanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/5,
                Toolkit.getDefaultToolkit().getScreenSize().height));


        // I/O panels of the specific panel
        this.inputPanel = new JPanel();
        this.inputPanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/5,
                Toolkit.getDefaultToolkit().getScreenSize().height/2));
        this.outputPanel = new JPanel();
        this.outputPanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/5,
                Toolkit.getDefaultToolkit().getScreenSize().height/2));


        TitledBorder inputInfo = new TitledBorder("Input port");
        TitledBorder outputInfo = new TitledBorder("Output port");
        this.inputPanel.setBorder(inputInfo);
        this.outputPanel.setBorder(outputInfo);

        this.inputPanel.setLayout(new GridLayout(30,1));
        this.outputPanel.setLayout(new GridLayout(30,1));

        this.ioPanel.add(inputPanel);
        this.ioPanel.add(outputPanel);
    }

    public void updateInfo() {
        if (this.frame.getSelectedPipeline() != null) {
            GProcXPipeline selected = this.frame.getSelectedPipeline();

            this.enterType.setText(selected.getType());

            // set docs
            this.basicPanel.removeAll();
            for (GProcXDoc doc : this.frame.getSelectedPipeline().getDocs()) {
                this.basicPanel.add(this.newDocBox(this.frame.getSelectedPipeline().getDocs(), doc), BorderLayout.WEST);
                JLabel tempDoc = new JLabel(doc.getContent());
                tempDoc.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/5,
                        Toolkit.getDefaultToolkit().getScreenSize().height/5));
                this.basicPanel.add(tempDoc);

                this.basicPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
            }
            JButton addDocButtion = new JButton("New info");
            addDocButtion.addActionListener(new AddDocListener());
            this.basicPanel.add(addDocButtion, BorderLayout.SOUTH);




            // set qnames
            this.elementBox.clear();
            for (QName qname : this.frame.getSelectedPipeline().getQNames()) {
                this.elementBox.add(this.newElementBox(this.frame.getSelectedPipeline().getQNames(), qname));
            }
            this.elementPanel.removeAll();
            for (Box hBox : this.elementBox) {
                this.elementPanel.add(hBox, BorderLayout.WEST);
            }

            JButton addElementButton = new JButton("Add");
            addElementButton.addActionListener(new AddElementListener());
            this.elementPanel.add(addElementButton, BorderLayout.SOUTH);


            // set inputs
            this.inputPanel.removeAll();
            this.inputPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
            for (GProcXPort port : this.frame.getSelectedPipeline().getInputs()) {
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
            JButton addPortButtion = new JButton("New port");
            addPortButtion.addActionListener(new AddInPortListener());
            this.inputPanel.add(addPortButtion, BorderLayout.SOUTH);




            this.outputPanel.removeAll();
            this.outputPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
            for (GProcXPort port : this.frame.getSelectedPipeline().getOutputs()) {
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
            addPortButtion = new JButton("New port");
            addPortButtion.addActionListener(new AddOutPortListener());
            this.outputPanel.add(addPortButtion, BorderLayout.SOUTH);







            this.repaint();
        }
    }

    private Box newDocBox(ArrayList<GProcXDoc> docs, GProcXDoc doc) {
        Box newBox = Box.createHorizontalBox();
        newBox.add(new EditDocButton(doc, "Edit"), BorderLayout.WEST);
        newBox.add(Box.createHorizontalStrut(10));
        newBox.add(new DeleteDocButton(docs, doc, "Delete"));
        newBox.add(Box.createHorizontalStrut(30));
        newBox.add(new JLabel(doc.getType()));
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
                            qname.getUriLexical() + "=",
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
        public DeleteDocButton(final ArrayList<GProcXDoc> docs, final GProcXDoc doc, String text) {
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
                                docs.remove(doc);
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
                        frame.getSelectedPipeline().addQName(new QName("", inputContent, ""));
                        frame.updateInfo();
                    }
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

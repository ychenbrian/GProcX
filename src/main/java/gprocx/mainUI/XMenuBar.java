package gprocx.mainUI;

import com.xml_project.morganaxproc.*;
import com.xml_project.morganaxproc.filesystem.XProcFilesystem;
import com.xml_project.morganaxproc.security.XProcSecurityException;
import gprocx.core.GProcXProcessor;
import gprocx.step.GProcXStep;
import gprocx.step.StepInfo;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.ParsingException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class XMenuBar extends JMenuBar {

    private XFrame frame;

    public XMenuBar(XFrame frame) {
        this.frame = frame;

        JMenu fileMenu = new JMenu("File");
        JMenuItem newMenuItem = new JMenuItem("New"); fileMenu.add(newMenuItem);
        JMenuItem openMenuItem = new JMenuItem("Open"); fileMenu.add(openMenuItem);
        JMenuItem saveMenuItem = new JMenuItem("Save"); fileMenu.add(saveMenuItem);
        fileMenu.addSeparator();
        JMenuItem importMenuItem = new JMenuItem("Import"); fileMenu.add(importMenuItem);
        JMenuItem exportMenuItem = new JMenuItem("Export"); fileMenu.add(exportMenuItem);
        JMenuItem runMenuItem = new JMenuItem("Run"); fileMenu.add(runMenuItem);

        JMenu editMenu = new JMenu("Edit");
        JMenuItem cutMenuItem = new JMenuItem("Cut"); editMenu.add(cutMenuItem);
        JMenuItem copyMenuItem = new JMenuItem("Copy"); editMenu.add(copyMenuItem);
        JMenuItem pasteMenuItem = new JMenuItem("Paste"); editMenu.add(pasteMenuItem);
        editMenu.addSeparator();
        JMenuItem closeTabMenuItem = new JMenuItem("Close tab"); editMenu.add(closeTabMenuItem);

        JMenu insertMenu = new JMenu("Insert");
        JMenuItem pipelineMenuItem = new JMenuItem("Pipeline"); insertMenu.add(pipelineMenuItem);
        JMenuItem pipeMenuItem = new JMenuItem("Pipe"); insertMenu.add(pipeMenuItem);
        insertMenu.addSeparator();
        JMenuItem atomicMenuItem = new JMenuItem("Atomic step"); insertMenu.add(atomicMenuItem);
        JMenuItem otherStepMenuItem = new JMenuItem("Other step"); insertMenu.add(otherStepMenuItem);
        
        newMenuItem.addActionListener(new NewMenu());
        openMenuItem.addActionListener(new OpenMenu());
        saveMenuItem.addActionListener(new SaveMenu());
        importMenuItem.addActionListener(new ImportMenu());
        exportMenuItem.addActionListener(new ExportMenu());
        runMenuItem.addActionListener(new RunMenu(this.frame));

        cutMenuItem.addActionListener(new CutMenu(this.frame));
        copyMenuItem.addActionListener(new CopyMenu(this.frame));
        pasteMenuItem.addActionListener(new PasteMenu(this.frame));
        closeTabMenuItem.addActionListener(new CloseTabMenu(this.frame));

        pipelineMenuItem.addActionListener(new PipelineMenu(this.frame));
        atomicMenuItem.addActionListener(new AtomicMenu(this.frame));
        otherStepMenuItem.addActionListener(new OtherStepMenu(this.frame));
        pipeMenuItem.addActionListener(new PipeMenu(this.frame));

        this.add(fileMenu);
        this.add(editMenu);
        this.add(insertMenu);
    }

    private File showFileOpen() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);

        fileChooser.setFileFilter(new FileNameExtensionFilter("gprocx(*.gprocx)", "gprocx"));

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {

            File file = fileChooser.getSelectedFile();

            return file;
        }
        return null;
    }

    private File showFileSave() {

        frame.showInformationMessage("You are going to save the current project.");

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("untitled.gprocx"));

        int result = fileChooser.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {

            File file = fileChooser.getSelectedFile();

            return file;
        }
        return null;
    }

    private File showFileImport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);

        fileChooser.setFileFilter(new FileNameExtensionFilter("xpl(*.xpl)", "xpl"));

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {

            File file = fileChooser.getSelectedFile();

            return file;
        }
        return null;
    }

    private File showFileExport() {

        frame.showInformationMessage("You are going to export the current pipeline.");

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("untitled.xpl"));

        int result = fileChooser.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {

            File file = fileChooser.getSelectedFile();

            return file;
        }
        return null;
    }

    private class NewMenu implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            final Object[] selectionValues = new Object[]{"p:declare-step", "p:pipeline", "p:library"};

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
                GProcXStep newPipeline = new GProcXStep();

                newPipeline.setType((String)inputContent);
                StepInfo.setStepInfo(frame, newPipeline);

                frame.addMainStep(newPipeline);
            }
        }
    }

    public static class CloseTabMenu implements ActionListener {
        XFrame frame;

        public CloseTabMenu(XFrame frame) {
            this.frame = frame;
        }
    	public void actionPerformed(ActionEvent e) {
            this.frame.removeCurrentTab();
            this.frame.setSelectedStep(this.frame.getCurrentStep());
        }
    }
    
    private class OpenMenu implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            File file = showFileOpen();
            if (file == null) {
                return;
            }

            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));

                GProcXStep pipeline = (GProcXStep) ois.readObject();
                frame.addMainStep(pipeline);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private class SaveMenu implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            GProcXStep pipeline = frame.getMainStep();

            File file = showFileSave();
            if (file == null) {
                return;
            }

            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                oos.writeObject(pipeline);
                oos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ImportMenu implements ActionListener {

        public void actionPerformed(ActionEvent event) {
        	
        	
        	final Object[] selectionValues = new Object[]{".xpl file", "Oxygen workspace"};

            Object inputContent = JOptionPane.showInputDialog(
                    null,
                    "Import from file or Oxygen",
                    "Source",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    selectionValues,
                    selectionValues[0]
            );
        	
            if (inputContent != null) {
            	String choice = (String)inputContent;
            	Builder parser = new Builder();
                Document docs = null;
            	
            	if (choice.equals(".xpl file")) {
            		File file = showFileImport();
                    if (file == null) {
                    	frame.showErrorMessage("Cannot open this file");
                        return;
                    }

                    try {
                        docs = parser.build(file);
                    } catch (ParsingException e) {
                        frame.showErrorMessage(e.getMessage());
                        return;
                    } catch (IOException e) {
                        frame.showErrorMessage(e.getMessage());
                        return;
                    }
            	} else { // from Oxygen workspace
                    try {
                        docs = parser.build(new StringReader(frame.getOxygenCode()));
                    } catch (ParsingException e) {
                        frame.showErrorMessage(e.getMessage());
                        return;
                    } catch (IOException e) {
                        frame.showErrorMessage(e.getMessage());
                        return;
                    }
            	}
            	
            	XProcEngine engine = XProcEngine.newXProc();
                XProcCompiler compiler = engine.newXProcCompiler();
                XProcPipeline pipeline = null;
                try {
                    XProcSource pipelineSource = new XProcSource(docs);
                    pipeline = compiler.compile(pipelineSource);
                } catch (XProcInterfaceException e) {
                    frame.showErrorMessage(e.getMessage());
                    return;
                } catch (XProcSecurityException e) {
                    frame.showErrorMessage(e.getMessage());
                    return;
                } catch (XProcFilesystem.UnsupportedXMLVersionException e) {
                    frame.showErrorMessage(e.getMessage());
                    return;
                } catch (IOException e) {
                    frame.showErrorMessage(e.getMessage());
                    return;
                } catch (XProcCompiler.XProcCompilerException e) {
                    frame.showErrorMessage(e.getMessage());
                    return;
                } catch (NullPointerException e) {
                    frame.showErrorMessage("This is not a runnable XProc program.");
                    return;
                }

                Element mainEle = (Element) docs.getChild(0);
                GProcXStep newPipeline = new GProcXStep();
                GProcXProcessor.setPipeline(frame, null, newPipeline, mainEle);

                frame.addMainStep(newPipeline);
            }
        }
    }

    private class ExportMenu implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            File file = showFileExport();

            if (file == null) {
                return;
            }
            try {
                PrintStream ps = new PrintStream(new FileOutputStream(file));
                ps.println(frame.getCode());
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static class CutMenu implements ActionListener {
        XFrame frame;

        public CutMenu(XFrame frame) {
            this.frame = frame;
        }

        public void actionPerformed(ActionEvent e) {
            if (frame.getSelectedStep() != null) {
                if (!frame.getSelectedStep().isAtomic()) {
                    frame.showErrorMessage("You can only cut atomic steps. And you need to select the step first.");
                    return;
                }

                //frame.getFigureTabs().removeStep(frame.getSelectedStep().getUUID());
                frame.setNewStep(new GProcXStep(frame, frame.getSelectedStep()));
                frame.getMainStep().deleteChild(frame.getSelectedStep());

                frame.setSelectedStep(frame.getMainStep());
            }
        }
    }

    public static class CopyMenu implements ActionListener {
        XFrame frame;

        public CopyMenu(XFrame frame) {
            this.frame = frame;
        }

        public void actionPerformed(ActionEvent e) {
            if (frame.getSelectedStep() != null) {
                if (!frame.getSelectedStep().isAtomic()) {
                    frame.showErrorMessage("You can only copy atomic steps. And you need to select the step first.");
                    return;
                }
                frame.setNewStep(new GProcXStep(frame, frame.getSelectedStep()));
            }
        }
    }

    public static class PasteMenu implements ActionListener {
        XFrame frame;

        public PasteMenu(XFrame frame) {
            this.frame = frame;
        }

        public void actionPerformed(ActionEvent e) {
            if (frame.getNewStep() != null) {

                GProcXStep newPipeline = new GProcXStep(frame, frame.getNewStep());
                newPipeline.setParent(frame.getMainStep());
                frame.setNewStep(newPipeline);
                frame.setDrawStepActive(true);
            }
        }
    }

    public static class PipelineMenu implements ActionListener {

        XFrame frame;

        public PipelineMenu(XFrame frame) {
            this.frame = frame;
        }

        public void actionPerformed(ActionEvent e) {

            if (!frame.getMainStep().getType().equals("p:library")) {
                XFrame.showErrorMessage("You can only insert pipelines into p:library.");
                return;
            }
            Object[] selectionValues = new String[]{"p:declare-step", "p:pipeline"};

            Object inputContent = JOptionPane.showInputDialog(
                    null,
                    "Choose the type:",
                    "Pipeline",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    selectionValues,
                    selectionValues[0]
            );
            if (inputContent != null) {
                GProcXStep newPipeline = new GProcXStep();
                newPipeline.setType((String) inputContent);
                StepInfo.setStepInfo(frame, newPipeline);

                frame.setNewStep(newPipeline);
                frame.setDrawStepActive(true);
            }
        }
    }

    public static class AtomicMenu implements ActionListener {

        XFrame frame;

        public AtomicMenu(XFrame frame) {
            this.frame = frame;
        }

    	public void actionPerformed(ActionEvent e) {

            if (frame.getMainStep().getType().equals("p:library")) {
                XFrame.showErrorMessage("You cannot insert steps into p:library.");
                return;
            }
            Object[] selectionValues = StepInfo.getAtomicTypes();

            Object inputContent = JOptionPane.showInputDialog(
                    null,
                    "Choose the atomic step:",
                    "Atomic steps",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    selectionValues,
                    selectionValues[0]
            );
            if (inputContent != null) {
                //frame.setNewStep((String) inputContent);
                GProcXStep newPipeline = new GProcXStep();
                newPipeline.setType((String) inputContent);
                StepInfo.setStepInfo(frame, newPipeline);

                frame.setNewStep(newPipeline);
                frame.setDrawStepActive(true);
            }

        }
    }

    public static class OtherStepMenu implements ActionListener {

        XFrame frame;

        public OtherStepMenu(XFrame frame) {
            this.frame = frame;
        }

        public void actionPerformed(ActionEvent e) {

            if (frame.getMainStep().getType().equals("p:library")) {
                XFrame.showErrorMessage("You cannot insert steps into p:library.");
                return;
            }
            Object[] selectionValues = StepInfo.getOtherTypes();

            Object inputContent = JOptionPane.showInputDialog(
                    null,
                    "Choose the step:",
                    "Other steps",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    selectionValues,
                    selectionValues[0]
            );
            if (inputContent != null) {
                GProcXStep newPipeline = new GProcXStep();
                newPipeline.setType((String) inputContent);
                StepInfo.setStepInfo(frame, newPipeline);

                frame.setNewStep(newPipeline);
                frame.setDrawStepActive(true);
            }

        }
    }

    public static class PipeMenu implements ActionListener {

        XFrame frame;

        public PipeMenu(XFrame frame) {
            this.frame = frame;
        }

        public void actionPerformed(ActionEvent e) {
            if (frame.getMainStep().getType().equals("p:library")) {
                XFrame.showErrorMessage("You cannot insert pips into p:library.");
                return;
            }
            frame.setDrawPipe01Active(true);
            frame.showInformationMessage("Select the FROM step or port.");
        }
    }

    public static class RunMenu implements ActionListener {
        XFrame frame;

        public RunMenu(XFrame frame) {
            this.frame = frame;
        }

        public void actionPerformed(ActionEvent event) {
            if (!frame.getMainStep().getType().equals("p:declare-step") && !frame.getMainStep().getType().equals("p:pipeline")) {
                frame.showErrorMessage("You can only run root pipelines.");
            } else {
                Builder parser = new Builder();
                Document docs = null;

                try {
                    docs = parser.build(new StringReader(frame.getMainStep().toString(0)));
                } catch (ParsingException e) {
                    frame.showErrorMessage(e.getMessage());
                    return;
                } catch (IOException e) {
                    frame.showErrorMessage(e.getMessage());
                    return;
                }

                XProcEngine engine = XProcEngine.newXProc();
                XProcCompiler compiler = engine.newXProcCompiler();
                XProcPipeline pipeline = null;
                try {
                    XProcSource pipelineSource = new XProcSource(docs);
                    pipeline = compiler.compile(pipelineSource);
                } catch (XProcInterfaceException e) {
                    //frame.showErrorMessage(e.getMessage());
                    return;
                } catch (XProcSecurityException e) {
                    //frame.showErrorMessage(e.getMessage());
                    return;
                } catch (XProcFilesystem.UnsupportedXMLVersionException e) {
                    //frame.showErrorMessage(e.getMessage());
                    return;
                } catch (IOException e) {
                    //frame.showErrorMessage(e.getMessage());
                    return;
                } catch (XProcCompiler.XProcCompilerException e) {
                    frame.showErrorMessage(e.getMessage());
                    return;
                } catch (NullPointerException e) {
                    frame.showErrorMessage("This is not a runnable XProc program.");
                    return;
                }

                XProcOutput output = pipeline.run();
                if (output.wasSuccessful()){
                    String[] ports = output.getPortNames();
                    for (int i=0; i < ports.length; i++){
                        XProcResult result = output.getResults(ports[i]);
                        String[] documents = result.getDocumentsSerialized();
                        for (int j=0; j < documents.length; j++)
                            JOptionPane.showMessageDialog(
                                    null,
                                    documents[j].substring(38),
                                    ports[j],
                                    JOptionPane.PLAIN_MESSAGE
                            );
                    }
                } else {
                    frame.showErrorMessage("Fail to run this program.");
                }
            }
        }
    }
}

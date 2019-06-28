package com.oxygenxml.sdksamples.workspace.step;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

public class AtomicInfo {

    private ArrayList<Node> fList;

    public AtomicInfo() {

        try {
            File fXmlFile = new File("xproc-1.0.xpl");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("p:declare-pipeline");

            this.fList = new ArrayList<Node>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                this.fList.add(nodeList.item(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Node getElement(String type) {
        for (Node fNode : this.fList) {
            if (fNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element)fNode;

                //System.out.println(eElement.getAttribute("type"));
                if (element.getAttribute("type").equals(type)) {
                    return fNode;
                }
            }
        }
        return null;
    }

    public ArrayList<Node> getNodeList() {
        return this.fList;
    }
}

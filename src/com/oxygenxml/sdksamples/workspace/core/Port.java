package com.oxygenxml.sdksamples.workspace.core;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;

public class Port {

    protected String port;
    protected boolean primary;
    protected boolean sequence;
    protected String kind;

    protected ArrayList<InSource> sources;
    protected ArrayList<QName> qnames;

    public Port(Node cNode) {

        this.sources = new ArrayList<InSource>();
        this.qnames = new ArrayList<QName>();

        Element element = (Element) cNode;
        this.port = element.getAttribute("port");
        this.addQName(new QName("", "port", this.port));
        this.kind = "document";

        if (element.getAttribute("primary").equals("true")) {
            this.primary = true;
        } else if (element.getAttribute("primary").equals("false")) {
            this.primary = false;
        } else {
            if (this.port.equals("source") || this.port.equals("result")) {
                this.primary = true;
            } else {
                this.primary = false;
            }
        }

        if (element.getAttribute("sequence").equals("true")) {
            this.sequence = true;
        } else {
            this.sequence = false;
        }

        this.kind = "document";
        if (!element.getAttribute("kind").equals("")) {
            this.kind = element.getAttribute("kind");
        }
    }

    public Port(String port, boolean primary, boolean sequence, String kind) {
        this.port = port;
        this.primary = primary;
        this.sequence = sequence;
        this.kind = kind;

        this.qnames = new ArrayList<QName>();
        this.addQName(new QName("", "port", this.port));
    }

    public void addQName(QName qname) {
        this.qnames.add(qname);
    }

    public String getPort() {
        return port;
    }

    public boolean isPrimary() {
        return primary;
    }

    public boolean isSequence() {
        return sequence;
    }

    public String getKind() {
        return kind;
    }

    public ArrayList<InSource> getSources() {
        return this.sources;
    }
}

package com.oxygenxml.sdksamples.workspace.core;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class StepOption {

    private String name;
    private String select;
    private boolean required;

    public StepOption(Node cNode) {

        Element element = (Element) cNode;
        this.name = element.getAttribute("name");
        this.select = element.getAttribute("select");

        if (element.getAttribute("required").equals("true")) {
            this.required = true;
        } else {
            this.required = false;
        }
    }

    public String getName() {
        return name;
    }

    public boolean isRequired() {
        return required;
    }

    public String getSelect() {
        return select;
    }
}

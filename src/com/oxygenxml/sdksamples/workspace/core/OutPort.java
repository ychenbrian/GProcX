package com.oxygenxml.sdksamples.workspace.core;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;

public class OutPort extends Port {

    public OutPort(Node cNode) {
        super(cNode);
    }

    public OutPort(String port, boolean primary, boolean sequence, String kind) {
        super(port, primary, sequence, kind);
    }

    public String toString() {
        String code = "";

        code += "<" + "p:output";

        for (QName qname : qnames) {

            if (qname.getValue() != null) {
                code += " " + qname;
            }
        }

        if (!sources.isEmpty()) {
            code += ">\n    ";
            for (InSource source : sources) {
                code += source.toString();
            }
            code += "</p:output>\n";
        } else {
            code += "/>\n";
        }

        return code;
    }
}

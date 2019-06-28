package com.oxygenxml.sdksamples.workspace.core;

import java.util.ArrayList;

public class InSource {

    private ArrayList<QName> qnames;
    private String sourceType;

    public InSource(String sourceType) {
        this.sourceType = sourceType;
        this.qnames = new ArrayList<QName>();
    }

    public void addQName(QName qname) {
        this.qnames.add(qname);
    }

    public ArrayList<QName> getQNames() {
        return this.qnames;
    }

    public String getSourceType() {
        return this.sourceType;
    }

    public String toString() {
        String code = "";
        code += "<" + this.sourceType + " ";
        for (QName qname : qnames) {
            code += qname.toString();
        }
        code += "/>";
        return code;
    }
}

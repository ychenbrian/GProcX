package gprocx.core;

import java.util.ArrayList;

public class IOSource {

    private ArrayList<QName> qnames;
    private String sourceType;

    public IOSource(String sourceType) {
        this.sourceType = sourceType;
        this.qnames = new ArrayList<QName>();

        if (this.sourceType.equals("p:document")) {
            this.qnames.add(new QName("", "href", ""));
        } else if (sourceType.equals("p:data")) {
            this.qnames.add(new QName("", "href", ""));
            this.qnames.add(new QName("", "wrapper", ""));
            this.qnames.add(new QName("", "wrapper-prefix", ""));
            this.qnames.add(new QName("", "wrapper-namespace", ""));
            this.qnames.add(new QName("", "content-type", ""));
        } else if (sourceType.equals("p:inline")) {
            this.qnames.add(new QName("", "exclude-inline-prefixes", ""));
        }
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
        code += "<" + this.sourceType;
        for (QName qname : qnames) {
            if (!qname.getValue().equals("")) {
                code += " " + qname.toString();
            }
        }
        code += "/>\n";
        return code;
    }
}

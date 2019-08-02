package gprocx.core;

import gprocx.step.GProcXPipe;
import gprocx.step.GProcXPipeline;

import java.io.Serializable;

public class OutPort extends GProcXPort implements Serializable {

    public OutPort(GProcXPipeline parent, String port, boolean primary, boolean sequence, String kind) {
        super(parent, port, primary, sequence, kind);
    }

    public OutPort() {
        super();
    }

    @Override
    public String getPort() {
        for (QName qname : this.qnames) {
            if (qname.getLexical().equals("port")) {
                return qname.getValue();
            }
        }
        return "result";
    }

    public String toString(int retract) {
        String code = "";

        for (int i = 0; i < retract; i++) {
            code += "    ";
        }
        code += "<" + "p:output";

        for (QName namespace : this.namespaces) {
            if (!hasDefineNS(namespace)) {
                if (!namespace.getValue().equals("")) {
                    code += " " + namespace;
                }
            }
        }
        for (QName qname : qnames) {
            if (qname.getLexical().equals("primary")) {
                if (this.getPort().equals("result") && qname.getValue().equals("true")) {
                    continue;
                }
            }
            if (qname.getLexical().equals("sequence") && qname.getValue().equals("false")) {
                continue;
            }
            if (!qname.getValue().equals("")) {
                code += " " + qname;
            }
        }

        if (!sources.isEmpty()) {
            code += ">\n";

            for (GProcXPipe pipe : pipes) {
                if (!pipe.isValid()) {
                    continue;
                }
                if (!pipe.isDefault()) {
                    code += pipe.toString(retract + 1);
                }
            }

            for (int i = 0; i < retract; i++) {
                code += "    ";
            }
            code += "</p:output>\n";
        } else {
            code += "/>\n";
        }

        return code;
    }
}

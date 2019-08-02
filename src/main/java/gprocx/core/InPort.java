package gprocx.core;

import gprocx.step.GProcXPipe;
import gprocx.step.GProcXPipeline;

import java.io.Serializable;

public class InPort extends GProcXPort implements Serializable {

    public InPort(GProcXPipeline parent, String port, boolean primary, boolean sequence, String kind) {
        super(parent, port, primary, sequence, kind);
    }

    public InPort() {
        super();
    }

    @Override
    public String getPort() {
        for (QName qname : this.qnames) {
            if (qname.getLexical().equals("port")) {
                return qname.getValue();
            }
        }
        return "source";
    }

    public String toString(int retract) {
        String code = "";

        for (int i = 0; i < retract; i++) {
            code += "    ";
        }

        code += "<" + "p:input";

        for (QName namespace : this.namespaces) {
            if (!hasDefineNS(namespace)) {
                if (!namespace.getValue().equals("")) {
                    code += " " + namespace;
                }
            }
        }
        for (QName qname : qnames) {
            if (qname.getLexical().equals("primary")) {
                if (this.getPort().equals("source") && qname.getValue().equals("true")) {
                    continue;
                }
            }
            if (qname.getLexical().equals("sequence") && qname.getValue().equals("false")) {
                continue;
            }
            if (!qname.getValue().equals("")) {
                code += " " + qname.toString();
            }
        }

        if (!isBasic()) {
            code += ">\n";

            for (GProcXPipe pipe : pipes) {
                if (!pipe.isValid()) {
                    continue;
                }
               // if (!pipe.isDefault()) {
                    code += pipe.toString(retract + 1);
                //}
            }

            for (IOSource source : sources) {
                code += source.toString(retract + 1);
            }

            for (int i = 0; i < retract; i++) {
                code += "    ";
            }
            code += "</p:input>\n";
        } else {
            code += "/>\n";
        }

        return code;
    }
}

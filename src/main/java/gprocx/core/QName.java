package gprocx.core;

import java.io.Serializable;

public class QName implements Serializable {

    private String lexical = "";
    private String value = "";
    private boolean isRequired = false;

    public QName(QName qname) {
        this.lexical = qname.lexical;
        this.value = qname.value;
    }

    public QName(String lexical) {
        this.lexical = lexical;
    }

    public QName(String lexical, String value) {
        this.lexical = lexical;
        this.value = value;
    }

    public void setLexical(String lexical) {
        this.lexical = lexical;
    }

    public String getLexical() {
        return this.lexical;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }

    public String toString() {

        String code = "";
        code += lexical + "=\"";
        code += value + "\"";

        return code;
    }
}

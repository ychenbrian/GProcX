package gprocx.core;

public class QName {

    private String uri = "";
    private String lexical = "";
    private String value = "";

    public QName(QName qname) {
        this.uri = qname.uri;
        this.lexical = qname.lexical;
        this.value = qname.value;
    }

    public QName(String uri, String lexical) {
        this.uri = uri;
        this.lexical = lexical;
    }

    public QName(String qname) {
        for (int i = 0; i < qname.length(); i++) {
            if (qname.charAt(i) != ':') {
                this.uri += qname.charAt(i);
            } else {
                for (int j = i + 1; j < qname.length(); j++) {
                    this.lexical += qname.charAt(j);
                }
                break;
            }
        }
    }

    public QName(String uri, String lexical, String value) {
        this.uri = uri;
        this.lexical = lexical;
        this.value = value;
    }

    public void setLexical(String lexical) {
        this.lexical = lexical;
    }

    public String getLexical() {
        return this.lexical;
    }

    public String getUriLexical() {
        String code = "";
        if (this.uri != "") {
            code += uri + ":";
        }
        code += lexical;

        return code;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String toString() {

        String code = "";
        if (this.uri != "") {
            code += uri + ":";
        }
        code += lexical + "=\"";
        code += value + "\"";

        return code;
    }
}

package gprocx.core;

public class QName {

    private String uri;
    private String lexical;
    private String value = null;

    public QName(String uri, String lexical) {
        this.uri = uri;
        this.lexical = lexical;
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

package gprocx.step;

import gprocx.core.InPort;
import gprocx.core.OutPort;
import gprocx.core.StepOption;
import gprocx.mainUI.XFrame;

public class StepInfo {

    public StepInfo() {}

    public static void setPipelineInfo(XFrame frame, Pipeline pipeline) {

        String type = pipeline.getType();
        //Pipeline newPipeline = new Pipeline(frame, type);

        /*
         else if (type.equals("")) {
            pipeline.setDocumentation("");
            pipeline.addInput(new InPort("source", true, false, "document"));
            pipeline.addOutput(new OutPort("result", true, false, "document"));
            pipeline.addOption(new StepOption("", "", false));
        }
        */

        if (type.equals("p:declare-step")) {
            pipeline.setDocumentation("A p:declare-step provides the type and signature of an atomic step or pipeline. It declares the inputs, outputs, and options for all steps of that type.");
            pipeline.setIsAtomic(false);
            pipeline.addInput(new InPort("source", true, false, "document"));
            pipeline.addOutput(new OutPort("result", true, false, "document"));
            pipeline.addOption(new StepOption("type", "", false));
            pipeline.addOption(new StepOption("psvi-required", "", false));
            pipeline.addOption(new StepOption("xpath-version", "", false));
            pipeline.addOption(new StepOption("exclude-inline-prefixes", "", false));
            pipeline.addOption(new StepOption("version", "1.0", false));
            pipeline.addOption(new StepOption("xmlns:p", "http://www.w3.org/ns/xproc", false));
        } else if (type.equals("p:add-attribute")) {
            pipeline.setDocumentation("The p:add-attribute step adds a single attribute to a set of matching elements. The input document specified on the source is processed for matches specified by the match pattern in the match option. For each of these matches, the attribute whose name is specified by the attribute-name option is set to the attribute value specified by the attribute-value option.");
            pipeline.addInput(new InPort("source", true, false, "document"));
            pipeline.addOutput(new OutPort("result", true, false, "document"));
            pipeline.addOption(new StepOption("match", "", true));
            pipeline.addOption(new StepOption("attribute-name", "", true));
            pipeline.addOption(new StepOption("attribute-prefix", "", false));
            pipeline.addOption(new StepOption("attribute-namespace", "", false));
            pipeline.addOption(new StepOption("attribute-value", "", true));
        } else if (type.equals("p:add-xml-base")) {
            pipeline.setDocumentation("The p:add-xml-base step exposes the base URI via explicit xml:base attributes. The input document from the source port is replicated to the result port with xml:base attributes added to or corrected on each element as specified by the options on this step.");
            pipeline.addInput(new InPort("source", true, false, "document"));
            pipeline.addOutput(new OutPort("result", true, false, "document"));
            pipeline.addOption(new StepOption("all", "false", false));
            pipeline.addOption(new StepOption("relative", "true", false));
        } else if (type.equals("p:compare")) {
            pipeline.setDocumentation("The p:compare step compares two documents for equality.");
            pipeline.addInput(new InPort("source", true, false, "document"));
            pipeline.addInput(new InPort("alternate", false, false, "document"));
            pipeline.addOutput(new OutPort("result", false, false, "document"));
            pipeline.addOption(new StepOption("fail-if-not-equal", "false", false));
        } else if (type.equals("p:count")) {
            pipeline.setDocumentation("The p:count step counts the number of documents in the source input sequence and returns a single document on result containing that number. The generated document contains a single c:result element whose contents is the string representation of the number of documents in the sequence.");
            pipeline.addInput(new InPort("source", true, true, "document"));
            pipeline.addOutput(new OutPort("result", true, false, "document"));
            pipeline.addOption(new StepOption("limit", "0", false));
        } else if (type.equals("p:delete")) {
            pipeline.setDocumentation("The p:delete step deletes items specified by a match pattern from the source input document and produces the resulting document, with the deleted items removed, on the result port.");
            pipeline.addInput(new InPort("source", true, false, "document"));
            pipeline.addOutput(new OutPort("result", true, false, "document"));
            pipeline.addOption(new StepOption("match", "", true));
        } else if (type.equals("p:directory-list")) {
            pipeline.setDocumentation("The p:directory-list step produces a list of the contents of a specified directory.");
            pipeline.addOutput(new OutPort("result", true, false, "document"));
            pipeline.addOption(new StepOption("path", "", true));
            pipeline.addOption(new StepOption("include-filter", "", false));
            pipeline.addOption(new StepOption("exclude-filter", "", false));
        } else if (type.equals("p:error")) {
            pipeline.setDocumentation("The p:error step generates a dynamic error using the input provided to the step.");
            pipeline.addInput(new InPort("source", false, false, "document"));
            pipeline.addOutput(new OutPort("result", true, true, "document"));
            pipeline.addOption(new StepOption("code", "", true));
            pipeline.addOption(new StepOption("code-prefix", "", false));
            pipeline.addOption(new StepOption("code-namespace", "", false));
        } else if (type.equals("p:escape-markup")) {
            pipeline.setDocumentation("The p:escape-markup step applies XML serialization to the children of the document element and replaces those children with their serialization. The outcome is a single element with text content that represents the \"escaped\" syntax of the children as they were serialized.");
            pipeline.addInput(new InPort("source", true, false, "document"));
            pipeline.addOutput(new OutPort("result", true, false, "document"));
            pipeline.addOption(new StepOption("cdata-section-elements", "", false));
            pipeline.addOption(new StepOption("doctype-public", "", false));
            pipeline.addOption(new StepOption("doctype-system", "", false));
            pipeline.addOption(new StepOption("escape-uri-attributes", "false", false));
            pipeline.addOption(new StepOption("include-content-type", "true", false));
            pipeline.addOption(new StepOption("indent", "false", false));
            pipeline.addOption(new StepOption("media-type", "", false));
            pipeline.addOption(new StepOption("method", "xml", false));
            pipeline.addOption(new StepOption("omit-xml-declaration", "true", false));
            pipeline.addOption(new StepOption("standalone", "omit", false));
            pipeline.addOption(new StepOption("undeclare-prefixes", "", false));
            pipeline.addOption(new StepOption("version", "1.0", false));
        } else if (type.equals("p:filter")) {
            pipeline.setDocumentation("The p:filter step selects portions of the source document based on a (possibly dynamically constructed) XPath select expression.");
            pipeline.addInput(new InPort("source", true, false, "document"));
            pipeline.addOutput(new OutPort("result", true, true, "document"));
            pipeline.addOption(new StepOption("select", "", true));
        } else if (type.equals("p:http-request")) {
            pipeline.setDocumentation("The p:http-request step provides for interaction with resources over HTTP or related protocols. The input document provided on the source port specifies a request by a single c:request element. This element specifies the method, resource, and other request properties as well as possibly including an entity body (content) for the request.");
            pipeline.addInput(new InPort("source", true, false, "document"));
            pipeline.addOutput(new OutPort("result", true, false, "document"));
            pipeline.addOption(new StepOption("byte-order-mark", "", false));
            pipeline.addOption(new StepOption("cdata-section-elements", "", false));
            pipeline.addOption(new StepOption("doctype-public", "", false));
            pipeline.addOption(new StepOption("doctype-system", "", false));
            pipeline.addOption(new StepOption("encoding", "", false));
            pipeline.addOption(new StepOption("escape-uri-attributes", "false", false));
            pipeline.addOption(new StepOption("include-content-type", "true", false));
            pipeline.addOption(new StepOption("indent", "false", false));
            pipeline.addOption(new StepOption("media-type", "", false));
            pipeline.addOption(new StepOption("method", "xml", false));
            pipeline.addOption(new StepOption("normalization-form", "none", false));
            pipeline.addOption(new StepOption("omit-xml-declaration", "true", false));
            pipeline.addOption(new StepOption("standalone", "omit", false));
            pipeline.addOption(new StepOption("undeclare-prefixes", "", false));
            pipeline.addOption(new StepOption("version", "1.0", false));
        } else if (type.equals("p:identity")) {
            pipeline.setDocumentation("The p:identity step makes a verbatim copy of its input available on its output.");
            pipeline.addInput(new InPort("source", true, false, "document"));
            pipeline.addOutput(new OutPort("result", true, false, "document"));
        } else if (type.equals("p:insert")) {
            pipeline.setDocumentation("The p:insert step inserts the insertion port's document into the source port's document relative to the matching elements in the source port's document.");
            pipeline.addInput(new InPort("source", true, false, "document"));
            pipeline.addInput(new InPort("insertion", false, true, "document"));
            pipeline.addOutput(new OutPort("result", true, false, "document"));
            pipeline.addOption(new StepOption("match", "/*", false));
            pipeline.addOption(new StepOption("position", "", true));
        } else if (type.equals("p:label-elements")) {
            pipeline.setDocumentation("The p:label-elements step generates a label for each matched element and stores that label in the specified attribute.");
            pipeline.addInput(new InPort("source", true, false, "document"));
            pipeline.addOutput(new OutPort("result", true, false, "document"));
            pipeline.addOption(new StepOption("attribute", "xml:id", false));
            pipeline.addOption(new StepOption("attribute-prefix", "", false));
            pipeline.addOption(new StepOption("attribute-namespace", "", false));
            pipeline.addOption(new StepOption("label", "concat(&#34;_&#34;,$p:index)", false));
            pipeline.addOption(new StepOption("match", "*", false));
            pipeline.addOption(new StepOption("replace", "true", false));
        } else if (type.equals("p:load")) {
            pipeline.setDocumentation("The p:load step has no inputs but produces as its result an XML resource specified by an IRI.");
            pipeline.addOutput(new OutPort("result", true, false, "document"));
            pipeline.addOption(new StepOption("href", "", true));
            pipeline.addOption(new StepOption("dtd-validate", "false", false));
        } else if (type.equals("p:make-absolute-uris")) {
            pipeline.setDocumentation("The p:make-absolute-uris step makes an element or attribute's value in the source document an absolute IRI value in the result document.");
            pipeline.addInput(new InPort("source", true, false, "document"));
            pipeline.addOutput(new OutPort("result", true, false, "document"));
            pipeline.addOption(new StepOption("match", "", true));
            pipeline.addOption(new StepOption("base-uri", "", false));
        } else if (type.equals("p:namespace-rename")) {
            pipeline.setDocumentation("The p:namespace-rename step renames any namespace declaration or use of a namespace in a document to a new IRI value.");
            pipeline.addInput(new InPort("source", true, false, "document"));
            pipeline.addOutput(new OutPort("result", true, false, "document"));
            pipeline.addOption(new StepOption("from", "", false));
            pipeline.addOption(new StepOption("to", "", false));
            pipeline.addOption(new StepOption("apply-to", "all", false));
        } else if (type.equals("p:pack")) {
            pipeline.setDocumentation("The p:pack step merges two document sequences in a pair-wise fashion.");
            pipeline.addInput(new InPort("source", true, true, "document"));
            pipeline.addInput(new InPort("alternate", false, true, "document"));
            pipeline.addOutput(new OutPort("result", true, true, "document"));
            pipeline.addOption(new StepOption("wrapper", "", true));
            pipeline.addOption(new StepOption("wrapper-prefix", "", false));
            pipeline.addOption(new StepOption("wrapper-namespace", "", false));
        } else if (type.equals("p:parameters")) {
            pipeline.setDocumentation("The p:parameters step exposes a set of parameters as a c:param-set document.");
            pipeline.addInput(new InPort("parameters", false, false, "parameters"));
            pipeline.addOutput(new OutPort("result", false, false, "document"));
        } else if (type.equals("p:rename")) {
            pipeline.setDocumentation("The p:rename step renames elements, attributes, or processing-instruction targets in a document.");
            pipeline.addInput(new InPort("source", true, false, "document"));
            pipeline.addOutput(new OutPort("result", true, false, "document"));
            pipeline.addOption(new StepOption("match", "", true));
            pipeline.addOption(new StepOption("new-name", "", true));
            pipeline.addOption(new StepOption("new-prefix", "", false));
            pipeline.addOption(new StepOption("new-namespace", "", false));
        } else if (type.equals("p:replace")) {
            pipeline.setDocumentation("The p:replace step replaces matching nodes in its primary input with the document element of the replacement port's document.");
            pipeline.addInput(new InPort("source", true, false, "document"));
            pipeline.addInput(new InPort("replacement", false, false, "document"));
            pipeline.addOutput(new OutPort("result", true, false, "document"));
            pipeline.addOption(new StepOption("match", "", true));
        } else if (type.equals("p:set-attributes")) {
            pipeline.setDocumentation("The p:set-attributes step sets attributes on matching elements.");
            pipeline.addInput(new InPort("source", true, false, "document"));
            pipeline.addInput(new InPort("attributes", false, false, "document"));
            pipeline.addOutput(new OutPort("result", true, false, "document"));
            pipeline.addOption(new StepOption("match", "", true));
        } else if (type.equals("p:sink")) {
            pipeline.setDocumentation("The p:sink step accepts a sequence of documents and discards them. It has no output.");
            pipeline.addInput(new InPort("source", true, true, "document"));
        } else if (type.equals("p:split-sequence")) {
            pipeline.setDocumentation("The p:split-sequence step accepts a sequence of documents and divides it into two sequences.");
            pipeline.addInput(new InPort("source", true, true, "document"));
            pipeline.addOutput(new OutPort("matched", true, true, "document"));
            pipeline.addOutput(new OutPort("not-matched", false, true, "document"));
            pipeline.addOption(new StepOption("initial-only", "false", false));
            pipeline.addOption(new StepOption("test", "", true));
        } else if (type.equals("p:store")) {
            pipeline.setDocumentation("The p:store step stores a serialized version of its input to a URI. This step outputs a reference to the location of the stored document.");
            pipeline.addInput(new InPort("source", true, false, "document"));
            pipeline.addOutput(new OutPort("result", false, false, "document"));
            pipeline.addOption(new StepOption("href", "", true));
            pipeline.addOption(new StepOption("byte-order-mark", "", false));
            pipeline.addOption(new StepOption("cdata-section-elements", "", false));
            pipeline.addOption(new StepOption("doctype-public", "", false));
            pipeline.addOption(new StepOption("doctype-system", "", false));
            pipeline.addOption(new StepOption("encoding", "", false));
            pipeline.addOption(new StepOption("escape-uri-attributes", "false", false));
            pipeline.addOption(new StepOption("include-content-type", "true", false));
            pipeline.addOption(new StepOption("indent", "false", false));
            pipeline.addOption(new StepOption("media-type", "", false));
            pipeline.addOption(new StepOption("method", "xml", false));
            pipeline.addOption(new StepOption("normalization-form", "none", false));
            pipeline.addOption(new StepOption("omit-xml-declaration", "true", false));
            pipeline.addOption(new StepOption("standalone", "omit", false));
            pipeline.addOption(new StepOption("undeclare-prefixes", "", false));
            pipeline.addOption(new StepOption("version", "1.0", false));
        } else if (type.equals("p:string-replace")) {
            pipeline.setDocumentation("The p:string-replace step matches nodes in the document provided on the source port and replaces them with the string result of evaluating an XPath expression.");
            pipeline.addInput(new InPort("source", true, false, "document"));
            pipeline.addOutput(new OutPort("result", true, false, "document"));
            pipeline.addOption(new StepOption("match", "", true));
            pipeline.addOption(new StepOption("replace", "", true));
        } else if (type.equals("p:unescape-markup")) {
            pipeline.setDocumentation("The p:unescape-markup step takes the string value of the document element and parses the content as if it was a Unicode character stream containing serialized XML. The output consists of the same document element with children that result from the parse. This is the reverse of the p:escape-markup step.");
            pipeline.addInput(new InPort("source", true, false, "document"));
            pipeline.addOutput(new OutPort("result", true, false, "document"));
            pipeline.addOption(new StepOption("namespace", "", false));
            pipeline.addOption(new StepOption("content-type", "application/xml", false));
            pipeline.addOption(new StepOption("encoding", "", false));
            pipeline.addOption(new StepOption("charset", "", false));
        } else if (type.equals("p:unwrap")) {
            pipeline.setDocumentation("The p:unwrap step replaces matched elements with their children.");
            pipeline.addInput(new InPort("source", true, false, "document"));
            pipeline.addOutput(new OutPort("result", true, false, "document"));
            pipeline.addOption(new StepOption("match", "", true));
        } else if (type.equals("p:wrap")) {
            pipeline.setDocumentation("The p:wrap step wraps matching nodes in the source document with a new parent element.");
            pipeline.addInput(new InPort("source", true, false, "document"));
            pipeline.addOutput(new OutPort("result", true, false, "document"));
            pipeline.addOption(new StepOption("wrapper", "", true));
            pipeline.addOption(new StepOption("wrapper-prefix", "", false));
            pipeline.addOption(new StepOption("wrapper-namespace", "", false));
            pipeline.addOption(new StepOption("match", "", true));
            pipeline.addOption(new StepOption("group-adjacent", "", false));
        } else if (type.equals("p:wrap-sequence")) {
            pipeline.setDocumentation("The p:wrap-sequence step accepts a sequence of documents and produces either a single document or a new sequence of documents.");
            pipeline.addInput(new InPort("source", true, true, "document"));
            pipeline.addOutput(new OutPort("result", true, true, "document"));
            pipeline.addOption(new StepOption("wrapper", "", true));
            pipeline.addOption(new StepOption("wrapper-prefix", "", false));
            pipeline.addOption(new StepOption("wrapper-namespace", "", false));
            pipeline.addOption(new StepOption("group-adjacent", "", false));
        } else if (type.equals("p:xinclude")) {
            pipeline.setDocumentation("The p:xinclude step applies [XInclude] processing to the source document.");
            pipeline.addInput(new InPort("source", true, false, "document"));
            pipeline.addOutput(new OutPort("result", true, false, "document"));
            pipeline.addOption(new StepOption("fixup-xml-base", "false", false));
            pipeline.addOption(new StepOption("fixup-xml-lang", "false", false));
        } else if (type.equals("p:xslt")) {
            pipeline.setDocumentation("The p:xslt step applies an [XSLT 1.0] or [XSLT 2.0] stylesheet to a document.");
            pipeline.addInput(new InPort("source", true, true, "document"));
            pipeline.addInput(new InPort("stylesheet", false, false, "document"));
            pipeline.addInput(new InPort("parameters", false, false, "parameter"));
            pipeline.addOutput(new OutPort("result", true, false, "document"));
            pipeline.addOutput(new OutPort("secondary", false, true, "document"));
            pipeline.addOption(new StepOption("initial-mode", "", false));
            pipeline.addOption(new StepOption("template-name", "", false));
            pipeline.addOption(new StepOption("output-base-uri", "", false));
            pipeline.addOption(new StepOption("version", "", false));
        } else if (type.equals("p:exec")) {
            pipeline.setDocumentation("The p:exec step runs an external command passing the input that arrives on its source port as standard input, reading result from standard output, and errors from standard error.");
            pipeline.addInput(new InPort("source", true, true, "document"));
            pipeline.addOutput(new OutPort("result", true, false, "document"));
            pipeline.addOutput(new OutPort("errors", false, false, "document"));
            pipeline.addOutput(new OutPort("exit-status", false, false, "document"));
            pipeline.addOption(new StepOption("command", "", true));
            pipeline.addOption(new StepOption("args", "", false));
            pipeline.addOption(new StepOption("cwd", "", false));
            pipeline.addOption(new StepOption("source-is-xml", "true", false));
            pipeline.addOption(new StepOption("result-is-xml", "true", false));
            pipeline.addOption(new StepOption("wrap-result-lines", "false", false));
            pipeline.addOption(new StepOption("errors-is-xml", "false", false));
            pipeline.addOption(new StepOption("wrap-error-lines", "false", false));
            pipeline.addOption(new StepOption("path-separator", "", false));
            pipeline.addOption(new StepOption("failure-threshold", "", false));
            pipeline.addOption(new StepOption("arg-separator", " ", false));
            pipeline.addOption(new StepOption("byte-order-mark", "", false));
            pipeline.addOption(new StepOption("cdata-section-elements", "", false));
            pipeline.addOption(new StepOption("doctype-public", "", false));
            pipeline.addOption(new StepOption("doctype-system", "", false));
            pipeline.addOption(new StepOption("encoding", "", false));
            pipeline.addOption(new StepOption("escape-uri-attributes", "false", false));
            pipeline.addOption(new StepOption("include-content-type", "true", false));
            pipeline.addOption(new StepOption("indent", "false", false));
            pipeline.addOption(new StepOption("media-type", "", false));
            pipeline.addOption(new StepOption("method", "xml", false));
            pipeline.addOption(new StepOption("normalization-form", "none", false));
            pipeline.addOption(new StepOption("omit-xml-declaration", "true", false));
            pipeline.addOption(new StepOption("standalone", "omit", false));
            pipeline.addOption(new StepOption("undeclare-prefixes", "", false));
            pipeline.addOption(new StepOption("version", "1.0", false));
        } else if (type.equals("p:hash")) {
            pipeline.setDocumentation("The p:hash step generates a hash, or digital \"fingerprint\", for some value and injects it into the source document.");
            pipeline.addInput(new InPort("source", true, false, "document"));
            pipeline.addInput(new InPort("parameters", false, false, "parameters"));
            pipeline.addOutput(new OutPort("result", true, false, "document"));
            pipeline.addOption(new StepOption("value", "", true));
            pipeline.addOption(new StepOption("algorithm", "", true));
            pipeline.addOption(new StepOption("match", "", true));
            pipeline.addOption(new StepOption("version", "", false));
        } else if (type.equals("p:uuid")) {
            pipeline.setDocumentation("The p:uuid step generates a [UUID] and injects it into the source document.");
            pipeline.addInput(new InPort("source", true, false, "document"));
            pipeline.addOutput(new OutPort("result", true, false, "document"));
            pipeline.addOption(new StepOption("match", "", true));
            pipeline.addOption(new StepOption("version", "", false));
        } else if (type.equals("p:validate-with-relax-ng")) {
            pipeline.setDocumentation("The p:validate-with-relax-ng step applies [RELAX NG] validation to the source document.");
            pipeline.addInput(new InPort("source", true, false, "document"));
            pipeline.addInput(new InPort("schema", false, false, "document"));
            pipeline.addOutput(new OutPort("result", true, false, "document"));
            pipeline.addOption(new StepOption("dtd-attribute-values", "false", false));
            pipeline.addOption(new StepOption("dtd-id-idref-warnings", "false", false));
            pipeline.addOption(new StepOption("assert-valid", "true", false));
        } else if (type.equals("p:validate-with-schematron")) {
            pipeline.setDocumentation("The p:validate-with-schematron step applies [Schematron] processing to the source document.");
            pipeline.addInput(new InPort("source", true, false, "document"));
            pipeline.addInput(new InPort("parameters", false, false, "parameters"));
            pipeline.addInput(new InPort("schema", false, false, "document"));
            pipeline.addOutput(new OutPort("result", true, false, "document"));
            pipeline.addOutput(new OutPort("report", false, true, "document"));
            pipeline.addOption(new StepOption("phase", "#ALL", false));
            pipeline.addOption(new StepOption("assert-valid", "true", false));
        } else if (type.equals("p:validate-with-xml-schema")) {
            pipeline.setDocumentation("The p:validate-with-xml-schema step applies [W3C XML Schema: Part 1] validity assessment to the source input.");
            pipeline.addInput(new InPort("source", true, false, "document"));
            pipeline.addInput(new InPort("schema", false, true, "document"));
            pipeline.addOutput(new OutPort("result", true, false, "document"));
            pipeline.addOption(new StepOption("use-location-hints", "false", false));
            pipeline.addOption(new StepOption("try-namespaces", "false", false));
            pipeline.addOption(new StepOption("assert-valid", "true", false));
            pipeline.addOption(new StepOption("mode", "strict", false));
        } else if (type.equals("p:www-form-urldecode")) {
            pipeline.setDocumentation("The p:www-form-urldecode step decodes a x-www-form-urlencoded string into a set of parameters.");
            pipeline.addOutput(new OutPort("result", true, false, "document"));
            pipeline.addOption(new StepOption("value", "", true));
        } else if (type.equals("p:www-form-urlencode")) {
            pipeline.setDocumentation("The p:www-form-urlencode step encodes a set of parameter values as a x-www-form-urlencoded string and injects it into the source document.");
            pipeline.addInput(new InPort("source", true, false, "document"));
            pipeline.addInput(new InPort("parameters", false, false, "parameters"));
            pipeline.addOutput(new OutPort("result", true, false, "document"));
            pipeline.addOption(new StepOption("match", "", true));
        } else if (type.equals("p:xquery")) {
            pipeline.setDocumentation("The p:xquery step applies an [XQuery 1.0] query to the sequence of documents provided on the source port.");
            pipeline.addInput(new InPort("source", true, true, "document"));
            pipeline.addInput(new InPort("query", false, false, "document"));
            pipeline.addInput(new InPort("parameters", false, false, "parameters"));
            pipeline.addOutput(new OutPort("result", true, true, "document"));
        } else if (type.equals("p:xsl-formatter")) {
            pipeline.setDocumentation("The p:xsl-formatter step receives an [XSL 1.1] document and renders the content. The result of rendering is stored to the URI provided via the href option. A reference to that result is produced on the output port.");
            pipeline.addInput(new InPort("source", true, false, "document"));
            pipeline.addInput(new InPort("parameters", false, false, "parameters"));
            pipeline.addOutput(new OutPort("result", false, false, "document"));
            pipeline.addOption(new StepOption("href", "", true));
            pipeline.addOption(new StepOption("content-type", "", false));
        }
    }

    public static String[] getStepTypes() {

        String[] types = new String[]{
                "p:add-attribute",
                "p:add-xml-base",
                "p:compare",
                "p:count",
                "p:delete",
                "p:directory-list",
                "p:error",
                "p:escape-markup",
                "p:filter",
                "p:http-request",
                "p:identity",
                "p:insert",
                "p:label-elements",
                "p:load",
                "p:make-absolute-uris",
                "p:namespace-rename",
                "p:pack",
                "p:parameters",
                "p:rename",
                "p:replace",
                "p:set-attributes",
                "p:sink",
                "p:split-sequence",
                "p:store",
                "p:string-replace",
                "p:unescape-markup",
                "p:unwrap",
                "p:wrap",
                "p:wrap-sequence",
                "p:xinclude",
                "p:xslt",
                "p:exec",
                "p:hash",
                "p:uuid",
                "p:validate-with-relax-ng",
                "p:validate-with-schematron",
                "p:validate-with-xml-schema",
                "p:www-form-urldecode",
                "p:www-form-urlencode",
                "p:xquery",
                "p:xsl-formatter"
        };


        return types;
    }
}

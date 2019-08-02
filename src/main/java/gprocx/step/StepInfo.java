package gprocx.step;

import gprocx.core.*;
import gprocx.mainUI.XFrame;

import java.awt.*;

public class StepInfo {

    public StepInfo() {}

    public static void setPipelineInfo(XFrame frame, GProcXPipeline pipeline) {

        String type = pipeline.getType();
        //GProcXPipeline newPipeline = new GProcXPipeline(frame, type);

        /*
         else if (type.equals("")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "")); 
            pipeline.addInput(new InPort(pipeline, "source", true, false, "document"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
            pipeline.addOption(new GProcXOption("", "", false));
        }
        */
        pipeline.addOption(new GProcXOption("name", "", false));

        if (type.equals("p:declare-step")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "empty"));
            pipeline.setAtomic(false);
            pipeline.setBuildin(false);
            pipeline.addInput(new InPort(pipeline, "source", true, false, "document"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
            pipeline.addOption(new GProcXOption("name", "myPipeline", false));
            pipeline.addOption(new GProcXOption("type", "", false));
            pipeline.addOption(new GProcXOption("psvi-required", "", false));
            pipeline.addOption(new GProcXOption("xpath-version", "", false));
            pipeline.addOption(new GProcXOption("exclude-inline-prefixes", "", false));
            pipeline.addOption(new GProcXOption("version", "1.0", false));
            pipeline.addNamespace(new QName("xmlns:p", "http://www.w3.org/ns/xproc"));
        } else if (type.equals("p:pipeline")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "empty"));
            pipeline.setAtomic(false);
            pipeline.setBuildin(false);
            pipeline.addInput(new InPort(pipeline, "source", true, false, "document"));
            pipeline.addInput(new InPort(pipeline, "parameters", true, false, "parameters"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
            pipeline.addOption(new GProcXOption("name", "myPipeline", false));
            pipeline.addOption(new GProcXOption("type", "", false));
            pipeline.addOption(new GProcXOption("psvi-required", "", false));
            pipeline.addOption(new GProcXOption("xpath-version", "", false));
            pipeline.addOption(new GProcXOption("exclude-inline-prefixes", "", false));
            pipeline.addOption(new GProcXOption("version", "", false));
        } else if (type.equals("p:library")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "empty"));
            pipeline.setAtomic(false);
            pipeline.setBuildin(false);
            pipeline.addOption(new GProcXOption("psvi-required", "", false));
            pipeline.addOption(new GProcXOption("xpath-version", "", false));
            pipeline.addOption(new GProcXOption("exclude-inline-prefixes", "", false));
            pipeline.addOption(new GProcXOption("version", "", false));
        } else if (type.equals("p:for-each")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "A for-each is specified by the p:for-each element. It is a compound step that processes a sequence of documents, applying its subpipeline to each document in turn.")); 
            pipeline.setAtomic(false);
            pipeline.setBuildin(true);
            pipeline.addInput(new InPort(pipeline, "iteration-source", true, false, "document"));
        } else if (type.equals("p:viewport")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "A viewport is specified by the p:viewport element. It is a compound step that processes a single document, applying its subpipeline to one or more subtrees of the document."));
            pipeline.setAtomic(false);
            pipeline.setBuildin(true);
            pipeline.addInput(new InPort(pipeline, "viewport-source", true, false, "document"));
            pipeline.addOption(new GProcXOption("match", "", true));
        } else if (type.equals("p:group")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "A group is specified by the p:group element. It is a compound step that encapsulates the behavior of its subpipeline."));
            pipeline.setAtomic(false);
            pipeline.setBuildin(true);
        } else if (type.equals("p:choose")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "A choose is specified by the p:choose element. It is a multi-container step that selects exactly one of a list of alternative subpipelines based on the evaluation of XPath expressions."));
            pipeline.setAtomic(false);
            pipeline.setBuildin(true);
        } else if (type.equals("p:when")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "A p:when specifies one subpipeline guarded by a test expression. Each p:when branch of the p:choose has a test attribute which must contain an XPath expression. That XPath expression's effective boolean value is the guard for the subpipeline contained within that p:when."));
            pipeline.setAtomic(false);
            pipeline.setBuildin(true);
            pipeline.addInput(new InPort(pipeline, "xpath-context", true, false, "document"));
            pipeline.addOption(new GProcXOption("test", "", true));
        } else if (type.equals("p:otherwise")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "A p:otherwise specifies the default branch; the subpipeline selected if no test expression on any preceding p:when evaluates to true."));
            pipeline.setAtomic(false);
            pipeline.setBuildin(true);
        } else if (type.equals("p:group")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "In a p:try, the p:group represents the initial subpipeline. The p:try step evaluates the initial subpipeline and, if no errors occur, the outputs of that pipeline are the outputs of the p:try step."));
            pipeline.setAtomic(false);
            pipeline.setBuildin(true);
        } else if (type.equals("p:catch")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "A recovery (or “catch”) pipeline is identified with a p:catch element. If the recovery subpipeline is evaluated, the outputs of the recovery subpipeline are the outputs of the p:try step."));
            pipeline.setAtomic(false);
            pipeline.setBuildin(true);
        } else if (type.equals("p:with-option")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "A p:with-option provides an actual value for an option when a step is invoked."));
            pipeline.setAtomic(false);
            pipeline.setBuildin(true);
            pipeline.addOption(new GProcXOption("select", "", true));
        } else if (type.equals("p:with-param")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:with-param element is used to establish the value of a parameter. The parameter must be given a value when it is used."));
            pipeline.setAtomic(false);
            pipeline.setBuildin(true);
            pipeline.addOption(new GProcXOption("select", "", true));
            pipeline.addOption(new GProcXOption("port", "", false));
        } else if (type.equals("p:variable")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "A p:variable declares a variable and associates a value with it. The content of the select attribute is an XPath expression which will be evaluated to provide the value of the variable."));
            pipeline.setAtomic(false);
            pipeline.setBuildin(true);
            pipeline.addOption(new GProcXOption("select", "", true));
        } else if (type.equals("p:import")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "An p:import loads a pipeline or pipeline library, making it available in the pipeline or library which contains the p:import."));
            pipeline.setAtomic(false);
            pipeline.setBuildin(true);
            pipeline.addOption(new GProcXOption("href", "", true));
        } else if (type.equals("p:option")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "A p:option declares an option and may associate a default value with it. The p:option tag can only be used in a p:declare-step or a p:pipeline (which is a syntactic abbreviation for a step declaration)."));
            pipeline.setAtomic(false);
            pipeline.setBuildin(true);
            pipeline.addOption(new GProcXOption("required", "", false));
            pipeline.addOption(new GProcXOption("select", "", false));
        }










        else if (type.equals("p:add-attribute")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:add-attribute step adds a single attribute to a set of matching elements. The input document specified on the source is processed for matches specified by the match pattern in the match option. For each of these matches, the attribute whose name is specified by the attribute-name option is set to the attribute value specified by the attribute-value option.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, false, "document"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
            pipeline.addOption(new GProcXOption("match", "", true));
            pipeline.addOption(new GProcXOption("attribute-name", "", true));
            pipeline.addOption(new GProcXOption("attribute-prefix", "", false));
            pipeline.addOption(new GProcXOption("attribute-namespace", "", false));
            pipeline.addOption(new GProcXOption("attribute-value", "", true));
        } else if (type.equals("p:add-xml-base")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:add-xml-base step exposes the base URI via explicit xml:base attributes. The input document from the source port is replicated to the result port with xml:base attributes added to or corrected on each element as specified by the options on this step.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, false, "document"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
            pipeline.addOption(new GProcXOption("all", "false", false));
            pipeline.addOption(new GProcXOption("relative", "true", false));
        } else if (type.equals("p:compare")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:compare step compares two documents for equality.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, false, "document"));
            pipeline.addInput(new InPort(pipeline, "alternate", false, false, "document"));
            pipeline.addOutput(new OutPort(pipeline, "result", false, false, "document"));
            pipeline.addOption(new GProcXOption("fail-if-not-equal", "false", false));
        } else if (type.equals("p:count")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:count step counts the number of documents in the source input sequence and returns a single document on result containing that number. The generated document contains a single c:result element whose contents is the string representation of the number of documents in the sequence.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, true, "document"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
            pipeline.addOption(new GProcXOption("limit", "0", false));
        } else if (type.equals("p:delete")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:delete step deletes items specified by a match pattern from the source input document and produces the resulting document, with the deleted items removed, on the result port.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, false, "document"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
            pipeline.addOption(new GProcXOption("match", "", true));
        } else if (type.equals("p:directory-list")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:directory-list step produces a list of the contents of a specified directory.")); 
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
            pipeline.addOption(new GProcXOption("path", "", true));
            pipeline.addOption(new GProcXOption("include-filter", "", false));
            pipeline.addOption(new GProcXOption("exclude-filter", "", false));
        } else if (type.equals("p:error")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:error step generates a dynamic error using the input provided to the step.")); 
            pipeline.addInput(new InPort(pipeline, "source", false, false, "document"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, true, "document"));
            pipeline.addOption(new GProcXOption("code", "", true));
            pipeline.addOption(new GProcXOption("code-prefix", "", false));
            pipeline.addOption(new GProcXOption("code-namespace", "", false));
        } else if (type.equals("p:escape-markup")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:escape-markup step applies XML serialization to the children of the document element and replaces those children with their serialization. The outcome is a single element with text content that represents the \"escaped\" syntax of the children as they were serialized.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, false, "document"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
            pipeline.addOption(new GProcXOption("cdata-section-elements", "", false));
            pipeline.addOption(new GProcXOption("doctype-public", "", false));
            pipeline.addOption(new GProcXOption("doctype-system", "", false));
            pipeline.addOption(new GProcXOption("escape-uri-attributes", "false", false));
            pipeline.addOption(new GProcXOption("include-content-type", "true", false));
            pipeline.addOption(new GProcXOption("indent", "false", false));
            pipeline.addOption(new GProcXOption("media-type", "", false));
            pipeline.addOption(new GProcXOption("method", "xml", false));
            pipeline.addOption(new GProcXOption("omit-xml-declaration", "true", false));
            pipeline.addOption(new GProcXOption("standalone", "omit", false));
            pipeline.addOption(new GProcXOption("undeclare-prefixes", "", false));
            pipeline.addOption(new GProcXOption("version", "1.0", false));
        } else if (type.equals("p:filter")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:filter step selects portions of the source document based on a (possibly dynamically constructed) XPath select expression.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, false, "document"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, true, "document"));
            pipeline.addOption(new GProcXOption("select", "", true));
        } else if (type.equals("p:http-request")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:http-request step provides for interaction with resources over HTTP or related protocols. The input document provided on the source port specifies a request by a single c:request element. This element specifies the method, resource, and other request properties as well as possibly including an entity body (content) for the request.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, false, "document"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
            pipeline.addOption(new GProcXOption("byte-order-mark", "", false));
            pipeline.addOption(new GProcXOption("cdata-section-elements", "", false));
            pipeline.addOption(new GProcXOption("doctype-public", "", false));
            pipeline.addOption(new GProcXOption("doctype-system", "", false));
            pipeline.addOption(new GProcXOption("encoding", "", false));
            pipeline.addOption(new GProcXOption("escape-uri-attributes", "false", false));
            pipeline.addOption(new GProcXOption("include-content-type", "true", false));
            pipeline.addOption(new GProcXOption("indent", "false", false));
            pipeline.addOption(new GProcXOption("media-type", "", false));
            pipeline.addOption(new GProcXOption("method", "xml", false));
            pipeline.addOption(new GProcXOption("normalization-form", "none", false));
            pipeline.addOption(new GProcXOption("omit-xml-declaration", "true", false));
            pipeline.addOption(new GProcXOption("standalone", "omit", false));
            pipeline.addOption(new GProcXOption("undeclare-prefixes", "", false));
            pipeline.addOption(new GProcXOption("version", "1.0", false));
        } else if (type.equals("p:identity")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:identity step makes a verbatim copy of its input available on its output.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, false, "document"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
        } else if (type.equals("p:insert")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:insert step inserts the insertion port's document into the source port's document relative to the matching elements in the source port's document.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, false, "document"));
            pipeline.addInput(new InPort(pipeline, "insertion", false, true, "document"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
            pipeline.addOption(new GProcXOption("match", "/*", false));
            pipeline.addOption(new GProcXOption("position", "", true));
        } else if (type.equals("p:label-elements")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:label-elements step generates a label for each matched element and stores that label in the specified attribute.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, false, "document"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
            pipeline.addOption(new GProcXOption("attribute", "xml:id", false));
            pipeline.addOption(new GProcXOption("attribute-prefix", "", false));
            pipeline.addOption(new GProcXOption("attribute-namespace", "", false));
            pipeline.addOption(new GProcXOption("label", "concat(&#34;_&#34;,$p:index)", false));
            pipeline.addOption(new GProcXOption("match", "*", false));
            pipeline.addOption(new GProcXOption("replace", "true", false));
        } else if (type.equals("p:load")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:load step has no inputs but produces as its result an XML resource specified by an IRI.")); 
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
            pipeline.addOption(new GProcXOption("href", "", true));
            pipeline.addOption(new GProcXOption("dtd-validate", "false", false));
        } else if (type.equals("p:make-absolute-uris")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:make-absolute-uris step makes an element or attribute's value in the source document an absolute IRI value in the result document.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, false, "document"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
            pipeline.addOption(new GProcXOption("match", "", true));
            pipeline.addOption(new GProcXOption("base-uri", "", false));
        } else if (type.equals("p:namespace-rename")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:namespace-rename step renames any namespace declaration or use of a namespace in a document to a new IRI value.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, false, "document"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
            pipeline.addOption(new GProcXOption("from", "", false));
            pipeline.addOption(new GProcXOption("to", "", false));
            pipeline.addOption(new GProcXOption("apply-to", "all", false));
        } else if (type.equals("p:pack")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:pack step merges two document sequences in a pair-wise fashion.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, true, "document"));
            pipeline.addInput(new InPort(pipeline, "alternate", false, true, "document"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, true, "document"));
            pipeline.addOption(new GProcXOption("wrapper", "", true));
            pipeline.addOption(new GProcXOption("wrapper-prefix", "", false));
            pipeline.addOption(new GProcXOption("wrapper-namespace", "", false));
        } else if (type.equals("p:parameters")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:parameters step exposes a set of parameters as a c:param-set document.")); 
            pipeline.addInput(new InPort(pipeline, "parameters", false, false, "parameters"));
            pipeline.addOutput(new OutPort(pipeline, "result", false, false, "document"));
        } else if (type.equals("p:rename")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:rename step renames elements, attributes, or processing-instruction targets in a document.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, false, "document"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
            pipeline.addOption(new GProcXOption("match", "", true));
            pipeline.addOption(new GProcXOption("new-name", "", true));
            pipeline.addOption(new GProcXOption("new-prefix", "", false));
            pipeline.addOption(new GProcXOption("new-namespace", "", false));
        } else if (type.equals("p:replace")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:replace step replaces matching nodes in its primary input with the document element of the replacement port's document.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, false, "document"));
            pipeline.addInput(new InPort(pipeline, "replacement", false, false, "document"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
            pipeline.addOption(new GProcXOption("match", "", true));
        } else if (type.equals("p:set-attributes")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:set-attributes step sets attributes on matching elements.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, false, "document"));
            pipeline.addInput(new InPort(pipeline, "attributes", false, false, "document"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
            pipeline.addOption(new GProcXOption("match", "", true));
        } else if (type.equals("p:sink")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:sink step accepts a sequence of documents and discards them. It has no output.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, true, "document"));
        } else if (type.equals("p:split-sequence")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:split-sequence step accepts a sequence of documents and divides it into two sequences.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, true, "document"));
            pipeline.addOutput(new OutPort(pipeline, "matched", true, true, "document"));
            pipeline.addOutput(new OutPort(pipeline, "not-matched", false, true, "document"));
            pipeline.addOption(new GProcXOption("initial-only", "false", false));
            pipeline.addOption(new GProcXOption("test", "", true));
        } else if (type.equals("p:store")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:store step stores a serialized version of its input to a URI. This step outputs a reference to the location of the stored document.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, false, "document"));
            pipeline.addOutput(new OutPort(pipeline, "result", false, false, "document"));
            pipeline.addOption(new GProcXOption("href", "", true));
            pipeline.addOption(new GProcXOption("byte-order-mark", "", false));
            pipeline.addOption(new GProcXOption("cdata-section-elements", "", false));
            pipeline.addOption(new GProcXOption("doctype-public", "", false));
            pipeline.addOption(new GProcXOption("doctype-system", "", false));
            pipeline.addOption(new GProcXOption("encoding", "", false));
            pipeline.addOption(new GProcXOption("escape-uri-attributes", "false", false));
            pipeline.addOption(new GProcXOption("include-content-type", "true", false));
            pipeline.addOption(new GProcXOption("indent", "false", false));
            pipeline.addOption(new GProcXOption("media-type", "", false));
            pipeline.addOption(new GProcXOption("method", "xml", false));
            pipeline.addOption(new GProcXOption("normalization-form", "none", false));
            pipeline.addOption(new GProcXOption("omit-xml-declaration", "true", false));
            pipeline.addOption(new GProcXOption("standalone", "omit", false));
            pipeline.addOption(new GProcXOption("undeclare-prefixes", "", false));
            pipeline.addOption(new GProcXOption("version", "1.0", false));
        } else if (type.equals("p:string-replace")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:string-replace step matches nodes in the document provided on the source port and replaces them with the string result of evaluating an XPath expression.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, false, "document"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
            pipeline.addOption(new GProcXOption("match", "", true));
            pipeline.addOption(new GProcXOption("replace", "", true));
        } else if (type.equals("p:unescape-markup")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:unescape-markup step takes the string value of the document element and parses the content as if it was a Unicode character stream containing serialized XML. The output consists of the same document element with children that result from the parse. This is the reverse of the p:escape-markup step.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, false, "document"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
            pipeline.addOption(new GProcXOption("namespace", "", false));
            pipeline.addOption(new GProcXOption("content-type", "application/xml", false));
            pipeline.addOption(new GProcXOption("encoding", "", false));
            pipeline.addOption(new GProcXOption("charset", "", false));
        } else if (type.equals("p:unwrap")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:unwrap step replaces matched elements with their children.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, false, "document"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
            pipeline.addOption(new GProcXOption("match", "", true));
        } else if (type.equals("p:wrap")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:wrap step wraps matching nodes in the source document with a new parent element.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, false, "document"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
            pipeline.addOption(new GProcXOption("wrapper", "", true));
            pipeline.addOption(new GProcXOption("wrapper-prefix", "", false));
            pipeline.addOption(new GProcXOption("wrapper-namespace", "", false));
            pipeline.addOption(new GProcXOption("match", "", true));
            pipeline.addOption(new GProcXOption("group-adjacent", "", false));
        } else if (type.equals("p:wrap-sequence")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:wrap-sequence step accepts a sequence of documents and produces either a single document or a new sequence of documents.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, true, "document"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, true, "document"));
            pipeline.addOption(new GProcXOption("wrapper", "", true));
            pipeline.addOption(new GProcXOption("wrapper-prefix", "", false));
            pipeline.addOption(new GProcXOption("wrapper-namespace", "", false));
            pipeline.addOption(new GProcXOption("group-adjacent", "", false));
        } else if (type.equals("p:xinclude")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:xinclude step applies [XInclude] processing to the source document.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, false, "document"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
            pipeline.addOption(new GProcXOption("fixup-xml-base", "false", false));
            pipeline.addOption(new GProcXOption("fixup-xml-lang", "false", false));
        } else if (type.equals("p:xslt")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:xslt step applies an [XSLT 1.0] or [XSLT 2.0] stylesheet to a document.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, true, "document"));
            pipeline.addInput(new InPort(pipeline, "stylesheet", false, false, "document"));
            pipeline.addInput(new InPort(pipeline, "parameters", false, false, "parameter"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
            pipeline.addOutput(new OutPort(pipeline, "secondary", false, true, "document"));
            pipeline.addOption(new GProcXOption("initial-mode", "", false));
            pipeline.addOption(new GProcXOption("template-name", "", false));
            pipeline.addOption(new GProcXOption("output-base-uri", "", false));
            pipeline.addOption(new GProcXOption("version", "", false));
        } else if (type.equals("p:exec")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:exec step runs an external command passing the input that arrives on its source port as standard input, reading result from standard output, and errors from standard error.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, true, "document"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
            pipeline.addOutput(new OutPort(pipeline, "errors", false, false, "document"));
            pipeline.addOutput(new OutPort(pipeline, "exit-status", false, false, "document"));
            pipeline.addOption(new GProcXOption("command", "", true));
            pipeline.addOption(new GProcXOption("args", "", false));
            pipeline.addOption(new GProcXOption("cwd", "", false));
            pipeline.addOption(new GProcXOption("source-is-xml", "true", false));
            pipeline.addOption(new GProcXOption("result-is-xml", "true", false));
            pipeline.addOption(new GProcXOption("wrap-result-lines", "false", false));
            pipeline.addOption(new GProcXOption("errors-is-xml", "false", false));
            pipeline.addOption(new GProcXOption("wrap-error-lines", "false", false));
            pipeline.addOption(new GProcXOption("path-separator", "", false));
            pipeline.addOption(new GProcXOption("failure-threshold", "", false));
            pipeline.addOption(new GProcXOption("arg-separator", " ", false));
            pipeline.addOption(new GProcXOption("byte-order-mark", "", false));
            pipeline.addOption(new GProcXOption("cdata-section-elements", "", false));
            pipeline.addOption(new GProcXOption("doctype-public", "", false));
            pipeline.addOption(new GProcXOption("doctype-system", "", false));
            pipeline.addOption(new GProcXOption("encoding", "", false));
            pipeline.addOption(new GProcXOption("escape-uri-attributes", "false", false));
            pipeline.addOption(new GProcXOption("include-content-type", "true", false));
            pipeline.addOption(new GProcXOption("indent", "false", false));
            pipeline.addOption(new GProcXOption("media-type", "", false));
            pipeline.addOption(new GProcXOption("method", "xml", false));
            pipeline.addOption(new GProcXOption("normalization-form", "none", false));
            pipeline.addOption(new GProcXOption("omit-xml-declaration", "true", false));
            pipeline.addOption(new GProcXOption("standalone", "omit", false));
            pipeline.addOption(new GProcXOption("undeclare-prefixes", "", false));
            pipeline.addOption(new GProcXOption("version", "1.0", false));
        } else if (type.equals("p:hash")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:hash step generates a hash, or digital \"fingerprint\", for some value and injects it into the source document.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, false, "document"));
            pipeline.addInput(new InPort(pipeline, "parameters", false, false, "parameters"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
            pipeline.addOption(new GProcXOption("value", "", true));
            pipeline.addOption(new GProcXOption("algorithm", "", true));
            pipeline.addOption(new GProcXOption("match", "", true));
            pipeline.addOption(new GProcXOption("version", "", false));
        } else if (type.equals("p:uuid")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:uuid step generates a [UUID] and injects it into the source document.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, false, "document"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
            pipeline.addOption(new GProcXOption("match", "", true));
            pipeline.addOption(new GProcXOption("version", "", false));
        } else if (type.equals("p:validate-with-relax-ng")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:validate-with-relax-ng step applies [RELAX NG] validation to the source document.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, false, "document"));
            pipeline.addInput(new InPort(pipeline, "schema", false, false, "document"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
            pipeline.addOption(new GProcXOption("dtd-attribute-values", "false", false));
            pipeline.addOption(new GProcXOption("dtd-id-idref-warnings", "false", false));
            pipeline.addOption(new GProcXOption("assert-valid", "true", false));
        } else if (type.equals("p:validate-with-schematron")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:validate-with-schematron step applies [Schematron] processing to the source document.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, false, "document"));
            pipeline.addInput(new InPort(pipeline, "parameters", false, false, "parameters"));
            pipeline.addInput(new InPort(pipeline, "schema", false, false, "document"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
            pipeline.addOutput(new OutPort(pipeline, "report", false, true, "document"));
            pipeline.addOption(new GProcXOption("phase", "#ALL", false));
            pipeline.addOption(new GProcXOption("assert-valid", "true", false));
        } else if (type.equals("p:validate-with-xml-schema")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:validate-with-xml-schema step applies [W3C XML Schema: Part 1] validity assessment to the source input.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, false, "document"));
            pipeline.addInput(new InPort(pipeline, "schema", false, true, "document"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
            pipeline.addOption(new GProcXOption("use-location-hints", "false", false));
            pipeline.addOption(new GProcXOption("try-namespaces", "false", false));
            pipeline.addOption(new GProcXOption("assert-valid", "true", false));
            pipeline.addOption(new GProcXOption("mode", "strict", false));
        } else if (type.equals("p:www-form-urldecode")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:www-form-urldecode step decodes a x-www-form-urlencoded string into a set of parameters.")); 
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
            pipeline.addOption(new GProcXOption("value", "", true));
        } else if (type.equals("p:www-form-urlencode")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:www-form-urlencode step encodes a set of parameter values as a x-www-form-urlencoded string and injects it into the source document.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, false, "document"));
            pipeline.addInput(new InPort(pipeline, "parameters", false, false, "parameters"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
            pipeline.addOption(new GProcXOption("match", "", true));
        } else if (type.equals("p:xquery")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:xquery step applies an [XQuery 1.0] query to the sequence of documents provided on the source port.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, true, "document"));
            pipeline.addInput(new InPort(pipeline, "query", false, false, "document"));
            pipeline.addInput(new InPort(pipeline, "parameters", false, false, "parameters"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, true, "document"));
        } else if (type.equals("p:xsl-formatter")) {
            pipeline.addDoc(new GProcXDoc("p:documentation", "The p:xsl-formatter step receives an [XSL 1.1] document and renders the content. The result of rendering is stored to the URI provided via the href option. A reference to that result is produced on the output port.")); 
            pipeline.addInput(new InPort(pipeline, "source", true, false, "document"));
            pipeline.addInput(new InPort(pipeline, "parameters", false, false, "parameters"));
            pipeline.addOutput(new OutPort(pipeline, "result", false, false, "document"));
            pipeline.addOption(new GProcXOption("href", "", true));
            pipeline.addOption(new GProcXOption("content-type", "", false));
        } else {
            pipeline.addInput(new InPort(pipeline, "source", true, false, "document"));
            pipeline.addOutput(new OutPort(pipeline, "result", true, false, "document"));
        }

        pipeline.addOption(new GProcXOption("use-when", "", false));
        pipeline.addOption(new GProcXOption("xml:id", "", false));
        pipeline.addOption(new GProcXOption("xml:base", "", false));
    }

    public static String[] getAtomicTypes() {

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
                "p:xquery",
                "p:xsl-formatter"
        };


        return types;
    }

    public static String[] getOtherTypes() {
        String[] types = new String[]{
                "p:for-each",
                "p:viewport",
                "p:group",
                "p:choose",
                "p:try",
                "p:when",
                "p:otherwise",
                "p:group",
                "p:catch",
                "p:with-option",
                "p:with-param",
                "p:variable",
                "p:import",
                "p:option"
        };

        return types;
    }
}

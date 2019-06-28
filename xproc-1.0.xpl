<?xml version="1.0" encoding="UTF-8"?>
<!-- $Id: xproc-1.0.xpl,v 1.3 2010/02/04 17:06:39 ht Exp $ -->
<p:library xmlns:p="http://www.w3.org/ns/xproc" version="1.0">

   <p:declare-pipeline type="p:declare-step" xml:id="declare-step">
      <p:documentation>The p:declare-pipeline pipeline is the main pipeline.</p:documentation>
      <p:input port="source"/>
      <p:output port="result"/>
   </p:declare-pipeline>

   <p:declare-pipeline type="p:add-attribute" xml:id="add-attribute">
      <p:documentation>The p:add-attribute pipeline adds a single attribute to a set of matching elements. The input document specified on the source is processed for matches specified by the match pattern in the match option. For each of these matches, the attribute whose name is specified by the attribute-name option is set to the attribute value specified by the attribute-value option.</p:documentation>
      <p:input port="source"/>
      <p:output port="result"/>
      <p:option name="match" required="true"/>
      <p:option name="attribute-name" required="true"/>
      <p:option name="attribute-prefix"/>
      <p:option name="attribute-namespace"/>
      <p:option name="attribute-value" required="true"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:add-xml-base" xml:id="add-xml-base">
      <p:documentation>The p:add-xml-base pipeline exposes the base URI via explicit xml:base attributes. The input document from the source port is replicated to the result port with xml:base attributes added to or corrected on each element as specified by the options on this pipeline.</p:documentation>
      <p:input port="source"/>
      <p:output port="result"/>
      <p:option name="all" select="'false'"/>
      <p:option name="relative" select="'true'"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:compare" xml:id="compare">
      <p:documentation>The p:compare pipeline compares two documents for equality.</p:documentation>
      <p:input port="source" primary="true"/>
      <p:input port="alternate"/>
      <p:output port="result" primary="false"/>
      <p:option name="fail-if-not-equal" select="'false'"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:count" xml:id="count">
      <p:documentation>The p:count pipeline counts the number of documents in the source input sequence and returns a single document on result containing that number. The generated document contains a single c:result element whose contents is the string representation of the number of documents in the sequence.</p:documentation>
      <p:input port="source" sequence="true"/>
      <p:output port="result"/>
      <p:option name="limit" select="0"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:delete" xml:id="delete">
      <p:documentation>The p:delete pipeline deletes items specified by a match pattern from the source input document and produces the resulting document, with the deleted items removed, on the result port.</p:documentation>
      <p:input port="source"/>
      <p:output port="result"/>
      <p:option name="match" required="true"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:directory-list" xml:id="directory-list">
      <p:documentation>The p:directory-list pipeline produces a list of the contents of a specified directory.</p:documentation>
      <p:output port="result"/>
      <p:option name="path" required="true"/>
      <p:option name="include-filter"/>
      <p:option name="exclude-filter"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:error" xml:id="error">
      <p:documentation>The p:error pipeline generates a dynamic error using the input provided to the pipeline.</p:documentation>
      <p:input port="source" primary="false"/>
      <p:output port="result" sequence="true"/>
      <p:option name="code" required="true"/>
      <p:option name="code-prefix"/>
      <p:option name="code-namespace"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:escape-markup" xml:id="escape-markup">
      <p:documentation>The p:escape-markup pipeline applies XML serialization to the children of the document element and replaces those children with their serialization. The outcome is a single element with text content that represents the "escaped" syntax of the children as they were serialized.</p:documentation>
      <p:input port="source"/>
      <p:output port="result"/>
      <p:option name="cdata-section-elements" select="''"/>
      <p:option name="doctype-public"/>
      <p:option name="doctype-system"/>
      <p:option name="escape-uri-attributes" select="'false'"/>
      <p:option name="include-content-type" select="'true'"/>
      <p:option name="indent" select="'false'"/>
      <p:option name="media-type"/>
      <p:option name="method" select="'xml'"/>
      <p:option name="omit-xml-declaration" select="'true'"/>
      <p:option name="standalone" select="'omit'"/>
      <p:option name="undeclare-prefixes"/>
      <p:option name="version" select="'1.0'"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:filter" xml:id="filter">
      <p:documentation>The p:filter pipeline selects portions of the source document based on a (possibly dynamically constructed) XPath select expression.</p:documentation>
      <p:input port="source"/>
      <p:output port="result" sequence="true"/>
      <p:option name="select" required="true"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:http-request" xml:id="http-request">
      <p:documentation>The p:http-request pipeline provides for interaction with resources over HTTP or related protocols. The input document provided on the source port specifies a request by a single c:request element. This element specifies the method, resource, and other request properties as well as possibly including an entity body (content) for the request.</p:documentation>
      <p:input port="source"/>
      <p:output port="result"/>
      <p:option name="byte-order-mark"/>
      <p:option name="cdata-section-elements" select="''"/>
      <p:option name="doctype-public"/>
      <p:option name="doctype-system"/>
      <p:option name="encoding"/>
      <p:option name="escape-uri-attributes" select="'false'"/>
      <p:option name="include-content-type" select="'true'"/>
      <p:option name="indent" select="'false'"/>
      <p:option name="media-type"/>
      <p:option name="method" select="'xml'"/>
      <p:option name="normalization-form" select="'none'"/>
      <p:option name="omit-xml-declaration" select="'true'"/>
      <p:option name="standalone" select="'omit'"/>
      <p:option name="undeclare-prefixes"/>
      <p:option name="version" select="'1.0'"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:identity" xml:id="identity">
      <p:documentation>The p:identity pipeline makes a verbatim copy of its input available on its output.</p:documentation>
      <p:input port="source" sequence="true"/>
      <p:output port="result" sequence="true"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:insert" xml:id="insert">
      <p:documentation>The p:insert pipeline inserts the insertion port's document into the source port's document relative to the matching elements in the source port's document.</p:documentation>
      <p:input port="source" primary="true"/>
      <p:input port="insertion" sequence="true"/>
      <p:output port="result"/>
      <p:option name="match" select="'/*'"/>
      <p:option name="position" required="true"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:label-elements" xml:id="label-elements">
      <p:documentation>The p:label-elements pipeline generates a label for each matched element and stores that label in the specified attribute.</p:documentation>
      <p:input port="source"/>
      <p:output port="result"/>
      <p:option name="attribute" select="'xml:id'"/>
      <p:option name="attribute-prefix"/>
      <p:option name="attribute-namespace"/>
      <p:option name="label" select="'concat(&#34;_&#34;,$p:index)'"/>
      <p:option name="match" select="'*'"/>
      <p:option name="replace" select="'true'"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:load" xml:id="load">
      <p:documentation>The p:load pipeline has no inputs but produces as its result an XML resource specified by an IRI.</p:documentation>
      <p:output port="result"/>
      <p:option name="href" required="true"/>
      <p:option name="dtd-validate" select="'false'"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:make-absolute-uris" xml:id="make-absolute-uris">
      <p:documentation>The p:make-absolute-uris pipeline makes an element or attribute's value in the source document an absolute IRI value in the result document.</p:documentation>
      <p:input port="source"/>
      <p:output port="result"/>
      <p:option name="match" required="true"/>
      <p:option name="base-uri"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:namespace-rename" xml:id="namespace-rename">
      <p:documentation>The p:namespace-rename pipeline renames any namespace declaration or use of a namespace in a document to a new IRI value.</p:documentation>
      <p:input port="source"/>
      <p:output port="result"/>
      <p:option name="from"/>
      <p:option name="to"/>
      <p:option name="apply-to" select="'all'"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:pack" xml:id="pack">
      <p:documentation>The p:pack pipeline merges two document sequences in a pair-wise fashion.</p:documentation>
      <p:input port="source" sequence="true" primary="true"/>
      <p:input port="alternate" sequence="true"/>
      <p:output port="result" sequence="true"/>
      <p:option name="wrapper" required="true"/>
      <p:option name="wrapper-prefix"/>
      <p:option name="wrapper-namespace"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:parameters" xml:id="parameters">
      <p:documentation>The p:parameters pipeline exposes a set of parameters as a c:param-set document.</p:documentation>
      <p:input port="parameters" kind="parameter" primary="false"/>
      <p:output port="result" primary="false"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:rename" xml:id="rename">
      <p:documentation>The p:rename pipeline renames elements, attributes, or processing-instruction targets in a document.</p:documentation>
      <p:input port="source"/>
      <p:output port="result"/>
      <p:option name="match" required="true"/>
      <p:option name="new-name" required="true"/>
      <p:option name="new-prefix"/>
      <p:option name="new-namespace"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:replace" xml:id="replace">
      <p:documentation>The p:replace pipeline replaces matching nodes in its primary input with the document element of the replacement port's document.</p:documentation>
      <p:input port="source" primary="true"/>
      <p:input port="replacement"/>
      <p:output port="result"/>
      <p:option name="match" required="true"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:set-attributes" xml:id="set-attributes">
      <p:documentation>The p:set-attributes pipeline sets attributes on matching elements.</p:documentation>
      <p:input port="source" primary="true"/>
      <p:input port="attributes"/>
      <p:output port="result"/>
      <p:option name="match" required="true"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:sink" xml:id="sink">
      <p:documentation>The p:sink pipeline accepts a sequence of documents and discards them. It has no output.</p:documentation>
      <p:input port="source" sequence="true"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:split-sequence" xml:id="split-sequence">
      <p:documentation>The p:split-sequence pipeline accepts a sequence of documents and divides it into two sequences.</p:documentation>
      <p:input port="source" sequence="true"/>
      <p:output port="matched" sequence="true" primary="true"/>
      <p:output port="not-matched" sequence="true"/>
      <p:option name="initial-only" select="'false'"/>
      <p:option name="test" required="true"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:store" xml:id="store">
      <p:documentation>The p:store pipeline stores a serialized version of its input to a URI. This pipeline outputs a reference to the location of the stored document.</p:documentation>
      <p:input port="source"/>
      <p:output port="result" primary="false"/>
      <p:option name="href" required="true"/>
      <p:option name="byte-order-mark"/>
      <p:option name="cdata-section-elements" select="''"/>
      <p:option name="doctype-public"/>
      <p:option name="doctype-system"/>
      <p:option name="encoding"/>
      <p:option name="escape-uri-attributes" select="'false'"/>
      <p:option name="include-content-type" select="'true'"/>
      <p:option name="indent" select="'false'"/>
      <p:option name="media-type"/>
      <p:option name="method" select="'xml'"/>
      <p:option name="normalization-form" select="'none'"/>
      <p:option name="omit-xml-declaration" select="'true'"/>
      <p:option name="standalone" select="'omit'"/>
      <p:option name="undeclare-prefixes"/>
      <p:option name="version" select="'1.0'"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:string-replace" xml:id="string-replace">
      <p:documentation>The p:string-replace pipeline matches nodes in the document provided on the source port and replaces them with the string result of evaluating an XPath expression.</p:documentation>
      <p:input port="source"/>
      <p:output port="result"/>
      <p:option name="match" required="true"/>
      <p:option name="replace" required="true"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:unescape-markup" xml:id="unescape-markup">
      <p:documentation>The p:unescape-markup pipeline takes the string value of the document element and parses the content as if it was a Unicode character stream containing serialized XML. The output consists of the same document element with children that result from the parse. This is the reverse of the p:escape-markup pipeline.</p:documentation>
      <p:input port="source"/>
      <p:output port="result"/>
      <p:option name="namespace"/>
      <p:option name="content-type" select="'application/xml'"/>
      <p:option name="encoding"/>
      <p:option name="charset"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:unwrap" xml:id="unwrap">
      <p:documentation>The p:unwrap pipeline replaces matched elements with their children.</p:documentation>
      <p:input port="source"/>
      <p:output port="result"/>
      <p:option name="match" required="true"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:wrap" xml:id="wrap">
      <p:documentation>The p:wrap pipeline wraps matching nodes in the source document with a new parent element.</p:documentation>
      <p:input port="source"/>
      <p:output port="result"/>
      <p:option name="wrapper" required="true"/>
      <p:option name="wrapper-prefix"/>
      <p:option name="wrapper-namespace"/>
      <p:option name="match" required="true"/>
      <p:option name="group-adjacent"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:wrap-sequence" xml:id="wrap-sequence">
      <p:documentation>The p:wrap-sequence pipeline accepts a sequence of documents and produces either a single document or a new sequence of documents.</p:documentation>
      <p:input port="source" sequence="true"/>
      <p:output port="result" sequence="true"/>
      <p:option name="wrapper" required="true"/>
      <p:option name="wrapper-prefix"/>
      <p:option name="wrapper-namespace"/>
      <p:option name="group-adjacent"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:xinclude" xml:id="xinclude">
      <p:documentation>The p:xinclude pipeline applies [XInclude] processing to the source document.</p:documentation>
      <p:input port="source"/>
      <p:output port="result"/>
      <p:option name="fixup-xml-base" select="'false'"/>
      <p:option name="fixup-xml-lang" select="'false'"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:xslt" xml:id="xslt">
      <p:documentation>The p:xslt pipeline applies an [XSLT 1.0] or [XSLT 2.0] stylesheet to a document.</p:documentation>
      <p:input port="source" sequence="true" primary="true"/>
      <p:input port="stylesheet"/>
      <p:input port="parameters" kind="parameter"/>
      <p:output port="result" primary="true"/>
      <p:output port="secondary" sequence="true"/>
      <p:option name="initial-mode"/>
      <p:option name="template-name"/>
      <p:option name="output-base-uri"/>
      <p:option name="version"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:exec" xml:id="exec">
      <p:documentation>The p:exec pipeline runs an external command passing the input that arrives on its source port as standard input, reading result from standard output, and errors from standard error.</p:documentation>
      <p:input port="source" primary="true" sequence="true"/>
      <p:output port="result" primary="true"/>
      <p:output port="errors"/>
      <p:output port="exit-status"/>
      <p:option name="command" required="true"/>
      <p:option name="args" select="''"/>
      <p:option name="cwd"/>
      <p:option name="source-is-xml" select="'true'"/>
      <p:option name="result-is-xml" select="'true'"/>
      <p:option name="wrap-result-lines" select="'false'"/>
      <p:option name="errors-is-xml" select="'false'"/>
      <p:option name="wrap-error-lines" select="'false'"/>
      <p:option name="path-separator"/>
      <p:option name="failure-threshold"/>
      <p:option name="arg-separator" select="' '"/>
      <p:option name="byte-order-mark"/>
      <p:option name="cdata-section-elements" select="''"/>
      <p:option name="doctype-public"/>
      <p:option name="doctype-system"/>
      <p:option name="encoding"/>
      <p:option name="escape-uri-attributes" select="'false'"/>
      <p:option name="include-content-type" select="'true'"/>
      <p:option name="indent" select="'false'"/>
      <p:option name="media-type"/>
      <p:option name="method" select="'xml'"/>
      <p:option name="normalization-form" select="'none'"/>
      <p:option name="omit-xml-declaration" select="'true'"/>
      <p:option name="standalone" select="'omit'"/>
      <p:option name="undeclare-prefixes"/>
      <p:option name="version" select="'1.0'"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:hash" xml:id="hash">
      <p:documentation>The p:hash pipeline generates a hash, or digital “fingerprint”, for some value and injects it into the source document.</p:documentation>
      <p:input port="source" primary="true"/>
      <p:output port="result"/>
      <p:input port="parameters" kind="parameter"/>
      <p:option name="value" required="true"/>
      <p:option name="algorithm" required="true"/>
      <p:option name="match" required="true"/>
      <p:option name="version"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:uuid" xml:id="uuid">
      <p:documentation>The p:uuid pipeline generates a [UUID] and injects it into the source document.</p:documentation>
      <p:input port="source" primary="true"/>
      <p:output port="result"/>
      <p:option name="match" required="true"/>
      <p:option name="version"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:validate-with-relax-ng" xml:id="validate-with-relax-ng">
      <p:documentation>The p:validate-with-relax-ng pipeline applies [RELAX NG] validation to the source document.</p:documentation>
      <p:input port="source" primary="true"/>
      <p:input port="schema"/>
      <p:output port="result"/>
      <p:option name="dtd-attribute-values" select="'false'"/>
      <p:option name="dtd-id-idref-warnings" select="'false'"/>
      <p:option name="assert-valid" select="'true'"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:validate-with-schematron" xml:id="validate-with-schematron">
      <p:documentation>The p:validate-with-schematron pipeline applies [Schematron] processing to the source document.</p:documentation>
      <p:input port="parameters" kind="parameter"/>
      <p:input port="source" primary="true"/>
      <p:input port="schema"/>
      <p:output port="result" primary="true"/>
      <p:output port="report" sequence="true"/>
      <p:option name="phase" select="'#ALL'"/>
      <p:option name="assert-valid" select="'true'"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:validate-with-xml-schema" xml:id="validate-with-xml-schema">
      <p:documentation>The p:validate-with-xml-schema pipeline applies [W3C XML Schema: Part 1] validity assessment to the source input.</p:documentation>
      <p:input port="source" primary="true"/>
      <p:input port="schema" sequence="true"/>
      <p:output port="result"/>
      <p:option name="use-location-hints" select="'false'"/>
      <p:option name="try-namespaces" select="'false'"/>
      <p:option name="assert-valid" select="'true'"/>
      <p:option name="mode" select="'strict'"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:www-form-urldecode" xml:id="www-form-urldecode">
      <p:documentation>The p:www-form-urldecode pipeline decodes a x-www-form-urlencoded string into a set of parameters.</p:documentation>
      <p:output port="result"/>
      <p:option name="value" required="true"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:www-form-urlencode" xml:id="www-form-urlencode">
      <p:documentation>The p:www-form-urlencode pipeline encodes a set of parameter values as a x-www-form-urlencoded string and injects it into the source document.</p:documentation>
      <p:input port="source" primary="true"/>
      <p:output port="result"/>
      <p:input port="parameters" kind="parameter"/>
      <p:option name="match" required="true"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:xquery" xml:id="xquery">
      <p:documentation>The p:xquery pipeline applies an [XQuery 1.0] query to the sequence of documents provided on the source port.</p:documentation>
      <p:input port="source" sequence="true" primary="true"/>
      <p:input port="query"/>
      <p:input port="parameters" kind="parameter"/>
      <p:output port="result" sequence="true"/>
   </p:declare-pipeline>
   <p:declare-pipeline type="p:xsl-formatter" xml:id="xsl-formatter">
      <p:documentation>The p:xsl-formatter pipeline receives an [XSL 1.1] document and renders the content. The result of rendering is stored to the URI provided via the href option. A reference to that result is produced on the output port.</p:documentation>
      <p:input port="source"/>
      <p:input port="parameters" kind="parameter"/>
      <p:output port="result" primary="false"/>
      <p:option name="href" required="true"/>
      <p:option name="content-type"/>
   </p:declare-pipeline>
</p:library>
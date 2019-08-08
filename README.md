# GProcX - A GUI authoring tool for XProc
GProcX is the extended version of GProc, these two tools are both for XProc. XProc is a XML pipeline language defined in https://www.w3.org/TR/xproc/. This tool is an addon for Oxygen XML editor, developers can build pipelines in the GUI created by GProcX.
![The main interface of GProcX](https://github.com/ychenbrian/GProcX/blob/master/image/gui.jpg)

## Installation
This add-on is compatible with Oxygen XML Editor 17.0 or higher. The installation time for the tool will be less than five minutes.

To install the add-on, follow these instructions:

 1. Clone this repository using `git clone https://github.com/ychenbrian/GProcX.git`
 2. Open Oxygen XML Editor, `Help -> Install new add-ons...`
 3. Select file: `GProcX\target\addon.xml` (not the file in the root address)
 4. Click the box and `Next>`
 5. Accept all the license agreements, and click `Install`
 6. For the warning message, click `Continue anyway`
 7. Waiting for the installation
 8. Restart the software to complete the installation

## Running

To open GProcX, just click the GProcX button in the toolbar of Oxygen. A new window will appear.

### Build a new pipeline
You can build the pipeline directly , or start a new projects through `File -> New`. There are three types of projects, `p:declare-step`, `p:pipeline`, and `p:library`.

Add steps and pipes to the drawing panel, and set information for the selected steps in the configuration panel.

### Import a pipeline
GProcX also supports the import of a pipeline from a `.xpl` file (XProc program) or from the work space of Oxygen.

If any errors occur during the import process, the error message will be displayed.

### Save the project
You can save the current pipeline at anytime by clicking `File -> Save`. The saved file is a `.gprocx` file.

### Export the pipeline
GProcX supports `File -> Export` and `File -> Run` the entire pipeline.

## Features
The main feature is, developers can import an XProc program from file or from Oxygen to GProcX. This feature allows them to check or edit an existing pipeline.

GProcX uses MorganaXProc to validate and complies XProc programs.

## License
This project is licensed under [Apache License 2.0](https://github.com/ychenbrian/GProcX/blob/master/LICENSE)
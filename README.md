# Useful Test-Utils
This repo has some useful utilities that will help engineers especially SDETs to fulfil few operations.


## Utilities:
**removeConfigNodesFromTestNgResultsXML**:

There are n number of config nodes like _@BeforeMethod_, _@AfterMethod_ etc in a testng-results.xml file. A real time usefulness of this utility is, ALM treats every config node as a test case, so it is better to remove these config nodes before you upload the testng-results.xml to ALM. This will give accurate result of how many **test cases** failed and passed.

**Usage**:

```TestUtils.removeConfigNodesFromTestNgResultsXML("path_to_your_input_testng-results.xml_file");```

**downloadZip**:

This method will help to download a zip file from a remote location.

**Usage**:

```TestUtils.downloadZip("remoteZipLocation","location_to_download/file.zip",1024);```

**unZipFolder**:

This method will help to unzip a folder and extract all its contents

**Usage**:

```TestUtils.unZipFolder("path to the zip folder");```


## My site:
https://sdeting.com/

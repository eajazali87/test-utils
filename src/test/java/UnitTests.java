import org.testng.annotations.Test;

import java.io.*;

public class UnitTests {

    @Test
    public void removeConfigNodesTest(){
        TestUtils.removeConfigNodesFromTestNgResultsXML(System.getProperty("user.dir")+"/src/test/TestData/testng-results.xml");
    }

    @Test
    public void downloadZipTest() throws IOException {
        String remoteZipLocation="http://publib.boulder.ibm.com/bpcsamp/monitoring/clipsAndTacks/download/ClipsAndTacksF1.zip";
        String INPUT_ZIP_FILE = System.getProperty("user.dir")+"/ClipsAndTacksF1.zip";
        TestUtils.downloadZip(remoteZipLocation,INPUT_ZIP_FILE,1024);
    }

    @Test
    public void unzipFolderTest(){
        String zipFilePath=System.getProperty("user.dir")+"/src/test/TestData/ClipsAndTacksF1.zip";
        TestUtils.unzipFolder(zipFilePath);
    }
}

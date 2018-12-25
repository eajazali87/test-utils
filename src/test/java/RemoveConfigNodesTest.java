import org.testng.annotations.Test;

public class RemoveConfigNodesTest {

    @Test
    public void removeConfigNodesTest(){
        TestUtils.removeConfigNodesFromTestNgResultsXML(System.getProperty("user.dir")+"/src/test/TestData/testng-results.xml");
    }
}

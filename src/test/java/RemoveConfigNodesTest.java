import org.testng.annotations.Test;

public class RemoveConfigNodesTest {

    @Test
    public void removeConfigNodesTest(){
        TestUtils.removeConfigNodes("/Users/umahaea/Documents/workspace/test-utils/src/test/TestData/testng-results.xml");
    }
}

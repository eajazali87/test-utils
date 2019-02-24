import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;


/**
 * @author Eajaz
 * Created on 12/25/18
 */

public class TestUtils {

    static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    static DocumentBuilder builder;

    static {
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    protected static Document doc = null;

    protected static TransformerFactory tf = TransformerFactory.newInstance();
    protected static Transformer t = null;

    public TestUtils() throws ParserConfigurationException {
    }

    /**
     * @param inputFilePath the location of the testng-results.xml file
     * <p>The removeConfigNodes method implements an logic that does the below operations:
     *      1. Remove test-method nodes where status = "Skip"
     *      2. Remove test-method nodes whose "is-config" attribute value is "true". This removes things like beforeClass, afterClass, setUp etc.
     *      3. In each test-method node, append a unique number [1],[2],[3], etc. to each name attribute (If you use ALM, then this forces ALM to bring these in a separate tests, otherwise ALM will bring these in as different runs for the same test</p>
     */
    public static void removeConfigNodesFromTestNgResultsXML(String inputFilePath) {

        File f = new File(inputFilePath);

        if (f.exists()) {
        } else {
            System.out
                .println("file not found, please check if your input file is in the correct path");
            System.exit(1);
        }

        try {
            doc = builder.parse(inputFilePath);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        NodeList testMethodNodeList = doc.getElementsByTagName("test-method"); //Mention the node
        NodeList lineNodeList = doc.getElementsByTagName("line"); //Mention the node
        List<Element> removeElements = new LinkedList<Element>(); // a List for removing nodes
        List<Element> retainElements = new LinkedList<Element>(); // a List for retaining nodes


        //Iterate over and find out <test-method> node with attribute "is-config" equals "true" or "status" equals "SKIP"
        for (int i = 0; i < testMethodNodeList.getLength(); i++) {
            Element e = (Element) testMethodNodeList.item(i);
            if ((e.getAttribute("is-config").equals("true")) || e.getAttribute("status").equals(
                "SKIP")) { //If you have to remove any node with a unique value, pass it on here
                removeElements.add(e);
            } else {
                retainElements.add(
                    e); //If you have to retain any node with a unique value, pass it on here with a condition
            }
        }

        for (int i = 0; i < lineNodeList.getLength(); i++) {
            Element e = (Element) lineNodeList.item(i);
            removeElements.add(e);
        }

        // Permanently delete/remove the <test-method> node with attribute name that does not end with "Test" or "status" equals "SKIP"
        for (Element e : removeElements) {
            e.getParentNode().removeChild(e);
        }

        NodeList newList = doc.getElementsByTagName("test-method");

        int i;
        for (i = 0; i < (retainElements.size()); i++) {
            Node value = newList.item(i).getAttributes().getNamedItem("name");
            String val = value.getNodeValue();
            value.setNodeValue(val.replace(retainElements.get(i).getAttribute("name"),
                retainElements.get(i).getAttribute("name") + "[" + (i + 1) + "]"));
        }

        //If you need to check the final count of <test-method> nodes
        System.out.println("Total no. of valid <test-methods> : " + retainElements.size());

        //Write it to a file the final testng.xml file
        try {
            t = tf.newTransformer();
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }
        try {
            t.transform(new DOMSource(doc),
                new StreamResult(new File(System.getProperty("user.dir") + "/testng-config-free-results.xml")));
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>Downloads a zip directory from a remote location</p>
     * @param remoteZipLocation the remote location of the zip file, eg: http://publib.boulder.ibm.com/bpcsamp/monitoring/clipsAndTacks/download/ClipsAndTacksF1.zip
     * @param inputZipFilePath will download the zip to the location specified, eg: http://url.com/file.zip
     * @param bufferSize eg: set to 1024
     * **/

    public static void downloadZip(String remoteZipLocation, String inputZipFilePath, int bufferSize) throws IOException {
        URL url = new URL(remoteZipLocation);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        InputStream inputStream = connection.getInputStream();
        OutputStream outputStream = new FileOutputStream(inputZipFilePath);

        byte[] buf = new byte[bufferSize];
        int n = inputStream.read(buf);
        while (n >= 0) {
            outputStream.write(buf, 0, n);
            n = inputStream.read(buf);
        }
        outputStream.flush();
    }

    /**
     * <p>Unzips a directory and extract all of its contents</p>
     * @param inputZipFilePath path to the zip folder
     *
     * **/

    public static void unzipFolder(String inputZipFilePath){
        try {
            ZipFile zipFile = new ZipFile(inputZipFilePath);
            Enumeration<?> enu = zipFile.entries();
            while (enu.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) enu.nextElement();

                String name = zipEntry.getName();
                long size = zipEntry.getSize();
                long compressedSize = zipEntry.getCompressedSize();
                System.out.printf("name: %-20s | size: %6d | compressed size: %6d\n",
                    name, size, compressedSize);

                File file = new File(name);
                if (name.endsWith("/")) {
                    file.mkdirs();
                    continue;
                }

                File parent = file.getParentFile();
                if (parent != null) {
                    parent.mkdirs();
                }

                InputStream is = zipFile.getInputStream(zipEntry);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] bytes = new byte[1024];
                int length;
                while ((length = is.read(bytes)) >= 0) {
                    fos.write(bytes, 0, length);
                }
                is.close();
                fos.close();

            }
            zipFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

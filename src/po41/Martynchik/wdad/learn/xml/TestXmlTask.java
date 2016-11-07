package po41.Martynchik.wdad.learn.xml;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public class TestXmlTask {
    public static void main(String[] args)
            throws IOException, TransformerException, SAXException, ParserConfigurationException {
        XmlTask workBitch = new XmlTask();
        System.out.println("Bill: " + workBitch.getBill("somestreet", 40, 1));
        //workBitch.setTariff("electricity", 6);
        //workBitch.addRegistration("somestreet", 40, 2, 2016, 3, 15, 15, 300, 15);
        //System.out.println("Bill: " + workBitch.getBill("somestreet", 40, 2));
    }
}
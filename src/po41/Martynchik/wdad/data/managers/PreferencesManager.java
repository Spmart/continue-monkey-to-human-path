package po41.Martynchik.wdad.data.managers;

import org.xml.sax.SAXException;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class PreferencesManager {
    private static final String path = "src/PO41/Martynchik/wdad/resources/configurations/appconfig.xml";
    private Document appconfig;
    private static final String CREATE_REGISTRY_TAG_NAME = "createregistry";
    private static final String REGISTRY_ADDRESS_TAG_NAME = "registryaddress";
    private static final String REGISTRY_PORT_TAG_NAME = "registryport";
    private static final String POLICY_PATH_TAG_NAME = "policypath";
    private static final String USE_CODEBASE_ONLY_TAG_NAME = "usecodebaseonly";
    private static final String CLASS_PROVIDER_TAG_NAME = "classprovider";
    private static final String YES_TEXT_VALUE = "yes";
    private static final String NO_TEXT_VALUE = "no";

    /**РЎРѕР·РґР°РµС‚ РґРµСЂРµРІРѕ DOM*/
    private void createDocument() throws IOException, ParserConfigurationException, SAXException {
        File appconfig = new File(path);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true); //Р’Р°Р¶РЅР°СЏ С€С‚СѓРєР°. РЎ РЅРµР№ РїР°СЂСЃРµСЂ РёРіРЅРѕСЂРёСЂСѓРµРј РїСЂРѕР±РµР»С‹.
        DocumentBuilder builder = factory.newDocumentBuilder();
        this.appconfig = builder.parse(appconfig);
    }

    /**РџРµСЂРµР·Р°РїРёСЃС‹РІР°РµС‚ РґРѕРєСѓРјРµРЅС‚*/
    private void rewriteDocument() throws TransformerException, IOException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        DOMSource domSource = new DOMSource(appconfig);
        StreamResult streamResult = new StreamResult(new File(path));
        transformer.transform(domSource, streamResult);
    }

    public boolean getCreateRegistryFlag() {
        NodeList createRegistry = appconfig.getElementsByTagName(CREATE_REGISTRY_TAG_NAME);
        String flag = createRegistry.item(0).getTextContent();
        if (flag.equalsIgnoreCase(YES_TEXT_VALUE)) return true;
        else return false;
    }

    public String getRegistryAddress() {
        NodeList registryAddress = appconfig.getElementsByTagName(REGISTRY_ADDRESS_TAG_NAME);
        return registryAddress.item(0).getTextContent();
    }

    public int getRegistryPort() {
        NodeList registryPort = appconfig.getElementsByTagName(REGISTRY_PORT_TAG_NAME);
        return Integer.valueOf(registryPort.item(0).getTextContent());
    }

    public String getPolicyPath() {
        NodeList policyPath = appconfig.getElementsByTagName(POLICY_PATH_TAG_NAME);
        return policyPath.item(0).getTextContent();
    }

    public boolean getUseCodeBaseOnlyFlag() {
        NodeList codeBaseOnly = appconfig.getElementsByTagName(USE_CODEBASE_ONLY_TAG_NAME);
        String flag = codeBaseOnly.item(0).getTextContent();
        if (flag.equalsIgnoreCase(YES_TEXT_VALUE)) return true;
        else return false;
    }

    public String getClassProvider() {
        NodeList classProvider = appconfig.getElementsByTagName(CLASS_PROVIDER_TAG_NAME);
        return classProvider.item(0).getTextContent();
    }

    public void setCreateRegistryFlag(boolean flag) throws TransformerException, IOException {
        NodeList createRegistry = appconfig.getElementsByTagName(CREATE_REGISTRY_TAG_NAME);
        if (flag == true) createRegistry.item(0).setTextContent(YES_TEXT_VALUE);
        else createRegistry.item(0).setTextContent(YES_TEXT_VALUE);
        rewriteDocument();
    }

    public void setRegistryAddress(String address) throws TransformerException, IOException {
        NodeList registryAddress = appconfig.getElementsByTagName(REGISTRY_ADDRESS_TAG_NAME);
        registryAddress.item(0).setTextContent(address);
        rewriteDocument();
    }

    public void setRegistryPort(int port) throws TransformerException, IOException {
        NodeList registryPort = appconfig.getElementsByTagName(REGISTRY_PORT_TAG_NAME);
        String value = String.valueOf(port);
        registryPort.item(0).setTextContent(value);
        rewriteDocument();
    }

    public void setPolicyPath(String path) throws TransformerException, IOException {
        NodeList policyPath = appconfig.getElementsByTagName(POLICY_PATH_TAG_NAME);
        policyPath.item(0).setTextContent(path);
        rewriteDocument();
    }

    public void setUseCodeBaseOnly(boolean flag) throws TransformerException, IOException {
        NodeList codeBaseOnly = appconfig.getElementsByTagName(USE_CODEBASE_ONLY_TAG_NAME);
        if (flag == true) codeBaseOnly.item(0).setTextContent(YES_TEXT_VALUE);
        else codeBaseOnly.item(0).setTextContent(NO_TEXT_VALUE);
        rewriteDocument();
    }

    public void setClassProvider(String url) throws TransformerException, IOException {
        NodeList classProvider = appconfig.getElementsByTagName(CLASS_PROVIDER_TAG_NAME);
        classProvider.item(0).setTextContent(url);
        rewriteDocument();
    }
}
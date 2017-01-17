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
import javax.xml.xpath.*;
import java.util.Properties;

public class PreferencesManager {
    private static final String path = "src/PO41/Martynchik/wdad/resources/configurations/appconfig.xml";
    private Document appconfig;
    private static final String CREATE_REGISTRY_TAG_NAME = "createregistry";
    private static final String REGISTRY_ADDRESS_TAG_NAME = "registryaddress";
    private static final String REGISTRY_PORT_TAG_NAME = "registryport";
    private static final String POLICY_PATH_TAG_NAME = "policypath";
    private static final String USE_CODEBASE_ONLY_TAG_NAME = "usecodebaseonly";
    private static final String CLASS_PROVIDER_TAG_NAME = "classprovider";
    private static final String BINDED_OBJECT_TAG_NAME = "bindedobject";
    private static final String NAME_TAG_NAME = "name";
    private static final String CLASS_TAG_NAME = "class";
    private static final String SERVER_TAG_NAME = "server";
    private static final String YES_TEXT_VALUE = "yes";
    private static final String NO_TEXT_VALUE = "no";
    private static PreferencesManager instance;

    private PreferencesManager() throws IOException, ParserConfigurationException, SAXException{
        createDocument();
    }

    public static PreferencesManager getInstance() throws ParserConfigurationException, IOException, SAXException {
        if (instance == null) instance = new PreferencesManager();
        return instance;
    }

    private void createDocument() throws IOException, ParserConfigurationException, SAXException {
        File appconfig = new File(path);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        this.appconfig = builder.parse(appconfig);
    }

    private void rewriteDocument() throws TransformerException, IOException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        DOMSource domSource = new DOMSource(appconfig);
        StreamResult streamResult = new StreamResult(new File(path));
        transformer.transform(domSource, streamResult);
    }

    public void setProperty(String key, String value) throws IOException, TransformerException {
        String[] tags = key.split("\\.");
        NodeList node = appconfig.getElementsByTagName(tags[tags.length - 1]);
        node.item(0).setTextContent(value);
        rewriteDocument();
    }

    public String getProperty(String key) throws IOException{
        String[] tags = key.split("\\.");
        NodeList node = appconfig.getElementsByTagName(tags[tags.length - 1]);
        return node.item(0).getTextContent();
    }

    public void setProperties(Properties prop) throws IOException, TransformerException {
        for (String key: prop.stringPropertyNames())
            setProperty(key, prop.getProperty(key));
    }

    public Properties getProperties() throws IOException, XPathExpressionException {
        Properties properties = new Properties();
        String key, value;
        XPath xPath = XPathFactory.newInstance().newXPath();
        String expression = "//*[not(*)]";
        NodeList node = (NodeList) xPath.compile(expression).evaluate(appconfig, XPathConstants.NODESET);
        for (int i = 0; i < node.getLength(); i++){
            key = getNodePath(node.item(i));
            value = getProperty(key);
            properties.put(key, value);
        }
        return properties;
    }

    public void addBindedObject(String name, String className) throws IOException, TransformerException {
        Element bindedObjectNode = appconfig.createElement(BINDED_OBJECT_TAG_NAME);
        bindedObjectNode.setAttribute(NAME_TAG_NAME, name);
        bindedObjectNode.setAttribute(CLASS_TAG_NAME, className);
        appconfig.getElementsByTagName(SERVER_TAG_NAME).item(0).appendChild(bindedObjectNode);
        rewriteDocument();
    }

    public void removeBindedObject(String name) throws IOException, TransformerException {
        NodeList bindedObjectList = appconfig.getElementsByTagName(BINDED_OBJECT_TAG_NAME);
        NamedNodeMap bindedObjectListAttributes;
        for (int i = 0; i < bindedObjectList.getLength(); i++) {
            bindedObjectListAttributes = bindedObjectList.item(i).getAttributes();
            if (bindedObjectListAttributes.getNamedItem(NAME_TAG_NAME).getNodeValue().equals(name)) {
                bindedObjectList.item(i).getParentNode().removeChild(bindedObjectList.item(i));
            }
        }
        rewriteDocument();
    }

    private static String getNodePath(Node node) {
        Node parent = node.getParentNode();
        if (parent == null || parent.getNodeName().equalsIgnoreCase("#appconfig")) //Думаю, это работает так
            return node.getNodeName();
        return getNodePath(parent) + '.' + node.getNodeName();
    }

    @Deprecated
    public boolean getCreateRegistryFlag() {
        NodeList createRegistry = appconfig.getElementsByTagName(CREATE_REGISTRY_TAG_NAME);
        String flag = createRegistry.item(0).getTextContent();
        if (flag.equalsIgnoreCase(YES_TEXT_VALUE)) return true;
        else return false;
    }

    @Deprecated
    public String getRegistryAddress() {
        NodeList registryAddress = appconfig.getElementsByTagName(REGISTRY_ADDRESS_TAG_NAME);
        return registryAddress.item(0).getTextContent();
    }

    @Deprecated
    public int getRegistryPort() {
        NodeList registryPort = appconfig.getElementsByTagName(REGISTRY_PORT_TAG_NAME);
        return Integer.valueOf(registryPort.item(0).getTextContent());
    }

    @Deprecated
    public String getPolicyPath() {
        NodeList policyPath = appconfig.getElementsByTagName(POLICY_PATH_TAG_NAME);
        return policyPath.item(0).getTextContent();
    }

    @Deprecated
    public boolean getUseCodeBaseOnlyFlag() {
        NodeList codeBaseOnly = appconfig.getElementsByTagName(USE_CODEBASE_ONLY_TAG_NAME);
        String flag = codeBaseOnly.item(0).getTextContent();
        if (flag.equalsIgnoreCase(YES_TEXT_VALUE)) return true;
        else return false;
    }

    @Deprecated
    public String getClassProvider() {
        NodeList classProvider = appconfig.getElementsByTagName(CLASS_PROVIDER_TAG_NAME);
        return classProvider.item(0).getTextContent();
    }

    @Deprecated
    public void setCreateRegistryFlag(boolean flag) throws TransformerException, IOException {
        NodeList createRegistry = appconfig.getElementsByTagName(CREATE_REGISTRY_TAG_NAME);
        if (flag == true) createRegistry.item(0).setTextContent(YES_TEXT_VALUE);
        else createRegistry.item(0).setTextContent(YES_TEXT_VALUE);
        rewriteDocument();
    }

    @Deprecated
    public void setRegistryAddress(String address) throws TransformerException, IOException {
        NodeList registryAddress = appconfig.getElementsByTagName(REGISTRY_ADDRESS_TAG_NAME);
        registryAddress.item(0).setTextContent(address);
        rewriteDocument();
    }

    @Deprecated
    public void setRegistryPort(int port) throws TransformerException, IOException {
        NodeList registryPort = appconfig.getElementsByTagName(REGISTRY_PORT_TAG_NAME);
        String value = String.valueOf(port);
        registryPort.item(0).setTextContent(value);
        rewriteDocument();
    }

    @Deprecated
    public void setPolicyPath(String path) throws TransformerException, IOException {
        NodeList policyPath = appconfig.getElementsByTagName(POLICY_PATH_TAG_NAME);
        policyPath.item(0).setTextContent(path);
        rewriteDocument();
    }

    @Deprecated
    public void setUseCodeBaseOnly(boolean flag) throws TransformerException, IOException {
        NodeList codeBaseOnly = appconfig.getElementsByTagName(USE_CODEBASE_ONLY_TAG_NAME);
        if (flag == true) codeBaseOnly.item(0).setTextContent(YES_TEXT_VALUE);
        else codeBaseOnly.item(0).setTextContent(NO_TEXT_VALUE);
        rewriteDocument();
    }

    @Deprecated
    public void setClassProvider(String url) throws TransformerException, IOException {
        NodeList classProvider = appconfig.getElementsByTagName(CLASS_PROVIDER_TAG_NAME);
        classProvider.item(0).setTextContent(url);
        rewriteDocument();
    }
}
package po41.Martynchik.wdad.learn.rmi;

import po41.Martynchik.wdad.data.managers.PreferencesManager;
import po41.Martynchik.wdad.utils.PreferencesConstantManager;
import po41.Martynchik.wdad.data.managers.XmlDataManager;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class Client {
    private static PreferencesManager preferencesManager;
    private static final String XML_DATA_MANAGER = "XmlDataManager";

    public static void main(String[] args) throws IOException {
        try {
            preferencesManager = PreferencesManager.getInstance();
        } catch (ParserConfigurationException | IOException | SAXException ex) {
            ex.printStackTrace();
        }

        System.setProperty("java.rmi.server.useCodebaseOnly", preferencesManager.getProperty(PreferencesConstantManager.USE_CODE_BASE_ONLY));
        //System.setProperty("java.rmi.server.codebase", PreferencesConstantManager.CLASS_PROVIDER);
        System.setProperty("java.security.policy", preferencesManager.getProperty(PreferencesConstantManager.POLICY_PATH).trim());
        System.setSecurityManager(new SecurityManager());

        Registry registry;
        registry = LocateRegistry.getRegistry(
                preferencesManager.getProperty(PreferencesConstantManager.REGISTRY_ADDRESS),
                Integer.parseInt(preferencesManager.getProperty(PreferencesConstantManager.REGISTRY_PORT)));

        try {
            XmlDataManager xmlDataManager = (XmlDataManager) registry.lookup(XML_DATA_MANAGER);
            workXmlManager(xmlDataManager);
        } catch (NotBoundException e) {
            System.err.println("Your code is shit!");
            e.printStackTrace();
        }
    }

    private static void workXmlManager(XmlDataManager xmlDataManager) throws RemoteException {
        /*
        Building building = new Building("somestreet", 40);
        System.out.println(xmlDataManager.getBill(building, 1));

        Flat flat = xmlDataManager.getFlat(building, 1);
        System.out.println("flat info: number - " + flat.getNumber() + ", area - " + flat.getArea() +
                ", personsQuantity" + flat.getPersonsQuantity());
        System.out.println("Registration : ");
        List<Registration> regs = flat.getRegistration();
        for (int i = 0; i < regs.size(); i++) {
            Registration reg = regs.get(i);
            System.out.println("Registration " + i + ":");
            System.out.println("date: year -" + reg.getData().getYear() + ", month" + reg.getData().getMonth());
            System.out.println("cold water:" + reg.getColdwater());
            System.out.println("hot water:" + reg.getHotwater());
            System.out.println("electricity:" + reg.getElectricity());
            System.out.println("gas:" + reg.getGas());
        }

        Date registrationDate = null;
        registrationDate.setYear(2016);
        registrationDate.setMonth(5);

        Registration registration = new Registration(registrationDate, 350, 224, 150, 100);
        xmlDataManager.setTariff("gas", 110);
        xmlDataManager.addRegistration(building, 3, registration);
        */
        Building building = new Building("somestreet", 40);
        System.out.println("Bill for somestreet, 40, 1: " + xmlDataManager.getBill(building, 1));
    }
}
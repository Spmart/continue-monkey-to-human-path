package po41.Martynchik.wdad.learn.rmi;

import po41.Martynchik.wdad.learn.xml.XmlTask;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import po41.Martynchik.wdad.data.managers.XmlDataManager;

import javax.xml.transform.TransformerException;

public class XmlDataManagerImpl implements XmlDataManager {
    private XmlTask xmlTask;

    public double getBill(Building building, int flatNumber) {
        return xmlTask.getBill(building.getStreet(), building.getNumber(), flatNumber);
    }

    public Flat getFlat(Building building, int flatNumber) {
        return xmlTask.getFlat(building, flatNumber);
    }

    public void setTariff(String tariffName, int newValue) {
        try {
            xmlTask.setTariff(tariffName, newValue);
        } catch (IOException ex) {
            Logger.getLogger(XmlDataManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            System.out.println("Насяйника, трансформерма эксэпшонэ ХЕРРРРАААКСС!");
        }
    }

    public void addRegistration(Building building, int flatNumber, Registration registrations) {
        try {
            xmlTask.addRegistration(building.getStreet(), building.getNumber(), flatNumber, registrations.getData().getYear(), registrations.getData().getMonth(),
                    registrations.getColdwater(), registrations.getHotwater(), registrations.getElectricity(), registrations.getGas());
        } catch (IOException ex) {
            Logger.getLogger(XmlDataManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            System.out.println("Насяйника, трансформерма эксэпшонэ ХЕРРРРАААКСС!");
        }
    }
}
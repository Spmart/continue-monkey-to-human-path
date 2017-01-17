package po41.Martynchik.wdad.data.managers;

import po41.Martynchik.wdad.learn.rmi.Building;
import po41.Martynchik.wdad.learn.rmi.Flat;
import po41.Martynchik.wdad.learn.rmi.Registration;

public interface XmlDataManager {
    public double getBill (Building building, int flatNumber);
    public Flat getFlat (Building building, int flatNumber);
    public void setTariff (String tariffName, int newValue);
    public void addRegistration (Building building, int flatNumber, Registration registrations);
}
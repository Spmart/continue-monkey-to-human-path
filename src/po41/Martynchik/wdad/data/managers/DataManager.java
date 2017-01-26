package po41.Martynchik.wdad.data.managers;

import po41.Martynchik.wdad.learn.rmi.Building;
import po41.Martynchik.wdad.learn.rmi.Flat;
import po41.Martynchik.wdad.learn.rmi.Registration;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DataManager extends Remote {
    public double getBill (Building building, int flatNumber) throws RemoteException;
    public Flat getFlat (Building building, int flatNumber) throws RemoteException;
    public void setTariff (String tariffName, int newValue) throws RemoteException;
    public void addRegistration (Building building, int flatNumber, Registration registrations) throws RemoteException;
}
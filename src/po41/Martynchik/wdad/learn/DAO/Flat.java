package po41.Martynchik.wdad.learn.DAO;

import java.util.Set;

public class Flat {
    private int id;
    private int number;
    private Building building;
    private int personsQuantity;
    private double area;
    private Set<Registration> registrations;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPersonsQuantity() {
        return personsQuantity;
    }

    public void setPersonsQuantity(int personsQuantity) {
        this.personsQuantity = personsQuantity;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public Set<Registration> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(Set<Registration> registrations) {
        this.registrations = registrations;
    }
}

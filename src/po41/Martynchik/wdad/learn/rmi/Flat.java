package po41.Martynchik.wdad.learn.rmi;

import java.util.List;

public class Flat {
    private int number;
    private int personsQuantity;
    private double area;
    private List<Registration> registrations;

    public Flat(int number, int personsQuantity, double area, List<Registration> registrations){
        this.area = area;
        this.number = number;
        this.personsQuantity = personsQuantity;
        this.registrations = registrations;
    }

    public int getNumber(){
        return number;
    }

    public void setNumber(int number){
        this.number = number;
    }

    public int getPersonsQuantity(){
        return personsQuantity;
    }

    public void setPersonsQuantity(int personsQuantity){
        this.personsQuantity = personsQuantity;
    }

    public double getArea(){
        return area;
    }

    public void setArea(double area){
        this.area = area;
    }

    public List<Registration> getRegistration(){
        return registrations;
    }

    public void setRegistration(List<Registration> registrations){
        this.registrations = registrations;
    }
}

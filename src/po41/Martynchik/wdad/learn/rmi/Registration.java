package po41.Martynchik.wdad.learn.rmi;

import java.util.Date;

public class Registration {
    private Date data;
    private double coldwater;
    private double hotwater;
    private double electricity;
    private double gas;

    public Registration(Date data, double coldwater, double hotwater, double electricity, double gas){
        this.data = data;
        this.coldwater = coldwater;
        this.hotwater = hotwater;
        this.electricity = electricity;
        this.gas = gas;
    }

    public Date getData(){
        return data;
    }

    public void setData(Date data){
        this.data = data;
    }

    public double getColdwater(){
        return coldwater;
    }

    public void setColdwater(double coldwater){
        this.coldwater = coldwater;
    }

    public double getHotwater(){
        return hotwater;
    }

    public void setHotwater(double hotwater){
        this.hotwater = hotwater;
    }

    public double getElectricity(){
        return electricity;
    }

    public void setElectricity(double electricity){
        this.electricity = electricity;
    }

    public double getGas(){
        return gas;
    }

    public void setGas(double gas){
        this.gas = gas;
    }
}

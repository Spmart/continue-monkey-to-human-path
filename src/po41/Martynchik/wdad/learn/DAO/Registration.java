package po41.Martynchik.wdad.learn.DAO;

import java.util.Date;
import java.util.HashMap;

public class Registration {
    private int id;
    private Date date;
    private Flat flat;
    private HashMap<Tariff, Double> amounts;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Flat getFlat(){
        return flat;
    }

    public void setFlat(Flat flat){
        this.flat = flat;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public HashMap<Tariff, Double> getAmounts() {
        return amounts;
    }

    public void setAmounts(HashMap<Tariff, Double> amounts) {
        this.amounts = amounts;
    }
}

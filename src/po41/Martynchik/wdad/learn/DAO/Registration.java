package po41.Martynchik.wdad.learn.DAO;

import java.time.LocalDate;
import java.util.HashMap;

public class Registration {
    private int id;
    private LocalDate date;
    private HashMap<Tariff, Double> amounts;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public HashMap<Tariff, Double> getAmounts() {
        return amounts;
    }

    public void setAmounts(HashMap<Tariff, Double> amounts) {
        this.amounts = amounts;
    }
}

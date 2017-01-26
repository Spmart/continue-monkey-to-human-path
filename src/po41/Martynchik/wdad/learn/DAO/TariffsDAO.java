package po41.Martynchik.wdad.learn.DAO;

import java.util.Collection;

public interface TariffsDAO {
    public String insertTariff (Tariff tariff);
    public boolean deleteTariff (Tariff tariff);
    public Tariff findTariff (String name);
    public boolean updateTariff (Tariff tariff);
    public boolean saveOrUpdateGenre (Tariff tariff);
    public Collection<Tariff> findTariffs ();
}

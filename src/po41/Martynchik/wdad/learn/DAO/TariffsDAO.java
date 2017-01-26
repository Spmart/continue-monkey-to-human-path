package po41.Martynchik.wdad.learn.DAO;

import java.util.Collection;

public interface TariffsDAO {
    public boolean insertTariff (Tariff tariff);
    public boolean deleteTariff (Tariff tariff);
    public Tariff findTariff (String name);
    public boolean updateTariff (Tariff tariff);
    public boolean saveOrUpdateTariff(Tariff tariff);
    public Collection<Tariff> findTariffs ();
}

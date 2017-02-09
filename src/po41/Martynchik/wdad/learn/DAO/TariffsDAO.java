/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package po41.Martynchik.wdad.learn.DAO;

import java.util.Collection;

/**
 *
 * @author 000
 */
public interface TariffsDAO {
    public boolean insertTariff (Tariff tariff);
    public boolean deleteTariff (Tariff tariff);
    public Tariff findTariff (String name);
    public boolean updateTariff (Tariff tariff);
    public boolean saveOrUpdateGenre (Tariff tariff);
    public Collection<Tariff> findTariffs ();
}

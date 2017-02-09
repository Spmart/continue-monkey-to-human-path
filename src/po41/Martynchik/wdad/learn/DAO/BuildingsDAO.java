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
public interface BuildingsDAO {
    public boolean insertBuilding (Building building);
    public boolean deleteBuilding (Building building);
    public Building findBuilding (int id);
    public boolean updateBuilding (Building building);
    public boolean saveOrUpdateBuilding (Building
    building);
    public Collection<Building> findBuildings (String
    streetName);
}

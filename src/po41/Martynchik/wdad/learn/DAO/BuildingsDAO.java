package po41.Martynchik.wdad.learn.DAO;

import java.util.Collection;

public interface BuildingsDAO {
    public boolean insertBuilding (Building building);
    public Building findBuilding (int id);
    public boolean updateBuilding (Building building);
    public boolean saveOrUpdateBuilding (Building building);
    public Collection<Building> findBuildings (String streetName);
}

package po41.Martynchik.wdad.learn.DAO;

import po41.Martynchik.wdad.data.storage.SqlPerRequestDAOFactory;

public class TestDAO {
    public static void main(String[] args) {
        SqlPerRequestDAOFactory factory = new SqlPerRequestDAOFactory();
        BuildingsDAO buildings = factory.getBuildingsDAO();
        Building building = new Building();
        building.setId(123);
        building.setNumber(1);
        building.setStreetName("Specialistov");

        buildings.insertBuilding(building);
    }
}

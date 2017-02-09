package po41.Martynchik.wdad.learn.DAO;


import java.sql.Connection;
import java.util.Collection;
import javax.sql.DataSource;

import po41.Martynchik.wdad.data.storage.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * @author 000
 */
public class BuildingsDAOImpl implements BuildingsDAO {
    private DataSource dataSource = null;

    public BuildingsDAOImpl() {
        try {
            dataSource = DataSourceFactory.createDataSource();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean insertBuilding(Building building) {
        if (building != null) {
            try (Connection connection = dataSource.getConnection()) {
                Statement statement = connection.createStatement();
                StringBuilder getStreetId = new StringBuilder("Select * From street Where name = '" + building.getStreetName() + "'");
                ResultSet result = statement.executeQuery(getStreetId.toString());
                if (result.next()) {
                    int streetId = result.getInt("id");
                    StringBuilder addBuilding = new StringBuilder(
                            "INSERT INTO buildings (id,number,street_id) VALUES ('" + building.getId() + "','" + building.getNumber() + "','" + streetId + "')");
                    statement.executeUpdate(addBuilding.toString());
                    if (building.getFlats() != null)
                        for (Flat flats : building.getFlats()) {
                            FlatsDAOImpl insertFlats = new FlatsDAOImpl();
                            insertFlats.saveOrUpdateFlat(flats);
                        }

                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public boolean deleteBuilding(Building building) {
        
        /* "Select * From street Where street_name = '"+ building.street+"'"
            int streetId = result.getInt("id");
            "SET foreign_key_checks = 0;
            DELETE From buildings Where number ='" + building.number+"' AND street_id ='"+ streetId+"'" 
        */
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            if (building.getFlats() != null)
                for (Flat flats : building.getFlats()) {
                    FlatsDAOImpl deleteFlats = new FlatsDAOImpl();
                    deleteFlats.deleteFlat(flats);
                }
            StringBuilder query = new StringBuilder(" DELETE From buildings Where id = '" + building.getId() + "'");
            statement.executeUpdate(query.toString());

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Building findBuilding(int id) {
        Building building = new Building();
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            StringBuilder getBuilding = new StringBuilder("Select * From buildings Where id ='" + id + "'");
            ResultSet result = statement.executeQuery(getBuilding.toString());
            result.next();
            int buildingNumber = result.getInt("number");
            int streetId = result.getInt("street_id");
            StringBuilder getStreet = new StringBuilder("SELECT * FROM `street` WHERE `id` ='" + streetId + "'");
            ResultSet resultStreet = statement.executeQuery(getStreet.toString());
            resultStreet.next();
            building.setId(id);
            building.setNumber(buildingNumber);
            building.setStreetName(resultStreet.getString("name"));
            building.setFlats(getFlats(connection, building));


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return building;
    }

    public boolean updateBuilding(Building building) {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            StringBuilder getStreetId = new StringBuilder("Select * From street Where name = '" + building.getStreetName() + "'");
            ResultSet result = statement.executeQuery(getStreetId.toString());
            result.next();
            int streetId = result.getInt("id");
            StringBuilder updateBuilding = new StringBuilder("UPDATE buildings SET number='" + building.getNumber() + "', street_id='"
                    + streetId + "' WHERE id='" + building.getId() + "'");
            statement.executeUpdate(updateBuilding.toString());
            if (building.getFlats() != null)
                for (Flat flat : building.getFlats()) {
                    FlatsDAOImpl update = new FlatsDAOImpl();
                    update.saveOrUpdateFlat(flat);
                }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean saveOrUpdateBuilding(Building building) {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            StringBuilder buildings = new StringBuilder("SELECT id FROM buildings WHERE id='" + building.getId() + "'");
            ResultSet result = statement.executeQuery(buildings.toString());
            if (result.next()) {
                return updateBuilding(building);
            } else {
                return insertBuilding(building);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public Collection<Building> findBuildings(String streetName) {
        Collection<Building> buildings = new LinkedList<>();
        try {
            try (Connection connection = dataSource.getConnection()) {
                Statement statement = connection.createStatement();
                StringBuilder getStreetId = new StringBuilder("SELECT * FROM street WHERE name='" + streetName + "'");
                ResultSet result = statement.executeQuery(getStreetId.toString());
                if (result.next()) {
                    int streetId = result.getInt("id");


                    StringBuilder getBuildings = new StringBuilder("SELECT * FROM buildings WHERE street_id='" + streetId + "'");
                    ResultSet resultBuildings = statement.executeQuery(getBuildings.toString());
                    while (resultBuildings.next()) {
                        Building building = new Building();
                        building.setId(resultBuildings.getInt("id"));
                        building.setNumber(resultBuildings.getInt("number"));
                        building.setStreetName(streetName);
                        building.setFlats(getFlats(connection, building));
                        buildings.add(building);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return buildings;
    }

    public LinkedHashSet<Flat> getFlats(Connection connection, Building building) throws SQLException {
        LinkedHashSet<Flat> flats = new LinkedHashSet<>();
        Statement statement = connection.createStatement();
        StringBuilder getFlats = new StringBuilder("SELECT * FROM flats WHERE buildings_id='" + building.getId() + "'");
        ResultSet result = statement.executeQuery(getFlats.toString());
        while (result.next()) {
            Flat flat = new Flat();
            flat.setArea(result.getDouble("area"));
            flat.setBuilding(building);
            flat.setId(result.getInt("id"));
            flat.setNumber(result.getInt("number"));
            flat.setPersonsQuantity(result.getInt("persons_quantity"));
            flat.setRegistrations(getRegistrations(connection, flat));
            flats.add(flat);
        }
        return flats;
    }

    public LinkedHashSet<Registration> getRegistrations(Connection connection, Flat flat) throws SQLException {
        Statement statement = connection.createStatement();
        LinkedHashSet<Registration> registrations = new LinkedHashSet<>();
        StringBuilder getRegistrations = new StringBuilder("SELECT * FROM registrations WHERE flats_id='" + flat.getId() + "'");
        ResultSet result = statement.executeQuery(getRegistrations.toString());
        while (result.next()) {
            String date = result.getDate("date").toString();
            String[] data = date.split("-");
            Date regDate = new Date();
            regDate.setYear(Integer.parseInt(data[0]));
            regDate.setMonth(Integer.parseInt(data[1]));
            regDate.setDate(Integer.parseInt(data[2]));
            Registration registration = new Registration();
            registration.setDate(regDate);
            registration.setId(result.getInt("id"));
            registration.setFlat(flat);
            registration.setAmounts(getAmounts(connection, registration.getId()));
            registrations.add(registration);
        }
        return registrations;
    }

    public HashMap<Tariff, Double> getAmounts(Connection connection, int registrationId) throws SQLException {
        Statement statement = connection.createStatement();
        HashMap<Tariff, Double> amounts = new HashMap<>();
        StringBuilder getRegistrationTariff = new StringBuilder("SELECT * FROM `registrations-tariffs` WHERE registrations_id='" + registrationId + "'");
        ResultSet resultRegistrationTariff = statement.executeQuery(getRegistrationTariff.toString());
        while (resultRegistrationTariff.next()) {
            Tariff tariff = new Tariff();
            tariff.setName(resultRegistrationTariff.getString("tariffs_name"));
            tariff.setCost(getCost(connection, tariff.getName()));
            amounts.put(tariff, resultRegistrationTariff.getDouble("amount"));
        }
        return amounts;
    }

    public Double getCost(Connection connection, String name) throws SQLException {
        Statement statement = connection.createStatement();
        StringBuilder getCost = new StringBuilder("SELECT cost FROM tariffs WHERE name='" + name + "'");
        ResultSet result = statement.executeQuery(getCost.toString());
        result.next();
        return result.getDouble("cost");
    }

}

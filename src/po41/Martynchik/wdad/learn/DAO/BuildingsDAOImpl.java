package po41.Martynchik.wdad.learn.DAO;

import org.xml.sax.SAXException;
import po41.Martynchik.wdad.data.storage.DataSourceFactory;

import java.io.IOException;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;
import java.sql.SQLException;
import java.util.Collection;

public class BuildingsDAOImpl implements BuildingsDAO {
    private DataSource dataSource = null; //К - костыли. Так и живем...

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

    @Override
    public boolean insertBuilding(Building building) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM street WHERE name = ?");
                statement.setString(1, building.getStreetName());
                ResultSet result = statement.executeQuery();
                int streetId = 0;
                if (result.first()) {
                    streetId = result.getInt("id");
                }

                statement = connection.prepareStatement("INSERT INTO buildings (number, street_id) VALUES(?, ?)");
                statement.setInt(1, building.getNumber());
                statement.setInt(2, streetId);

                for (Flat flat : building.getFlats()) {
                    statement = connection.prepareStatement("INSERT INTO flats (number, buildings_id) VALUES(?, ?)");
                    statement.setInt(1, flat.getNumber());
                    statement.setInt(2, building.getId());
                    statement.executeUpdate();

                    for (Registration registration : flat.getRegistrations()) {
                        statement = connection.prepareStatement("INSERT INTO registrations (date, flats_id) VALUES(?, ?)");
                        statement.setDate(1, java.sql.Date.valueOf(registration.getDate()));
                        statement.setInt(2, flat.getId());

                        for (Map.Entry<Tariff, Double> amount : registration.getAmounts().entrySet()) {
                            statement = connection.prepareStatement(
                                    "INSERT INTO registrations_tariffs (amount, registrations_id,tariffs_name) VALUES(?,?,?)");
                            statement.setDouble(1, amount.getValue());
                            statement.setInt(2, registration.getId());
                            statement.setString(3, amount.getKey().getName());
                        }
                    }
                }
            }
        } catch (SQLException e) {
            return false;
        }
        return true;
    }


    @Override
    public Building findBuilding(int id) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM buildings INNER JOIN street ON buildings.street_id = street.id WHERE buildings.id =?");

                statement.setInt(1, id);
                ResultSet result = statement.executeQuery();
                if (result.first()) {
                    Building building = new Building();
                    building.setId(result.getInt("id"));
                    building.setNumber(result.getInt("number"));
                    building.setStreetName(result.getString("name"));
                    building.setFlats(getFlats(connection, building));
                    return building;
                }
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public boolean updateBuilding(Building building) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statment = connection.prepareStatement(
                        "UPDATE buildings SET number=? WHERE id=?");
                statment.setDouble(1, building.getNumber());
                statment.setInt(2, building.getId());
                statment.executeUpdate();

                for (Flat flat : building.getFlats()) {
                    statment = connection.prepareStatement(
                            "UPDATE flats SET number=?,buildings_id=? WHERE id=?");
                    statment.setDouble(1, flat.getNumber());
                    statment.setInt(2, flat.getBuilding().getId());
                    statment.setInt(3, flat.getId());
                    statment.executeUpdate();

                    for (Registration registration : flat.getRegistrations()) {
                        statment = connection.prepareStatement(
                                "UPDATE registrations SET date=? WHERE id=?");
                        statment.setInt(1, registration.getId());
                        statment.executeUpdate();

                        for (Map.Entry<Tariff, Double> amount : registration.getAmounts().entrySet()) {
                            statment = connection.prepareStatement(
                                    "UPDATE registrations_tariffs SET amount=? VALUES(?,?)"
                                            + " WHERE tariffs_name=?");
                            statment.setDouble(1, amount.getValue());
                            statment.setString(2, amount.getKey().getName());
                            statment.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean saveOrUpdateBuilding(Building building) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statment = connection.prepareStatement(
                        "SELECT * FROM buildings,street WHERE id=? OR (number=? AND buildings.street_id=street.id)");
                statment.setInt(1, building.getId());
                statment.setInt(2, building.getNumber());
                boolean result = statment.execute();
                if (!result) {
                    return updateBuilding(building);
                } else {
                    return insertBuilding(building);
                }
            }
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Collection<Building> findBuildings(String streetName) {
        Collection<Building> buildings = new LinkedList<>();
        try {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM street WHERE name=?");
                statement.setString(1, streetName);

                ResultSet result = statement.executeQuery();
                int streetId = -1;
                if (result.first())
                    streetId = result.getInt("id");

                if (streetId != -1) {
                    statement = connection.prepareStatement("SELECT * FROM buildings WHERE street_id=?");
                    statement.setInt(1, streetId);
                    while (result.next()) {
                        Building building = new Building();
                        building.setId(result.getInt("id"));
                        building.setNumber(result.getInt("number"));
                        building.setStreetName(result.getString("name"));
                        building.setFlats(getFlats(connection, building));
                        buildings.add(building);
                    }
                }
            }
        } catch (SQLException e) {
            return null;
        }
        return buildings;
    }

    public boolean deleteBuilding(Building building) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statment = connection.prepareStatement("DELETE FROM buildings WHERE id=?");
                statment.setInt(1, building.getId());
                statment.executeUpdate();
            }
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    private LinkedHashSet<Flat> getFlats(Connection connection, Building building) throws SQLException {
        PreparedStatement statment = connection.prepareStatement("SELECT * FROM flats WHERE buildings.id =?");
        statment.setInt(1, building.getId());
        ResultSet flatsResult = statment.executeQuery();
        LinkedHashSet<Flat> flats = new LinkedHashSet<>();
        while (flatsResult.next()) {
            Flat flat = new Flat();
            flat.setBuilding(building);
            flat.setId(flatsResult.getInt("id"));
            flat.setNumber(flatsResult.getInt("number"));
            flat.setRegistrations(getRegistration(connection, flat));
            flats.add(flat);
        }
        return flats;
    }


    private LinkedHashSet<Registration> getRegistration(Connection connection, Flat flat) throws SQLException {
        LinkedHashSet<Registration> registrations = null;
        PreparedStatement statment = connection.prepareStatement("SELECT * FROM registrations WHERE flats_id=?");
        statment.setInt(1, flat.getId());
        ResultSet result = statment.executeQuery();
        while (result.next()) {
            Registration registration = new Registration();
            registration.setDate(result.getDate("date").toLocalDate());
            registration.setId(result.getInt("id"));

            HashMap<Tariff, Double> amounts = getAmounts(connection, registration.getId());
            registration.setAmounts(amounts);
            registrations.add(registration);
        }
        return registrations;
    }

    private HashMap<Tariff, Double> getAmounts(Connection connection, int registrationId) throws SQLException {
        HashMap<Tariff, Double> amounts = new HashMap<>();
        HashMap<String, Tariff> tarrifs = findTariffs();
        PreparedStatement statment = connection.prepareStatement(
                "SELECT * FROM registrations-tariffs WHERE registrations_id=?");
        statment.setInt(1, registrationId);
        ResultSet tarrifsResult = statment.executeQuery();

        while (tarrifsResult.next()) {
            amounts.put(
                    tarrifs.get(tarrifsResult.getString("tariffs_name")),
                    tarrifsResult.getDouble("amount"));
        }
        return amounts;
    }

    private HashMap<String, Tariff> findTariffs() {
        HashMap<String, Tariff> tariffs = new HashMap<>();
        try {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM tarrifs");

                ResultSet result = statement.executeQuery();
                while (result.next()) {
                    Tariff tarrif = new Tariff();
                    tarrif.setName(result.getString("name"));
                    tarrif.setCost(result.getDouble("cost"));
                    tariffs.put(tarrif.getName(), tarrif);
                }
            }
        } catch (SQLException e) {
            return null;
        }
        return tariffs;
    }
}
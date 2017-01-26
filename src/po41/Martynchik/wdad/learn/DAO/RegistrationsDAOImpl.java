package po41.Martynchik.wdad.learn.DAO;

import org.xml.sax.SAXException;
import po41.Martynchik.wdad.data.storage.DataSourceFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;

public class RegistrationsDAOImpl implements RegistrationsDAO {
    private DataSource dataSource;

    public RegistrationsDAOImpl() {
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
    public boolean insertRegistration(Registration registration) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                int flatId = getFlatId(connection, registration.getId());
                if (flatId != -1) {
                    PreparedStatement statment = connection.prepareStatement(
                            "INSERT INTO registrations (date,flats_id) VALUES(?,?)");
                    statment.setDate(1, java.sql.Date.valueOf(registration.getDate()));
                    statment.setInt(2, flatId);
                    statment.executeUpdate();

                    for (Map.Entry<Tariff, Double> amount : registration.getAmounts().entrySet()) {
                        statment = connection.prepareStatement(
                                "INSERT INTO registrations_tariffs (amount,registrations_id,tariffs_name) VALUES(?,?,?)");
                        statment.setDouble(1, amount.getValue());
                        statment.setInt(2, registration.getId());
                        statment.setString(2, amount.getKey().getName());
                        statment.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteRegistration(Registration registration) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statment = connection.prepareStatement(
                        "DELETE FROM registrations-tarrifs WHERE registrations_id=?");
                statment.setInt(1, registration.getId());
                statment.executeUpdate();

                statment = connection.prepareStatement(
                        "DELETE FROM registrations WHERE id=?");
                statment.setInt(1, registration.getId());
                statment.executeUpdate();
            }
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public Registration findRegistration(int id) {
        Registration registration = null;
        try {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statment = connection.prepareStatement(
                        "SELECT * FROM registrations WHERE id=?");
                statment.setInt(1, id);
                ResultSet result = statment.executeQuery();
                if (result.first()) {
                    registration = new Registration();
                    registration.setDate(result.getDate("date").toLocalDate());
                    registration.setId(result.getInt("id"));

                    HashMap<String, Tariff> tarrifs = findTariffs();

                    statment = connection.prepareStatement(
                            "SELECT * FROM registrations-tariffs WHERE registrations_id=?");
                    statment.setInt(1, id);
                    result = statment.executeQuery();
                    HashMap<Tariff, Double> amounts = new HashMap<>();
                    while (result.next()) {
                        amounts.put(
                                tarrifs.get(result.getString("tariffs_name")),
                                result.getDouble("amount"));
                    }
                }
            }
        } catch (SQLException e) {
            return null;
        }
        return registration;
    }

    @Override
    public boolean updateRegistration(Registration registration) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statment = connection.prepareStatement(
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
                statment.executeUpdate();
            }
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean saveOrUpdateRegistration(Registration registration) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statment = connection.prepareStatement(
                        "SELECT * FROM registrations WHERE id=?");
                statment.setInt(1, registration.getId());

                boolean result = statment.execute();

                if (!result) {
                    return updateRegistration(registration);
                } else {
                    return insertRegistration(registration);
                }
            }
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Collection<Registration> findRegistrationsByDate(LocalDate date) {
        LinkedList<Registration> registrations = null;
        try {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statment = connection.prepareStatement(
                        "SELECT * FROM registrations WHERE date=?");
                statment.setDate(1, java.sql.Date.valueOf(date));
                ResultSet result = statment.executeQuery();
                while (result.next()) {
                    Registration registration = new Registration();
                    registration.setDate(result.getDate("date").toLocalDate());
                    registration.setId(result.getInt("id"));

                    HashMap<Tariff, Double> amounts = getAmounts(connection, registration.getId());
                    registration.setAmounts(amounts);
                    registrations.add(registration);
                }
            }
        } catch (SQLException e) {
            return null;
        }
        return registrations;
    }


    @Override
    public Collection<Registration> findRegistrationsByFlat(Flat flat) {
        LinkedList<Registration> registrations = null;
        try {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statment = connection.prepareStatement(
                        "SELECT * FROM registrations WHERE flats_id=?");
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
            }
        } catch (SQLException e) {
            return null;
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

    private int getFlatId(Connection connection, int registrationId) throws SQLException {
        PreparedStatement statment = connection.prepareStatement(
                "SELECT flats.id as id FROM flats WHERE id=?"); //ХЗ
        statment.setInt(1, registrationId);
        ResultSet flatsResult = statment.executeQuery();
        if (flatsResult.first())
            return flatsResult.getInt("id");
        else
            return -1;
    }

    private HashMap<String, Tariff> findTariffs() {
        HashMap<String, Tariff> tariffs = new HashMap<>();
        try {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statment = connection.prepareStatement("SELECT * FROM tarrifs");
                ResultSet result = statment.executeQuery();
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

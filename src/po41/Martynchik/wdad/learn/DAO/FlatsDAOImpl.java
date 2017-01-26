package po41.Martynchik.wdad.learn.DAO;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import po41.Martynchik.wdad.learn.DAO.*;
import po41.Martynchik.wdad.data.storage.DataSourceFactory;

public class FlatsDAOImpl implements FlatsDAO {
    private DataSource dataSource;

    public FlatsDAOImpl() {
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
    public boolean insertFlat(Flat flat) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statment = connection.prepareStatement(
                        "INSERT INTO flats (number,buildings_id) VALUES(?,?)");
                statment.setInt(1, flat.getId());
                statment.setInt(2, flat.getBuilding().getId());
                statment.executeUpdate();

                for (Registration registration : flat.getRegistrations()) {
                    statment = connection.prepareStatement(
                            "INSERT INTO registrations (date, flats_id) VALUES(?, ?)");
                    statment.setDate(1, java.sql.Date.valueOf(registration.getDate()));
                    statment.setInt(2, flat.getId());

                    for (Map.Entry<Tariff, Double> amount : registration.getAmounts().entrySet()) {
                        statment = connection.prepareStatement(
                                "INSERT INTO registrations_tariffs (amount, registrations_id,tariffs_name) VALUES(?,?,?)");
                        statment.setDouble(1, amount.getValue());
                        statment.setInt(2, registration.getId());
                        statment.setString(3, amount.getKey().getName());
                    }
                }
            }
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteFlat(Flat flat) {
//        try{
//            try (Connection connection = dataSource.getConnection()) {
//                PreparedStatement statment = connection.prepareStatement(
//                        "DELETE FROM registrations-tarrifs WHERE registrations_id='?'");
//                statment.setInt(1,registration.getId());
//                statment.executeUpdate();
//
//                statment = connection.prepareStatement(
//                        "DELETE FROM registrations WHERE id='?'");
//                statment.setInt(1,registration.getId());
//                statment.executeUpdate();
//            }
//        }catch (SQLException e){
//            return false;
//        }
        return true;
    }

    @Override
    public Flat findFlat(int id) {
        Flat flat = null;
        try {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM flats WHERE id =?");
                statement.setInt(1, id);
                ResultSet result = statement.executeQuery();

                if (result.first()) {
                    flat = new Flat();
                    flat.setId(id);
                    flat.setNumber(result.getInt("number"));
                    flat.setRegistrations(getRegistration(connection, flat));
                }
            }
        } catch (SQLException e) {
            return null;
        }
        return flat;
    }

    @Override
    public boolean updateFlat(Flat flat) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statment = connection.prepareStatement(
                        "UPDATE flats SET number=?,buildings_id=? WHERE id=?");
                statment.setDouble(1, flat.getNumber());
                statment.setInt(2, flat.getBuilding().getId());
                statment.setInt(3, flat.getId());
                statment.executeUpdate();

                for (Registration registration : flat.getRegistrations()) {
                    statment = connection.prepareStatement("UPDATE registrations SET date=? WHERE id=?");
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
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean saveOrUpdateFlat(Flat flat) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statment = connection.prepareStatement("SELECT * FROM flats WHERE id=?");
                boolean result = statment.execute();
                if (!result) {
                    return updateFlat(flat);
                } else {
                    return insertFlat(flat);
                }
            }
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Collection<Flat> findFlatsByLastRegistrationDate(LocalDate regDate) {
        throw new UnsupportedOperationException("Not supported yet.");
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

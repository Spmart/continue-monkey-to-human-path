/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package po41.Martynchik.wdad.learn.DAO;

import po41.Martynchik.wdad.data.storage.DataSourceFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * @author 000
 */
public class RegistrationsDAOImpl implements RegistrationsDAO {
    private DataSource dataSource = null;

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

    public boolean insertRegistration(Registration registration) {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            StringBuilder addRegistration = new StringBuilder("INSERT INTO registrations(id, date, flats_id) VALUES('" + registration.getId() + "','" +
                    registration.getDate().getYear() + "-" + registration.getDate().getMonth() + "-"
                    + registration.getDate().getDate() + "','" + registration.getFlat().getId() + "')");
            statement.executeUpdate(addRegistration.toString());
            if (registration.getAmounts() != null)
                for (Map.Entry<Tariff, Double> amount : registration.getAmounts().entrySet()) {
                    StringBuilder addRegistrationTariffs = new StringBuilder("INSERT INTO `registrations-tariffs` "
                            + "(id, amount, registrations_id,tariffs_name) VALUES(NULL,'" + amount.getValue() + "','" + registration.getId() + "','"
                            + amount.getKey().getName() + "')");
                    statement.executeUpdate(addRegistrationTariffs.toString());
                }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean deleteRegistration(Registration registration) {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            StringBuilder deleteRegistrationTariffs = new StringBuilder(" DELETE FROM `registrations-tariffs`"
                    + " WHERE registrations_id='" + registration.getId() + "'");
            statement.executeUpdate(deleteRegistrationTariffs.toString());
            StringBuilder deleteRegistration = new StringBuilder("DELETE FROM registrations WHERE id='"
                    + registration.getId() + "'");
            statement.executeUpdate(deleteRegistration.toString());

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public Registration findRegistration(int id) {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            StringBuilder findRegistration = new StringBuilder("SELECT * FROM registrations WHERE id='" + id + "'");
            ResultSet result = statement.executeQuery(findRegistration.toString());
            result.next();
            FlatsDAOImpl flat = new FlatsDAOImpl();
            Registration registration = new Registration();
            String date = result.getDate("date").toString();
            String[] data = date.split("-");
            Date regDate = new Date();
            regDate.setYear(Integer.parseInt(data[0]));
            regDate.setMonth(Integer.parseInt(data[1]));
            regDate.setDate(Integer.parseInt(data[2]));
            registration.setDate(regDate);
            registration.setFlat(flat.findFlat(result.getInt("flats_id")));
            registration.setId(result.getInt("id"));
            registration.setAmounts(getAmounts(connection, id));
            return registration;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateRegistration(Registration registration) {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            int year = registration.getDate().getYear();
            int month = registration.getDate().getMonth();
            int date = registration.getDate().getDate();
            StringBuilder updateRegistration = new StringBuilder("UPDATE registrations SET date='" + year
                    + "-" + month + "-" + date
                    + "', flats_id='" + registration.getFlat().getId() + "' WHERE id='" + registration.getId() + "'");
            statement.executeUpdate(updateRegistration.toString());
            if (registration.getAmounts() != null)
                for (Map.Entry<Tariff, Double> amount : registration.getAmounts().entrySet()) {
                    StringBuilder updateRegistrationTariffs = new StringBuilder("UPDATE `registrations-tariffs` SET amount='" + amount.getValue()
                            + "', tariffs_name='" + amount.getKey().getName() + "' WHERE registrations_id='" + registration.getId() + "'");
                    statement.executeUpdate(updateRegistrationTariffs.toString());
                }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean saveOrUpdateRegistration(Registration registration) {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            StringBuilder findRegistration = new StringBuilder("SELECT * FROM registrations WHERE id='" + registration.getId() + "'");
            ResultSet result = statement.executeQuery(findRegistration.toString());
            if (result.next()) {
                return updateRegistration(registration);
            } else {
                return insertRegistration(registration);
            }

        } catch (SQLException e) {
            return false;
        }
    }

    public Collection<Registration> findRegistrationsByDate(Date date) {
        Collection<Registration> registrations = new LinkedList();
        FlatsDAOImpl flat = new FlatsDAOImpl();
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            StringBuilder findRegistration = new StringBuilder("SELECT * FROM registrations WHERE date='" + date.getYear() + "-" + date.getMonth()
                    + "-" + date.getDate() + "'");
            ResultSet result = statement.executeQuery(findRegistration.toString());
            while (result.next()) {
                Registration registration = new Registration();
                registration.setId(result.getInt("id"));
                registration.setDate(date);
                registration.setFlat(flat.findFlat(result.getInt("flats_id")));
                registration.setAmounts(getAmounts(connection, registration.getId()));
                registrations.add(registration);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return registrations;
    }

    public Collection<Registration> findRegistrationsByFlat(Flat flat) {
        Collection<Registration> registrations = new LinkedList();
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            StringBuilder findRegistration = new StringBuilder("SELECT * FROM registrations WHERE flats_id='" + flat.getId() + "'");
            ResultSet result = statement.executeQuery(findRegistration.toString());
            while (result.next()) {
                String date = result.getDate("date").toString();
                String[] data = date.split("-");
                Date regDate = new Date();
                regDate.setYear(Integer.parseInt(data[0]));
                regDate.setMonth(Integer.parseInt(data[1]));
                regDate.setDate(Integer.parseInt(data[2]));
                Registration registration = new Registration();
                registration.setId(result.getInt("id"));
                registration.setDate(regDate);
                registration.setFlat(flat);
                registration.setAmounts(getAmounts(connection, registration.getId()));
                registrations.add(registration);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
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
        return result.getDouble("cost");
    }
}

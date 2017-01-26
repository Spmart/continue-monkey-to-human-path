package po41.Martynchik.wdad.learn.DAO;

import org.xml.sax.SAXException;
import po41.Martynchik.wdad.data.storage.DataSourceFactory;
import java.io.IOException;
import java.util.Collection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;

public class TariffsDAOImpl implements TariffsDAO {
    private DataSource dataSource;

    public TariffsDAOImpl() {
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
    public boolean insertTariff(Tariff tariff) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statment = connection.prepareStatement(
                        "INSERT INTO tariffs (name, cost) VALUES(?, ?)");
                statment.setString(1, tariff.getName());
                statment.setDouble(2, tariff.getCost());
                statment.executeUpdate();
            }
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteTariff(Tariff tariff) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statment = connection.prepareStatement("DELETE FROM tarrifs WHERE name=?");
                statment.setString(1, tariff.getName());
                statment.executeUpdate();
            }
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public Tariff findTariff(String name) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statment = connection.prepareStatement("SELECT * FROM tarrifs WHERE name=?");
                statment.setString(1, name);
                ResultSet result = statment.executeQuery();
                if (result.first()) {
                    result.next();
                    Tariff tariff = new Tariff();
                    tariff.setName(result.getString("name"));
                    tariff.setCost(result.getDouble("cost"));
                    return tariff;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public boolean updateTariff(Tariff tariff) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statment = connection.prepareStatement(
                        "UPDATE tariffs SET cost=? WHERE name=?");
                statment.setDouble(1, tariff.getCost());
                statment.setString(2, tariff.getName());
                statment.executeUpdate();
            }
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean saveOrUpdateTariff(Tariff tariff) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statment = connection.prepareStatement(
                        "SELECT * FROM tarrifs WHERE name=?");
                statment.setString(1, tariff.getName());
                boolean result = statment.execute();
                if (!result) {
                    return updateTariff(tariff);
                } else {
                    return insertTariff(tariff);
                }
            }
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Collection<Tariff> findTariffs() {
        Collection<Tariff> tarrifs = new LinkedList<>();
        try {
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statment = connection.prepareStatement(
                        "SELECT * FROM tarrifs");
                ResultSet result = statment.executeQuery();
                while (result.next()) {
                    Tariff tarrif = new Tariff();
                    tarrif.setName(result.getString("name"));
                    tarrif.setCost(result.getDouble("cost"));
                    tarrifs.add(tarrif);
                }
            }
        } catch (SQLException e) {
            return null;
        }
        return tarrifs;
    }
}
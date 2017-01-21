package po41.Martynchik.wdad.data.managers;

import po41.Martynchik.wdad.learn.rmi.Building;
import po41.Martynchik.wdad.learn.rmi.Client;
import po41.Martynchik.wdad.learn.rmi.Flat;
import po41.Martynchik.wdad.learn.rmi.Registration;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.*;

public class JDBCDataManager implements DataManager {

    private Connection connection;
    private Statement statement = null;
    private ResultSet resultSet = null;

    private DataSource dataSource;

    public JDBCDataManager(DataSource dataSource, Connection connection) {
        this.dataSource = dataSource;
        this.connection = connection;
    }

    private void execUpdate(String query) {
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null)
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (statement != null)
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }
    private int execQuery(String query) {
        String salaryAvg = "0";
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                salaryAvg = resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null)
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (statement != null)
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return BigDecimal.valueOf(Double.parseDouble(salaryAvg)).intValue();
    }

    @Override
    public double getBill(Building building, int flatNumber) throws RemoteException {
        return 0;
    }

    @Override
    public Flat getFlat(Building building, int flatNumber) throws RemoteException {
        return null;
    }

    @Override
    public void setTariff(String tariffName, int newValue) throws RemoteException {

    }

    @Override
    public void addRegistration(Building building, int flatNumber, Registration registrations) throws RemoteException {

    }
}

package po41.Martynchik.wdad.data.managers;

import org.xml.sax.SAXException;
import po41.Martynchik.wdad.data.storage.DataSourceFactory;
import po41.Martynchik.wdad.learn.rmi.Building;
import po41.Martynchik.wdad.learn.rmi.Client;
import po41.Martynchik.wdad.learn.rmi.Flat;

import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class TestJDBCDataManager {
    public static void main(String[] args)
            throws IOException, SAXException, ParserConfigurationException, SQLException {
        Connection connection = null;
        JDBCDataManager jdbcDataManager;

        try {
            DataSource dataSource = DataSourceFactory.createDataSource();
            connection = dataSource.getConnection();
            jdbcDataManager = new JDBCDataManager(dataSource, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try { if (connection != null) connection.close(); } catch (SQLException e) {}
        }
    }
}

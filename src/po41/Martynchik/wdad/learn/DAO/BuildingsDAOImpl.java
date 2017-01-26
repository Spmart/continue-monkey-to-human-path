package po41.Martynchik.wdad.learn.DAO;

import org.xml.sax.SAXException;
import po41.Martynchik.wdad.data.storage.DataSourceFactory;

import java.io.IOException;
import java.util.Collection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;

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
    public int insertBuilding(Building building) {
        return 0;
    }

    @Override
    public boolean deleteAuthor(Building building) {
        return false;
    }

    @Override
    public Building findBuilding(int id) {
        return null;
    }

    @Override
    public boolean updateBuilding(Building building) {
        return false;
    }

    @Override
    public boolean saveOrUpdateBuilding(Building building) {
        return false;
    }

    @Override
    public Collection<Building> findBuildings(String streetName, int number) {
        return null;
    }
}

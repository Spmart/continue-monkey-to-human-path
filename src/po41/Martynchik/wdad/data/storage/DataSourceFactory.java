package po41.Martynchik.wdad.data.storage;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.xml.sax.SAXException;
import po41.Martynchik.wdad.data.managers.PreferencesManager;
import po41.Martynchik.wdad.utils.PreferencesConstantManager;

import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class DataSourceFactory {
    private static PreferencesManager preferencesManager;

    public static javax.sql.DataSource createDataSource()
            throws IOException, SAXException, ParserConfigurationException, ClassNotFoundException {
        preferencesManager = PreferencesManager.getInstance();
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName(preferencesManager.getProperty(PreferencesConstantManager.HOST_NAME));
        dataSource.setPort(Integer.valueOf(preferencesManager.getProperty(PreferencesConstantManager.PORT)));
        dataSource.setDatabaseName(preferencesManager.getProperty(PreferencesConstantManager.DBNAME));
        dataSource.setUser(preferencesManager.getProperty(PreferencesConstantManager.USER));
        dataSource.setPassword(preferencesManager.getProperty(PreferencesConstantManager.PASSWORD));
        return dataSource;
    }

    public static javax.sql.DataSource createDataSource(String className, String driverType,
                                                        String host, int port, String dbName,
                                                        String user, String password)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {

        return ((DataSource) Class.forName(className).newInstance());
    }
}

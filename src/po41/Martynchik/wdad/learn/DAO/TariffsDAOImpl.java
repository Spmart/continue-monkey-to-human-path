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
import java.util.LinkedList;
import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class TariffsDAOImpl implements TariffsDAO{
    private DataSource dataSource = null;
    
    public TariffsDAOImpl(){
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
    public boolean insertTariff (Tariff tariff){
        try (Connection connection = dataSource.getConnection()) {
                Statement statement = connection.createStatement();
                StringBuilder insertTariff = new StringBuilder("INSERT INTO tariffs(name, cost) VALUES('"+tariff.getName()+
                        "','"+tariff.getCost()+"')");
                statement.executeUpdate(insertTariff.toString());
        }
         catch (SQLException e){
                 return false;
             }
        return true;
    }
    public boolean deleteTariff (Tariff tariff){
         try (Connection connection = dataSource.getConnection()) {
                Statement statement = connection.createStatement();
                StringBuilder deleteTariff = new StringBuilder("DELETE FROM tariffs WHERE name='"+tariff.getName()+"'");
                statement.executeUpdate(deleteTariff.toString());
        }
         catch (SQLException e){
                 return false;
             }
        return true;
    }
    public Tariff findTariff (String name){
        Tariff tariff = null;
         try (Connection connection = dataSource.getConnection()) {
                Statement statement = connection.createStatement();
                StringBuilder findTariff = new StringBuilder("SELECT * FROM tariff WHERE name='"+name+"'");
                ResultSet result = statement.executeQuery(findTariff.toString());
                if (result.next()){
                    tariff = new Tariff();
                    tariff.setName(result.getString("name"));
                    tariff.setCost(result.getDouble("cost"));
                }
        }
         catch (SQLException e){
                 return tariff;
             }
         return tariff;
    }
    public boolean updateTariff (Tariff tariff){
        try (Connection connection = dataSource.getConnection()) {
                Statement statement = connection.createStatement();
                StringBuilder updateTariff = new StringBuilder("UPDATE tariffs SET cost='"+tariff.getCost()+"' WHERE name='"+tariff.getName()+"'");
                statement.executeUpdate(updateTariff.toString());
        }
         catch (SQLException e){
                 return false;
             }
        return true;
    }
    public boolean saveOrUpdateGenre (Tariff tariff){
        try (Connection connection = dataSource.getConnection()) {
                Statement statement = connection.createStatement();
                StringBuilder findTariff = new StringBuilder("SELECT * FROM tariffs WHERE name='"+tariff.getName()+"'");
                ResultSet result = statement.executeQuery(findTariff.toString());
                if (result.next()){
                    return updateTariff(tariff);
                }
                else {
                    return insertTariff(tariff);
                }
        }
         catch (SQLException e){
                 return false;
             }
    }
    public Collection<Tariff> findTariffs (){
        Collection<Tariff> tariffs = new LinkedList();
        try (Connection connection = dataSource.getConnection()) {
                Statement statement = connection.createStatement();
                StringBuilder findTariff = new StringBuilder("SELECT * FROM tariffs");
                ResultSet result = statement.executeQuery(findTariff.toString());
                while (result.next()){
                    Tariff tariff = new Tariff();
                    tariff.setName(result.getString("name"));
                    tariff.setCost(result.getDouble("cost"));
                    tariffs.add(tariff);
                }
        }
         catch (SQLException e){
                 return tariffs;
             }
        return tariffs;
    }
    
}

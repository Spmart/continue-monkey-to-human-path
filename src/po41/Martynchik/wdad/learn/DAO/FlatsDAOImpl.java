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
import java.util.Set;
import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author 000
 */
public class FlatsDAOImpl implements FlatsDAO {
     private DataSource dataSource= null;
   
    public FlatsDAOImpl(){
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
    public boolean insertFlat (Flat flat){
        try (Connection connection = dataSource.getConnection()) {
                Statement statement = connection.createStatement();
                StringBuilder addFlat = new StringBuilder("INSERT INTO flats(id, number, buildings_id, persons_quantity, area) VALUES ('"+
                                flat.getId()+"','"+flat.getNumber()+"','"+flat.getBuilding().getId()+"','"
                        +flat.getPersonsQuantity()+"','"+flat.getArea()+"')");
                        statement.executeUpdate(addFlat.toString());
                        if (flat.getRegistrations()!= null)
                        for (Registration registrations: flat.getRegistrations()){
                            RegistrationsDAOImpl insertRegistration = new RegistrationsDAOImpl();
                            insertRegistration.saveOrUpdateRegistration(registrations);
                        }
        }
        catch (SQLException e){
            e.printStackTrace();
                 return false;
             }
        
        return true;
    }
    public boolean deleteFlat (Flat flat){
        try (Connection connection = dataSource.getConnection()) {
                Statement statement = connection.createStatement();
                if (flat.getRegistrations()!= null)
                 for (Registration registrations: flat.getRegistrations()){
                            RegistrationsDAOImpl deleteRegistration = new RegistrationsDAOImpl();
                            deleteRegistration.deleteRegistration(registrations);
                        }
                    StringBuilder query = new StringBuilder(" DELETE From flats Where id = '" + flat.getId()+
                            "'");
                    statement.executeUpdate(query.toString());
                   
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public Flat findFlat (int id){
         try (Connection connection = dataSource.getConnection()) {
                Statement statement = connection.createStatement();
                    StringBuilder query = new StringBuilder("SELECT * FROM flats WHERE id='" + id +"'");
                    ResultSet result = statement.executeQuery(query.toString());
                    result.next();
                    
                    BuildingsDAOImpl building = new BuildingsDAOImpl();
                    Flat flat = new Flat();
                    flat.setArea(result.getDouble("area"));
                    flat.setBuilding(building.findBuilding(result.getInt("buildings_id")));
                    flat.setId(id);
                    flat.setNumber(result.getInt("number"));
                    flat.setPersonsQuantity(result.getInt("persons_quantity"));
                    flat.setRegistrations(getRegistrations(connection, flat));
                     
                    return flat;
        }
        catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        
    }
    public boolean updateFlat (Flat flat){
        try (Connection connection = dataSource.getConnection()) {
                Statement statement = connection.createStatement();
                StringBuilder updateBuilding = new StringBuilder("UPDATE flats SET number='"+flat.getNumber()+"', buildings_id='"
                        +flat.getBuilding().getId()+"', persons_quantity='"+flat.getPersonsQuantity()+"', area='"+flat.getArea()
                        +"' WHERE id='"+flat.getId()+"'");
                statement.executeUpdate(updateBuilding.toString());
                if (flat.getRegistrations()!= null)
                for (Registration registrations: flat.getRegistrations()){
                    RegistrationsDAOImpl update = new RegistrationsDAOImpl();
                    update.saveOrUpdateRegistration(registrations);
                }
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
       return true;
    }
    public boolean saveOrUpdateFlat (Flat flat){
         try (Connection connection = dataSource.getConnection()) {
                Statement statement = connection.createStatement();
                StringBuilder flats = new StringBuilder("SELECT id FROM flats WHERE id='"+flat.getId()+"'");
                ResultSet result = statement.executeQuery(flats.toString());
                if(result.next()){
                    return updateFlat(flat);
                }
                else {
                    return insertFlat(flat);
                }
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public Collection<Flat> findFlatsByLastRegistrationDate (Date regDate){
        Collection<Flat> flats = new LinkedList();
        try (Connection connection = dataSource.getConnection()) {
                Statement statement = connection.createStatement();
                StringBuilder flatsByLastRegistration = new StringBuilder("SELECT * FROM registrations WHERE date='"+regDate.getYear()+"-"+
                        regDate.getMonth()+"-"+regDate.getDate()+"'");
                ResultSet result = statement.executeQuery(flatsByLastRegistration.toString());
                while (result.next()){
                    Flat flat = findFlat(result.getInt("flats_id"));
                    flats.add(flat);
                }
                
        }
        catch (SQLException e){
            e.printStackTrace();
           return null;
        }
        return flats;
    }
    
    public Set<Registration> getRegistrations(Connection connection, Flat flat) throws SQLException{
        Statement statement = connection.createStatement();
        Set<Registration> registrations = null;
        StringBuilder getRegistrations = new StringBuilder("SELECT * FROM registratons WHERE flats_id='"+flat.getId()+"'");
        ResultSet result = statement.executeQuery(getRegistrations.toString());
        while(result.next()){
            String date = result.getDate("date").toString();
                String[] data = date.split("-");
                Date regDate = new Date();
                regDate.setYear(Integer.parseInt(data[0]));
                regDate.setMonth(Integer.parseInt(data[1]));
                regDate.setDate(Integer.parseInt(data[2]));
            Registration registration = new Registration();
            registration.setDate(regDate);
            registration.setId(result.getInt("id"));
            registration.setFlat(flat);
            registration.setAmounts(getAmounts(connection, registration.getId()));
            registrations.add(registration);
        }
        return registrations;
    }
    
    public HashMap<Tariff, Double> getAmounts(Connection connection, int registrationId) throws SQLException{
        Statement statement = connection.createStatement();
        HashMap<Tariff, Double> amounts = new HashMap<>();
        StringBuilder getRegistrationTariff = new StringBuilder("SELECT * FROM `registrations-tariffs` WHERE registrations_id='"+registrationId+"'");
        ResultSet resultRegistrationTariff = statement.executeQuery(getRegistrationTariff.toString());
        while (resultRegistrationTariff.next()){
            Tariff tariff = new Tariff();
            tariff.setName(resultRegistrationTariff.getString("tariffs_name"));
            tariff.setCost(getCost(connection, tariff.getName()));
            amounts.put(tariff, resultRegistrationTariff.getDouble("amount"));
        }
        return amounts;
    }
    
    public Double getCost(Connection connection, String name) throws SQLException{
        Statement statement = connection.createStatement();
        StringBuilder getCost = new StringBuilder("SELECT cost FROM tariffs WHERE name='"+name+"'");
        ResultSet result = statement.executeQuery(getCost.toString());
        return result.getDouble("cost");
    }
}

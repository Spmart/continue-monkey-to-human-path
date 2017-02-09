/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package po41.Martynchik.wdad.learn.DAO;

import java.util.Collection;
import java.util.Date;

/**
 *
 * @author 000
 */
public interface RegistrationsDAO {
    public boolean insertRegistration (Registration
    registration);
    public boolean deleteRegistration (Registration
    registration);
    public Registration findRegistration (int id);
    public boolean updateRegistration (Registration
    registration);
    public boolean saveOrUpdateRegistration (Registration
    registration);
    public Collection<Registration>
    findRegistrationsByDate (Date date);
    public Collection<Registration>
    findRegistrationsByFlat (Flat flat);
}

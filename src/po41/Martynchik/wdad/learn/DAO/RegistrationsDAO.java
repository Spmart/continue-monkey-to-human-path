package po41.Martynchik.wdad.learn.DAO;

import java.time.LocalDate;
import java.util.Collection;

public interface RegistrationsDAO {
    public int insertRegistration (Registration registration);
    public boolean deleteRegistration (Registration registration);
    public Registration findRegistration (int id);
    public boolean updateRegistration (Registration registration);
    public boolean saveOrUpdateRegistration (Registration registration);
    public Collection<Registration> findRegistrationsByDate (LocalDate date);
    public Collection<Registration> findRegistrationsByFlat (Flat flat);
}

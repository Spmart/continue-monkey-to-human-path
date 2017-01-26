package po41.Martynchik.wdad.learn.DAO;

import java.time.LocalDate;
import java.util.Collection;

public class RegistrationsDAOImpl implements RegistrationsDAO {
    @Override
    public int insertRegistration(Registration registration) {
        return 0;
    }

    @Override
    public boolean deleteRegistration(Registration registration) {
        return false;
    }

    @Override
    public Registration findRegistration(int id) {
        return null;
    }

    @Override
    public boolean updateRegistration(Registration registration) {
        return false;
    }

    @Override
    public boolean saveOrUpdateRegistration(Registration registration) {
        return false;
    }

    @Override
    public Collection<Registration> findRegistrationsByDate(LocalDate date) {
        return null;
    }

    @Override
    public Collection<Registration> findRegistrationsByFlat(Flat flat) {
        return null;
    }
}

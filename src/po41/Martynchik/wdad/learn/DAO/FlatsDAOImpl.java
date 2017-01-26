package po41.Martynchik.wdad.learn.DAO;

import java.time.LocalDate;
import java.util.Collection;

public class FlatsDAOImpl implements FlatsDAO {
    @Override
    public int insertFlat(Flat flat) {
        return 0;
    }

    @Override
    public boolean deleteFlat(Flat flat) {
        return false;
    }

    @Override
    public Flat findFlat(int id) {
        return null;
    }

    @Override
    public boolean updateFlat(Flat flat) {
        return false;
    }

    @Override
    public boolean saveOrUpdateFlat(Flat flat) {
        return false;
    }

    @Override
    public Collection<Flat> findFlatsByLastRegistrationDate(LocalDate regDate) {
        return null;
    }
}

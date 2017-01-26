package po41.Martynchik.wdad.learn.DAO;

import java.time.LocalDate;
import java.util.Collection;

public interface FlatsDAO {
    public boolean insertFlat (Flat flat);
    public boolean deleteFlat (Flat flat);
    public Flat findFlat (int id);
    public boolean updateFlat (Flat flat);
    public boolean saveOrUpdateFlat (Flat flat);
    public Collection<Flat> findFlatsByLastRegistrationDate (LocalDate regDate);
}

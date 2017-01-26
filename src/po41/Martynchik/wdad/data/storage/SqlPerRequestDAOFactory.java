package po41.Martynchik.wdad.data.storage;

import po41.Martynchik.wdad.learn.DAO.*;

public class SqlPerRequestDAOFactory extends DAOFactory{

    @Override
    public BuildingsDAO getBuildingsDAO() {
        return new BuildingsDAOImpl();
    }

    @Override
    public FlatsDAO getFlatsDAO() {
        return new FlatsDAOImpl();
    }

    @Override
    public RegistrationsDAO getRegistrationsDAO() {
        return new RegistrationsDAOImpl();
    }

    @Override
    public TariffsDAO getTariffsDAO() {
        return new TariffsDAOImpl();
    }

}

package po41.Martynchik.wdad.data.storage;

import po41.Martynchik.wdad.learn.DAO.*;

public abstract class DAOFactory {
    private static DAOFactory instance;

    protected DAOFactory() {
    }

    public static DAOFactory getDaoFactory() {
        if (instance == null) {
            instance = new DAOFactory() {
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

            };
        }
        return instance;
    }

    public abstract BuildingsDAO getBuildingsDAO();

    public abstract FlatsDAO getFlatsDAO();

    public abstract RegistrationsDAO getRegistrationsDAO();

    public abstract TariffsDAO getTariffsDAO();
}


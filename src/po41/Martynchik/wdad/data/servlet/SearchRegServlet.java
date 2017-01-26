package po41.Martynchik.wdad.data.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.json.simple.*;
import po41.Martynchik.wdad.data.storage.DAOFactory;
import po41.Martynchik.wdad.learn.DAO.*;

    @WebServlet("/SearchReg")
    public class SearchRegServlet extends HttpServlet {

        @Override
        public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            resp.setContentType("application/json");
            PrintWriter pw = resp.getWriter();
            JSONArray jsonArray = null;

            DAOFactory daoFactory = DAOFactory.getDaoFactory();
            BuildingsDAO buildingDAO = daoFactory.getBuildingsDAO();
            FlatsDAO flatsDAO = daoFactory.getFlatsDAO();
            RegistrationsDAO registrationDAO = daoFactory.getRegistrationsDAO();

            Collection<Building> buildings = null;
            Building building = null;
            Flat flat = null;
            ArrayList<Registration> registrations = new ArrayList<>();

            if (req.getParameter("streetname") != null) {
                String name = req.getParameter("name");
                buildings = buildingDAO.findBuildings(name);
            }

            if (req.getParameter("buildingnumber") != null) {
                int buildingNumber = Integer.valueOf(req.getParameter("buildingnumber"));
                if (buildings != null) {
                    for (Building b : buildings) {
                        if (buildingNumber == b.getNumber()) {
                            building = b;
                            break;
                        }
                    }
                }
            }

            if (req.getParameter("flatnumber") != null) {
                int flatNumber = Integer.valueOf(req.getParameter("flatnumber"));
                if (building != null) {
                    for (Flat f : building.getFlats()) {
                        if (flatNumber == f.getNumber()) {
                            flat = f;
                            break;
                        }
                    }
                }
            }

            if (req.getParameter("afterDate") != null) {
                Date dateReg = Date.valueOf(req.getParameter("afterDate"));
                ArrayList<Registration> result = new ArrayList<Registration>();
                if (flat != null) {
                    for (Registration regestration : flat.getRegistrations()) {
                        if (regestration.getDate().isAfter(dateReg.toLocalDate())) {
                            result.add(regestration);
                        }
                    }
                    registrations = result;
                }
            } else {
                registrations = new ArrayList<Registration>(flat.getRegistrations());
            }
            jsonArray.addAll(registrations); //Передаем любую коллекцию
            pw.write(jsonArray.toString());
        }
    }

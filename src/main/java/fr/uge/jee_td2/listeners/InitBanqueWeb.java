package fr.uge.jee_td2.listeners;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class InitBanqueWeb implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext application = sce.getServletContext();

        String nomDS = application.getInitParameter("banqueDataSource");

        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup(nomDS);

            application.setAttribute("banqueDataSource", ds);

            System.out.println("DataSource stocké dans le contexte application !");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
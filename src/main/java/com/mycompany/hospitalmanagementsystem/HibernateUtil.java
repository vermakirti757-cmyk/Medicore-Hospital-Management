package com.mycompany.hospitalmanagementsystem;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory factory;
    static {
        try {
            factory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Doctor.class)
                    .addAnnotatedClass(Appointment.class)
                    .buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static SessionFactory getFactory() {
        return factory;
    }
}
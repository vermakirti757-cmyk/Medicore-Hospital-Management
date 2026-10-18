package com.mycompany.hospitalmanagementsystem;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Hospitalmanagementsystem extends JFrame {
    static SessionFactory factory;

    public Hospitalmanagementsystem() {
        // --- AUTO ADD LOGIC AAPKA WAHI RAHEGA ---
        Session s0 = factory.openSession();
        s0.beginTransaction();
        long dcount = (Long) s0.createQuery("select count(*) from Doctor").uniqueResult();
        if (dcount < 6) {
            s0.createQuery("delete from Doctor").executeUpdate();
            String[][] docs = {
                {"amit", "eye", "1pm"}, {"sunita", "heart", "10am"},
                {"raj", "ortho", "11am"}, {"kirti", "child", "9am"},
                {"aman", "general", "2pm"}, {"neha", "neuro", "3pm"}
            };
            for (String[] doc : docs) {
                Doctor d = new Doctor();
                d.setName(doc[0]); d.setSpecialty(doc[1]); d.setAvailableTime(doc[2]);
                s0.save(d);
            }
        }
        long pcount = (Long) s0.createQuery("select count(*) from Patient").uniqueResult();
        if (pcount < 6) {
            s0.createQuery("delete from Patient").executeUpdate();
            Object[][] pats = {
                {"rahul", 20, "male", "fever"}, {"priya", 22, "female", "cold"},
                {"arjun", 25, "male", "headache"}, {"simran", 19, "female", "cough"},
                {"vikas", 30, "male", "diabetes"}, {"pooja", 28, "female", "allergy"}
            };
            for (Object[] pt : pats) {
                Patient p = new Patient();
                p.setName((String)pt[0]); p.setAge((int)pt[1]); p.setGender((String)pt[2]); p.setDisease((String)pt[3]);
                s0.save(p);
            }
        }
        s0.getTransaction().commit();
        s0.close();

        // --- SWING MAIN MENU (Console ki jagah) ---
        setTitle("Hospital Management System - MediCore");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("HOSPITAL MANAGEMENT SYSTEM", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
        add(title, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(3, 2, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton b1 = new JButton("Add Doctor");
        JButton b2 = new JButton("View All Doctors");
        JButton b3 = new JButton("Add Patient");
        JButton b4 = new JButton("View All Patients");
        JButton b5 = new JButton("Book Appointment");
        JButton b6 = new JButton("View Appointments");

        panel.add(b1); panel.add(b2);
        panel.add(b3); panel.add(b4);
        panel.add(b5); panel.add(b6);
        add(panel, BorderLayout.CENTER);

        // Actions - Hibernate wala kaam ab button se hoga
        b1.addActionListener(e -> addDoctor());
        b2.addActionListener(e -> viewDoctors());
        b3.addActionListener(e -> addPatient());
        b4.addActionListener(e -> viewPatients());
        b5.addActionListener(e -> bookAppointment());
        b6.addActionListener(e -> viewAppointments());

        setVisible(true);
    }

    // --- AAPKE CONSOLE WALE FUNCTIONS AB SWING ME ---
    void addDoctor() {
        String name = JOptionPane.showInputDialog(this, "Enter Doctor Name:");
        if(name==null) return;
        String sp = JOptionPane.showInputDialog(this, "Enter Specialty:");
        String time = JOptionPane.showInputDialog(this, "Enter Available Time:");
        Session s = factory.openSession(); s.beginTransaction();
        Doctor d = new Doctor(); d.setName(name); d.setSpecialty(sp); d.setAvailableTime(time);
        s.save(d); s.getTransaction().commit(); s.close();
        JOptionPane.showMessageDialog(this, "Doctor Added Successfully!");
    }

    void viewDoctors() {
        Session s = factory.openSession();
        List<Doctor> list = s.createQuery("from Doctor", Doctor.class).list();
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID","Name","Specialty","Time"},0);
        for (Doctor d : list) model.addRow(new Object[]{d.getId(), d.getName(), d.getSpecialty(), d.getAvailableTime()});
        s.close();
        showTable("All Doctors", model);
    }

    void addPatient() {
        String name = JOptionPane.showInputDialog(this, "Enter Patient Name:");
        if(name==null) return;
        int age = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Age:"));
        String gender = JOptionPane.showInputDialog(this, "Enter Gender:");
        String disease = JOptionPane.showInputDialog(this, "Enter Disease:");
        Session s = factory.openSession(); s.beginTransaction();
        Patient p = new Patient(); p.setName(name); p.setAge(age); p.setGender(gender); p.setDisease(disease);
        s.save(p); s.getTransaction().commit(); s.close();
        JOptionPane.showMessageDialog(this, "Patient Added Successfully!");
    }

    void viewPatients() {
        Session s = factory.openSession();
        List<Patient> list = s.createQuery("from Patient", Patient.class).list();
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID","Name","Age","Gender","Disease"},0);
        for (Patient p : list) model.addRow(new Object[]{p.getId(), p.getName(), p.getAge(), p.getGender(), p.getDisease()});
        s.close();
        showTable("All Patients", model);
    }

    void bookAppointment() {
        String pn = JOptionPane.showInputDialog(this, "Enter Patient Name:");
        String dn = JOptionPane.showInputDialog(this, "Enter Doctor Name:");
        String date = JOptionPane.showInputDialog(this, "Enter Date (2026-05-14):");
        Session s = factory.openSession(); s.beginTransaction();
        Appointment a = new Appointment(); a.setPatientName(pn); a.setDoctorName(dn); a.setAppointmentDate(date); a.setStatus("Booked");
        s.save(a); s.getTransaction().commit(); s.close();
        JOptionPane.showMessageDialog(this, "Appointment Booked Successfully!");
    }

    void viewAppointments() {
        Session s = factory.openSession();
        List<Appointment> list = s.createQuery("from Appointment", Appointment.class).list();
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID","Patient","Doctor","Date","Status"},0);
        for (Appointment a : list) model.addRow(new Object[]{a.getId(), a.getPatientName(), a.getDoctorName(), a.getAppointmentDate(), a.getStatus()});
        s.close();
        showTable("All Appointments", model);
    }

    void showTable(String title, DefaultTableModel model) {
        JDialog d = new JDialog(this, title, true);
        d.setSize(700, 400); d.setLocationRelativeTo(this);
        d.add(new JScrollPane(new JTable(model)));
        d.setVisible(true);
    }

    public static void main(String[] args) {
        factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        SwingUtilities.invokeLater(() -> new Hospitalmanagementsystem());
    }
}
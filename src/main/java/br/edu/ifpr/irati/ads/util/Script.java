package br.edu.ifpr.irati.ads.util;

import br.edu.ifpr.irati.ads.dao.Dao;
import br.edu.ifpr.irati.ads.dao.GenericDAO;
import br.edu.ifpr.irati.ads.exception.PersistenceException;
import br.edu.ifpr.irati.ads.model.Espaco;
import br.edu.ifpr.irati.ads.model.Servidor;
import br.edu.ifpr.irati.ads.model.Vigilante;
import org.hibernate.Session;

public class Script {

    public static void main(String[] args) throws PersistenceException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        createLaboratios(session);
        createProfessores(session);
        createVigilantes(session);
    }

    public static void createVigilantes(Session session) {
        Dao<Vigilante> dao = new GenericDAO<>(Vigilante.class, session);

        Vigilante v1 = new Vigilante();
        v1.setNome("Zé da Villa");
        v1.setCpf("00011122233");

        Vigilante v2 = new Vigilante();
        v2.setNome("Zé da Goiaba");
        v2.setCpf("99988877744");

        try {
            dao.salvar(v1);
            dao.salvar(v2);
        } catch (PersistenceException ex) {
            ex.printStackTrace();
        }
    }

    public static void createProfessores(Session session) {
        Dao<Servidor> dao = new GenericDAO<>(Servidor.class, session);

        Servidor p1 = new Servidor();
        p1.setNome("Valter");
        p1.setSiape("A1520");
        Servidor p2 = new Servidor();
        p2.setNome("Hernani");
        p2.setSiape("Y56FA");
        Servidor p3 = new Servidor();
        p3.setNome("Thalita");
        p3.setSiape("BV514");

        try {
            dao.salvar(p1);
            dao.salvar(p2);
            dao.salvar(p3);
        } catch (PersistenceException ex) {
            ex.printStackTrace();
        }
    }

    public static void createLaboratios(Session session) {
        Dao<Espaco> labDAO = new GenericDAO<>(Espaco.class, session);

        Espaco lab01 = new Espaco();
        lab01.setNome("Sala 01");
        Espaco lab02 = new Espaco();
        lab02.setNome("Sala 02");
        Espaco lab03 = new Espaco();
        lab03.setNome("Sala 03");
        Espaco lab04 = new Espaco();
        lab04.setNome("Sala 04");

        try {
            labDAO.salvar(lab01);
            labDAO.salvar(lab02);
            labDAO.salvar(lab03);
            labDAO.salvar(lab04);
        } catch (PersistenceException ex) {
            ex.printStackTrace();
        }
    }
}

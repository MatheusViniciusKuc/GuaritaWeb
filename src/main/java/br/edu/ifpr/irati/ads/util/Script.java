package br.edu.ifpr.irati.ads.util;

import br.edu.ifpr.irati.ads.dao.Dao;
import br.edu.ifpr.irati.ads.dao.GenericDAO;
import br.edu.ifpr.irati.ads.exception.PersistenceException;
import br.edu.ifpr.irati.ads.model.DadosPessoais;
import br.edu.ifpr.irati.ads.model.Endereco;
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
        DadosPessoais dp = new DadosPessoais();
        dp.setCpf("00011122233");
        dp.setNome("Zé da Villa");
        dp.setEmail("zedavilla@gmail.com");
        dp.setTelefone("42999859898");
        Endereco e = new Endereco();
        e.setUf("PR");
        e.setCidade("Irati");
        e.setBairro("Rio");
        e.setCep("84500000");
        e.setNumero("416");
        e.setRua("KIa");
        dp.setEndereco(e);
        v1.setDadosPessoais(dp);

        Vigilante v2 = new Vigilante();
        DadosPessoais dp2 = new DadosPessoais();
        dp2.setCpf("99988877744");
        dp2.setNome("Zé da Goiaba");
        dp2.setEmail("zedavilla@gmail.com");
        dp2.setTelefone("42999859898");
        Endereco e2 = new Endereco();
        e2.setUf("PR");
        e2.setCidade("Irati");
        e2.setBairro("Rio");
        e2.setCep("84500000");
        e2.setNumero("416");
        e2.setRua("KIa");
        dp2.setEndereco(e2);
        v2.setDadosPessoais(dp2);

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
        p1.setSiape("1111111");
        DadosPessoais dp = new DadosPessoais();
        dp.setCpf("12345678911");
        dp.setNome("Kaio");
        dp.setEmail("Kaio@gmail.com");
        dp.setTelefone("42999859845");
        Endereco e = new Endereco();
        e.setUf("PR");
        e.setCidade("Operador");
        e.setBairro("Rio");
        e.setCep("84500000");
        e.setNumero("235");
        e.setRua("KIa");
        dp.setEndereco(e);
        p1.setDadosPessoais(dp);
        
        
        Servidor p2 = new Servidor();
        p2.setSiape("2222222");
        DadosPessoais dp2 = new DadosPessoais();
        dp2.setCpf("98758926562");
        dp2.setNome("Poliana");
        dp2.setEmail("Poliana@gmail.com");
        dp2.setTelefone("42999859831");
        Endereco e2 = new Endereco();
        e2.setUf("PR");
        e2.setCidade("Operador");
        e2.setBairro("Rio");
        e2.setCep("84500000");
        e2.setNumero("2021");
        e2.setRua("KIa");
        dp2.setEndereco(e2);
        p2.setDadosPessoais(dp2);

        try {
            dao.salvar(p1);
            dao.salvar(p2);
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

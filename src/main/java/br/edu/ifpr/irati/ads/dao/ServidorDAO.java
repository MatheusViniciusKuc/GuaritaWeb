package br.edu.ifpr.irati.ads.dao;

import br.edu.ifpr.irati.ads.exception.PersistenceException;
import br.edu.ifpr.irati.ads.model.Servidor;
import jakarta.persistence.Query;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class ServidorDAO extends GenericDAO<Servidor> {

    public ServidorDAO(Session session) {
        super(Servidor.class, session);
    }

    public Servidor buscarPorSIAPE(String siape) throws PersistenceException {
        try {
            String hql = "FROM Servidor WHERE siape = :siape";
            Query query = session.createQuery(hql);
            query.setParameter("siape", siape);
            Servidor s = (Servidor) query.getSingleResult();
            return s;
        } catch (HibernateException | NullPointerException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    public Servidor buscarPorCPF(String cpf) throws PersistenceException {
        try {
            String hql = "FROM Servidor WHERE SUBSTRING(cpf, 1, 6) = :cpf";
            Query query = session.createQuery(hql);
            query.setParameter("cpf", cpf);
            Servidor s = (Servidor) query.getSingleResult();
            return s;
        } catch (HibernateException | NullPointerException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

}

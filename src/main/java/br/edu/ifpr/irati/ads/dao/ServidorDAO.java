package br.edu.ifpr.irati.ads.dao;

import br.edu.ifpr.irati.ads.exception.PersistenceException;
import br.edu.ifpr.irati.ads.model.Servidor;
import jakarta.persistence.Query;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class ServidorDAO extends GenericDAO<Servidor> {

    public ServidorDAO(Session session) {
        super(Servidor.class, session);
    }

    public Servidor buscarPorSIAPE(String siape) throws PersistenceException {
        try {
            String hql = "FROM Servidor WHERE siape = :siape "
                    + "AND excluido = :excluido";
            Query query = session.createQuery(hql);
            query.setParameter("siape", siape);
            query.setParameter("excluido", false);

            return (Servidor) query.getSingleResult();
        } catch (HibernateException | NullPointerException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    public Servidor buscarPorCPF(String cpf) throws PersistenceException {
        try {
            String hql = "FROM Servidor s WHERE SUBSTRING(s.dadosPessoais.cpf, 1, 6) = :cpf AND s.excluido = :excluido";

            Query query = session.createQuery(hql);
            query.setParameter("cpf", cpf);
            query.setParameter("excluido", false);

            return (Servidor) query.getSingleResult();
        } catch (HibernateException | NullPointerException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    public List<Servidor> buscarTodosAtivos() throws PersistenceException {
        try {
            String hql = "FROM Servidor WHERE excluido = :excluido";
            Query query = session.createQuery(hql);
            query.setParameter("excluido", false);

            return query.getResultList();
        } catch (HibernateException | NullPointerException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

}

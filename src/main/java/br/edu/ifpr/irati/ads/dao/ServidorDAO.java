package br.edu.ifpr.irati.ads.dao;

import br.edu.ifpr.irati.ads.exception.PersistenceException;
import br.edu.ifpr.irati.ads.model.Servidor;
import jakarta.persistence.NoResultException;
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
        } catch (HibernateException e) {
            throw new PersistenceException(e.getMessage());
        } catch (NullPointerException npe) {
            return null;
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

    public Servidor existeCPF(String cpf) throws PersistenceException {
        try {
            String hql = "SELECT s FROM Servidor s "
                    + "WHERE s.dadosPessoais.cpf = :cpf";

            return (Servidor) session.createQuery(hql)
                    .setParameter("cpf", cpf)
                    .uniqueResult();
        } catch (NoResultException nre) {
            return null;
        } catch (HibernateException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    public Servidor existeSiape(String siape) throws PersistenceException {
        try {
            String hql = "SELECT s FROM Servidor s "
                    + "WHERE s.siape = :siape";

            return (Servidor) session.createQuery(hql)
                    .setParameter("siape", siape)
                    .uniqueResult();
        } catch (NoResultException nre) {
            return null;
        } catch (HibernateException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    public Servidor existeEmail(String email) throws PersistenceException {
        try {
            String hql = "SELECT s FROM Servidor s "
                    + "WHERE s.dadosPessoais.email = :email";

            return (Servidor) session.createQuery(hql)
                    .setParameter("email", email)
                    .uniqueResult();
        } catch (NoResultException nre) {
            return null;
        } catch (HibernateException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

}

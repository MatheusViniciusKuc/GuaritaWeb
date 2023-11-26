package br.edu.ifpr.irati.ads.dao;

import br.edu.ifpr.irati.ads.exception.PersistenceException;
import br.edu.ifpr.irati.ads.model.Vigilante;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class VigilanteDAO extends GenericDAO<Vigilante> {

    public VigilanteDAO(Session session) {
        super(Vigilante.class, session);
    }

    public Vigilante buscarPorCPF(String cpf) throws PersistenceException {
        try {
            String hql = "FROM Vigilante v WHERE SUBSTRING(v.dadosPessoais.cpf, 1, 6) = :cpf AND v.excluido = :excluido";

            Query query = session.createQuery(hql);
            query.setParameter("cpf", cpf);
            query.setParameter("excluido", false);

            return (Vigilante) query.getSingleResult();
        } catch (HibernateException | NullPointerException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    public List<Vigilante> buscarTodosAtivos() throws PersistenceException {
        try {
            String hql = "FROM Vigilante WHERE excluido = :excluido";
            Query query = session.createQuery(hql);
            query.setParameter("excluido", false);

            return query.getResultList();
        } catch (HibernateException | NullPointerException e) {
            throw new PersistenceException(e.getMessage());
        }
    }
    
    public Vigilante existeCpf(String cpf) throws PersistenceException {
        try {
            String hql = "SELECT v FROM Vigilante v "
                    + "WHERE v.dadosPessoais.cpf = :cpf";

            return (Vigilante) session.createQuery(hql)
                    .setParameter("cpf", cpf)
                    .uniqueResult();
        } catch (NoResultException nre) {
            return null;
        } catch (HibernateException e) {
            throw new PersistenceException(e.getMessage());
        }
    }
    
    public Vigilante existeEmail(String email) throws PersistenceException {
        try {
            String hql = "SELECT v FROM Vigilante v "
                    + "WHERE v.dadosPessoais.email = :email";

            return (Vigilante) session.createQuery(hql)
                    .setParameter("email", email)
                    .uniqueResult();
        } catch (NoResultException nre) {
            return null;
        } catch (HibernateException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

}

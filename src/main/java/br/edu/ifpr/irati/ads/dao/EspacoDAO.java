package br.edu.ifpr.irati.ads.dao;

import br.edu.ifpr.irati.ads.exception.PersistenceException;
import br.edu.ifpr.irati.ads.model.Espaco;
import jakarta.persistence.Query;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class EspacoDAO extends GenericDAO<Espaco> {

    public EspacoDAO(Session session) {
        super(Espaco.class, session);
    }

    public List<Espaco> buscarEspacosDisponiveis() throws PersistenceException {
        try {
            String hql = "FROM Espaco WHERE disponivelEmprestimo = :disponivel";
            Query query = session.createQuery(hql);
            query.setParameter("disponivel", true);
            return query.getResultList();
        } catch (HibernateException | NullPointerException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

}

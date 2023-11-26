package br.edu.ifpr.irati.ads.dao;

import br.edu.ifpr.irati.ads.exception.PersistenceException;
import java.io.Serializable;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class GenericDAO<T> implements Dao<T>, Serializable {

    private final Class classePersistente;
    Session session;

    public GenericDAO(Class classePersistente, Session session) {
        this.classePersistente = classePersistente;
        this.session = session;
    }

    @Override
    public T buscarPorId(Serializable id) throws PersistenceException {
        T t = null;
        try {
            t = (T) session.find(classePersistente, id);
            return t;
        } catch (HibernateException | NullPointerException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void salvar(T t) throws PersistenceException {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(t);
            transaction.commit();
        } catch (HibernateException | NullPointerException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void alterar(T t) throws PersistenceException {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(t);
            transaction.commit();
        } catch (HibernateException | NullPointerException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public void excluir(T t) throws PersistenceException {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.remove(t);
            transaction.commit();
        } catch (HibernateException | NullPointerException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public List<T> buscarTodos() throws PersistenceException {
        try {
            String hql = "from " + this.classePersistente.getCanonicalName();
            Query query = session.createQuery(hql, this.classePersistente);
            List results = query.getResultList();
            return results;
        } catch (HibernateException | NullPointerException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

}

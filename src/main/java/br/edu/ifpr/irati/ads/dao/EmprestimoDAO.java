package br.edu.ifpr.irati.ads.dao;

import br.edu.ifpr.irati.ads.exception.PersistenceException;
import br.edu.ifpr.irati.ads.model.Emprestimo;
import br.edu.ifpr.irati.ads.model.Espaco;
import br.edu.ifpr.irati.ads.model.Status;
import br.edu.ifpr.irati.ads.util.Util;
import jakarta.persistence.Query;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class EmprestimoDAO extends GenericDAO<Emprestimo> {

    public EmprestimoDAO(Session session) {
        super(Emprestimo.class, session);
    }

    public boolean isDisponivelEmprestimo(Date dataInicio, Date dataFim,
            Espaco espaco) throws PersistenceException {
        try {
            String hql = "SELECT COUNT(*) FROM Emprestimo e "
                    + "WHERE ((:dataInicio < e.dataFim AND :dataFim > e.dataInicio) "
                    + "OR (:dataInicio <= e.dataInicio AND :dataFim >= e.dataFim)) "
                    + "AND e.espaco = :espaco "
                    + "AND e.status = :statusAgendado";

            Long count = (Long) session.createQuery(hql)
                    .setParameter("dataInicio", dataInicio)
                    .setParameter("dataFim", dataFim)
                    .setParameter("espaco", espaco)
                    .setParameter("statusAgendado", Status.AGENDADO)
                    .uniqueResult();

            return count == 0;
        } catch (HibernateException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    public List<Emprestimo> filtrarRelatorio(Date dateInicioFiltro, Date dateFinalFiltro)
            throws PersistenceException, ParseException {
        try {
            String hql = "FROM Emprestimo e "
                    + "WHERE 1 = 1";

            if (dateInicioFiltro != null) {
                hql += " AND e.dataInicio >= :dataInicio";
            }
            if (dateFinalFiltro != null) {
                hql += " AND e.dataFim <= :dataFim";
            }

            Query query = session.createQuery(hql);

            if (dateInicioFiltro != null) {
                dateInicioFiltro = Util.configurarDate(dateInicioFiltro, "01:00");
                query.setParameter("dataInicio", dateInicioFiltro);
            }
            if (dateFinalFiltro != null) {
                dateFinalFiltro = Util.configurarDate(dateFinalFiltro, "23:00");
                query.setParameter("dataFim", dateFinalFiltro);
            }

            return query.getResultList();
        } catch (HibernateException he) {
            throw new PersistenceException(he.getMessage());
        }
    }
}

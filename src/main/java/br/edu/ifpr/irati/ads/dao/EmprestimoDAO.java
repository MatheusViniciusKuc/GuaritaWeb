package br.edu.ifpr.irati.ads.dao;

import br.edu.ifpr.irati.ads.exception.PersistenceException;
import br.edu.ifpr.irati.ads.model.Emprestimo;
import br.edu.ifpr.irati.ads.model.Espaco;
import br.edu.ifpr.irati.ads.model.Status;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class EmprestimoDAO extends GenericDAO<Emprestimo> {

    public EmprestimoDAO(Session session) {
        super(Emprestimo.class, session);
    }

    public boolean isDisponivelParaEmprestimo(Date dataInicio, Date dataFim,
            Espaco espaco) throws PersistenceException {
        try {
            String hql = "SELECT COUNT(*) FROM Emprestimo e "
                    + "WHERE ((:dataInicio BETWEEN e.dataInicio AND e.dataFim) "
                    + "OR (:dataFim BETWEEN e.dataInicio AND e.dataFim)) "
                    + "AND e.espaco = :espaco "
                    + "AND e.status IN (:statusPermitidos)";

            List<Status> statusPermitidos = Arrays.asList(Status.AGENDADO, Status.OCORRENCIA);
            
            Long count = (Long) session.createQuery(hql)
                    .setParameter("dataInicio", dataInicio)
                    .setParameter("dataFim", dataFim)
                    .setParameter("espaco", espaco)
                    .setParameterList("statusPermitidos", statusPermitidos)
                    .uniqueResult();

            return count == 0;
        } catch (HibernateException e) {
            throw new PersistenceException(e.getMessage());
        }
    }
}

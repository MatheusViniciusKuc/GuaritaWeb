package br.edu.ifpr.irati.ads.mb.relatorios;

import br.edu.ifpr.irati.ads.dao.EmprestimoDAO;
import br.edu.ifpr.irati.ads.exception.PersistenceException;
import br.edu.ifpr.irati.ads.model.Emprestimo;
import br.edu.ifpr.irati.ads.util.HibernateUtil;
import br.edu.ifpr.irati.ads.util.Util;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.hibernate.Session;

@ManagedBean
@ViewScoped
public class RelatorioEmprestimoMB implements Serializable {

    private Session session;
    private EmprestimoDAO dao;
    private List<Emprestimo> emprestimos;
    private Boolean ordenacaoCrescente;
    private Date dateInicioFiltro;
    private Date dateFinalFiltro;

    public RelatorioEmprestimoMB() {
        this.ordenacaoCrescente = true;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            dao = new EmprestimoDAO(session);
            this.emprestimos = dao.buscarTodos();
        } catch (PersistenceException ex) {
            this.emprestimos = new ArrayList<>();
        }
    }

    public void filtrarRelatorio() {
        try {
            this.emprestimos = dao.filtrarRelatorio(dateInicioFiltro, dateFinalFiltro);
        } catch (ParseException pe) {
            Util.mensagemErro("Houve um problema ao converter a Data, "
                    + "se persistir entre em contato",
                    "filtro_relatorio_emprestimo_btnFiltrar");
        } catch (PersistenceException pe) {
            Util.mensagemErro("Não foi possível Filtrar",
                    "filtro_relatorio_emprestimo_btnFiltrar");
        }
    }

    public void sortServidor() {
        Comparator<Emprestimo> comparator = Comparator.comparing(emp
                -> emp.getServidor().getDadosPessoais().getNome());

        ordenar(comparator);
        ordenacaoCrescente = !ordenacaoCrescente;
    }

    public void sortEspaco() {
        Comparator<Emprestimo> comparator = Comparator.comparing(emp
                -> emp.getEspaco().getNome());

        ordenar(comparator);
        ordenacaoCrescente = !ordenacaoCrescente;
    }

    public void sortDataInicial() {
        Comparator<Emprestimo> comparator = Comparator.comparing(Emprestimo::getDataInicio);

        ordenar(comparator);
        ordenacaoCrescente = !ordenacaoCrescente;
    }

    public void sortDataFinal() {
        Comparator<Emprestimo> comparator = Comparator.comparing(Emprestimo::getDataFim);

        ordenar(comparator);
        ordenacaoCrescente = !ordenacaoCrescente;
    }

    public void sortStatus() {
        Comparator<Emprestimo> comparator = Comparator.comparing(Emprestimo::getStatus);

        ordenar(comparator);
        ordenacaoCrescente = !ordenacaoCrescente;
    }

    public void sortConcluido() {
        Comparator<Emprestimo> comparator = Comparator.comparing(Emprestimo::getConcluidoPor,
                Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));

        ordenar(comparator);
        ordenacaoCrescente = !ordenacaoCrescente;
    }

    private void ordenar(Comparator<Emprestimo> comparator) {
        if (ordenacaoCrescente) {
            Collections.sort(emprestimos, comparator);
            return;
        }

        Collections.sort(emprestimos, comparator.reversed());
    }

    public List<Emprestimo> getEmprestimos() {
        return emprestimos;
    }

    public Date getDateFinalFiltro() {
        return dateFinalFiltro;
    }

    public Date getDateInicioFiltro() {
        return dateInicioFiltro;
    }

    public void setDateFinalFiltro(Date dateFinalFiltro) {
        this.dateFinalFiltro = dateFinalFiltro;
    }

    public void setDateInicioFiltro(Date dateInicioFiltro) {
        this.dateInicioFiltro = dateInicioFiltro;
    }

}

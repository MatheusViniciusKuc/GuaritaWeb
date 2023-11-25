package br.edu.ifpr.irati.ads.mb.relatorios;

import br.edu.ifpr.irati.ads.dao.Dao;
import br.edu.ifpr.irati.ads.dao.EmprestimoDAO;
import br.edu.ifpr.irati.ads.dao.GenericDAO;
import br.edu.ifpr.irati.ads.exception.PersistenceException;
import br.edu.ifpr.irati.ads.model.Emprestimo;
import br.edu.ifpr.irati.ads.model.Espaco;
import br.edu.ifpr.irati.ads.model.Ocorrencia;
import br.edu.ifpr.irati.ads.util.HibernateUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.hibernate.Session;

@ManagedBean
@ViewScoped
public class RelatorioOcorrenciaMB implements Serializable {

    private Session session;
    private List<Ocorrencia> ocorrencias;
    private Boolean ordenacaoCrescente;

    public RelatorioOcorrenciaMB() {
        this.ordenacaoCrescente = false;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Dao<Ocorrencia> dao = new GenericDAO<>(Ocorrencia.class, session);
            this.ocorrencias = dao.buscarTodos();
        } catch (PersistenceException ex) {
            this.ocorrencias = new ArrayList<>();
        }
        sortDataRegistro();
    }

    public void sortEspaco() {
        Comparator<Ocorrencia> comparator = Comparator.comparing(oco
                -> oco.getEspaco().getNome());

        ordenar(comparator);
        ordenacaoCrescente = !ordenacaoCrescente;
    }

    public void sortMensagem() {
        Comparator<Ocorrencia> comparator = Comparator.comparing(Ocorrencia::getOcorrido);

        ordenar(comparator);
        ordenacaoCrescente = !ordenacaoCrescente;
    }

    public void sortDataRegistro() {
        Comparator<Ocorrencia> comparator = Comparator.comparing(Ocorrencia::getDataRegistrada);

        ordenar(comparator);
        ordenacaoCrescente = !ordenacaoCrescente;
    }

    public void sortRegistradaPor() {
        Comparator<Ocorrencia> comparator = Comparator.comparing(oco
                -> (oco.getServidor() != null)
                ? oco.getServidor().getDadosPessoais().getNome()
                : oco.getVigilante().getDadosPessoais().getNome()
        );

        ordenar(comparator);
        ordenacaoCrescente = !ordenacaoCrescente;
    }

    private void ordenar(Comparator<Ocorrencia> comparator) {
        if (ordenacaoCrescente) {
            Collections.sort(ocorrencias, comparator);
            return;
        }

        Collections.sort(ocorrencias, comparator.reversed());
    }

    public List<Ocorrencia> getOcorrencias() {
        return ocorrencias;
    }

}

package br.edu.ifpr.irati.ads.mb;

import br.edu.ifpr.irati.ads.dao.Dao;
import br.edu.ifpr.irati.ads.dao.EspacoDAO;
import br.edu.ifpr.irati.ads.dao.GenericDAO;
import br.edu.ifpr.irati.ads.exception.PersistenceException;
import br.edu.ifpr.irati.ads.model.Espaco;
import br.edu.ifpr.irati.ads.util.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.hibernate.Session;

@ManagedBean
@SessionScoped
public class ModelMB {

    private Session session;
    private Espaco espaco;
    private List<Espaco> espacos;
    private EspacoDAO espacoDAO;

    public ModelMB() {
        configurarConfiguracoesIniciais();
    }

    private void configurarConfiguracoesIniciais() {
        session = HibernateUtil.getSessionFactory().openSession();
        try {
            espaco = new Espaco();
            espacoDAO = new EspacoDAO(session);
            espacos = espacoDAO.buscarTodosAtivos();
        } catch (PersistenceException ex) {
            espacos = new ArrayList<>();
        }
    }

    public void salvarEspaco() {
        try {
            if (espaco.getId() == null || espaco.getId() == 0) {
                espacoDAO.salvar(espaco);
                this.espacos.add(espaco);
            } else {
                espacoDAO.alterar(espaco);
            }
            cancelarEspaco();
        } catch (PersistenceException ex) {
            ex.printStackTrace();
        }
    }

    public void cancelarEspaco() {
        this.espaco = new Espaco();
    }

    public void alterarEspaco(Espaco espaco) {
        this.espaco = espaco;
    }

    public void excluirEspaco(Espaco esp) {
        try {
            if (espaco.equals(esp)) {
                cancelarEspaco();
            }
            espacos.remove(esp);
            esp.excluir();
            espacoDAO.alterar(esp);
        } catch (PersistenceException ex) {
            ex.printStackTrace();
        }
    }

    public void visualizarOcorrencias(Espaco espaco) {

    }

    public Espaco getEspaco() {
        return espaco;
    }

    public List<Espaco> getEspacos() {
        return espacos;
    }

    public void setEspaco(Espaco espaco) {
        this.espaco = espaco;
    }

    public void setEspacos(List<Espaco> espacos) {
        this.espacos = espacos;
    }

}

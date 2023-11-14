package br.edu.ifpr.irati.ads.mb;

import br.edu.ifpr.irati.ads.dao.Dao;
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
    private Dao<Espaco> espacoDAO;

    public ModelMB() {
        configurarConfiguracoesIniciais();
    }

    private void configurarConfiguracoesIniciais() {
        session = HibernateUtil.getSessionFactory().openSession();
        try {
            espaco = new Espaco();
            espacoDAO = new GenericDAO<>(Espaco.class, session);
            espacos = espacoDAO.buscarTodos();
        } catch (PersistenceException ex) {
            espacos = new ArrayList<>();
        }
    }

    public void salvarEspaco() {
        try {
            espacoDAO.salvar(espaco);
            this.espacos.add(espaco);
            this.espaco = new Espaco();
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

    public void excluirEspaco(Espaco espaco) {

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

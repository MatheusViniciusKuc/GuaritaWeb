package br.edu.ifpr.irati.ads.mb;

import br.edu.ifpr.irati.ads.dao.ServidorDAO;
import br.edu.ifpr.irati.ads.exception.PersistenceException;
import br.edu.ifpr.irati.ads.model.Servidor;
import br.edu.ifpr.irati.ads.util.HibernateUtil;
import br.edu.ifpr.irati.ads.util.Util;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.hibernate.Session;

@ManagedBean
@SessionScoped
public class ServidorMB implements Serializable {

    private Session session;
    private ServidorDAO servidorDAO;
    private Servidor servidor;
    private List<Servidor> servidores;
    private Boolean exibirModalExcluir;

    public ServidorMB() {
        configurarConfiguracoesIniciais();
    }

    private void configurarConfiguracoesIniciais() {
        session = HibernateUtil.getSessionFactory().openSession();
        try {
            this.servidorDAO = new ServidorDAO(session);
            this.servidores = servidorDAO.buscarTodosAtivos();
        } catch (PersistenceException ex) {
            this.servidores = new ArrayList<>();
        }
        cancelarServidor();
    }

    public String cancelarServidor() {
        this.servidor = new Servidor();
        this.exibirModalExcluir = false;
        return "servidor.xhtml";
    }

    public void salvarServidor() {
        try {
            if (servidor.getId() == null || servidor.getId() == 0) {
                servidorDAO.salvar(servidor);
                this.servidores.add(servidor);
            } else {
                servidorDAO.alterar(servidor);
            }

            cancelarServidor();
        } catch (PersistenceException ex) {
            Util.mensagemErro("Não foi possível salvar.", "salvar_cad_serv");
        }
    }

    public String alterarServidor(Servidor serv) {
        this.servidor = serv;
        return "servidor.xhtml";
    }

    public String abrirModalExcluir(Servidor serv) {
        this.servidor = serv;
        this.exibirModalExcluir = true;
        return "servidor.xhtml";
    }

    public void excluirServidor() {
        try {
            this.servidores.remove(servidor);
            this.servidor.excluir();
            this.servidorDAO.alterar(servidor);
            cancelarServidor();
        } catch (PersistenceException ex) {
            Util.mensagemErro("Não foi possível excluir", "excluir_cad_serv");
        }
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }

    public List<Servidor> getServidores() {
        return servidores;
    }

    public Boolean getExibirModalExcluir() {
        return exibirModalExcluir;
    }

    public Servidor getServidor() {
        return servidor;
    }

}

package br.edu.ifpr.irati.ads.mb;

import br.edu.ifpr.irati.ads.dao.ServidorDAO;
import br.edu.ifpr.irati.ads.exception.PersistenceException;
import br.edu.ifpr.irati.ads.exception.ValidacaoCampoException;
import br.edu.ifpr.irati.ads.model.Servidor;
import br.edu.ifpr.irati.ads.util.HibernateUtil;
import br.edu.ifpr.irati.ads.util.Util;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.hibernate.Session;

@ManagedBean
@SessionScoped
public class ServidorMB {

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

    public void cancelarServidor() {
        this.servidor = new Servidor();
        this.exibirModalExcluir = false;
    }

    public void salvarServidor() {
        try {
            if (servidor.getDadosPessoais().getNome().isBlank()) {
                throw new ValidacaoCampoException("O Campo Nome precisa estar preenchido!");
            }
            if (servidor.getSiape().isBlank()) {
                throw new ValidacaoCampoException("O Campo SIAPE precisa estar preenchido!");
            }
            if (servidor.getDadosPessoais().getCpf().isBlank()) {
                throw new ValidacaoCampoException("O Campo CPF precisa estar preenchido!");
            }
            if (servidor.getDadosPessoais().getEmail().isBlank()) {
                throw new ValidacaoCampoException("O Campo E-mail precisa estar preenchido!");
            }

            if (servidor.getId() == null || servidor.getId() == 0) {
                servidorDAO.salvar(servidor);
                this.servidores.add(servidor);
            } else {
                servidorDAO.alterar(servidor);
            }

            cancelarServidor();
        } catch (PersistenceException ex) {
            Util.mensagemErro("Não foi possível salvar.", "nome_vigilante");
        } catch (ValidacaoCampoException vce) {
            Util.mensagemErro(vce.getMessage(), "nome_vigilante");
        }
    }

    public void alterarServidor(Servidor serv) {
        this.servidor = serv;
    }

    public void abrirModalExcluir(Servidor serv) {
        this.servidor = serv;
        this.exibirModalExcluir = true;
    }

    public void excluirServidor() {
        try {
            this.servidores.remove(servidor);
            this.servidor.excluir();
            this.servidorDAO.alterar(servidor);
        } catch (PersistenceException ex) {
            Util.mensagemErro("Não foi possível excluir", "nome_servidor");
        }
        cancelarServidor();
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

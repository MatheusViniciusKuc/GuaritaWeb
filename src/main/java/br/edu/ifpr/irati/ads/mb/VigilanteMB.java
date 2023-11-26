package br.edu.ifpr.irati.ads.mb;

import br.edu.ifpr.irati.ads.dao.VigilanteDAO;
import br.edu.ifpr.irati.ads.exception.PersistenceException;
import br.edu.ifpr.irati.ads.exception.ValidacaoCampoException;
import br.edu.ifpr.irati.ads.model.Vigilante;
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
public class VigilanteMB implements Serializable {

    private Session session;
    private VigilanteDAO vigilanteDAO;
    private Vigilante vigilante;
    private List<Vigilante> vigilantes;
    private Boolean exibirModalExcluir;
    private Boolean disableInputText;

    public VigilanteMB() {
        configurarConfiguracoesIniciais();
    }

    private void configurarConfiguracoesIniciais() {
        session = HibernateUtil.getSessionFactory().openSession();
        try {
            this.vigilanteDAO = new VigilanteDAO(session);
            this.vigilantes = vigilanteDAO.buscarTodosAtivos();
        } catch (PersistenceException ex) {
            this.vigilantes = new ArrayList<>();
        }
        cancelarVigilante();
    }

    public void salvarVigilante() {
        try {
            if (vigilante.getId() == null || vigilante.getId() == 0) {
                if (vigilanteDAO.existeCpf(vigilante.getDadosPessoais().getCpf()) != null) {
                    throw new ValidacaoCampoException("Esse CPF já está em uso.");
                }
                if (vigilanteDAO.existeEmail(vigilante.getDadosPessoais().getEmail()) != null) {
                    throw new ValidacaoCampoException("Esse E-mail já está em uso.");
                }
                vigilanteDAO.salvar(vigilante);
                this.vigilantes.add(vigilante);
            } else {
                vigilanteDAO.alterar(vigilante);
            }

            cancelarVigilante();
        } catch (PersistenceException ex) {
            Util.mensagemErro("Não foi possível salvar.", "salvar_cad_vig");
        } catch (ValidacaoCampoException ex) {
            Util.mensagemErro(ex.getMessage(), "salvar_cad_vig");
        }
    }

    public String cancelarVigilante() {
        this.vigilante = new Vigilante();
        this.exibirModalExcluir = false;
        return "vigilante.xhtml";
    }

    public String alterarVigilante(Vigilante vig) {
        this.vigilante = vig;
        return "vigilante.xhtml";
    }

    public String abrirModalExcluir(Vigilante vig) {
        this.vigilante = vig;
        this.exibirModalExcluir = true;
        return "vigilante.xhtml";
    }

    public void excluirVigilante() {
        try {
            vigilantes.remove(vigilante);
            vigilante.excluir();
            vigilanteDAO.alterar(vigilante);
            cancelarVigilante();
        } catch (PersistenceException ex) {
            Util.mensagemErro("Não foi possível excluir", "excluir_cad_vig");
        }
    }

    public Vigilante getVigilante() {
        return vigilante;
    }

    public void setVigilante(Vigilante vigilante) {
        this.vigilante = vigilante;
    }

    public List<Vigilante> getVigilantes() {
        return vigilantes;
    }

    public Boolean getExibirModalExcluir() {
        return exibirModalExcluir;
    }

    public Boolean getDisableInputText() {
        return !(vigilante.getId() == null || vigilante.getId() == 0);
    }

}

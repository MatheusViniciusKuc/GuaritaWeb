package br.edu.ifpr.irati.ads.mb;

import br.edu.ifpr.irati.ads.dao.VigilanteDAO;
import br.edu.ifpr.irati.ads.exception.PersistenceException;
import br.edu.ifpr.irati.ads.exception.ValidacaoCampoException;
import br.edu.ifpr.irati.ads.model.Vigilante;
import br.edu.ifpr.irati.ads.util.HibernateUtil;
import br.edu.ifpr.irati.ads.util.Util;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.hibernate.Session;

@ManagedBean
@SessionScoped
public class VigilanteMB {

    private Session session;
    private VigilanteDAO vigilanteDAO;
    private Vigilante vigilante;
    private List<Vigilante> vigilantes;
    private Boolean exibirModalExcluir;

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
            if (vigilante.getDadosPessoais().getNome().isBlank()) {
                throw new ValidacaoCampoException("O Campo Nome precisa estar preenchido!");
            }
            if (vigilante.getDadosPessoais().getCpf().isBlank()) {
                throw new ValidacaoCampoException("O Campo CPF precisa estar preenchido!");
            }
            if (vigilante.getDadosPessoais().getEmail().isBlank()) {
                throw new ValidacaoCampoException("O Campo E-mail precisa estar preenchido!");
            }

            if (vigilante.getId() == null || vigilante.getId() == 0) {
                vigilanteDAO.salvar(vigilante);
                this.vigilantes.add(vigilante);
            } else {
                vigilanteDAO.alterar(vigilante);
            }

            cancelarVigilante();
        } catch (PersistenceException ex) {
            Util.mensagemErro("Não foi possível salvar.", "nome_vigilante");
        } catch (ValidacaoCampoException vce) {
            Util.mensagemErro(vce.getMessage(), "nome_vigilante");
        }
    }

    public void cancelarVigilante() {
        this.vigilante = new Vigilante();
        this.exibirModalExcluir = false;
    }

    public void alterarVigilante(Vigilante vig) {
        this.vigilante = vig;
    }

    public void abrirModalExcluir(Vigilante vig) {
        this.vigilante = vig;
        this.exibirModalExcluir = true;
    }

    public void excluirVigilante() {
        try {
            vigilantes.remove(vigilante);
            vigilante.excluir();
            vigilanteDAO.alterar(vigilante);
        } catch (PersistenceException ex) {
            Util.mensagemErro("Não foi possível excluir", "nome_vigilante");
        }
        cancelarVigilante();
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

}

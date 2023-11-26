package br.edu.ifpr.irati.ads.mb;

import br.edu.ifpr.irati.ads.dao.EmprestimoDAO;
import br.edu.ifpr.irati.ads.dao.EspacoDAO;
import br.edu.ifpr.irati.ads.dao.ServidorDAO;
import br.edu.ifpr.irati.ads.dao.VigilanteDAO;
import br.edu.ifpr.irati.ads.exception.PersistenceException;
import br.edu.ifpr.irati.ads.exception.ValidacaoCampoException;
import br.edu.ifpr.irati.ads.model.Emprestimo;
import br.edu.ifpr.irati.ads.model.Espaco;
import br.edu.ifpr.irati.ads.model.Modal;
import br.edu.ifpr.irati.ads.model.Ocorrencia;
import br.edu.ifpr.irati.ads.model.Servidor;
import br.edu.ifpr.irati.ads.model.Status;
import br.edu.ifpr.irati.ads.model.Vigilante;
import br.edu.ifpr.irati.ads.util.HibernateUtil;
import br.edu.ifpr.irati.ads.util.Util;
import jakarta.persistence.NoResultException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.hibernate.Session;

@ManagedBean
@SessionScoped
public class PrincipalMB implements Serializable {

    private Session session;
    private EmprestimoDAO emprestimoDAO;
    private ServidorDAO servidorDAO;
    private VigilanteDAO vigilanteDAO;
    private List<Emprestimo> emprestimos;
    private Emprestimo emprestimo;
    private Ocorrencia ocorrencia;

    private Boolean exibirModalServidor;
    private Boolean exibirModalVigilante;
    private Boolean exibirModalOcorrencia;
    private Boolean exibirModalCancelar;
    private Boolean perdeuChave;
    private Boolean isPaginaIngles;

    private String siapeConfirmarSaida;
    private String siapeOcorrencia;
    private String siapeCancelar;
    private String cpfOcorrencia;
    private String cpfConfirmarSaida;

    public PrincipalMB() {
        configurarConfiguracoesIniciais();
    }

    private void configurarConfiguracoesIniciais() {
        this.isPaginaIngles = false;
        this.session = HibernateUtil.getSessionFactory().openSession();
        try {
            this.vigilanteDAO = new VigilanteDAO(session);
            this.servidorDAO = new ServidorDAO(session);
            this.emprestimoDAO = new EmprestimoDAO(session);
            this.emprestimos = emprestimoDAO.buscarTodos();
        } catch (PersistenceException ex) {
            this.emprestimos = new ArrayList<>();
        }
        filtrarEmprestimos();
        limparDados();
    }

    private void verificarIdiomaPagina() {
        isPaginaIngles = Util.verificarIdiomaPagina();
    }

    private void filtrarEmprestimos() {
        this.emprestimos = emprestimos.stream()
                .filter(emp -> emp.getStatus().equals(Status.AGENDADO))
                .collect(Collectors.toList());
    }

    public void limparDados() {
        this.ocorrencia = new Ocorrencia();
        this.emprestimo = new Emprestimo();
        this.exibirModalCancelar = false;
        this.exibirModalOcorrencia = false;
        this.exibirModalServidor = false;
        this.exibirModalVigilante = false;
        this.perdeuChave = false;
        this.siapeConfirmarSaida = "";
        this.cpfConfirmarSaida = "";
        this.siapeOcorrencia = "";
        this.siapeCancelar = "";
        this.cpfOcorrencia = "";
    }

    public void registrarOcorrencia() {
        try {
            Espaco espaco = this.emprestimo.getEspaco();

            Servidor servidor = null;
            Vigilante vigilante = null;

            try {
                if (!cpfOcorrencia.isBlank()) {
                    vigilante = vigilanteDAO.buscarPorCPF(cpfOcorrencia);
                } else if (!siapeOcorrencia.isBlank()) {
                    servidor = servidorDAO.buscarPorSIAPE(siapeOcorrencia);
                } else {
                    throw new ValidacaoCampoException(isPaginaIngles
                            ? "Enter one of the fields: Server or Security Guard."
                            : "Informe um dos campos: Servidor ou Vigilante.");
                }
            } catch (NoResultException ex) {
                throw new ValidacaoCampoException(isPaginaIngles
                        ? "Server / Security Guard not found!"
                        : "Servidor / Vigilante não encontrado!");
            }

            EspacoDAO espacoDAO = new EspacoDAO(session);

            this.ocorrencia.setVigilante(vigilante);
            this.ocorrencia.setServidor(servidor);
            this.ocorrencia.setEspaco(espaco);

            espaco.setDisponivelEmprestimo(!perdeuChave);
            espaco.adicionarOcorrencia(ocorrencia);
            espacoDAO.alterar(espaco);

            if (perdeuChave) {
                emprestimo.setStatus(Status.OCORRENCIA);
                emprestimoDAO.alterar(emprestimo);
            }

            this.exibirModalOcorrencia = false;
            limparDados();
            filtrarEmprestimos();
        } catch (PersistenceException pe) {
            Util.mensagemErro(isPaginaIngles 
                    ? "Unable to register incident" 
                    : "Não foi possível registrar Ocorrência",
                    "principal_ocorrencia_btnConfirmar");
        } catch (ValidacaoCampoException vce) {
            Util.mensagemErro(vce.getMessage(),
                    "principal_ocorrencia_btnConfirmar");
        }
    }

    public void cancelarEmprestimo() {
        verificarIdiomaPagina();
        try {
            Servidor servidor = servidorDAO.buscarPorSIAPE(siapeCancelar);

            if (!emprestimo.getServidor().equals(servidor)) {
                throw new ValidacaoCampoException(isPaginaIngles
                        ? "The Servant must be the same one who made the loan."
                        : "O Servidor precisa ser o mesmo que fez o empréstimo.");
            }

            emprestimo.setStatus(Status.CANCELADO);
            emprestimoDAO.alterar(emprestimo);
            limparDados();
            filtrarEmprestimos();
        } catch (PersistenceException ex) {
            Util.mensagemErro(isPaginaIngles
                    ? "Unable to cancel loan"
                    : "Não foi possível cancelar o Emprestimo",
                    "principal_cancelar_btnConfirmar");
        } catch (ValidacaoCampoException vce) {
            Util.mensagemErro(vce.getMessage(),
                    "principal_cancelar_btnConfirmar");
        } catch (NoResultException nre) {
            Util.mensagemErro(isPaginaIngles
                    ? "Unable to locate Servant"
                    : "Não foi possível localizar o Servidor",
                    "principal_cancelar_btnConfirmar");
        }
    }

    public void concluirEmprestimo() {
        verificarIdiomaPagina();
        try {
            Vigilante vigilante = null;
            Servidor servidor = null;

            if (!cpfConfirmarSaida.isBlank()) {
                vigilante = vigilanteDAO.buscarPorCPF(cpfConfirmarSaida);
            } else if (!siapeConfirmarSaida.isBlank()) {
                servidor = servidorDAO.buscarPorSIAPE(siapeConfirmarSaida);
            }

            if (vigilante != null) {
                emprestimo.setStatus(Status.REALIZADO);
                emprestimo.setConcluidoPor("VIGILANTE - " + vigilante.getDadosPessoais().getNome());
            } else if (servidor != null) {
                if (!emprestimo.getServidor().equals(servidor)) {
                    throw new ValidacaoCampoException(isPaginaIngles
                            ? "The Servant must be the same one who made the loan."
                            : "O Servidor precisa ser o mesmo que fez o empréstimo.");
                }

                emprestimo.setStatus(Status.REALIZADO);
                emprestimo.setConcluidoPor("SERVIDOR - " + servidor.getDadosPessoais().getNome());
            }

            emprestimoDAO.alterar(emprestimo);
            limparDados();
            filtrarEmprestimos();
        } catch (ValidacaoCampoException vce) {
            Util.mensagemErro(vce.getMessage(),
                    "principal_liberarServ_btnConfirmar");
        } catch (PersistenceException ex) {
            Util.mensagemErro(isPaginaIngles
                    ? "Unable to complete loan"
                    : "Não foi possível concluir o Emprestimo",
                    "principal_liberarServ_btnConfirmar");
        } catch (NoResultException ex) {
            Util.mensagemErro(isPaginaIngles
                    ? "Unable to locate the person responsible for completing the loan"
                    : "Não foi possível localizar o responsável pela conclusão do empréstimo",
                    "principal_liberarServ_btnConfirmar");
        }
    }

    public void exibirModal(Emprestimo emprestimo, String modalString) {
        this.emprestimo = emprestimo;
        Modal modal = Util.buscarModal(modalString);
        switch (modal) {
            case ENTREGA_CHAVE_SERVIDOR:
                this.exibirModalServidor = true;
                break;
            case ENTREGA_CHAVE_VIGILANTE:
                this.exibirModalVigilante = true;
                break;
            case REGISTRAR_OCORRENCIA:
                this.exibirModalOcorrencia = true;
                break;
            case CANCELAR_EMPRESTIMO:
                this.exibirModalCancelar = true;
                break;
            default:
        }
    }

    public Boolean desativarBotoes(Emprestimo emp) {
        return emp.getStatus() != Status.AGENDADO;
    }

    public List<Emprestimo> getEmprestimos() {
        return emprestimos;
    }

    public Emprestimo getEmprestimo() {
        return emprestimo;
    }

    public void setEmprestimo(Emprestimo emprestimo) {
        this.emprestimo = emprestimo;
    }

    public Boolean getExibirModalCancelar() {
        return exibirModalCancelar;
    }

    public Boolean getExibirModalOcorrencia() {
        return exibirModalOcorrencia;
    }

    public Boolean getExibirModalServidor() {
        return exibirModalServidor;
    }

    public Boolean getExibirModalVigilante() {
        return exibirModalVigilante;
    }

    public Ocorrencia getOcorrencia() {
        return ocorrencia;
    }

    public void setOcorrencia(Ocorrencia ocorrencia) {
        this.ocorrencia = ocorrencia;
    }

    public void setSiapeConfirmarSaida(String siapeConfirmarSaida) {
        this.siapeConfirmarSaida = siapeConfirmarSaida;
    }

    public String getSiapeConfirmarSaida() {
        return siapeConfirmarSaida;
    }

    public void setPerdeuChave(Boolean perdeuChave) {
        this.perdeuChave = perdeuChave;
    }

    public Boolean getPerdeuChave() {
        return perdeuChave;
    }

    public void setCpfConfirmarSaida(String cpfConfirmarSaida) {
        this.cpfConfirmarSaida = cpfConfirmarSaida;
    }

    public String getCpfConfirmarSaida() {
        return cpfConfirmarSaida;
    }

    public String getSiapeOcorrencia() {
        return siapeOcorrencia;
    }

    public void setSiapeOcorrencia(String siapeOcorrencia) {
        this.siapeOcorrencia = siapeOcorrencia;
    }

    public void setCpfOcorrencia(String cpfOcorrencia) {
        this.cpfOcorrencia = cpfOcorrencia;
    }

    public String getCpfOcorrencia() {
        return cpfOcorrencia;
    }

    public void setSiapeCancelar(String siapeCancelar) {
        this.siapeCancelar = siapeCancelar;
    }

    public String getSiapeCancelar() {
        return siapeCancelar;
    }

}

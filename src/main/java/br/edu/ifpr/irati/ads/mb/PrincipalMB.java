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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.hibernate.Session;

@ManagedBean
@ViewScoped
public class PrincipalMB {

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

    private String cpf;
    private String cpfConfirmarSaida;
    private String siape;

    public PrincipalMB() {
        configurarConfiguracoesIniciais();
    }

    private void configurarConfiguracoesIniciais() {
        session = HibernateUtil.getSessionFactory().openSession();
        try {
            this.vigilanteDAO = new VigilanteDAO(session);
            this.servidorDAO = new ServidorDAO(session);
            this.emprestimoDAO = new EmprestimoDAO(session);
            this.emprestimos = emprestimoDAO.buscarTodos();
        } catch (PersistenceException ex) {
            this.emprestimos = new ArrayList<>();
        }
        limparDados();
    }

    public void limparDados() {
        this.ocorrencia = new Ocorrencia();
        this.emprestimo = new Emprestimo();
        this.exibirModalCancelar = false;
        this.exibirModalOcorrencia = false;
        this.exibirModalServidor = false;
        this.exibirModalVigilante = false;
        this.perdeuChave = false;
        this.cpf = "";
        this.siape = "";
        this.cpfConfirmarSaida = "";
    }

    public void registrarOcorrencia() {
        try {
            Espaco espaco = this.emprestimo.getEspaco();

            if (ocorrencia.getOcorrido().isBlank()) {
                throw new ValidacaoCampoException("Deve possuir uma mensagem.");
            }

            Servidor servidor = null;
            Vigilante vigilante = null;

            try {
                if (!cpf.isBlank()) {
                    vigilante = vigilanteDAO.buscarPorCPF(cpf);
                } else if (!siape.isBlank()) {
                    servidor = servidorDAO.buscarPorSIAPE(siape);
                } else {
                    throw new ValidacaoCampoException("Informe um dos campos: Servidor ou Vigilante.");
                }
            } catch (PersistenceException ex) {
                throw new ValidacaoCampoException("Servidor / Vigilante não encontrado!");
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
        } catch (PersistenceException pe) {
            Util.mensagemErro("Não foi possível registrar Ocorrência", "principal_ocorrencia");
            limparDados();
        } catch (ValidacaoCampoException vce) {
            Util.mensagemErro(vce.getMessage(), "principal_ocorrencia");
        }
    }

    public void cancelarEmprestimo() {
        try {
            if (siape.isBlank()) {
                throw new ValidacaoCampoException("Informe o campo: Servidor.");
            }

            Servidor servidor = servidorDAO.buscarPorSIAPE(siape);

            if (servidor == null) {
                throw new ValidacaoCampoException("SIAPE Informado inválido ou desativado.");
            }

            if (!emprestimo.getServidor().equals(servidor)) {
                throw new ValidacaoCampoException("O Servidor precisa ser o mesmo que fez o empréstimo.");
            }

            emprestimo.setStatus(Status.CANCELADO);
            emprestimoDAO.alterar(emprestimo);
            limparDados();
        } catch (PersistenceException ex) {
            Util.mensagemErro("Não foi possível cancelar o Emprestimo",
                    "servidor_nome_principal_cancelar");
            limparDados();
        } catch (ValidacaoCampoException vce) {
            Util.mensagemErro(vce.getMessage(), "servidor_nome_principal_cancelar");
        }
    }

    public void concluirEmprestimo() {
        try {
            Vigilante vigilante = null;
            Servidor servidor = null;

            if (!cpfConfirmarSaida.isBlank()) {
                vigilante = vigilanteDAO.buscarPorCPF(cpfConfirmarSaida);
            } else if (!siape.isBlank()) {
                servidor = servidorDAO.buscarPorSIAPE(siape);
            } else {
                throw new ValidacaoCampoException("Preenchar o campo para "
                        + "conseguir concluir o empréstimo.");
            }

            if (vigilante != null) {
                emprestimo.setStatus(Status.REALIZADO);
                emprestimo.setConcluidoPor("VIGILANTE - " + vigilante.getDadosPessoais().getNome());
            } else if (servidor != null) {
                if (!emprestimo.getServidor().equals(servidor)) {
                    throw new ValidacaoCampoException("O Servidor precisa ser o "
                            + "mesmo que fez o empréstimo.");
                }

                emprestimo.setStatus(Status.REALIZADO);
                emprestimo.setConcluidoPor("SERVIDOR - " + servidor.getDadosPessoais().getNome());
            } else {
                throw new ValidacaoCampoException("Não foi possível localizar "
                        + "o responsável pela conclisão do empréstimo.");
            }

            emprestimoDAO.alterar(emprestimo);
            limparDados();
        } catch (ValidacaoCampoException vce) {
            Util.mensagemErro(vce.getMessage(),
                    "servidor_nome_principal_cancelar");
        } catch (PersistenceException ex) {
            Util.mensagemErro("Não foi possível concluir o Emprestimo",
                    "servidor_nome_principal_concluir");
            limparDados();
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

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setSiape(String siape) {
        this.siape = siape;
    }

    public String getSiape() {
        return siape;
    }

    public String getCpf() {
        return cpf;
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

}

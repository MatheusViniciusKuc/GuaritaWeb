package br.edu.ifpr.irati.ads.mb;

import br.edu.ifpr.irati.ads.dao.EspacoDAO;
import br.edu.ifpr.irati.ads.dao.ServidorDAO;
import br.edu.ifpr.irati.ads.dao.VigilanteDAO;
import br.edu.ifpr.irati.ads.exception.PersistenceException;
import br.edu.ifpr.irati.ads.exception.ValidacaoCampoException;
import br.edu.ifpr.irati.ads.model.Espaco;
import br.edu.ifpr.irati.ads.model.Ocorrencia;
import br.edu.ifpr.irati.ads.model.Servidor;
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
public class ModelMB {

    private Session session;
    private Espaco espaco;
    private List<Espaco> espacos;
    private EspacoDAO espacoDAO;
    private Ocorrencia ocorrencia;
    private String siape;
    private String cpf;
    private String cpfVigilante;
    private Boolean exibirModalOcorrencia;
    private Boolean exibirModalLiberarEmprestimo;
    private Boolean exibirModalExcluir;
    private Boolean perdeuChave;
    private Boolean exibirOcorrencias;

    public ModelMB() {
        configurarConfiguracoesIniciais();
    }

    private void configurarConfiguracoesIniciais() {
        session = HibernateUtil.getSessionFactory().openSession();
        try {
            espacoDAO = new EspacoDAO(session);
            espacos = espacoDAO.buscarTodosAtivos();
        } catch (PersistenceException ex) {
            espacos = new ArrayList<>();
        }
        cancelarEspaco();
    }

    public void cancelarEspaco() {
        this.cpfVigilante = "";
        this.siape = "";
        this.cpf = "";
        this.exibirModalLiberarEmprestimo = false;
        this.exibirModalExcluir = false;
        this.perdeuChave = false;
        this.exibirModalOcorrencia = false;
        this.exibirOcorrencias = false;
        this.ocorrencia = new Ocorrencia();
        this.espaco = new Espaco();
    }

    public void salvarEspaco() {
        try {
            if (espaco.getNome().isBlank()) {
                throw new ValidacaoCampoException("Precisa preencher o campo Espaço!");
            }

            if (espaco.getId() == null || espaco.getId() == 0) {
                espacoDAO.salvar(espaco);
                this.espacos.add(espaco);
            } else {
                espacoDAO.alterar(espaco);
            }
            cancelarEspaco();
        } catch (PersistenceException ex) {
            Util.mensagemErro("Não foi possível salvar", "nome_esp");
        } catch (ValidacaoCampoException vce) {
            Util.mensagemErro(vce.getMessage(), "nome_esp");
        }
    }

    public void abrirModalLiberarEmprestimo(Espaco esp) {
        this.espaco = esp;
        this.exibirModalLiberarEmprestimo = true;
    }

    public void liberarEmprestimo() {
        try {
            if (cpfVigilante.isBlank()) {
                throw new ValidacaoCampoException("Precisa preencher o campo Vigilante!");
            }

            VigilanteDAO vigilanteDAO = new VigilanteDAO(session);
            Vigilante vigilante = vigilanteDAO.buscarPorCPF(cpfVigilante);

            this.ocorrencia.setEspaco(espaco);
            this.ocorrencia.setVigilante(vigilante);
            this.ocorrencia.setOcorrido("Liberado para novos Empréstimos.");

            this.espaco.setDisponivelEmprestimo(true);
            this.espaco.adicionarOcorrencia(ocorrencia);
            this.espacoDAO.alterar(espaco);

            cancelarEspaco();
        } catch (PersistenceException ex) {
            Util.mensagemErro("Não foi possível liberar!", "cpf_vigilante_liberar_esp");
        } catch (ValidacaoCampoException vce) {
            Util.mensagemErro(vce.getMessage(), "cpf_vigilante_liberar_esp");
        }
        cancelarEspaco();
    }

    public void abrirModalExcluir(Espaco esp) {
        this.exibirOcorrencias = false;
        this.espaco = esp;
        this.exibirModalExcluir = true;
    }

    public void excluirEspaco() {
        try {
            espacos.remove(espaco);
            espaco.excluir();
            espacoDAO.alterar(espaco);
        } catch (PersistenceException ex) {
            Util.mensagemErro("Não foi possível excluir", "nome_esp");
        }
        cancelarEspaco();
    }

    public void alterarEspaco(Espaco esp) {
        this.exibirOcorrencias = false;
        this.espaco = esp;
    }

    public void abrirModalOcorrencia(Espaco esp) {
        this.espaco = esp;
        this.exibirModalOcorrencia = true;
    }

    public void registrarOcorrencia() {
        try {
            if (ocorrencia.getOcorrido().isBlank()) 
                throw new ValidacaoCampoException("Deve possuir uma mensagem.");
            
            Servidor servidor = null;
            Vigilante vigilante = null;

            try {
                if (!cpf.isBlank()) {
                    VigilanteDAO vigilanteDAO = new VigilanteDAO(session);
                    vigilante = vigilanteDAO.buscarPorCPF(cpf);
                } else if (!siape.isBlank()) {
                    ServidorDAO servidorDAO = new ServidorDAO(session);
                    servidor = servidorDAO.buscarPorSIAPE(siape);
                } else {
                    throw new ValidacaoCampoException("Informe um dos campos: Servidor ou Vigilante.");
                }
            } catch (PersistenceException ex) {
                throw new ValidacaoCampoException("Servidor / Vigilante não encontrado!");
            }

            this.ocorrencia.setVigilante(vigilante);
            this.ocorrencia.setServidor(servidor);
            this.ocorrencia.setEspaco(espaco);

            this.espaco.setDisponivelEmprestimo(!perdeuChave);
            this.espaco.adicionarOcorrencia(ocorrencia);
            this.espacoDAO.alterar(espaco);

            this.exibirModalOcorrencia = false;
            cancelarEspaco();
        } catch (PersistenceException pe) {
            Util.mensagemErro("Não foi possível registrar Ocorrência", "nome_esp");
            cancelarEspaco();
        } catch (ValidacaoCampoException vce) {
            Util.mensagemErro(vce.getMessage(), "cpf_siape_registrar_oco");
        }
    }

    public void visualizarOcorrencias(Espaco esp) {
        alterarEspaco(esp);
        this.exibirOcorrencias = true;
    }

    public Boolean getExibirModalOcorrencia() {
        return exibirModalOcorrencia;
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

    public String getCpf() {
        return cpf;
    }

    public String getSiape() {
        return siape;
    }

    public void setPerdeuChave(Boolean perdeuChave) {
        this.perdeuChave = perdeuChave;
    }

    public Boolean getPerdeuChave() {
        return perdeuChave;
    }

    public Boolean getExibirModalLiberarEmprestimo() {
        return exibirModalLiberarEmprestimo;
    }

    public Boolean getExibirModalExcluir() {
        return exibirModalExcluir;
    }

    public String getCpfVigilante() {
        return cpfVigilante;
    }

    public void setCpfVigilante(String cpfVigilante) {
        this.cpfVigilante = cpfVigilante;
    }

    public Boolean getExibirOcorrencias() {
        return exibirOcorrencias;
    }

}

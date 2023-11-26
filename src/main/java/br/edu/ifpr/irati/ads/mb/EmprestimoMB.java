package br.edu.ifpr.irati.ads.mb;

import br.edu.ifpr.irati.ads.dao.EmprestimoDAO;
import br.edu.ifpr.irati.ads.dao.EspacoDAO;
import br.edu.ifpr.irati.ads.dao.ServidorDAO;
import br.edu.ifpr.irati.ads.exception.PersistenceException;
import br.edu.ifpr.irati.ads.exception.ValidacaoCampoException;
import br.edu.ifpr.irati.ads.model.Emprestimo;
import br.edu.ifpr.irati.ads.model.Espaco;
import br.edu.ifpr.irati.ads.model.Horario;
import br.edu.ifpr.irati.ads.model.Servidor;
import br.edu.ifpr.irati.ads.util.HibernateUtil;
import br.edu.ifpr.irati.ads.util.Util;
import jakarta.persistence.NoResultException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.hibernate.Session;

@ManagedBean
@SessionScoped
public class EmprestimoMB implements Serializable {

    private Session session;
    private EspacoDAO espDAO;
    private EmprestimoDAO empDAO;
    private List<Espaco> espacos;
    private Espaco espaco;
    private Emprestimo emprestimo;
    private String sixCPF;
    private Boolean exibirModal;
    private Horario horario;
    private Boolean isEspacoDisponivel;
    private Boolean isPaginaIngles;

    public EmprestimoMB() {
        configurarConfiguracoesIniciais();
    }

    private void configurarConfiguracoesIniciais() {
        session = HibernateUtil.getSessionFactory().openSession();
        try {
            empDAO = new EmprestimoDAO(session);
            espDAO = new EspacoDAO(session);
            espacos = espDAO.buscarEspacosDisponiveis();
            isEspacoDisponivel = true;
        } catch (PersistenceException ex) {
            isEspacoDisponivel = false;
        }
        isPaginaIngles = false;
        limparTela();
    }

    private void verificarIdiomaPagina() {
        isPaginaIngles = Util.verificarIdiomaPagina();
    }

    public void limparTela() {
        emprestimo = new Emprestimo();
        this.exibirModal = false;
        this.sixCPF = "";
        this.horario = new Horario();
    }

    public void configurarValores() {
        verificarIdiomaPagina();
        try {
            Servidor s = localizarServidor();

            Date dataInicio = Util.configurarDate(horario.getDataInicio(),
                    horario.getHorarioSelecionadoIni());
            Date dataFim = Util.configurarDate(horario.getDataFinal(),
                    horario.getHorarioSelecionadoFim());

            if (dataInicio.compareTo(dataFim) >= 0) {
                throw new ValidacaoCampoException(isPaginaIngles
                        ? "The End Date must be greater than the Start Date"
                        : "A Data de Fim precisa ser superior a Data de Início");
            }

            if (!empDAO.isDisponivelParaEmprestimo(dataInicio, dataFim, espaco)) {
                throw new ValidacaoCampoException(isPaginaIngles
                        ? "The schedule is already busy."
                        : "Horário já está ocupado.");
            }

            emprestimo.setDataInicio(dataInicio);
            emprestimo.setDataFim(dataFim);
            emprestimo.setServidor(s);
            emprestimo.setEspaco(espaco);

            this.exibirModal = true;
        } catch (PersistenceException | NoResultException ex) {
            Util.mensagemErro(isPaginaIngles
                    ? "Servant not found!"
                    : "Servidor não encontrado!",
                    "salvar_cad_emp");
        } catch (ValidacaoCampoException ex) {
            Util.mensagemErro(ex.getMessage(), "salvar_cad_emp");
        } catch (ParseException pe) {
            Util.mensagemErro(isPaginaIngles
                    ? "Problem converting the Date, if the problem persists, get in touch."
                    : "Problema ao converter a Data, se o problema persistir entre em contato.",
                    "salvar_cad_emp");
        }
    }

    private Servidor localizarServidor() throws ValidacaoCampoException, PersistenceException {
        ServidorDAO servDAO = new ServidorDAO(session);
        Servidor s = null;

        if (!emprestimo.getServidor().getSiape().isBlank()) {
            s = servDAO.buscarPorSIAPE(emprestimo.getServidor().getSiape());
        } else if (!sixCPF.isBlank()) {
            s = servDAO.buscarPorCPF(sixCPF);
        } else {
            throw new ValidacaoCampoException(isPaginaIngles
                    ? "One of these fields must be filled in: Siape or CPF!"
                    : "Um desses campos precisa estar preenchido: Siape ou CPF!");
        }

        return s;
    }

    public String salvar() {
        try {
            this.exibirModal = false;
            empDAO.salvar(emprestimo);
            limparTela();
            Util.expirarSessao();
            return "index.xhtml";
        } catch (PersistenceException e) {
            Util.mensagemErro("Erro ao salvar!", "siape_emp");
            return "emprestimo.xhtml";
        }
    }

    public void cancelarEmprestimo() {
        this.exibirModal = false;
    }

    public String getSixCPF() {
        return emprestimo.getServidor().getDadosPessoais().getCpf().isBlank()
                ? ""
                : emprestimo.getServidor().getDadosPessoais()
                        .getCpf().substring(0, 6);
    }

    public void setSixCPF(String sixCPF) {
        this.sixCPF = sixCPF;
    }

    public Emprestimo getEmprestimo() {
        return emprestimo;
    }

    public void setEmprestimo(Emprestimo emprestimo) {
        this.emprestimo = emprestimo;
    }

    public Espaco getEspaco() {
        return espaco;
    }

    public void setEspaco(Espaco espaco) {
        this.espaco = espaco;
    }

    public List<Espaco> getEspacos() {
        return espacos;
    }

    public Boolean getExibirModal() {
        return exibirModal;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public Horario getHorario() {
        return horario;
    }

    public Boolean getIsEspacoDisponivel() {
        return isEspacoDisponivel;
    }

}

package br.edu.ifpr.irati.ads.util;

import br.edu.ifpr.irati.ads.model.enums.Modal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

public class Util {

    public static Boolean verificarIdiomaPagina() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Locale locale = facesContext.getViewRoot().getLocale();

        return !(locale.getLanguage().equals("pt")
                && locale.getCountry().equals("BR"));
    }

    public static void mensagemErro(String mensagem, String campo) {
        FacesMessage mensagemFM = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, null);
        FacesContext.getCurrentInstance().addMessage(campo, mensagemFM);
    }

    public static Modal buscarModal(String modalString) {
        try {
            return Modal.valueOf(modalString);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static Date configurarDate(Date date, String horario) throws ParseException {
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");
        Date horaDesejada = formatoHora.parse(horario);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        Calendar calHora = Calendar.getInstance();
        calHora.setTime(horaDesejada);

        cal.set(Calendar.HOUR_OF_DAY, calHora.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, calHora.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND, 0);

        return cal.getTime();
    }

    public static void expirarSessao() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

        if (session != null) {
            session.invalidate();
        }
    }

}

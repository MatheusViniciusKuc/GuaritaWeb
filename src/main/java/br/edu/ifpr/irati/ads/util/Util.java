package br.edu.ifpr.irati.ads.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class Util {

    public static void mensagemErro(String mensagem, String campo) {
        FacesMessage mensagemFM = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, null);
        FacesContext.getCurrentInstance().addMessage(campo, mensagemFM);
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

}

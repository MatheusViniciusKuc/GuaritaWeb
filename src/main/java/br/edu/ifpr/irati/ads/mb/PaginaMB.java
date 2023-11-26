package br.edu.ifpr.irati.ads.mb;

import br.edu.ifpr.irati.ads.util.Util;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class PaginaMB {

    public String visitarPagina(String pagina) {
        Util.expirarSessao();
        return pagina + ".xhtml";
    }
}

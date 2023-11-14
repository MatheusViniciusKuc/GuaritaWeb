package br.edu.ifpr.irati.ads.util.converter;

import br.edu.ifpr.irati.ads.dao.Dao;
import br.edu.ifpr.irati.ads.dao.GenericDAO;
import br.edu.ifpr.irati.ads.exception.PersistenceException;
import br.edu.ifpr.irati.ads.model.Espaco;
import br.edu.ifpr.irati.ads.util.HibernateUtil;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.hibernate.Session;

@FacesConverter(forClass = Espaco.class, value = "espacoConverter")
public class EspacoConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {
        if (value == null || value.isEmpty()) {
            return null;
        } else {
            try {
                Integer id = Integer.valueOf(value);
                Session session = HibernateUtil.getSessionFactory().openSession();
                Dao<Espaco> espDAO = new GenericDAO<>(Espaco.class, session);
                Espaco lab = espDAO.buscarPorId(id);
                session.close();
                return lab;
            } catch (PersistenceException ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }   

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {
        Espaco esp = (Espaco) value;
        if (esp != null) {
            return String.valueOf(esp.getId());
        } else {
            return null;
        }
    }

}

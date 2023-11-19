package br.edu.ifpr.irati.ads.util;

import br.edu.ifpr.irati.ads.model.Emprestimo;
import br.edu.ifpr.irati.ads.model.Espaco;
import br.edu.ifpr.irati.ads.model.Ocorrencia;
import br.edu.ifpr.irati.ads.model.Servidor;
import br.edu.ifpr.irati.ads.model.Vigilante;
import java.util.HashMap;
import java.util.Map;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {

    private static SessionFactory sessionFactory = null;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Map<String, Object> settings = new HashMap<>();
            settings.put("connection.driver_class", "com.mysql.cj.jdbc.Driver");
            settings.put("dialect", "org.hibernate.dialect.MySQLDialect");
            settings.put("hibernate.connection.url", "jdbc:mysql://localhost:3306/GuaritaWeb?createDatabaseIfNotExist=true&useUnicode=yes&characterEncoding=UTF-8");
            settings.put("hibernate.connection.username", "root");
            settings.put("hibernate.hbm2ddl.auto", "update");
            settings.put("hibernate.connection.password", "root");
            settings.put("hibernate.current_session_context_class", "thread");
            settings.put("hibernate.show_sql", "false");
            settings.put("hibernate.format_sql", "false");

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(settings).build();

            MetadataSources metadataSources = new MetadataSources(serviceRegistry);
            metadataSources.addAnnotatedClass(Ocorrencia.class);
            metadataSources.addAnnotatedClass(Espaco.class);
            metadataSources.addAnnotatedClass(Vigilante.class);
            metadataSources.addAnnotatedClass(Servidor.class);
            metadataSources.addAnnotatedClass(Emprestimo.class);

            Metadata metadata = metadataSources.buildMetadata();

            sessionFactory = metadata.getSessionFactoryBuilder().build();
        }
        return sessionFactory;
    }
}

package ru.clevertec.zabalotcki.utils;

import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.clevertec.zabalotcki.model.GiftCertificate;
import ru.clevertec.zabalotcki.model.Tag;

@UtilityClass
public class HibernateUtils {

    public static SessionFactory sessionFactory() {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        configuration.addAnnotatedClass(GiftCertificate.class);
        configuration.addAnnotatedClass(Tag.class);
        return configuration.buildSessionFactory();
    }
}

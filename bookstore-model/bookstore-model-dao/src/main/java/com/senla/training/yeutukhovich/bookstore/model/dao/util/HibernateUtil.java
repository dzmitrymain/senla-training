package com.senla.training.yeutukhovich.bookstore.model.dao.util;

import com.senla.training.yeutukhovich.bookstore.util.constant.LoggerConstant;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateUtil.class);

    private static final SessionFactory SESSION_FACTORY;

    static {
        try {
            SESSION_FACTORY = new Configuration()
                    .configure()
                    .buildSessionFactory();
        } catch (Throwable e) {
            LOGGER.error(LoggerConstant.SESSION_FACTORY_BUILD_FAILURE.getMessage(), e.getMessage(), e);
            throw new ExceptionInInitializerError(e.getMessage());
        }
    }

    public static Session getCurrentSession() {
        return SESSION_FACTORY.getCurrentSession();
    }
}

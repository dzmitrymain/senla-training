package com.senla.training.yeutukhovich.bookstore.model.dao.util;

import com.senla.training.yeutukhovich.bookstore.util.constant.LoggerConstant;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HibernateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateUtil.class);

    private SessionFactory sessionFactory;
    private ThreadLocal<Session> threadLocal = new ThreadLocal<>();

    public HibernateUtil() {
        initSessionFactory();
    }

    public Session getCurrentSession() {
        Session session;
        if (threadLocal.get() == null || !threadLocal.get().isOpen()) {
            session = sessionFactory.openSession();
            threadLocal.set(session);
        } else {
            session = threadLocal.get();
        }
        return session;
    }

    private void initSessionFactory() {
        try {
            sessionFactory = new Configuration()
                    .configure()
                    .buildSessionFactory();
        } catch (Throwable e) {
            LOGGER.error(LoggerConstant.SESSION_FACTORY_BUILD_FAILURE.getMessage(), e.getMessage(), e);
        }
    }
}

package ru.sukhoa.dao;


import com.sun.istack.internal.Nullable;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sukhoa.domain.FrontendDirector;
import ru.sukhoa.domain.FrontendDirectorInfo;

import java.util.List;

@Component
public class FrontendDirectorDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Nullable
    public FrontendDirector getDirector(long id) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            return (FrontendDirector) session.get(FrontendDirector.class, id);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<FrontendDirector> getDirectorsList() {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            //noinspection unchecked
            return session.createCriteria(FrontendDirector.class).list();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<FrontendDirectorInfo> getDirectorInfoListById(long id) {
        Session session = null;

        try {
            session = sessionFactory.openSession();
            //noinspection unchecked
            return session.createCriteria(FrontendDirectorInfo.class)
                    .add(Restrictions.eq("director.id", id)).list();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<FrontendDirectorInfo> getDirectorsInfoList() {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            //noinspection unchecked
            return session.createCriteria(FrontendDirectorInfo.class).list();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}

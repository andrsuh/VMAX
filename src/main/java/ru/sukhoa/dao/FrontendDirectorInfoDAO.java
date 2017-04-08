package ru.sukhoa.dao;


import com.sun.istack.internal.NotNull;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sukhoa.domain.FrontendDirectorInfo;

import java.util.List;

@Component
public class FrontendDirectorInfoDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @NotNull
    public List<FrontendDirectorInfo> getDirectorInfoListById(long id) {
        Session session = sessionFactory.openSession();

        //noinspection unchecked
        List<FrontendDirectorInfo> result = session.createCriteria(FrontendDirectorInfo.class)
                .add(Restrictions.eq("directorKey", id))
                .list();

        session.close();
        return result;
    }

    @NotNull
    public List<FrontendDirectorInfo> getDirectorsInfoList() {
        Session session = sessionFactory.openSession();
        //noinspection unchecked
        List<FrontendDirectorInfo> result = session.createCriteria(FrontendDirectorInfo.class).list();
        session.close();

        return result;
    }
}

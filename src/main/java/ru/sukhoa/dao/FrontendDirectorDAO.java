package ru.sukhoa.dao;


import com.sun.istack.internal.Nullable;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sukhoa.domain.FrontendDirector;

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
        Session session = sessionFactory.openSession();
        FrontendDirector frontendDirector = (FrontendDirector) session.get(FrontendDirector.class, id);
        session.close();
        return frontendDirector;
    }

    @Nullable
    public List<FrontendDirector> getDirectorsList() {
        Session session = sessionFactory.openSession();
        //noinspection unchecked
        List<FrontendDirector> frontendDirectors = session.createCriteria(FrontendDirector.class).list();
        session.close();
        return frontendDirectors;
    }

//    @NotNull
//    public List<DateTimeStatement> getTime() {
//        Session session = sessionFactory.openSession();
//        //noinspection unchecked
//        List<DateTimeStatement> times = session.createCriteria(DateTimeStatement.class).list();
//        session.close();
//        return times;
//    }
}

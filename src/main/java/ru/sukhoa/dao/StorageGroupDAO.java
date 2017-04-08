package ru.sukhoa.dao;

import com.sun.istack.internal.NotNull;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sukhoa.domain.StorageGroup;
import ru.sukhoa.domain.StorageGroupInfo;

import java.util.List;

@Component
public class StorageGroupDAO {
    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @NotNull
    public List<StorageGroup> getStorageGroupList() {
        Session session = sessionFactory.openSession();
        //noinspection unchecked
        List<StorageGroup> result = session.createCriteria(StorageGroup.class).list();
        session.close();

        return result;
    }
}

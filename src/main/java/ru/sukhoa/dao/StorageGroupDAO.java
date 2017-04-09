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
        Session session = null;
        try {
            session = sessionFactory.openSession();
            //noinspection unchecked
            return session.createCriteria(StorageGroup.class).list();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @NotNull
    public List<StorageGroupInfo> getStorageGroupInfoList() {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            //noinspection unchecked
            return session.createCriteria(StorageGroupInfo.class).list();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}

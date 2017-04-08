package ru.sukhoa.servicies;

import com.sun.istack.internal.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sukhoa.dao.StorageGroupDAO;
import ru.sukhoa.dao.StorageGroupInfoDAO;
import ru.sukhoa.domain.StorageGroup;
import ru.sukhoa.domain.StorageGroupInfo;

import java.util.List;

@Service
public class StorageGroupService {
    private StatisticsService statisticsService;

    private StorageGroupInfoDAO storageGroupInfoDAO;

    private StorageGroupDAO storageGroupDAO;

    @Autowired
    public void setStorageGroupDAO(StorageGroupDAO storageGroupDAO) {
        this.storageGroupDAO = storageGroupDAO;
    }

    @Autowired
    public void setStorageGroupInfoDAO(StorageGroupInfoDAO storageGroupInfoDAO) {
        this.storageGroupInfoDAO = storageGroupInfoDAO;
    }

    @Autowired
    public void setStatisticsService(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @NotNull
    public List<StorageGroup> getStorageGroupList() {
        return storageGroupDAO.getStorageGroupList();
    }

    @NotNull
    public List<StorageGroupInfo> getStorageGroupInfoList() {
        return storageGroupInfoDAO.getStorageGroupInfoList();
    }

}

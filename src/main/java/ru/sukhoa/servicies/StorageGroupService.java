package ru.sukhoa.servicies;

import com.sun.istack.internal.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sukhoa.dao.StorageGroupDAO;
import ru.sukhoa.dao.StorageGroupInfoDAO;
import ru.sukhoa.domain.FrontendDirector;
import ru.sukhoa.domain.StorageGroup;
import ru.sukhoa.domain.StorageGroupInfo;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StorageGroupService {
    private StatisticsService statisticsService;

    private StorageGroupInfoDAO storageGroupInfoDAO;

    private FrontendDirectorService frontendDirectorService;

    private StorageGroupDAO storageGroupDAO;

    @Autowired
    public void setFrontendDirectorService(FrontendDirectorService frontendDirectorService) {
        this.frontendDirectorService = frontendDirectorService;
    }

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

    public List<StorageGroup> getProblemGroupsForDirector(long directorId, Date fromDate, Date toDate) {
        boolean problemDetected = frontendDirectorService.getProblemDirectors(fromDate, toDate).stream()
                .map(FrontendDirector::getId)
                .anyMatch(x -> x.compareTo(directorId) == 0);

        if (!problemDetected) {
            throw new RuntimeException("No problems connected with specified director was found");
        }

        return storageGroupInfoDAO.getStorageGroupInfoList().stream()
                .filter(info -> info.satisfiedDate(fromDate, toDate)
                        && info.getSummaryBucketRate() > statisticsService.getResponseTimeSummaryRateUpperBound()
                        && info.getMbRate() > statisticsService.getGroupsMbRateUpperBound())
                .map(StorageGroupInfo::getStorageGroup)
                .distinct()
                .collect(Collectors.toList());
    }
}

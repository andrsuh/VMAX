package ru.sukhoa.servicies;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sukhoa.dao.StorageGroupDAO;
import ru.sukhoa.domain.StorageGroup;
import ru.sukhoa.domain.StorageGroupInfo;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StorageGroupService {
    private StatisticsService statisticsService;

    private FrontendDirectorService frontendDirectorService;

    private StorageGroupDAO storageGroupDAO;

    @Autowired
    public StorageGroupService(StatisticsService statisticsService,
                               FrontendDirectorService frontendDirectorService, StorageGroupDAO storageGroupDAO) {
        this.statisticsService = statisticsService;
        this.frontendDirectorService = frontendDirectorService;
        this.storageGroupDAO = storageGroupDAO;
    }

    public List<StorageGroup> getStorageGroupList() {
        return storageGroupDAO.getStorageGroupList();
    }

    public List<StorageGroup> getProblemGroupsForDirector(long directorId, @Nullable Date fromDate, @Nullable Date toDate) {
        // getting all dates between (fromDate, toDate) where problems with director (with specified id) occured
        final Set<Date> problemDates = frontendDirectorService.getProblemDirectorsInfoStream(fromDate, toDate)
                .filter(info -> info.getDirector().getId() == directorId)
                .map(info -> info.getDateStamp().getDatestamp())
                .collect(Collectors.toCollection(HashSet::new));

        if (problemDates.isEmpty()) {
            throw new RuntimeException("No problems connected with specified director was found");
        }

        // getting all SG which have been high loaded during same dates as a specified director
        return storageGroupDAO.getStorageGroupInfoList().stream()
                .filter(info -> problemDates.contains(info.getDateStamp().getDatestamp())
                        && info.getSummaryBucketRate() > statisticsService.getResponseTimeSummaryRateUpperBound()
                        && info.getMbRate() > statisticsService.getGroupsMbRateUpperBound())
                .map(StorageGroupInfo::getStorageGroup)
                .distinct()
                .collect(Collectors.toList());
    }
}

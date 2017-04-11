package ru.sukhoa.servicies;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sukhoa.dao.FrontendDirectorDAO;
import ru.sukhoa.domain.FrontendDirector;
import ru.sukhoa.domain.FrontendDirectorInfo;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FrontendDirectorService {

    private FrontendDirectorDAO frontendDirectorDAO;

    private StatisticsService statisticsService;

    @Autowired
    public FrontendDirectorService(FrontendDirectorDAO frontendDirectorDAO, StatisticsService statisticsService) {
        this.frontendDirectorDAO = frontendDirectorDAO;
        this.statisticsService = statisticsService;
    }

    public List<FrontendDirector> getDirectorsList() {
        return frontendDirectorDAO.getDirectorsList();
    }

    public List<FrontendDirector> getProblemDirectors(@Nullable Date fromDate, @Nullable Date toDate) {
        return getProblemDirectorsInfoStream(fromDate, toDate)
                .map(FrontendDirectorInfo::getDirector)
                .distinct()
                .collect(Collectors.toList());
    }

    public Stream<FrontendDirectorInfo> getProblemDirectorsInfoStream(@Nullable Date fromDate, @Nullable Date toDate) {
        return frontendDirectorDAO.getDirectorsInfoList().stream()
                .filter(info -> info.satisfiedDate(fromDate, toDate)
                        && (info.getSummaryBucketRate() > statisticsService.getQueueSummaryRateUpperBound()
                        && info.getMbRate() > statisticsService.getDirectorsMbRateUpperBound()));
    }

    public List<FrontendDirectorInfo> getDirectorInfoListById(long id, @Nullable Date fromDate, @Nullable Date toDate) {
        return frontendDirectorDAO.getDirectorInfoListById(id).stream()
                .filter(info -> info.satisfiedDate(fromDate, toDate))
                .collect(Collectors.toList());
    }

    public FrontendDirector getFrontendDirector(long id) {
        FrontendDirector feDirector = frontendDirectorDAO.getDirector(id);
        if (feDirector == null) {
            throw new IllegalArgumentException("Director with specified id does not exist");
        }
        return feDirector;
    }
}

package ru.sukhoa.servicies;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sukhoa.dao.FrontendDirectorDAO;
import ru.sukhoa.dao.FrontendDirectorInfoDAO;
import ru.sukhoa.domain.FrontendDirector;
import ru.sukhoa.domain.FrontendDirectorInfo;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FrontendDirectorService {

    private FrontendDirectorDAO frontendDirectorDAO;

    private FrontendDirectorInfoDAO frontendDirectorInfoDAO;

    private StatisticsService statisticsService;

    @Autowired
    public void setFrontendDirectorInfoDAO(FrontendDirectorInfoDAO frontendDirectorInfoDAO) {
        this.frontendDirectorInfoDAO = frontendDirectorInfoDAO;
    }

    @Autowired
    public void setFrontendDirectorDAO(FrontendDirectorDAO frontendDirectorDAO) {
        this.frontendDirectorDAO = frontendDirectorDAO;
    }

    @Autowired
    public void setStatisticsService(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @NotNull
    public FrontendDirector getFrontendDirector(long id) {
        FrontendDirector feDirector = frontendDirectorDAO.getDirector(id);
        if (feDirector == null) {
            throw new IllegalArgumentException("Director with specified id does not exist");
        }
        return feDirector;
    }

    @NotNull
    public List<FrontendDirector> getAllDirectors() {
        return frontendDirectorDAO.getAllDirectors();
    }

    @NotNull
    public List<FrontendDirectorInfo> getDirectorInfoListById(long id) {
        return frontendDirectorInfoDAO.getDirectorInfoListById(id);
    }

    @NotNull
    public List<FrontendDirector> getProblemDirectors(@Nullable Date fromDate, @Nullable Date toDate) {
        return frontendDirectorInfoDAO.getDirectorsInfoList().stream()
                .filter(info -> info.satisfiedDate(fromDate, toDate)
                        && info.getSummaryBucketRate() > statisticsService.getQueueSummaryRateUpperBound()
                        && info.getMbRate() > statisticsService.getDirectorsMbRateUpperBound())
                .map(x -> x.getDirector().getId())
                .distinct()
                .map(frontendDirectorDAO::getDirector)
                .collect(Collectors.toList());
    }
}

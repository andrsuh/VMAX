package ru.sukhoa.servicies;

import com.google.common.math.Quantiles;
import com.sun.istack.internal.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sukhoa.dao.FrontendDirectorInfoDAO;
import ru.sukhoa.domain.FrontendDirectorInfo;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private FrontendDirectorInfoDAO frontendDirectorInfoDAO;

    private volatile Double queueSummaryRateUpperBound;

    private volatile Double directorsMbRateUpperBound;

    @Autowired
    public void setFrontendDirectorInfoDAO(FrontendDirectorInfoDAO frontendDirectorInfoDAO) {
        this.frontendDirectorInfoDAO = frontendDirectorInfoDAO;
    }

    @PostConstruct
    public void initialize() {
        List<FrontendDirectorInfo> infos = frontendDirectorInfoDAO.getDirectorsInfoList();
        computeDirectorMbUpperBound(infos);
        computeQueueBucketUpperBound(infos);
    }

    private void computeQueueBucketUpperBound(@NotNull List<FrontendDirectorInfo> directorsInfoList) {
        List<Double> summaryBucketRateFunction = directorsInfoList.stream()
                .map(FrontendDirectorInfo::getSummaryBucketRate)
                .collect(Collectors.toList());

        queueSummaryRateUpperBound = computeBound(summaryBucketRateFunction);
    }

    private void computeDirectorMbUpperBound(@NotNull List<FrontendDirectorInfo> directorsInfoList) {
        List<Double> mbRateFunction = directorsInfoList.stream()
                .map(FrontendDirectorInfo::getMbRate)
                .collect(Collectors.toList());

        directorsMbRateUpperBound = computeBound(mbRateFunction);
    }

    private double computeBound(@NotNull List<? extends Number> values) {
        Map<Integer, Double> quantiles = Quantiles.quartiles().indexes(Arrays.asList(1, 3)).compute(values);
        return quantiles.get(3) + 1.5 * (quantiles.get(3) - quantiles.get(1));
    }


    @NotNull
    public Double getQueueSummaryRateUpperBound() {
        if (queueSummaryRateUpperBound == null) {
            synchronized (this) {
                if (queueSummaryRateUpperBound == null) {
                    computeQueueBucketUpperBound(frontendDirectorInfoDAO.getDirectorsInfoList());
                }
            }
        }
        return queueSummaryRateUpperBound;
    }

    @NotNull
    public Double getDirectorsMbRateUpperBound() {
        if (directorsMbRateUpperBound == null) {
            synchronized (this) {
                if (directorsMbRateUpperBound == null) {
                    computeDirectorMbUpperBound(frontendDirectorInfoDAO.getDirectorsInfoList());
                }
            }
        }
        return directorsMbRateUpperBound;
    }
}

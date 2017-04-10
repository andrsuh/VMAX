package ru.sukhoa.servicies;

import com.google.common.math.Quantiles;
import com.sun.istack.internal.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sukhoa.dao.FrontendDirectorDAO;
import ru.sukhoa.dao.StorageGroupDAO;
import ru.sukhoa.domain.BaseInfo;
import ru.sukhoa.domain.FrontendDirectorInfo;
import ru.sukhoa.domain.StorageGroupInfo;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsService {
    private FrontendDirectorDAO frontendDirectorDAO;

    private StorageGroupDAO storageGroupDAO;

    private volatile Double queueSummaryRateUpperBound;

    private volatile Double directorsMbRateUpperBound;

    private volatile Double responseTimeSummaryRateUpperBound;

    private volatile Double groupsMbRateUpperBound;

    @Autowired
    public StatisticsService(FrontendDirectorDAO frontendDirectorDAO, StorageGroupDAO storageGroupDAO) {
        this.frontendDirectorDAO = frontendDirectorDAO;
        this.storageGroupDAO = storageGroupDAO;
    }

    @PostConstruct
    public void initialize() {
        List<FrontendDirectorInfo> dInfos = frontendDirectorDAO.getDirectorsInfoList();
        List<StorageGroupInfo> gInfos = storageGroupDAO.getStorageGroupInfoList();
        queueSummaryRateUpperBound = computeBucketsUpperBound(dInfos);
        directorsMbRateUpperBound = computeMbUpperBound(dInfos);
        responseTimeSummaryRateUpperBound = computeBucketsUpperBound(gInfos);
        groupsMbRateUpperBound = computeMbUpperBound(gInfos);
    }

    private double computeBucketsUpperBound(@NotNull List<? extends BaseInfo> directorsInfoList) {
        List<Double> bucketRateValues = directorsInfoList.stream()
                .map(BaseInfo::getSummaryBucketRate)
                .filter(rate -> rate > 0.0)
                .collect(Collectors.toList());

        return computeBound(bucketRateValues);
    }

    private double computeMbUpperBound(@NotNull List<? extends BaseInfo> directorsInfoList) {
        List<Double> mbRateValues = directorsInfoList.stream()
                .map(BaseInfo::getMbRate)
                .collect(Collectors.toList());

        return computeBound(mbRateValues);
    }

    private double computeBound(@NotNull List<? extends Number> values) {
        Map<Integer, Double> quantiles = Quantiles.quartiles().indexes(Arrays.asList(1, 3)).compute(values);
        return quantiles.get(3) + 1.5 * (quantiles.get(3) - quantiles.get(1));
    }

    @NotNull
    public Double getQueueSummaryRateUpperBound() {
        return queueSummaryRateUpperBound;
    }

    @NotNull
    public Double getDirectorsMbRateUpperBound() {
        return directorsMbRateUpperBound;
    }

    @NotNull
    public Double getResponseTimeSummaryRateUpperBound() {
        return responseTimeSummaryRateUpperBound;
    }

    @NotNull
    public Double getGroupsMbRateUpperBound() {
        return groupsMbRateUpperBound;
    }
}

package ru.sukhoa.servicies;

import com.google.common.math.Quantiles;
import com.sun.istack.internal.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sukhoa.dao.FrontendDirectorInfoDAO;
import ru.sukhoa.dao.StorageGroupInfoDAO;
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

    private FrontendDirectorInfoDAO frontendDirectorInfoDAO;

    private StorageGroupInfoDAO storageGroupInfoDAO;

    private volatile Double queueSummaryRateUpperBound;

    private volatile Double directorsMbRateUpperBound;

    private volatile Double responseTimeSummaryRateUpperBound;

    private volatile Double groupsMbRateUpperBound;

    @Autowired
    public void setFrontendDirectorInfoDAO(FrontendDirectorInfoDAO frontendDirectorInfoDAO) {
        this.frontendDirectorInfoDAO = frontendDirectorInfoDAO;
    }

    @Autowired
    public void setStorageGroupInfoDAO(StorageGroupInfoDAO storageGroupInfoDAO) {
        this.storageGroupInfoDAO = storageGroupInfoDAO;
    }

    @PostConstruct
    public void initialize() {
        List<FrontendDirectorInfo> dInfos = frontendDirectorInfoDAO.getDirectorsInfoList();
        List<StorageGroupInfo> gInfos = storageGroupInfoDAO.getStorageGroupInfoList();
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
        if (queueSummaryRateUpperBound == null) {
            synchronized (this) {
                if (queueSummaryRateUpperBound == null) {
                    computeBucketsUpperBound(frontendDirectorInfoDAO.getDirectorsInfoList());
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
                    computeMbUpperBound(frontendDirectorInfoDAO.getDirectorsInfoList());
                }
            }
        }
        return directorsMbRateUpperBound;
    }

    @NotNull
    public Double getResponseTimeSummaryRateUpperBound() {
        if (responseTimeSummaryRateUpperBound == null) {
            synchronized (this) {
                if (responseTimeSummaryRateUpperBound == null) {
                    computeBucketsUpperBound(storageGroupInfoDAO.getStorageGroupInfoList());
                }
            }
        }
        return responseTimeSummaryRateUpperBound;
    }

    @NotNull
    public Double getGroupsMbRateUpperBound() {
        if (groupsMbRateUpperBound == null) {
            synchronized (this) {
                if (groupsMbRateUpperBound == null) {
                    computeMbUpperBound(storageGroupInfoDAO.getStorageGroupInfoList());
                }
            }
        }
        return groupsMbRateUpperBound;
    }
}

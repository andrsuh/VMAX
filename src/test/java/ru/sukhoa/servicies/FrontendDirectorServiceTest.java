package ru.sukhoa.servicies;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import ru.sukhoa.dao.FrontendDirectorDAO;
import ru.sukhoa.domain.FrontendDirectorInfo;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FrontendDirectorServiceTest {

    private static final double THRESHOLD = 100;

    @Mock
    private FrontendDirectorDAO frontendDirectorDAO;

    @Mock
    private StatisticsService statisticsService;

    @Spy
    @InjectMocks
    private FrontendDirectorService frontendDirectorService;

    @Before
    public void initialize() {
        when(statisticsService.getQueueSummaryRateUpperBound()).thenReturn(THRESHOLD);
        when(statisticsService.getDirectorsMbRateUpperBound()).thenReturn(THRESHOLD);
    }

    @After
    public void checkMethodWasCalled() {
        verify(frontendDirectorService, atLeastOnce()).getProblemDirectorsInfoStream(null, null);
    }

    @Test
    public void testInfoListIsEmpty() throws Exception {
        when(frontendDirectorDAO.getDirectorsInfoList()).thenReturn(Collections.emptyList());
        Stream<FrontendDirectorInfo> result = frontendDirectorService.getProblemDirectorsInfoStream(null, null);
        assertEquals(Stream.empty().count(), result.count());
    }

    @Test
    public void testIfAllDirectorsNormal() {
        int size = 100;

        List<FrontendDirectorInfo> mockedInfos = getMocketInformationWithRateInRange(size, 0, THRESHOLD - 1);

        Stream<FrontendDirectorInfo> result = frontendDirectorService.getProblemDirectorsInfoStream(null, null);
        assertEquals(0, result.count());
    }

    @Test
    public void testIfAllDirectorsProblem() {
        int size = 100;

        List<FrontendDirectorInfo> mockedInfos = getMocketInformationWithRateInRange(
                size, THRESHOLD + 1, 2 * THRESHOLD);

        when(frontendDirectorDAO.getDirectorsInfoList()).thenReturn(mockedInfos);

        Stream<FrontendDirectorInfo> result = frontendDirectorService.getProblemDirectorsInfoStream(null, null);
        assertEquals(size, result.count());
    }

    @Test
    public void testIfCorrectComputingProblemDirectors() {
        int size = 100;

        List<FrontendDirectorInfo> mockedInfos = getMocketInformationWithRateInRange(size, 0, THRESHOLD + 1);
        mockedInfos.addAll(getMocketInformationWithRateInRange(size, THRESHOLD + 1, 2 * THRESHOLD));

        when(frontendDirectorDAO.getDirectorsInfoList()).thenReturn(mockedInfos);

        Stream<FrontendDirectorInfo> result = frontendDirectorService.getProblemDirectorsInfoStream(null, null);
        assertEquals(size, result.count());
    }

    private List<FrontendDirectorInfo> getMocketInformationWithRateInRange(int size, double leftBound, double rightBound) {
        return Stream.generate(() -> {
            FrontendDirectorInfo info = mock(FrontendDirectorInfo.class);
            when(info.satisfiedDate(null, null)).thenReturn(true);
            when(info.getSummaryBucketRate()).thenReturn((Math.random() * (rightBound - leftBound) + leftBound));
            when(info.getMbRate()).thenReturn((Math.random() * (rightBound - leftBound) + leftBound));
            return info;
        }).limit(size).collect(Collectors.toList());
    }
}
package ru.sukhoa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sukhoa.domain.FrontendDirector;
import ru.sukhoa.domain.FrontendDirectorInfo;
import ru.sukhoa.domain.StorageGroup;
import ru.sukhoa.servicies.FrontendDirectorService;
import ru.sukhoa.servicies.StorageGroupService;

import java.util.Date;
import java.util.List;


@RestController
public class MainController {

    private FrontendDirectorService directorService;

    private StorageGroupService storageGroupService;

    @Autowired
    public MainController(FrontendDirectorService directorService, StorageGroupService storageGroupService) {
        this.directorService = directorService;
        this.storageGroupService = storageGroupService;
    }

    @RequestMapping(value = "problem_directors", method = RequestMethod.GET)
    public List<FrontendDirector> getProblemDirectors(
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date fromDate,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date toDate) {
        return directorService.getProblemDirectors(fromDate, toDate);
    }

    @RequestMapping(value = "problem_groups", method = RequestMethod.GET)
    public List<StorageGroup> getProblemGroupsForDirector(
            @RequestParam(value = "director_id", required = true) long directorId,
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date fromDate,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date toDate) {
        return storageGroupService.getProblemGroupsForDirector(directorId, fromDate, toDate);
    }

    @RequestMapping(value = "directors_info", method = RequestMethod.GET)
    public List<FrontendDirectorInfo> getDirectorInfo(
            @RequestParam(value = "director_id", required = true) long directorId,
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date fromDate,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date toDate) {
        return directorService.getDirectorInfoListById(directorId, fromDate, toDate);
    }

    @RequestMapping(value = "directors", method = RequestMethod.GET)
    public List<FrontendDirector> getAllDirectors() {
        return directorService.getDirectorsList();
    }

    @RequestMapping(value = "groups", method = RequestMethod.GET)
    public List<StorageGroup> getAllStorageGroups() {
        return storageGroupService.getStorageGroupList();
    }
}

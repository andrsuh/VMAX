package ru.sukhoa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sukhoa.domain.FrontendDirector;
import ru.sukhoa.servicies.FrontendDirectorService;

import java.util.Date;
import java.util.List;


@RestController
public class FrontendDirectorController {

    private FrontendDirectorService directorService;

    @Autowired
    public void setDirectorService(FrontendDirectorService directorService) {
        this.directorService = directorService;
    }

    @RequestMapping(value = "problem_directors", method = RequestMethod.GET)
    public List<FrontendDirector> getProblemDirectors(
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date fromDate,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date toDate) {
        return directorService.getProblemDirectors(fromDate, toDate);
    }

    @RequestMapping(value = "directors", method = RequestMethod.GET)
    public List<FrontendDirector> getAllDirectors() {
        return directorService.getAllDirectors();
    }
}

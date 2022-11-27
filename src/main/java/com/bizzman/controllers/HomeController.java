package com.bizzman.controllers;

import com.bizzman.dao.services.BusinessInformationService;
import com.bizzman.entities.BusinessInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/", produces = {MediaType.TEXT_HTML_VALUE })
public class HomeController {

    @Autowired
    private BusinessInformationService businessInformationService;

    @GetMapping
    public String getPageData(Model model) {
        BusinessInformation businessInformation = businessInformationService.getBusinessInformation().orElseThrow();
        model.addAttribute( "businessInformation", businessInformation);
        return "home/index";
    }
}

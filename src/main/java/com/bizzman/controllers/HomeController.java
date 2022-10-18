package com.bizzman.controllers;

import com.bizzman.dao.services.EmployeeService;
import com.bizzman.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "/", produces = {MediaType.TEXT_HTML_VALUE })
public class HomeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public String getPageData(Model model) {
        List<Employee> employees = (List<Employee>) employeeService.findAllEmployees();
        model.addAttribute( "employee", employees.get(0));
        return "home/index";
    }
}

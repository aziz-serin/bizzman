/*
 * Copyright 2022 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */

package com.bizzman.controllers;

import java.util.List;

import org.h2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bizzman.dao.services.EmployeeService;
import com.bizzman.entities.Employee;
import org.springframework.web.bind.annotation.RequestParam;
import org.webjars.NotFoundException;

@Controller
@RequestMapping(value = "/employees", produces = {MediaType.TEXT_HTML_VALUE })
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public String getPageData(Model model, @RequestParam(required = false) String sorted) {
        List<Employee> employees;
        if (StringUtils.isNullOrEmpty(sorted)) {
            model.addAttribute("employees", employeeService.findAllEmployees());
            return "employees/index";
        }
        switch (sorted) {
            case "age":
                employees = (List<Employee>) employeeService.getAllEmployeeSortedByAgeAscending();
                break;
            case "salary":
                employees = (List<Employee>) employeeService.getAllEmployeeSortedBySalaryAscending();
                break;
            case "date":
                employees = (List<Employee>) employeeService.getAllEmployeeSortedByJoiningDateAscending();
                break;
            default:
                employees = (List<Employee>) employeeService.findAllEmployees();
        }
        model.addAttribute( "employees", employees);
        return "employees/index";
    }

    @GetMapping("/{id}")
    public String getEvent(@PathVariable("id") long id, Model model) {
        Employee employee = employeeService.findEmployeeById(id).orElseThrow(() -> new NotFoundException("Employee not found!"));
        model.addAttribute("employee", employee);
        return "/employees/detailed";
    }
}

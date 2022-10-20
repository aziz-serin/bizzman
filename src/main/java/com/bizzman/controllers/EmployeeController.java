/*
 * Copyright 2022 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */

package com.bizzman.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bizzman.dao.services.EmployeeService;
import com.bizzman.entities.Employee;

@Controller
@RequestMapping(value = "/employee", produces = {MediaType.TEXT_HTML_VALUE })
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public String getPageData(Model model) {
        List<Employee> employees = (List<Employee>) employeeService.findAllEmployees();
        model.addAttribute( "employees", employees);
        return "employee/index";
    }
}

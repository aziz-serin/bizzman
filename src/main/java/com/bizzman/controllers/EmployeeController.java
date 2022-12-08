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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.bizzman.dao.services.EmployeeService;
import com.bizzman.entities.Employee;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.webjars.NotFoundException;

import javax.validation.Valid;

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
        Employee employee = employeeService.findEmployeeById(id).orElseThrow(()
                -> new NotFoundException("Employee not found!"));
        model.addAttribute("employee", employee);
        return "/employees/detailed";
    }

    @GetMapping("/editEmployees/{id}")
    public String getEmployeeEditor(@PathVariable("id") long id, Model model) {
        Employee employee = employeeService.findEmployeeById(id).orElseThrow(()
                -> new NotFoundException("Employee " + id + " not found"));
        model.addAttribute("employee", employee);
        return "employees/editor";
    }

    @PostMapping("/editEmployees/{id}")
    public String updateEmployee(@Valid Employee employee, BindingResult errors,
                                 Model model, RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            model.addAttribute("employee", employee);
            return "/employees/editor";
        }
        employeeService.save(employee);
        redirectAttributes.addFlashAttribute("ok_message", "Employee Details Updated!");
        return "/employees/detailed";
    }

    @DeleteMapping("/{id}")
    public String deleteEmployeeDetails(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        if (employeeService.findEmployeeById(id).isEmpty()) {
            throw new NotFoundException("Employee is not found!");
        }
        employeeService.delete(id);
        redirectAttributes.addFlashAttribute("ok_message", "Employee Details Deleted!");
        return "redirect:/employees";
    }

















}

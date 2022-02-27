package de.niklaseckert.epro_project.controller;

import de.niklaseckert.epro_project.repos.BusinessUnitRepository;
import de.niklaseckert.epro_project.repos.CompanyObjectiveRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {

    private final CompanyObjectiveRepository companyObjectiveRepository;
    private final BusinessUnitRepository businessUnitRepository;

    @GetMapping
    public String getDashboard(Model model) {
        model.addAttribute("company_objectives", companyObjectiveRepository.findAll());
        model.addAttribute("business_units", businessUnitRepository.findAll());
        return "dashboard.html";
    }
}

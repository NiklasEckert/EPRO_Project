package de.niklaseckert.epro_project.controller;

import de.niklaseckert.epro_project.repos.BusinessUnitRepository;
import de.niklaseckert.epro_project.repos.CompanyObjectiveRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller which handles all Dashboard requests.
 *
 * @author Niklas Eckert
 * @author Jakob Friedsam
 * @author Fabian Schulz
 */
@Controller
@AllArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {

    /** Repository which contains the {@link de.niklaseckert.epro_project.model.CompanyObjective Company Objectives}. */
    private final CompanyObjectiveRepository companyObjectiveRepository;

    /** Repository which contains the {@link de.niklaseckert.epro_project.model.BusinessUnit Business Units}. */
    private final BusinessUnitRepository businessUnitRepository;

    /**
     * Get Mapping for the Dashboard.
     *
     * @param model contains data that could be displayed on the webpage.
     * @return the name of the html page.
     */
    @GetMapping
    public String getDashboard(Model model) {
        model.addAttribute("company_objectives", companyObjectiveRepository.findAll());
        model.addAttribute("business_units", businessUnitRepository.findAll());
        return "dashboard.html";
    }
}

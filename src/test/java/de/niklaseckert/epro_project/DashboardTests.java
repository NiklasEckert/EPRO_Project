package de.niklaseckert.epro_project;

import de.niklaseckert.epro_project.repos.BusinessUnitRepository;
import de.niklaseckert.epro_project.repos.CompanyObjectiveRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DashboardTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompanyObjectiveRepository companyObjectiveRepository;

    @MockBean
    private BusinessUnitRepository businessUnitRepository;

    @Test
    @WithMockUser(roles = "READ_ONLY")
    public void getDashboard_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/dashboard")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attribute("company_objectives", companyObjectiveRepository.findAll()))
                .andExpect(model().attribute("business_units", businessUnitRepository.findAll()));
    }

    @Test
    @WithMockUser(roles = { "CO_OKR_ADMIN", "BUO_OKR_ADMIN" })
    public void getDashboard_isForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/dashboard")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isForbidden());
    }
}

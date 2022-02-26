package de.niklaseckert.epro_project;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.niklaseckert.epro_project.controller.exceptions.CompanyObjectiveNotFoundException;
import de.niklaseckert.epro_project.model.CompanyObjective;
import de.niklaseckert.epro_project.model.User;
import de.niklaseckert.epro_project.repos.CompanyObjectiveRepository;
import de.niklaseckert.epro_project.repos.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyObjectiveTests {

    @Autowired
    private MockMvc mockMvc;

    private final User user1 = User.builder().id(1L).username("TestUser1").password("TestPassword1").build();
    private final CompanyObjective CO1 = CompanyObjective.builder().id(1L).name("O1/2023").description("TestCompanyObjective1").user(user1).build();
    private final CompanyObjective CO2 = CompanyObjective.builder().id(2L).name("O2/2023").description("TestCompanyObjective2").user(user1).build();
    private final CompanyObjective CO3 = CompanyObjective.builder().id(3L).name("O3/2023").description("TestCompanyObjective3").user(user1).build();
    private final CompanyObjective CO4 = CompanyObjective.builder().id(4L).name("O4/2023").description("TestCompanyObjective4").user(user1).build();

    @MockBean
    private CompanyObjectiveRepository companyObjectiveRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockUser(roles = "BUO_OKR_ADMIN")
    public void getCheckRestriction_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/co")
                        .accept(MediaType.ALL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "READ_ONLY")
    public void getAllCompanyObjectives_success() throws Exception {
        Mockito.when(companyObjectiveRepository.findAll()).thenReturn(Arrays.asList(CO1, CO2, CO3, CO4));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/co")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.companyObjectiveList").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.companyObjectiveList[*].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.companyObjectiveList[*].name").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.companyObjectiveList[*].description").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.companyObjectiveList[*].user").isNotEmpty());
    }

    @Test
    @WithMockUser(roles = "READ_ONLY")
    public void getCompanyObjectiveById_success() throws Exception {
        Mockito.when(companyObjectiveRepository.findById(CO1.getId())).thenReturn(Optional.of(CO1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/co/{id}", CO1.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user").isNotEmpty());
    }

    @Test
    @WithMockUser(roles = {"READ_ONLY", "BUO_OKR_ADMIN"})
    public void postCheckRestriction_success() throws Exception {
        Mockito.when(companyObjectiveRepository.findById(CO1.getId())).thenReturn(Optional.of(CO1));

        mockMvc.perform(MockMvcRequestBuilders
                .post("/co")
                .content(new ObjectMapper().writeValueAsString(CO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "TestUser1", password = "TestPassword1", roles = "CO_OKR_ADMIN")
    public void post_success() throws Exception {
        Mockito.when(companyObjectiveRepository.save(CO1)).thenReturn(CO1);
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));

        mockMvc.perform(MockMvcRequestBuilders
                .post("/co")
                .content(new ObjectMapper().writeValueAsString(CO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(CO1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(CO1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(CO1.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username").value(CO1.getUser().getUsername()));
    }

    @Test
    @WithMockUser(username = "TestUser1", password = "TestPassword1", roles = "CO_OKR_ADMIN")
    public void putCompanyObjective_success() throws Exception {
        Mockito.when(companyObjectiveRepository.save(CO1)).thenReturn(CO1);
        Mockito.when(companyObjectiveRepository.findById(CO1.getId())).thenReturn(Optional.of(CO1));
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));

        mockMvc.perform(MockMvcRequestBuilders
                .put("/co/{id}", CO1.getId())
                .content(new ObjectMapper().writeValueAsString(CO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(CO1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(CO1.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username").value(CO1.getUser().getUsername()));
    }

    @Test
    @WithMockUser(username = "TestUser1", password = "TestPassword1", roles = "CO_OKR_ADMIN")
    public void putCompanyObjective_notFound() throws Exception {
        Mockito.when(companyObjectiveRepository.save(CO1)).thenReturn(CO1);
        Mockito.when(companyObjectiveRepository.findById(CO1.getId())).thenReturn(Optional.of(CO1));
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));

        mockMvc.perform(MockMvcRequestBuilders
                .put("/co/{id}", CO1.getId())
                .content(new ObjectMapper().writeValueAsString(CO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("" + CO1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(CO1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(CO1.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username").value(CO1.getUser().getUsername()));
    }

    @Test
    @WithMockUser(username = "TestUser1", password = "TestPassword1", roles = "CO_OKR_ADMIN")
    public void patchCompanyObjective_success() throws Exception {
        Mockito.when(companyObjectiveRepository.save(CO1)).thenReturn(CO1);
        Mockito.when(companyObjectiveRepository.findById(CO1.getId())).thenReturn(Optional.of(CO1));
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/co/{id}", CO1.getId())
                .content(new ObjectMapper().writeValueAsString(CO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(CO1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(CO1.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username").value(CO1.getUser().getUsername()));
    }

    @Test
    @WithMockUser(username = "TestUser1", password = "TestPassword1", roles = "CO_OKR_ADMIN")
    public void patchCompanyObjective_notFound() throws Exception {
        Mockito.when(companyObjectiveRepository.save(CO1)).thenReturn(CO1);
        Mockito.when(companyObjectiveRepository.findById(CO1.getId())).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/co/{id}", CO1.getId())
                .content(new ObjectMapper().writeValueAsString(CO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "TestUser1", password = "TestPassword1", roles = "CO_OKR_ADMIN")
    public void deleteCompanyObjective_success() throws Exception {
        Mockito.when(companyObjectiveRepository.findById(CO1.getId())).thenReturn(Optional.of(CO1));
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/co/{id}", CO1.getId()))
                .andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser(username = "TestUser1", password = "TestPassword1", roles = "CO_OKR_ADMIN")
    public void deleteCompanyObjective_notFound() throws Exception {
        Mockito.when(companyObjectiveRepository.findById(5L)).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/co/{id}", 5L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CompanyObjectiveNotFoundException));
    }
}

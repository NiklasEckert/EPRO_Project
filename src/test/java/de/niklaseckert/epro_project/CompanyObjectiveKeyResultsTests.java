package de.niklaseckert.epro_project;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.niklaseckert.epro_project.controller.exceptions.CompanyObjectiveKeyResultNotFoundException;
import de.niklaseckert.epro_project.model.CompanyObjective;
import de.niklaseckert.epro_project.model.CompanyObjectiveKeyResult;
import de.niklaseckert.epro_project.model.HistoryCompanyObjectiveKeyResult;
import de.niklaseckert.epro_project.model.User;
import de.niklaseckert.epro_project.repos.CompanyObjectiveKeyResultRepository;
import de.niklaseckert.epro_project.repos.CompanyObjectiveRepository;
import de.niklaseckert.epro_project.repos.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompanyObjectiveKeyResultsTests {

    @Autowired
    private MockMvc mockMvc;

    private final User user1 = User.builder().id(1L).username("TestUser1").password("TestPassword1").build();
    private final CompanyObjective CO1 = CompanyObjective.builder().id(1L).name("O1/2023").description("TestCompanyObjective1").user(user1).build();
    private final CompanyObjective CO2 = CompanyObjective.builder().id(2L).name("O2/2023").description("TestCompanyObjective2").user(user1).build();
    private final CompanyObjective CO3 = CompanyObjective.builder().id(3L).name("O3/2023").description("TestCompanyObjective3").user(user1).build();
    private final CompanyObjective CO4 = CompanyObjective.builder().id(4L).name("O4/2023").description("TestCompanyObjective4").user(user1).build();
    private final CompanyObjectiveKeyResult COKR1 = CompanyObjectiveKeyResult.builder().id(1L).name("TestKR1").description("TestDescription1").current(5).goal(20).confidenceLevel(50).comment("test").companyObjective(CO1).user(user1).build();
    private final CompanyObjectiveKeyResult COKR2 = CompanyObjectiveKeyResult.builder().id(2L).name("TestKR2").description("TestDescription2").current(7).goal(10).confidenceLevel(75).comment("test").companyObjective(CO1).user(user1).build();
    private final CompanyObjectiveKeyResult COKR3 = CompanyObjectiveKeyResult.builder().id(3L).name("TestKR3").description("TestDescription3").current(2).goal(69).confidenceLevel(35).comment("test").companyObjective(CO1).user(user1).build();
    private final CompanyObjectiveKeyResult COKR4 = CompanyObjectiveKeyResult.builder().id(4L).name("TestKR1").description("TestDescription4").current(0).goal(1).confidenceLevel(60).comment("test").companyObjective(CO2).user(user1).build();
    private final CompanyObjectiveKeyResult COKR5 = CompanyObjectiveKeyResult.builder().id(5L).name("TestKR2").description("TestDescription5").current(2).goal(4).confidenceLevel(75).comment("test").companyObjective(CO2).user(user1).build();
    private final CompanyObjectiveKeyResult COKR6 = CompanyObjectiveKeyResult.builder().id(6L).name("TestKR3").description("TestDescription6").current(11).goal(10).confidenceLevel(60).comment("test").companyObjective(CO2).user(user1).build();
    private final HistoryCompanyObjectiveKeyResult HCOKR1 = HistoryCompanyObjectiveKeyResult.builder().id(1L).name("TestKR1").description("TestDescription1").current(5).goal(20).confidenceLevel(50).comment("test").companyObjective(CO1).user(user1).companyObjectiveKeyResult(COKR1).build();
    private final HistoryCompanyObjectiveKeyResult HCOKR2 = HistoryCompanyObjectiveKeyResult.builder().id(1L).name("TestKR1").description("TestDescription2").current(5).goal(20).confidenceLevel(49).comment("test1").companyObjective(CO1).user(user1).companyObjectiveKeyResult(COKR1).build();
    private final HistoryCompanyObjectiveKeyResult HCOKR3 = HistoryCompanyObjectiveKeyResult.builder().id(1L).name("TestKR1").description("TestDescription3").current(5).goal(20).confidenceLevel(48).comment("test2").companyObjective(CO1).user(user1).companyObjectiveKeyResult(COKR1).build();
    private final HistoryCompanyObjectiveKeyResult HCOKR4 = HistoryCompanyObjectiveKeyResult.builder().id(1L).name("TestKR1").description("TestDescription4").current(5).goal(20).confidenceLevel(47).comment("test3").companyObjective(CO1).user(user1).companyObjectiveKeyResult(COKR1).build();
    private final HistoryCompanyObjectiveKeyResult HCOKR5 = HistoryCompanyObjectiveKeyResult.builder().id(1L).name("TestKR1").description("TestDescription5").current(5).goal(20).confidenceLevel(46).comment("test4").companyObjective(CO1).user(user1).companyObjectiveKeyResult(COKR1).build();
    private final HistoryCompanyObjectiveKeyResult HCOKR6 = HistoryCompanyObjectiveKeyResult.builder().id(1L).name("TestKR1").description("TestDescription6").current(5).goal(20).confidenceLevel(45).comment("test5").companyObjective(CO1).user(user1).companyObjectiveKeyResult(COKR1).build();

    @MockBean
    private CompanyObjectiveRepository companyObjectiveRepository;

    @MockBean
    private CompanyObjectiveKeyResultRepository companyObjectiveKeyResultRepository;

    @MockBean
    private UserRepository userRepository;

    @BeforeAll
    public void setup() {
        CO1.setKeyResults(List.of(COKR1, COKR2, COKR3));
        CO2.setKeyResults(List.of(COKR4, COKR5, COKR6));
    }

    @Test
    @WithMockUser(roles = "BUO_OKR_ADMIN")
    public void checkRestriction_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/co/{id}/keyResults", CO1.getId())
                .accept(MediaType.ALL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "READ_ONLY")
    public void getAllCompanyObjectiveKeyResults_success() throws Exception {
        Mockito.when(companyObjectiveRepository.findById(CO1.getId())).thenReturn(Optional.of(CO1));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/co/{id}/keyResults", CO1.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.companyObjectiveKeyResultList").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.companyObjectiveKeyResultList[*].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.companyObjectiveKeyResultList[*].name").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.companyObjectiveKeyResultList[*].description").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.companyObjectiveKeyResultList[*].current").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.companyObjectiveKeyResultList[*].goal").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.companyObjectiveKeyResultList[*].confidenceLevel").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.companyObjectiveKeyResultList[*].comment").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.companyObjectiveKeyResultList[*].user.username").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.companyObjectiveKeyResultList[*].achievement").isNotEmpty());
    }

    @Test
    @WithMockUser(roles = "READ_ONLY")
    public void getCompanyObjectiveKeyResultById_success() throws Exception {
        Mockito.when(companyObjectiveRepository.findById((CO1.getId()))).thenReturn(Optional.of(CO1));
        Mockito.when(companyObjectiveKeyResultRepository.findById(COKR1.getId())).thenReturn(Optional.of(COKR1));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/co/{id}/keyResults/{kid}", CO1.getId(), COKR1.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.goal").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.confidenceLevel").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.achievement").isNotEmpty());
    }

    @Test
    @WithMockUser(roles = {"READ_ONLY", "BUO_OKR_ADMIN"})
    public void postCheckRestriction_success() throws Exception {
        Mockito.when(companyObjectiveRepository.findById(CO1.getId())).thenReturn(Optional.of(CO1));

        mockMvc.perform(MockMvcRequestBuilders
                .post("/co/{id}/keyResults", CO1.getId())
                .content(new ObjectMapper().writeValueAsString(CO1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "TestUser1", password = "TestPassword1", roles = "CO_OKR_ADMIN")
    public void post_success() throws Exception {
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        Mockito.when(companyObjectiveRepository.findById(CO1.getId())).thenReturn(Optional.of(CO1));
        Mockito.when(companyObjectiveKeyResultRepository.save(COKR1)).thenReturn(COKR1);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/co/{id}/keyResults", CO1.getId())
                .content(new ObjectMapper().writeValueAsString(COKR1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(COKR1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(COKR1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(COKR1.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.current").value(COKR1.getCurrent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.goal").value(COKR1.getGoal()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.confidenceLevel").value(COKR1.getConfidenceLevel()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").value(COKR1.getComment()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username").value(COKR1.getUser().getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.achievement").value(COKR1.getAchievement()));
    }

    @Test
    @WithMockUser(username = "TestUser1", password = "TestPassword1", roles = "CO_OKR_ADMIN")
    public void putCompanyObjectiveKeyResult_success() throws Exception {
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        Mockito.when(companyObjectiveRepository.findById(CO1.getId())).thenReturn(Optional.of(CO1));
        Mockito.when(companyObjectiveKeyResultRepository.findById(COKR1.getId())).thenReturn(Optional.of(COKR1));
        Mockito.when(companyObjectiveKeyResultRepository.save(COKR1)).thenReturn(COKR1);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/co/{id}/keyResults/{kid}", CO1.getId(), COKR1.getId())
                .content(new ObjectMapper().writeValueAsString(COKR1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(COKR1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(COKR1.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.current").value(COKR1.getCurrent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.goal").value(COKR1.getGoal()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.confidenceLevel").value(COKR1.getConfidenceLevel()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").value(COKR1.getComment()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username").value(COKR1.getUser().getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.achievement").value(COKR1.getAchievement()));
    }

    @Test
    @WithMockUser(username = "TestUser1", password = "TestPassword1", roles = "CO_OKR_ADMIN")
    public void putCompanyObjectiveKeyResult_notFound() throws Exception {
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        Mockito.when(companyObjectiveRepository.findById(CO1.getId())).thenReturn(Optional.of(CO1));
        Mockito.when(companyObjectiveKeyResultRepository.findById(COKR1.getId())).thenReturn(Optional.of(COKR1));
        Mockito.when(companyObjectiveKeyResultRepository.save(COKR1)).thenReturn(COKR1);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/co/{id}/keyResults/{kid}", CO1.getId(), 5L)
                .content(new ObjectMapper().writeValueAsString(COKR1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("" + COKR1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(COKR1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(COKR1.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.current").value(COKR1.getCurrent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.goal").value(COKR1.getGoal()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.confidenceLevel").value(COKR1.getConfidenceLevel()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").value(COKR1.getComment()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username").value(COKR1.getUser().getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.achievement").value(COKR1.getAchievement()));
    }

    @Test
    @WithMockUser(username = "TestUser1", password = "TestPassword1", roles = "CO_OKR_ADMIN")
    public void patchCompanyObjectiveKeyResult_success() throws Exception {
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        Mockito.when(companyObjectiveRepository.findById(CO1.getId())).thenReturn(Optional.of(CO1));
        Mockito.when(companyObjectiveKeyResultRepository.findById(COKR1.getId())).thenReturn(Optional.of(COKR1));
        Mockito.when(companyObjectiveKeyResultRepository.save(COKR1)).thenReturn(COKR1);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/co/{id}/keyResults/{kid}", CO1.getId(), COKR1.getId())
                .content(new ObjectMapper().writeValueAsString(COKR1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(COKR1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(COKR1.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.current").value(COKR1.getCurrent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.goal").value(COKR1.getGoal()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.confidenceLevel").value(COKR1.getConfidenceLevel()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").value(COKR1.getComment()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username").value(COKR1.getUser().getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.achievement").value(COKR1.getAchievement()));
    }

    @Test
    @WithMockUser(username = "TestUser1", password = "TestPassword1", roles = "CO_OKR_ADMIN")
    public void patchCompanyObjective_notFound() throws Exception {
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        Mockito.when(companyObjectiveRepository.findById(CO1.getId())).thenReturn(Optional.of(CO1));
        Mockito.when(companyObjectiveKeyResultRepository.findById(COKR1.getId())).thenReturn(Optional.of(COKR1));
        Mockito.when(companyObjectiveKeyResultRepository.save(COKR1)).thenReturn(COKR1);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/co/{id}/keyResults/{kid}", CO1.getId(), 7L)
                .content(new ObjectMapper().writeValueAsString(COKR1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "TestUser1", password = "TestPassword1", roles = "CO_OKR_ADMIN")
    public void deleteCompanyObjectiveKeyResult_success() throws Exception {
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        Mockito.when(companyObjectiveKeyResultRepository.findById(COKR1.getId())).thenReturn(Optional.of(COKR1));

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/co/{id}/keyResults/{kid}", CO1.getId(), COKR1.getId()))
                .andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser(username = "TestUser1", password = "TestPassword1", roles = "CO_OKR_ADMIN")
    public void deleteCompanyObjectiveKeyResult_notFound() throws Exception {
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        Mockito.when(companyObjectiveKeyResultRepository.findById(7L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/co/{id}/keyResults/{kid}", CO1.getId(), 7L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CompanyObjectiveKeyResultNotFoundException));
    }

    @Test
    @WithMockUser(username = "TestUser1", password = "TestPassword1", roles = "CO_OKR_ADMIN")
    public void getCompanyObjectiveKeyResultHistory_success() throws Exception {
        COKR1.setHistory(List.of(HCOKR1, HCOKR2, HCOKR3, HCOKR4, HCOKR5, HCOKR6));

        Mockito.when(companyObjectiveRepository.findById(CO1.getId())).thenReturn(Optional.of(CO1));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/co/{id}/keyResults/{kid}/history", CO1.getId(), COKR1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.historyCompanyObjectiveKeyResultList").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.historyCompanyObjectiveKeyResultList[*].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.historyCompanyObjectiveKeyResultList[*].name").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.historyCompanyObjectiveKeyResultList[*].description").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.historyCompanyObjectiveKeyResultList[*].current").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.historyCompanyObjectiveKeyResultList[*].goal").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.historyCompanyObjectiveKeyResultList[*].confidenceLevel").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.historyCompanyObjectiveKeyResultList[*].comment").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.historyCompanyObjectiveKeyResultList[*].user.username").isNotEmpty());
    }
}

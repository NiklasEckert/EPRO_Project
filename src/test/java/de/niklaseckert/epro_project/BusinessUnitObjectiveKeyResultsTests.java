package de.niklaseckert.epro_project;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.niklaseckert.epro_project.model.*;
import de.niklaseckert.epro_project.repos.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BusinessUnitObjectiveKeyResultsTests {
    @Autowired
    private MockMvc mockMvc;

    private final User user1 = User.builder().id(1L).username("TestUser1").password("TestPassword1").build();
    private final User user2 = User.builder().id(2L).username("TestUser2").password("TestPassword2").build();
    private final BusinessUnit BU1 = BusinessUnit.builder().id(1L).name("TestUnit1").build();
    private final BusinessUnit BU2 = BusinessUnit.builder().id(2L).name("TestUnit2").build();
    private final BusinessUnitObjective BUO1 = BusinessUnitObjective.builder().id(1L).name("TestObjective1").description("TestDescription1").businessUnit(BU1).user(user1).build();
    private final BusinessUnitObjective BUO2 = BusinessUnitObjective.builder().id(2L).name("TestObjective2").description("TestDescription2").businessUnit(BU1).user(user1).build();
    private final BusinessUnitObjective BUO3 = BusinessUnitObjective.builder().id(3L).name("TestObjective3").description("TestDescription3").businessUnit(BU1).user(user1).build();
    private final BusinessUnitObjective BUO4 = BusinessUnitObjective.builder().id(4L).name("TestObjective4").description("TestDescription4").businessUnit(BU1).user(user1).build();
    private final CompanyObjective CO1 = CompanyObjective.builder().id(1L).name("O1/2023").description("TestCompanyObjective1").user(user1).build();
    private final CompanyObjectiveKeyResult COKR1 = CompanyObjectiveKeyResult.builder().id(1L).name("TestKR1").description("TestDescription1").current(5).goal(20).confidenceLevel(50).comment("test").companyObjective(CO1).user(user1).build();
    private final BusinessUnitObjectiveKeyResult BUOKR1= BusinessUnitObjectiveKeyResult.builder().id(1L).name("O1/2023").description("TestDescription1").current(15).goal(20).confidenceLevel(75).comment("TestComment1").businessUnitObjective(BUO1).user(user1).companyObjectiveKeyResult(COKR1).build();

    @MockBean
    private BusinessUnitRepository businessUnitRepository;

    @MockBean
    private BusinessUnitObjectiveRepository businessUnitObjectiveRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BusinessUnitObjectiveKeyResultRepository businessUnitObjectiveKeyResultRepository;

    @MockBean
    private CompanyObjectiveKeyResultRepository companyObjectiveKeyResultRepository;

    @BeforeAll
    public void setup() {
        BU1.setBusinessUnitObjectives(List.of(BUO1, BUO2, BUO3, BUO4));
        CO1.setKeyResults(List.of(COKR1));
        BUO1.setBusinessUnitObjectiveKeyResults(List.of(BUOKR1));
    }


    @Test
    @WithMockUser(roles= "CO_OKR_ADMIN")
    public void checkRestriction() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .get("/bu/{id}/objectives/{oid}/keyResults", BU1.getId(), BUO1.getId())
                .accept(MediaType.ALL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles= "READ_ONLY")
    public void getAllBusinessUnitObjectiveKeyResults_success() throws Exception {
        Mockito.when(businessUnitObjectiveRepository.findById(BUO1.getId())).thenReturn(Optional.of(BUO1));
        Mockito.when(businessUnitObjectiveKeyResultRepository.findAll()).thenReturn(List.of(BUOKR1));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/bu/{id}/objectives/{oid}/keyResults", BU1.getId(), BUO1.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.businessUnitObjectiveKeyResultList").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.businessUnitObjectiveKeyResultList[*].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.businessUnitObjectiveKeyResultList[*].name").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.businessUnitObjectiveKeyResultList[*].description").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.businessUnitObjectiveKeyResultList[*].current").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.businessUnitObjectiveKeyResultList[*].goal").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.businessUnitObjectiveKeyResultList[*].confidenceLevel").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.businessUnitObjectiveKeyResultList[*].comment").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.businessUnitObjectiveKeyResultList[*].user.username").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.businessUnitObjectiveKeyResultList[*].achievement").isNotEmpty());
    }

    @Test
    @WithMockUser(roles= "READ_ONLY")
    public void getAllBusinessUnitObjectiveKeyResultsByID_success() throws Exception {
        Mockito.when(businessUnitObjectiveRepository.findById(BUO1.getId())).thenReturn(Optional.of(BUO1));
        Mockito.when(businessUnitObjectiveKeyResultRepository.findById(BUOKR1.getId())).thenReturn(Optional.of(BUOKR1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/bu/{id}/objectives/{oid}/keyResults/{kid}", BU1.getId(), BUO1.getId(),BUOKR1.getId())
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
    @WithMockUser(roles = {"READ_ONLY", "CO_OKR_ADMIN"})
    public void postCheckRestriction_success() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .post("/bu/{id}/objectives/{oid}/keyResults", BU1.getId(), BUO1.getId())
                        .content(new ObjectMapper().writeValueAsString(BUOKR1))
                        .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.ALL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "TestUser1", password = "TestPassword1", roles = "BUO_OKR_ADMIN")
    public void post_success() throws Exception {
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        Mockito.when(businessUnitRepository.findById(BU1.getId())).thenReturn(Optional.of(BU1));
        Mockito.when(businessUnitObjectiveRepository.findById(BUO1.getId())).thenReturn(Optional.of(BUO1));
        Mockito.when(businessUnitObjectiveKeyResultRepository.save(BUOKR1)).thenReturn(BUOKR1);
        Mockito.when(companyObjectiveKeyResultRepository.findById(COKR1.getId())).thenReturn(Optional.of(COKR1));

        mockMvc.perform(MockMvcRequestBuilders
                .post("/bu/{id}/objectives/{oid}/keyResults", BU1.getId(), BUO1.getId())
                        .param("cokrId", ""+COKR1.getId())
                        .content(new ObjectMapper().writeValueAsString(BUOKR1))
                        .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(BUOKR1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(BUOKR1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(BUOKR1.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.current").value(BUOKR1.getCurrent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.goal").value(BUOKR1.getGoal()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.confidenceLevel").value(BUOKR1.getConfidenceLevel()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").value(BUOKR1.getComment()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username").value(BUOKR1.getUser().getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.achievement").value(BUOKR1.getAchievement()));
    }

    @Test
    @WithMockUser(username = "TestUser1", password = "TestPassword1", roles = "BUO_OKR_ADMIN")
    public void put_success() throws Exception {
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        Mockito.when(businessUnitRepository.findById(BU1.getId())).thenReturn(Optional.of(BU1));
        Mockito.when(businessUnitObjectiveRepository.findById(BUO1.getId())).thenReturn(Optional.of(BUO1));
        Mockito.when(businessUnitObjectiveKeyResultRepository.findById(BUOKR1.getId())).thenReturn(Optional.of(BUOKR1));
        Mockito.when(businessUnitObjectiveKeyResultRepository.save(BUOKR1)).thenReturn(BUOKR1);
        Mockito.when(companyObjectiveKeyResultRepository.findById(COKR1.getId())).thenReturn(Optional.of(COKR1));

        mockMvc.perform(MockMvcRequestBuilders
                .put("/bu/{id}/objectives/{oid}/keyResults/{kid}", BU1.getId(), BUO1.getId(), BUOKR1.getId())
                        .param("cokrId", ""+COKR1.getId())
                        .content(new ObjectMapper().writeValueAsString(BUOKR1))
                        .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(BUOKR1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(BUOKR1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(BUOKR1.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.current").value(BUOKR1.getCurrent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.goal").value(BUOKR1.getGoal()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.confidenceLevel").value(BUOKR1.getConfidenceLevel()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").value(BUOKR1.getComment()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username").value(BUOKR1.getUser().getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.achievement").value(BUOKR1.getAchievement()));

    }

    @Test
    @WithMockUser(username = "TestUser2", password = "TestPassword2", roles = "BUO_OKR_ADMIN")
    public void put_forbidden() throws Exception {
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findByUsername(user2.getUsername())).thenReturn(Optional.of(user2));
        Mockito.when(businessUnitRepository.findById(BU1.getId())).thenReturn(Optional.of(BU1));
        Mockito.when(businessUnitObjectiveRepository.findById(BUO1.getId())).thenReturn(Optional.of(BUO1));
        Mockito.when(businessUnitObjectiveKeyResultRepository.findById(BUOKR1.getId())).thenReturn(Optional.of(BUOKR1));
        Mockito.when(businessUnitObjectiveKeyResultRepository.save(BUOKR1)).thenReturn(BUOKR1);
        Mockito.when(companyObjectiveKeyResultRepository.findById(COKR1.getId())).thenReturn(Optional.of(COKR1));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/bu/{id}/objectives/{oid}/keyResults/{kid}", BU1.getId(), BUO1.getId(), BUOKR1.getId())
                                .param("cokrId", ""+COKR1.getId())
                                .content(new ObjectMapper().writeValueAsString(BUOKR1))
                                .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(username = "TestUser1", password = "TestPassword1", roles = "BUO_OKR_ADMIN")
    public void put_notFound() throws Exception {
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        Mockito.when(businessUnitRepository.findById(BU1.getId())).thenReturn(Optional.of(BU1));
        Mockito.when(businessUnitObjectiveRepository.findById(BUO1.getId())).thenReturn(Optional.of(BUO1));
        Mockito.when(businessUnitObjectiveKeyResultRepository.findById(BUOKR1.getId())).thenReturn(Optional.of(BUOKR1));
        Mockito.when(businessUnitObjectiveKeyResultRepository.save(BUOKR1)).thenReturn(BUOKR1);
        Mockito.when(companyObjectiveKeyResultRepository.findById(COKR1.getId())).thenReturn(Optional.of(COKR1));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/bu/{id}/objectives/{oid}/keyResults/{kid}", BU1.getId(), BUO1.getId(), 5L)
                        .param("cokrId", ""+COKR1.getId())
                        .content(new ObjectMapper().writeValueAsString(BUOKR1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(BUOKR1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(BUOKR1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(BUOKR1.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.current").value(BUOKR1.getCurrent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.goal").value(BUOKR1.getGoal()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.confidenceLevel").value(BUOKR1.getConfidenceLevel()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").value(BUOKR1.getComment()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username").value(BUOKR1.getUser().getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.achievement").value(BUOKR1.getAchievement()));
    }
    @Test
    @WithMockUser(username = "TestUser1", password = "TestPassword1", roles = "BUO_OKR_ADMIN")
    public void patch_success() throws Exception {
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        Mockito.when(businessUnitRepository.findById(BU1.getId())).thenReturn(Optional.of(BU1));
        Mockito.when(businessUnitObjectiveRepository.findById(BUO1.getId())).thenReturn(Optional.of(BUO1));
        Mockito.when(businessUnitObjectiveKeyResultRepository.findById(BUOKR1.getId())).thenReturn(Optional.of(BUOKR1));
        Mockito.when(businessUnitObjectiveKeyResultRepository.save(BUOKR1)).thenReturn(BUOKR1);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/bu/{id}/objectives/{oid}/keyResults/{kid}", BU1.getId(), BUO1.getId(), BUOKR1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(BUOKR1))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(BUOKR1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(BUOKR1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(BUOKR1.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.current").value(BUOKR1.getCurrent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.goal").value(BUOKR1.getGoal()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.confidenceLevel").value(BUOKR1.getConfidenceLevel()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").value(BUOKR1.getComment()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username").value(BUOKR1.getUser().getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.achievement").value(BUOKR1.getAchievement()));

    }

    @Test
    @WithMockUser(username = "TestUser2", password = "TestPassword2", roles = "BUO_OKR_ADMIN")
    public void patch_forbidden() throws Exception {
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findByUsername(user2.getUsername())).thenReturn(Optional.of(user2));
        Mockito.when(businessUnitRepository.findById(BU1.getId())).thenReturn(Optional.of(BU1));
        Mockito.when(businessUnitObjectiveRepository.findById(BUO1.getId())).thenReturn(Optional.of(BUO1));
        Mockito.when(businessUnitObjectiveKeyResultRepository.findById(BUOKR1.getId())).thenReturn(Optional.of(BUOKR1));
        Mockito.when(businessUnitObjectiveKeyResultRepository.save(BUOKR1)).thenReturn(BUOKR1);
        Mockito.when(companyObjectiveKeyResultRepository.findById(COKR1.getId())).thenReturn(Optional.of(COKR1));

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/bu/{id}/objectives/{oid}/keyResults/{kid}", BU1.getId(), BUO1.getId(), BUOKR1.getId())
                        .param("cokrId", ""+COKR1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(BUOKR1))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }
    @Test
    @WithMockUser(username = "TestUser1", password = "TestPassword1", roles = "BUO_OKR_ADMIN")
    public void patch_notFound() throws Exception {
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        Mockito.when(businessUnitRepository.findById(BU1.getId())).thenReturn(Optional.of(BU1));
        Mockito.when(businessUnitObjectiveRepository.findById(BUO1.getId())).thenReturn(Optional.of(BUO1));
        Mockito.when(businessUnitObjectiveKeyResultRepository.findById(BUOKR1.getId())).thenReturn(Optional.of(BUOKR1));
        Mockito.when(businessUnitObjectiveKeyResultRepository.save(BUOKR1)).thenReturn(BUOKR1);
        Mockito.when(companyObjectiveKeyResultRepository.findById(COKR1.getId())).thenReturn(Optional.of(COKR1));

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/bu/{id}/objectives/{oid}/keyResults/{kid}", BU1.getId(), BUO1.getId(), 5L)
                        .param("cokrId", ""+COKR1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(BUOKR1))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }
    @Test
    @WithMockUser(username = "TestUser1", password = "TestPassword1", roles = "BUO_OKR_ADMIN")
    public void delete_success() throws  Exception {
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        Mockito.when(businessUnitObjectiveKeyResultRepository.findById(BUOKR1.getId())).thenReturn(Optional.of(BUOKR1));

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/bu/{id}/objectives/{oid}/keyResults/{kid}", BU1.getId(), BUO1.getId(), BUOKR1.getId()))
                .andExpect(status().isAccepted());
    }
    @Test
    @WithMockUser(username = "TestUser2", password = "TestPassword2", roles = "BUO_OKR_ADMIN")
    public void delete_forbidden() throws  Exception {
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findByUsername(user2.getUsername())).thenReturn(Optional.of(user2));
        Mockito.when(businessUnitObjectiveKeyResultRepository.findById(BUOKR1.getId())).thenReturn(Optional.of(BUOKR1));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/bu/{id}/objectives/{oid}/keyResults/{kid}", BU1.getId(), BUO1.getId(), BUOKR1.getId()))
                .andExpect(status().isForbidden());
    }
    @Test
    @WithMockUser(username = "TestUser1", password = "TestPassword1", roles = "BUO_OKR_ADMIN")
    public void delete_notFound() throws  Exception {
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        Mockito.when(businessUnitObjectiveKeyResultRepository.findById(BUOKR1.getId())).thenReturn(Optional.of(BUOKR1));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/bu/{id}/objectives/{oid}/keyResults/{kid}", BU1.getId(), BUO1.getId(), 5L))
                .andExpect(status().isNotFound());
    }



}

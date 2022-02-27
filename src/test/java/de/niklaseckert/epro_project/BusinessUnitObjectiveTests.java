package de.niklaseckert.epro_project;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.niklaseckert.epro_project.model.BusinessUnit;
import de.niklaseckert.epro_project.model.BusinessUnitObjective;
import de.niklaseckert.epro_project.model.User;
import de.niklaseckert.epro_project.repos.BusinessUnitObjectiveRepository;
import de.niklaseckert.epro_project.repos.BusinessUnitRepository;
import de.niklaseckert.epro_project.repos.UserRepository;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BusinessUnitObjectiveTests {

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

    @MockBean
    private BusinessUnitRepository businessUnitRepository;

    @MockBean
    private BusinessUnitObjectiveRepository businessUnitObjectiveRepository;

    @MockBean
    private UserRepository userRepository;

    @BeforeAll
    public void setup() {
        BU1.setBusinessUnitObjectives(List.of(BUO1, BUO2, BUO3, BUO4));
    }

    @Test
    @WithMockUser(roles = "CO_OKR_ADMIN")
    public void getCheckRestriction_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/bu/{id}/objectives", BU1.getId())
                        .accept(MediaType.ALL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "READ_ONLY")
    public void getAllBusinessUnitObjectives_success() throws Exception {
        Mockito.when(businessUnitRepository.findById(BU1.getId())).thenReturn(Optional.of(BU1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/bu/{id}/objectives", BU1.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.businessUnitObjectiveList").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.businessUnitObjectiveList[*].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.businessUnitObjectiveList[*].name").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.businessUnitObjectiveList[*].description").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.businessUnitObjectiveList[*].user.username").isNotEmpty());
    }

    @Test
    @WithMockUser(roles = "READ_ONLY")
    public void getBusinessUnitObjectiveById_success() throws Exception {
        Mockito.when(businessUnitRepository.findById(BU1.getId())).thenReturn(Optional.of(BU1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/bu/{id}/objectives/{oid}", BU1.getId(), BUO1.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username").isNotEmpty());
    }

    @Test
    @WithMockUser(roles = {"READ_ONLY", "CO_OKR_ADMIN"})
    public void postCheckRestriction_success() throws Exception {
        Mockito.when(businessUnitRepository.findById(BU1.getId())).thenReturn(Optional.of(BU1));
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        Mockito.when(businessUnitObjectiveRepository.save(BUO1)).thenReturn(BUO1);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/bu/{id}/objectives", BU1.getId())
                        .content(new ObjectMapper().writeValueAsString(BUO1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "TestUser1", password = "TestPassword1", roles = "BUO_OKR_ADMIN")
    public void postBusinessUnitObjective_success() throws Exception {
        Mockito.when(businessUnitRepository.findById(BU1.getId())).thenReturn(Optional.of(BU1));
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        Mockito.when(businessUnitObjectiveRepository.save(BUO1)).thenReturn(BUO1);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/bu/{id}/objectives", BU1.getId())
                        .content(new ObjectMapper().writeValueAsString(BUO1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(BUO1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(BUO1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username").value(BUO1.getUser().getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(BUO1.getDescription()));
    }

    @Test
    @WithMockUser(username = "TestUser1", password = "TestPassword1", roles = "BUO_OKR_ADMIN")
    public void putBusinessUnitObjective_success() throws Exception {
        Mockito.when(businessUnitRepository.findById(BU1.getId())).thenReturn(Optional.of(BU1));
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        Mockito.when(businessUnitObjectiveRepository.findById(BUO1.getId())).thenReturn(Optional.of(BUO1));
        Mockito.when(businessUnitObjectiveRepository.save(BUO1)).thenReturn(BUO1);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/bu/{id}/objectives/{oid}", BU1.getId(), BUO1.getId())
                        .content(new ObjectMapper().writeValueAsString(BUO1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(BUO1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(BUO1.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username").value(BUO1.getUser().getUsername()));
    }

    @Test
    @WithMockUser(username = "TestUser2", password = "TestPassword2", roles = "BUO_OKR_ADMIN")
    public void putBusinessUnitObjective_forbidden() throws Exception {
        Mockito.when(businessUnitRepository.findById(BU1.getId())).thenReturn(Optional.of(BU1));
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findByUsername(user2.getUsername())).thenReturn(Optional.of(user2));
        Mockito.when(businessUnitObjectiveRepository.findById(BUO1.getId())).thenReturn(Optional.of(BUO1));
        Mockito.when(businessUnitObjectiveRepository.save(BUO1)).thenReturn(BUO1);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/bu/{id}/objectives/{oid}", BU1.getId(), BUO1.getId())
                        .content(new ObjectMapper().writeValueAsString(BUO1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "TestUser1", password = "TestPassword1", roles = "BUO_OKR_ADMIN")
    public void putBusinessUnitObjective_notFound() throws Exception {
        Mockito.when(businessUnitRepository.findById(BU1.getId())).thenReturn(Optional.of(BU1));
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        Mockito.when(businessUnitObjectiveRepository.findById(BUO1.getId())).thenReturn(Optional.of(BUO1));
        Mockito.when(businessUnitObjectiveRepository.save(BUO1)).thenReturn(BUO1);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/bu/{id}/objectives/{oid}", BU1.getId(), 5L)
                        .content(new ObjectMapper().writeValueAsString(BUO1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(BUO1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(BUO1.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username").value(BUO1.getUser().getUsername()));
    }

    @Test
    @WithMockUser(username = "TestUser1", password = "TestPassword1", roles = "BUO_OKR_ADMIN")
    public void patchBusinessUnitObjective_success() throws Exception {
        Mockito.when(businessUnitRepository.findById(BU1.getId())).thenReturn(Optional.of(BU1));
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        Mockito.when(businessUnitObjectiveRepository.findById(BUO1.getId())).thenReturn(Optional.of(BUO1));
        Mockito.when(businessUnitObjectiveRepository.save(BUO1)).thenReturn(BUO1);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/bu/{id}/objectives/{oid}", BU1.getId(), BUO1.getId())
                        .content(new ObjectMapper().writeValueAsString(BUO1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(BUO1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(BUO1.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username").value(BUO1.getUser().getUsername()));

    }

    @Test
    @WithMockUser(username = "TestUser2", password = "TestPassword2", roles = "BUO_OKR_ADMIN")
    public void patchBusinessUnitObjective_forbidden() throws Exception {
        Mockito.when(businessUnitRepository.findById(BU1.getId())).thenReturn(Optional.of(BU1));
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findByUsername(user2.getUsername())).thenReturn(Optional.of(user2));
        Mockito.when(businessUnitObjectiveRepository.findById(BUO1.getId())).thenReturn(Optional.of(BUO1));
        Mockito.when(businessUnitObjectiveRepository.save(BUO1)).thenReturn(BUO1);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/bu/{id}/objectives/{oid}", BU1.getId(), BUO1.getId())
                        .content(new ObjectMapper().writeValueAsString(BUO1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "TestUser1", password = "TestPassword1", roles = "BUO_OKR_ADMIN")
    public void patchBusinessUnitObjective_notFound() throws Exception {
        Mockito.when(businessUnitRepository.findById(BU1.getId())).thenReturn(Optional.of(BU1));
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        Mockito.when(businessUnitObjectiveRepository.findById(BUO1.getId())).thenReturn(Optional.of(BUO1));
        Mockito.when(businessUnitObjectiveRepository.save(BUO1)).thenReturn(BUO1);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/bu/{id}/objectives/{oid}", BU1.getId(), 5L)
                        .content(new ObjectMapper().writeValueAsString(BUO1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "TestUser1", password = "TestPassword1", roles = "BUO_OKR_ADMIN")
    public void deleteBusinessUnitObjective_success() throws Exception {
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        Mockito.when(businessUnitObjectiveRepository.findById(BUO1.getId())).thenReturn(Optional.of(BUO1));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/bu/{id}/objectives/{oid}", BU1.getId(), BUO1.getId()))
                .andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser(username = "TestUser2", password = "TestPassword2", roles = "BUO_OKR_ADMIN")
    public void deleteBusinessUnitObjective_forbidden() throws Exception {
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findByUsername(user2.getUsername())).thenReturn(Optional.of(user2));
        Mockito.when(businessUnitObjectiveRepository.findById(BUO1.getId())).thenReturn(Optional.of(BUO1));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/bu/{id}/objectives/{oid}", BU1.getId(), BUO1.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "TestUser1", password = "TestPassword1", roles = "BUO_OKR_ADMIN")
    public void deleteBusinessUnitObjective_notFound() throws Exception {
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(Optional.of(user1));
        Mockito.when(businessUnitObjectiveRepository.findById(BUO1.getId())).thenReturn(Optional.of(BUO1));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/bu/{id}/objectives/{oid}", BU1.getId(), 5L))
                .andExpect(status().isNotFound());
    }


}

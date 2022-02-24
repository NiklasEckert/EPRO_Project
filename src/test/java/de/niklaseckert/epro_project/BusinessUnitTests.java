package de.niklaseckert.epro_project;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.niklaseckert.epro_project.controller.exceptions.BusinessUnitNotFoundException;
import de.niklaseckert.epro_project.model.BusinessUnit;
import de.niklaseckert.epro_project.repos.BusinessUnitRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BusinessUnitTests {

    @Autowired
    private MockMvc mockMvc;

    private final BusinessUnit BU1 = BusinessUnit.builder().id(1L).name("TestUnit1").build();
    private final BusinessUnit BU2 = BusinessUnit.builder().id(2L).name("TestUnit2").build();

    @MockBean
    private BusinessUnitRepository businessUnitRepository;

    @Test
    @WithMockUser(roles = "CO_OKR_ADMIN")
    public void getCheckRestriction_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/bu")
                        .accept(MediaType.ALL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "READ_ONLY")
    public void getAllBusinessUnits_success() throws Exception {
        Mockito.when(businessUnitRepository.findAll()).thenReturn(Arrays.asList(BU1, BU2));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/bu")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.businessUnitList").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.businessUnitList[*].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.businessUnitList[*].name").isNotEmpty());
    }

    @Test
    @WithMockUser(roles = "READ_ONLY")
    public void getBusinessUnitById_success() throws Exception {
        Mockito.when(businessUnitRepository.findById(BU1.getId())).thenReturn(Optional.of(BU1));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/bu/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").isNotEmpty());
    }

    @Test
    @WithMockUser(roles = { "READ_ONLY", "CO_OKR_ADMIN"} )
    public void postCheckRestriction_success() throws Exception {
        Mockito.when(businessUnitRepository.findById(BU1.getId())).thenReturn(Optional.of(BU1));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/bu")
                        .content(new ObjectMapper().writeValueAsString(BU1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "BUO_OKR_ADMIN")
    public void post_success() throws Exception {
        BusinessUnit businessUnit = BusinessUnit.builder()
                .id(1L)
                .name("TestUnit")
                .build();

        Mockito.when(businessUnitRepository.save(businessUnit)).thenReturn(businessUnit);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/bu")
                .content(new ObjectMapper().writeValueAsString(businessUnit))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("TestUnit"));
    }

    @Test
    @WithMockUser(roles = "BUO_OKR_ADMIN")
    public void putBusinessUnit_success() throws Exception {
        BusinessUnit businessUnit = BusinessUnit.builder()
                .id(1L)
                .name("UnitTest")
                .build();

        Mockito.when(businessUnitRepository.findById(businessUnit.getId())).thenReturn(Optional.of(businessUnit));
        Mockito.when(businessUnitRepository.save(businessUnit)).thenReturn(businessUnit);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/bu/{id}", BU1.getId())
                .content(new ObjectMapper().writeValueAsString(businessUnit))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("UnitTest"));
    }

    @Test
    @WithMockUser(roles = "BUO_OKR_ADMIN")
    public void putBusinessUnit_notFound() throws Exception {
        BusinessUnit updatedBusinessUnit = BusinessUnit.builder()
                .id(3L)
                .name("UnitNullIdTest")
                .build();

        Mockito.when(businessUnitRepository.findById(updatedBusinessUnit.getId())).thenReturn(Optional.empty());
        Mockito.when(businessUnitRepository.save(updatedBusinessUnit)).thenReturn(updatedBusinessUnit);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/bu/{id}",updatedBusinessUnit.getId())
                        .content(new ObjectMapper().writeValueAsString(updatedBusinessUnit))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("" + updatedBusinessUnit.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("UnitNullIdTest"));
    }

    @Test
    @WithMockUser(roles = "BUO_OKR_ADMIN")
    public void patchBusinessUnit_success() throws Exception {
        BusinessUnit businessUnit = BusinessUnit.builder()
                .id(1L)
                .name("UnitTest")
                .build();

        Mockito.when(businessUnitRepository.findById(businessUnit.getId())).thenReturn(Optional.of(businessUnit));
        Mockito.when(businessUnitRepository.save(businessUnit)).thenReturn(businessUnit);

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/bu/{id}", BU1.getId())
                .content(new ObjectMapper().writeValueAsString(businessUnit))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("UnitTest"));
    }

    @Test
    @WithMockUser(roles = "BUO_OKR_ADMIN")
    public void patchBusinessUnit_notFound() throws Exception {
        BusinessUnit updatedBusinessUnit = BusinessUnit.builder()
                .id(3L)
                .name("UnitNullIdTest")
                .build();

        Mockito.when(businessUnitRepository.findById(updatedBusinessUnit.getId())).thenReturn(Optional.empty());
        Mockito.when(businessUnitRepository.save(updatedBusinessUnit)).thenReturn(updatedBusinessUnit);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/bu/{id}",updatedBusinessUnit.getId())
                        .content(new ObjectMapper().writeValueAsString(updatedBusinessUnit))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "BUO_OKR_ADMIN")
    public void deleteBusinessUnitById_success() throws Exception {
        Mockito.when(businessUnitRepository.findById(BU1.getId())).thenReturn(Optional.of(BU1));

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/bu/{id}", BU1.getId()))
                .andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser(roles = "BUO_OKR_ADMIN")
    public void deleteBusinessUnitById_notFound() throws Exception {
        Mockito.when(businessUnitRepository.findById(5L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/bu/{id}", 5L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BusinessUnitNotFoundException));
    }

}

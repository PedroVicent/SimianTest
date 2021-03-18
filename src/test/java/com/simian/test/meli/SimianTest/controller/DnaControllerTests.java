package com.simian.test.meli.SimianTest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simian.test.meli.SimianTest.domain.Dna;
import com.simian.test.meli.SimianTest.dto.DnaDTO;
import com.simian.test.meli.SimianTest.dto.StatsDTO;
import com.simian.test.meli.SimianTest.repository.DnaRepository;
import com.simian.test.meli.SimianTest.service.DnaService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = DnaController.class)
public class DnaControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DnaService dnaService;


    @Test
    public void validaSimioShouldOk() throws Exception {
        when(dnaService.isSimio(any(DnaDTO.class))).thenReturn(true);
        DnaDTO dnaDTO = new DnaDTO();
        dnaDTO.setDna(Arrays.asList("ATGCTA", "CACTGC", "TTATGT", "ACAAGG", "CGCTAA", "TCACGG"));
        String jsonDnaDto = "";
        try {
            jsonDnaDto = objectMapper.writeValueAsString(dnaDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        mockMvc.perform(
                post("/simian")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDnaDto)).andExpect(status().isOk());
    }

    @Test
    public void validaSimioShouldForbidden() throws Exception {
        when(dnaService.isSimio(any(DnaDTO.class))).thenReturn(false);
        DnaDTO dnaDTO = new DnaDTO();
        dnaDTO.setDna(Arrays.asList("ATGCTA", "CACTGC", "TTATGT", "ACAAGG", "CGCTAA", "TCACGG"));
        String jsonDnaDto = "";
        try {
            jsonDnaDto = objectMapper.writeValueAsString(dnaDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        mockMvc.perform(
                post("/simian")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDnaDto)).andExpect(status().isForbidden());
    }

    @Test
    public void retornarStatusShouldOk() throws Exception {
        StatsDTO statsDTO = new StatsDTO();
        statsDTO.setCountHumanDna(1);
        statsDTO.setCountMutantDna(2);
        statsDTO.setRatio(new BigDecimal(2));
        when(dnaService.retornarStatus()).thenReturn(statsDTO);

        String statsDtoAsJson = objectMapper.writeValueAsString(statsDTO);

        mockMvc.perform(
                get("/stats")
                        .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
        .andExpect(content().json(statsDtoAsJson));
    }
}

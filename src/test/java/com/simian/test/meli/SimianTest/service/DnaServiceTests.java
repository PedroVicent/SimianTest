package com.simian.test.meli.SimianTest.service;

import com.simian.test.meli.SimianTest.domain.Dna;
import com.simian.test.meli.SimianTest.dto.DnaDTO;
import com.simian.test.meli.SimianTest.dto.StatsDTO;
import com.simian.test.meli.SimianTest.repository.DnaRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DnaServiceTests {

    @Autowired
    private DnaService dnaService;

    @MockBean
    private DnaRepository dnaRepository;

    @Test
    public void isDiagonalSimianShouldOk(){
        when(dnaRepository.findByDna(anyString())).thenReturn(Optional.empty());
        when(dnaRepository.save(any(Dna.class))).thenReturn(new Dna());
        DnaDTO dnaDTO = new DnaDTO();
        dnaDTO.setDna(Arrays.asList("ATGCTA", "CACTGC", "TTATGT", "ACAAGG", "CGCTAA", "TCACGG"));
        boolean isSimio = dnaService.isSimio(dnaDTO);

        verify(dnaRepository).findByDna(anyString());
        verify(dnaRepository).save(any(Dna.class));
        assertTrue(isSimio);
    }

    @Test
    public void isDiagonalSimianExistShouldOk(){
        Dna dna = new Dna();
        dna.setSimio(true);
        dna.setDna("ATGCTACACTGCTTATGTACAAGGCGCTAATCACGG");
        when(dnaRepository.findByDna(anyString())).thenReturn(Optional.of(dna));
        when(dnaRepository.save(any(Dna.class))).thenReturn(new Dna());
        DnaDTO dnaDTO = new DnaDTO();
        dnaDTO.setDna(Arrays.asList("ATGCTA", "CACTGC", "TTATGT", "ACAAGG", "CGCTAA", "TCACGG"));
        boolean isSimio = dnaService.isSimio(dnaDTO);

        verify(dnaRepository).findByDna(anyString());
        verify(dnaRepository, times(0)).save(any(Dna.class));
        assertTrue(isSimio);
    }

    @Test
    public void isDiagonalInvertidoSimianShouldOk(){
        when(dnaRepository.findByDna(anyString())).thenReturn(Optional.empty());
        when(dnaRepository.save(any(Dna.class))).thenReturn(new Dna());
        DnaDTO dnaDTO = new DnaDTO();
        dnaDTO.setDna(Arrays.asList("TCGGTA", "CGAAAT", "ATTAGG", "TTACCC", "TCGGAA", "CTTCGA"));
        boolean isSimio = dnaService.isSimio(dnaDTO);

        verify(dnaRepository).findByDna(anyString());
        verify(dnaRepository).save(any(Dna.class));
        assertTrue(isSimio);
    }

    @Test
    public void isHorizontalSimianShouldOk(){
        when(dnaRepository.findByDna(anyString())).thenReturn(Optional.empty());
        when(dnaRepository.save(any(Dna.class))).thenReturn(new Dna());
        DnaDTO dnaDTO = new DnaDTO();
        dnaDTO.setDna(Arrays.asList("TTGCTA", "CACTGC", "TAATGT", "AAAAGG", "CACTAA", "TAACGG"));
        boolean isSimio = dnaService.isSimio(dnaDTO);

        verify(dnaRepository).findByDna(anyString());
        verify(dnaRepository).save(any(Dna.class));
        assertTrue(isSimio);
    }

    @Test
    public void isVerticalSimianShouldOk(){
        when(dnaRepository.findByDna(anyString())).thenReturn(Optional.empty());
        when(dnaRepository.save(any(Dna.class))).thenReturn(new Dna());
        DnaDTO dnaDTO = new DnaDTO();
        dnaDTO.setDna(Arrays.asList("TTGCTA", "TACTGC", "TAATGT", "TAAAGG", "CACTAA", "TAACGG"));
        boolean isSimio = dnaService.isSimio(dnaDTO);

        verify(dnaRepository).findByDna(anyString());
        verify(dnaRepository).save(any(Dna.class));
        assertTrue(isSimio);
    }

    @Test
    public void isNotSimian(){
        when(dnaRepository.findByDna(anyString())).thenReturn(Optional.empty());
        when(dnaRepository.save(any(Dna.class))).thenReturn(new Dna());
        DnaDTO dnaDTO = new DnaDTO();
        dnaDTO.setDna(Arrays.asList("TTGCTA", "TACTGC", "GAATGT", "TAATGG", "CCCAAA", "TAACGG"));
        boolean isSimio = dnaService.isSimio(dnaDTO);

        verify(dnaRepository).findByDna(anyString());
        verify(dnaRepository).save(any(Dna.class));
        assertFalse(isSimio);
    }

    @Test
    public void isSimianInvalidArray(){
        when(dnaRepository.findByDna(anyString())).thenReturn(Optional.empty());
        when(dnaRepository.save(any(Dna.class))).thenReturn(new Dna());
        DnaDTO dnaDTO = new DnaDTO();
        dnaDTO.setDna(Arrays.asList("TACTGC", "GAATGT", "TAATGG", "CCCAAA", "TAACGG"));
        boolean isSimio = dnaService.isSimio(dnaDTO);

        verify(dnaRepository, times(0)).findByDna(anyString());
        verify(dnaRepository, times(0)).save(any(Dna.class));
        assertFalse(isSimio);
    }

    @Test
    public void retornarStatusWithOk(){
        Dna dna = new Dna();
        dna.setSimio(true);
        dna.setDna("ATGCTACACTGCTTATGTACAAGGCGCTAATCACGG");
        Dna dna1 = new Dna();
        dna1.setSimio(true);
        dna1.setDna("ATGCTACACTGCTTATGTACAAGGCGCTAATCACGG");
        Dna dna2 = new Dna();
        dna2.setSimio(false);
        dna2.setDna("ATGCTACACTGCTTATGTACAAGGCGCTAATCACGG");
        List<Dna> dnaList = Arrays.asList(dna, dna1, dna2);

        when(dnaRepository.findAll()).thenReturn(dnaList);
        StatsDTO statsDTO = dnaService.retornarStatus();

        verify(dnaRepository).findAll();
        assertEquals(statsDTO.getCountHumanDna(), 1);
        assertEquals(statsDTO.getCountMutantDna(), 2);
        assertEquals(statsDTO.getRatio(), new BigDecimal("2.00"));

    }

}

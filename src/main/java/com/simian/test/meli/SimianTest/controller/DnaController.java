package com.simian.test.meli.SimianTest.controller;

import com.simian.test.meli.SimianTest.dto.DnaDTO;
import com.simian.test.meli.SimianTest.dto.StatsDTO;
import com.simian.test.meli.SimianTest.service.DnaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

import javax.validation.Valid;

@Controller
@RequestMapping()
@RequiredArgsConstructor
public class DnaController {

	private final DnaService dnaService;

	@PostMapping(value = "/simian", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> validaSimio(@Valid @RequestBody DnaDTO dna) {
		if (dnaService.isSimio(dna)) {
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	}

	@GetMapping(value = "/stats", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StatsDTO> retornarStatus() {
		return ResponseEntity.ok(dnaService.retornarStatus());
	}

}

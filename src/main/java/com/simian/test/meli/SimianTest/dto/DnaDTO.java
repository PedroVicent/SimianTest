package com.simian.test.meli.SimianTest.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class DnaDTO {

	@NotEmpty
	private List<String> dna;

}

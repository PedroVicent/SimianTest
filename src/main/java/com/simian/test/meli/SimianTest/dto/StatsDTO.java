package com.simian.test.meli.SimianTest.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class StatsDTO {
	
	private long countMutantDna;

	private long countHumanDna;

	private BigDecimal ratio;

}

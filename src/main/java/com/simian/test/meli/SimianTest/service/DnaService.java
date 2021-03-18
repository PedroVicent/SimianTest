package com.simian.test.meli.SimianTest.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.simian.test.meli.SimianTest.domain.Dna;
import com.simian.test.meli.SimianTest.dto.DnaDTO;
import com.simian.test.meli.SimianTest.dto.StatsDTO;
import com.simian.test.meli.SimianTest.repository.DnaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(propagation = Propagation.SUPPORTS)
@RequiredArgsConstructor
public class DnaService {

	private final DnaRepository dnaRepository;

	public boolean isSimio(DnaDTO dnaDTO) {

		List<String> dna = dnaDTO.getDna();

		if (isInvalidDnaFormat(dna)) {
			return false;
		}

		boolean isSimio;

		String dnaBase = String.join("", dna);
		Optional<Dna> dnaExistente = dnaRepository.findByDna(dnaBase);
		if (dnaExistente.isPresent()) {
			isSimio = dnaExistente.get().getSimio();
		} else {
			isSimio = validarSimio(dna);
			Dna dnaCriado = new Dna();
			dnaCriado.setDna(dnaBase);
			dnaCriado.setSimio(isSimio);
			dnaRepository.save(dnaCriado);
		}

		return isSimio;
	}

	private boolean isInvalidDnaFormat(List<String> dna) {
		return dna.stream().anyMatch(base -> base.length() != dna.size() || base.length() < 4)
				|| caracteresInvalidos(dna);
	}

	private boolean caracteresInvalidos(List<String> listaDna) {

		Pattern p = Pattern.compile("[^A|C|T|G]", Pattern.CASE_INSENSITIVE);
		return listaDna.stream().anyMatch(dna -> {
			Matcher m = p.matcher(dna);
			return m.find();
		});
	}

	public StatsDTO retornarStatus() {
		List<Dna> dnas = dnaRepository.findAll();
		StatsDTO status = new StatsDTO();
		if (!dnas.isEmpty()) {
			status.setCountHumanDna(dnas.stream().filter(x -> Boolean.FALSE.equals(x.getSimio())).count());
			status.setCountMutantDna(dnas.stream().filter(x -> Boolean.TRUE.equals(x.getSimio())).count());
			BigDecimal divide = new BigDecimal(status.getCountMutantDna()).divide(new BigDecimal(status.getCountHumanDna()), 2, BigDecimal.ROUND_CEILING);
			status.setRatio(divide);
		}
		return status;
	}

	private boolean validarSimio(List<String> listaDna) {

		boolean isSimio = listaDna.stream().anyMatch(dna -> {
			char[] charArray = dna.toCharArray();
			return verificaIgualdade(charArray, 0, 0);
		});
		
		if (!isSimio) {
			isSimio = validaVertical(listaDna);
		}

		return isSimio;
	}

	private boolean verificaIgualdade(char[] charArray, Integer i, int counter) {

		if (canBeSimian(charArray.length, i, counter)) {
			return false;
		}

		if (i < charArray.length && i + 1 < charArray.length && counter != 3) {

			if (charArray[i] == charArray[i + 1]) {
				counter++;
				return verificaIgualdade(charArray, i + 1, counter);
			} else {
				counter = 0;
				return verificaIgualdade(charArray, i + 1, counter);
			}

		}

		return counter == 3;
	}

	private boolean canBeSimian(int sizeList, Integer i, int counter) {
		return !((sizeList - i) + counter >= 3);
	}

	private boolean validaVertical(List<String> dna) {

		boolean isSimio = false;

		for (int i = 0; i < dna.size(); i++) {
			isSimio = verificaIgualdade(dna, 0, 0, i);
			if (isSimio) {
				break;
			}
		}
		if (!isSimio) {
			isSimio = validaDiagonal(dna);
		}

		return isSimio;
	}

	private boolean verificaIgualdade(List<String> dna, Integer index, Integer counter, Integer column) {

		if (canBeSimian(dna.size(), index, counter)) {
			return false;
		}
		
		if (index < dna.size() && index + 1 < dna.size() && counter != 3) {

			if (dna.get(index).charAt(column) == dna.get(index + 1).charAt(column)) {
				counter++;
			} else {
				counter = 0;
			}
			return verificaIgualdade(dna, index + 1, counter, column);
		}

		return counter == 3;
	}

	private boolean validaDiagonal(List<String> dna) {

		boolean isSimio = false;
		for (int index = 0; index < dna.size(); index++) {
			if(isSimio) {
				break;
			}
			for (int coluna = 0; coluna < dna.size() - 1; coluna++) {
				isSimio = verificaIgualdadeDiagonal(dna, index, coluna, 0, dna.size());
				if (isSimio) {
					break;
				}
			}
		}

		if (!isSimio) {
			isSimio = validaDiagonalInverso(dna);
		}

		return isSimio;
	}

	private boolean verificaIgualdadeDiagonal(List<String> dna, int index, int coluna, int counter, int listSize) {

		if(canBeSimian(listSize, index, counter)) {
			return false;
		}
		
		if (index < dna.size() && index + 1 < dna.size() && coluna < dna.size() - 1 && counter != 3) {
			if (dna.get(index).charAt(coluna) == dna.get(index + 1).charAt(coluna + 1)) {
				counter++;
			} else {
				counter = 0;
			}
			return verificaIgualdadeDiagonal(dna, index + 1, coluna + 1, counter, listSize-1);

		}

		return counter == 3;
	}
	

	private boolean validaDiagonalInverso(List<String> dna) {

		boolean isSimio = false;
		for (int index = 0; index <= dna.size(); index++) {
			if (isSimio) {
				break;
			}
			for (int coluna = dna.size() - 1; coluna > 0; coluna--) {
				isSimio = verificaIgualdadeDiagonalInverso(dna, index, coluna, 0, dna.size());
				if (isSimio) {
					break;
				}
			}
		}

		return isSimio;
	}

	private boolean verificaIgualdadeDiagonalInverso(List<String> dna, int index, int coluna, int counter, int listSize) {
		
		if (canBeSimian(listSize, index, counter)) {
			return false;
		}
		
		if (!dna.isEmpty() && index < dna.size() && index + 1 < dna.size() && coluna > 0 && counter != 3) {
			if (dna.get(index).charAt(coluna) == dna.get(index + 1).charAt(coluna - 1)) {
				counter++;
			} else {
				counter = 0;
			}
			return verificaIgualdadeDiagonalInverso(dna, index + 1, coluna - 1, counter, listSize-1);

		}
		return counter == 3;
	}

}

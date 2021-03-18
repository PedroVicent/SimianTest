package com.simian.test.meli.SimianTest.repository;

import java.util.List;
import java.util.Optional;

import com.simian.test.meli.SimianTest.domain.Dna;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DnaRepository extends CrudRepository<Dna, String> {

	List<Dna> findAll();

	Optional<Dna> findByDna(String dna);

}

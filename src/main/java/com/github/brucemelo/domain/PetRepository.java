package com.github.brucemelo.domain;


import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface PetRepository extends R2dbcRepository<Pet, Integer> {

    Flux<Pet> findAllBy(Pageable pageable);
}

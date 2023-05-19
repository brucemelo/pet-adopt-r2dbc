package com.github.brucemelo.service;


import com.github.brucemelo.domain.Category;
import com.github.brucemelo.domain.Pet;
import com.github.brucemelo.domain.PetRepository;
import com.github.brucemelo.domain.Status;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.CriteriaDefinition;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class PetService {

    private final ProviderDogsService providerDogsService;
    private final ProviderCatsService providerCatsService;
    private final PetRepository petRepository;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public PetService(ProviderDogsService providerDogsService,
                      ProviderCatsService providerCatsService,
                      PetRepository petRepository,
                      R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.providerDogsService = providerDogsService;
        this.providerCatsService = providerCatsService;
        this.petRepository = petRepository;
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
    }


    public Flux<Pet> indexPets() {
        var dogs = providerDogsService.getDogs();
        var cats = providerCatsService.getCats();
        Flux<Pet> fluxCats = petRepository.saveAll(cats);
        Flux<Pet> fluxDogs = petRepository.saveAll(dogs);
        return Flux.merge(fluxCats, fluxDogs);
    }

    public Mono<Pet> makeAvailable(Integer id) {
        return petRepository.findById(id)
                .onErrorMap(e -> new PetNotFoundException())
                .map(Pet::makeAvailable)
                .flatMap(petRepository::save);
    }

    public Mono<Pet> adopt(Integer id) {
        return petRepository.findById(id)
                .onErrorMap(e -> new PetNotFoundException())
                .map(Pet::toAdopt)
                .flatMap(petRepository::save);
    }

    public Flux<Pet> findAll(Optional<String> term, Optional<String> category, Optional<String> status, PageRequest pageRequest) {
        var criteriaList = new ArrayList<Criteria>();
        if (term.isPresent()) {
            var criteria = Criteria.where("name").is(term.get());
            criteriaList.add(criteria);
        }
        if (category.isPresent()) {
            var criteria = Criteria.where("category").is(Category.valueOf(category.get()));
            criteriaList.add(criteria);
        }
        if (status.isPresent()) {
            var criteria = Criteria.where("status").is(Status.valueOf(status.get()));
            criteriaList.add(criteria);

        }
        return r2dbcEntityTemplate.select(Pet.class)
                .matching(Query.query(CriteriaDefinition.from(criteriaList)).with(pageRequest)).all();
    }

}

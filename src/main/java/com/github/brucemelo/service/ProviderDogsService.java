package com.github.brucemelo.service;

import com.github.brucemelo.domain.Pet;
import com.github.brucemelo.integration.IntegrationDogApi;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class ProviderDogsService {

    private final IntegrationDogApi integrationDogApi;

    public ProviderDogsService(IntegrationDogApi integrationDogApi) {
        this.integrationDogApi = integrationDogApi;
    }

    public Flux<Pet> getDogs() {
        return integrationDogApi.getDogs();
    }
}

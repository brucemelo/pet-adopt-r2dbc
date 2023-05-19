package com.github.brucemelo.integration;


import com.github.brucemelo.domain.Category;
import com.github.brucemelo.domain.Pet;
import com.github.brucemelo.domain.PetBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Profile({"dev", "prod"})
@Component
public class IntegrationDogApi {

    protected final UriConfigDogApi uriConfigDogApi;

    public IntegrationDogApi(UriConfigDogApi uriConfigDogApi) {
        this.uriConfigDogApi = uriConfigDogApi;
    }

    public final Flux<Pet> getDogs() {
        return getDogsApi()
                .map(dogApi -> PetBuilder.builder().name(dogApi.getBreed()).category(Category.Dog).build());
    }

    protected Flux<DogApi> getDogsApi() {
        var uri = uriConfigDogApi.getUri();
        return WebClient.create(uri).get().retrieve().bodyToFlux(DogApi.class);
    }
}

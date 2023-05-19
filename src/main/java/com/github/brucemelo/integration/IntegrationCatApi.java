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
public class IntegrationCatApi {

    protected final UriConfigCatApi uriConfigCatApi;

    public IntegrationCatApi(UriConfigCatApi uriConfigCatApi) {
        this.uriConfigCatApi = uriConfigCatApi;
    }

    public final Flux<Pet> getCats() {
        var catApis = getCatsApi();
        return catApis
                .map(dogApi ->
                        PetBuilder.builder().name((dogApi.getBreed())).category(Category.Cat)
                                .build());
    }

    protected Flux<CatApi> getCatsApi() {
        var uri = uriConfigCatApi.getUri();
        return WebClient.create(uri).get().retrieve().bodyToFlux(CatApi.class);
    }

}

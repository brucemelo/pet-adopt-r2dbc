package com.github.brucemelo.integration;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Profile({"local"})
@Component
public class IntegrationDogFakeApi extends IntegrationDogApi {

    Flux<DogApi> dogs;

    {
        dogs = Flux.just(new DogApi("dog 3"), new DogApi("dog 2"));
    }

    public IntegrationDogFakeApi(UriConfigDogApi uriConfigDogApi) {
        super(uriConfigDogApi);
    }


    @Override
    protected Flux<DogApi> getDogsApi() {
        return dogs;
    }
}

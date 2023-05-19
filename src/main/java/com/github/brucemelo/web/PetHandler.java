package com.github.brucemelo.web;


import com.github.brucemelo.domain.Pet;
import com.github.brucemelo.service.PetService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class PetHandler {

    private final PetService petService;

    public PetHandler(PetService petService) {
        this.petService = petService;
    }

    public Mono<ServerResponse> indexPets() {
        return petService.indexPets().then(ServerResponse.ok().build());
    }

    public Mono<ServerResponse> available(final ServerRequest req) {
        final var id = req.pathVariable("id");
        return petService.makeAvailable(Integer.valueOf(id))
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> adopt(final ServerRequest req) {
        final var id = req.pathVariable("id");
        return petService.adopt(Integer.valueOf(id))
                .flatMap(pet -> ServerResponse.noContent().build());
    }

    public Flux<Pet> findAll(final ServerRequest req) {
        var term = req.queryParam("term");
        var category = req.queryParam("category");
        var status = req.queryParam("status");
        var sizeParam = req.queryParam("size").orElse("10");
        var pageParam = req.queryParam("page").orElse("0");
        var size = Integer.parseInt(sizeParam);
        var page = Integer.parseInt(pageParam);
        return petService.findAll(
                term,
                category,
                status,
                PageRequest.of(page, size));
    }

}

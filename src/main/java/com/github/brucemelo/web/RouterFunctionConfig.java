package com.github.brucemelo.web;

import com.github.brucemelo.domain.Pet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterFunctionConfig {

    @Bean
    public RouterFunction<ServerResponse> petRoutes(final PetHandler petHandler) {
        return nest(path("/api"),
                nest(path("/pets"),
                        route(POST("/indexing"), req -> petHandler.indexPets())
                                .andRoute(PUT("/{id}/available"), petHandler::available)
                                .andRoute(PUT("/{id}/adopt"), petHandler::adopt)
                                .andRoute(GET("/search"),
                                        request -> ServerResponse.ok().body(petHandler.findAll(request), Pet.class))
                                .andRoute(GET(""), request -> ServerResponse.ok().bodyValue("ok"))
                )
        );
    }

}

package com.github.brucemelo.domain;

import io.soabase.recordbuilder.core.RecordBuilder;
import org.springframework.data.annotation.Id;

@RecordBuilder
public record Pet(@Id Integer id, String name, String description, Category category, Status status) {

    public Pet makeAvailable() {
        return PetBuilder.from(this).withStatus(Status.Available);
    }

    public Pet toAdopt() {
        return PetBuilder.from(this).withStatus(Status.Adopt);
    }

}

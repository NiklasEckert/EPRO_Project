package de.niklaseckert.epro_project.model.assembler;

import de.niklaseckert.epro_project.model.HistoryBusinessUnitObjectiveKeyResult;
import lombok.NonNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Component;

@Component
public class HistoryBusinessUnitObjectiveKeyResultAssembler implements RepresentationModelAssembler<HistoryBusinessUnitObjectiveKeyResult, EntityModel<HistoryBusinessUnitObjectiveKeyResult>> {

    @Override
    @NonNull
    public EntityModel<HistoryBusinessUnitObjectiveKeyResult> toModel(@NonNull HistoryBusinessUnitObjectiveKeyResult entity) {
        return EntityModel.of(entity);
    }
}

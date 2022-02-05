package de.niklaseckert.epro_project.model.assembler;


import de.niklaseckert.epro_project.model.HistoryCompanyObjectiveKeyResult;
import lombok.NonNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;


@Component
public class HistoryCompanyObjectiveKeyResultAssembler implements RepresentationModelAssembler<HistoryCompanyObjectiveKeyResult, EntityModel<HistoryCompanyObjectiveKeyResult>> {

    @Override
    @NonNull
    public EntityModel<HistoryCompanyObjectiveKeyResult> toModel(@NonNull HistoryCompanyObjectiveKeyResult entity) {
        return EntityModel.of(entity );

    }
}

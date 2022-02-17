package de.niklaseckert.epro_project.model.assembler;


import de.niklaseckert.epro_project.controller.CompanyObjectiveController;
import de.niklaseckert.epro_project.model.HistoryCompanyObjectiveKeyResult;
import lombok.NonNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class HistoryCompanyObjectiveKeyResultAssembler implements RepresentationModelAssembler<HistoryCompanyObjectiveKeyResult, EntityModel<HistoryCompanyObjectiveKeyResult>> {

    @Override
    @NonNull
    public EntityModel<HistoryCompanyObjectiveKeyResult> toModel(@NonNull HistoryCompanyObjectiveKeyResult entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(CompanyObjectiveController.class).oneKeyResult(entity.getCompanyObjective().getId(),entity.getId() )).withSelfRel(),
                linkTo(methodOn(CompanyObjectiveController.class).allKeyResults(entity.getCompanyObjective().getId())).withRel("key_results")
        );

    }
}

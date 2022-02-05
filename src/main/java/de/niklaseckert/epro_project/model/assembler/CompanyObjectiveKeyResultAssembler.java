package de.niklaseckert.epro_project.model.assembler;


import de.niklaseckert.epro_project.controller.CompanyObjectiveController;
import de.niklaseckert.epro_project.model.CompanyObjectiveKeyResult;
import lombok.NonNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CompanyObjectiveKeyResultAssembler implements RepresentationModelAssembler<CompanyObjectiveKeyResult, EntityModel<CompanyObjectiveKeyResult>> {
    @Override
    @NonNull
    public EntityModel<CompanyObjectiveKeyResult> toModel(@NonNull CompanyObjectiveKeyResult entity) {
        return EntityModel.of(entity, linkTo(methodOn(CompanyObjectiveController.class).oneKeyResult(entity.getCompanyObjective().getId(),entity.getId() )).withSelfRel());

    }
}

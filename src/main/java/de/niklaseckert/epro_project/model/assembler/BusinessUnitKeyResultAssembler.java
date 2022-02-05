package de.niklaseckert.epro_project.model.assembler;

import de.niklaseckert.epro_project.controller.BusinessUnitController;
import de.niklaseckert.epro_project.controller.CompanyObjectiveController;
import de.niklaseckert.epro_project.model.BusinessUnitObjectiveKeyResult;
import de.niklaseckert.epro_project.model.CompanyObjectiveKeyResult;
import lombok.NonNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BusinessUnitKeyResultAssembler implements RepresentationModelAssembler<BusinessUnitObjectiveKeyResult, EntityModel<BusinessUnitObjectiveKeyResult>> {

    @Override
    @NonNull
    public EntityModel<BusinessUnitObjectiveKeyResult> toModel(@NonNull BusinessUnitObjectiveKeyResult entity) {
        return EntityModel.of(entity, linkTo(methodOn(BusinessUnitController.class).oneKeyResult(entity.getBusinessUnitObjective().getBusinessUnit().getId(),entity.getBusinessUnitObjective().getId(), entity.getId())).withSelfRel());

    }

}

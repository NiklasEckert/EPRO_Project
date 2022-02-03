package de.niklaseckert.epro_project.model.assembler;

import de.niklaseckert.epro_project.controller.BusinessUnitController;
import de.niklaseckert.epro_project.model.BusinessUnitObjective;
import lombok.NonNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BusinessUnitObjectiveAssembler implements RepresentationModelAssembler<BusinessUnitObjective, EntityModel<BusinessUnitObjective>> {

    @Override
    @NonNull
    public EntityModel<BusinessUnitObjective> toModel(@NonNull BusinessUnitObjective entity) {
        return EntityModel.of(entity, linkTo(methodOn(BusinessUnitController.class).oneObjective(entity.getBusinessUnit().getId(),entity.getId() )).withSelfRel());
    }
}
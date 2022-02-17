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
        return EntityModel.of(entity,
                linkTo(methodOn(BusinessUnitController.class).oneObjective(entity.getBusinessUnit().getId(), entity.getId())).withSelfRel(),
                linkTo(methodOn(BusinessUnitController.class).allObjectives(entity.getId())).withRel("objectives"),
                linkTo(methodOn(BusinessUnitController.class).allKeyResults(entity.getBusinessUnit().getId(), entity.getId())).withRel("key_results"),
                linkTo(methodOn(BusinessUnitController.class).one(entity.getBusinessUnit().getId())).withRel("business_unit")
                );
    }
}
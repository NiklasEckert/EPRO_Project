package de.niklaseckert.epro_project.model.assembler;

import de.niklaseckert.epro_project.controller.BusinessUnitController;
import de.niklaseckert.epro_project.model.BusinessUnit;
import lombok.NonNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BusinessUnitAssembler implements RepresentationModelAssembler<BusinessUnit, EntityModel<BusinessUnit>> {

    @Override
    @NonNull
    public EntityModel<BusinessUnit> toModel(@NonNull BusinessUnit businessUnit) {

        return EntityModel.of(businessUnit,
                    linkTo(methodOn(BusinessUnitController.class).one(businessUnit.getId())).withSelfRel(),
                    linkTo(methodOn(BusinessUnitController.class).all()).withRel("business_units"),
                    linkTo(methodOn(BusinessUnitController.class).allObjectives(businessUnit.getId())).withRel("objectives")
                );
    }


}

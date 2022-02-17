package de.niklaseckert.epro_project.model.assembler;

import de.niklaseckert.epro_project.controller.BusinessUnitController;
import de.niklaseckert.epro_project.model.HistoryBusinessUnitObjectiveKeyResult;
import lombok.NonNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class HistoryBusinessUnitObjectiveKeyResultAssembler implements RepresentationModelAssembler<HistoryBusinessUnitObjectiveKeyResult, EntityModel<HistoryBusinessUnitObjectiveKeyResult>> {

    @Override
    @NonNull
    public EntityModel<HistoryBusinessUnitObjectiveKeyResult> toModel(@NonNull HistoryBusinessUnitObjectiveKeyResult entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(BusinessUnitController.class).oneKeyResult(entity.getBusinessUnitObjective().getBusinessUnit().getId(),entity.getBusinessUnitObjective().getId(), entity.getId())).withSelfRel(),
                linkTo(methodOn(BusinessUnitController.class).allKeyResults(entity.getBusinessUnitObjective().getId(),entity.getId())).withRel("key_results")
        );
    }
}

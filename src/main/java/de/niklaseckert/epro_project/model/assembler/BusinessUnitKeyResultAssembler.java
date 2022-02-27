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

/**
 * Class which constructs the Entity Model of a {@link BusinessUnitObjectiveKeyResult Business Unit Objective Key Result} inclusive links.
 *
 * @author Niklas Eckert
 * @author Jakob Friedsam
 * @author Fabian Schulz
 */
@Component
public class BusinessUnitKeyResultAssembler implements RepresentationModelAssembler<BusinessUnitObjectiveKeyResult, EntityModel<BusinessUnitObjectiveKeyResult>> {

    /**
     * Puts together the Entity Model of a {@link BusinessUnitObjectiveKeyResult Business Unit Objective Key Result}.
     *
     * @param entity {@link BusinessUnitObjectiveKeyResult Business Unit Objective Key Result} from which the model should be build of.
     * @return the Entity Model of the given {@link BusinessUnitObjectiveKeyResult Business Unit Objective Key Result}.
     */
    @Override
    @NonNull
    public EntityModel<BusinessUnitObjectiveKeyResult> toModel(@NonNull BusinessUnitObjectiveKeyResult entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(BusinessUnitController.class).oneKeyResult(entity.getBusinessUnitObjective().getBusinessUnit().getId(), entity.getBusinessUnitObjective().getId(), entity.getId())).withSelfRel(),
                linkTo(methodOn(BusinessUnitController.class).allKeyResults(entity.getBusinessUnitObjective().getId(), entity.getId())).withRel("key_results"),
                linkTo(methodOn(BusinessUnitController.class).oneObjective(entity.getBusinessUnitObjective().getBusinessUnit().getId(), entity.getBusinessUnitObjective().getId())).withRel("business_unit_objective"),
                linkTo(methodOn(BusinessUnitController.class).one(entity.getBusinessUnitObjective().getBusinessUnit().getId())).withRel("business_unit"),
                linkTo(methodOn(BusinessUnitController.class).keyResultHistory(entity.getBusinessUnitObjective().getBusinessUnit().getId(), entity.getBusinessUnitObjective().getId(), entity.getId())).withRel("history")
                 );

    }

}

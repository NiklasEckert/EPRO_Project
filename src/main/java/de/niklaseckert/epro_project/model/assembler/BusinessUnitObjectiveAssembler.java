package de.niklaseckert.epro_project.model.assembler;

import de.niklaseckert.epro_project.controller.BusinessUnitController;
import de.niklaseckert.epro_project.model.BusinessUnitObjective;
import lombok.NonNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Class which constructs the Entity Model of a {@link BusinessUnitObjective Business Unit Objective} inclusive links.
 *
 * @author Niklas Eckert
 * @author Jakob Friedsam
 * @author Fabian Schulz
 */
@Component
public class BusinessUnitObjectiveAssembler implements RepresentationModelAssembler<BusinessUnitObjective, EntityModel<BusinessUnitObjective>> {

    /**
     * Puts together the Entity Model of a {@link BusinessUnitObjective Business Unit Objective}.
     *
     * @param entity {@link BusinessUnitObjective Business Unit Objective} from which the model should be build of.
     * @return the Entity Model of the given {@link BusinessUnitObjective Business Unit Objective}.
     */
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
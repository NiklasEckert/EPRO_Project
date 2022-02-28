package de.niklaseckert.epro_project.model.assembler;


import de.niklaseckert.epro_project.controller.CompanyObjectiveController;
import de.niklaseckert.epro_project.model.CompanyObjectiveKeyResult;
import lombok.NonNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Class which constructs the Entity Model of a {@link CompanyObjectiveKeyResult Company Objective Key Result} inclusive links.
 *
 * @author Niklas Eckert
 * @author Jakob Friedsam
 * @author Fabian Schulz
 */
@Component
public class CompanyObjectiveKeyResultAssembler implements RepresentationModelAssembler<CompanyObjectiveKeyResult, EntityModel<CompanyObjectiveKeyResult>> {

    /**
     * Puts together the Entity Model of a {@link CompanyObjectiveKeyResult Company Objective Key Result}.
     *
     * @param entity {@link CompanyObjectiveKeyResult Company Objective Key Result} from which the model should be build of.
     * @return the Entity Model of the given {@link CompanyObjectiveKeyResult Company Objective Key Result}.
     */
    @Override
    @NonNull
    public EntityModel<CompanyObjectiveKeyResult> toModel(@NonNull CompanyObjectiveKeyResult entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(CompanyObjectiveController.class).oneKeyResult(entity.getCompanyObjective().getId(),entity.getId() )).withSelfRel(),
                linkTo(methodOn(CompanyObjectiveController.class).allKeyResults(entity.getCompanyObjective().getId())).withRel("key_results"),
                linkTo(methodOn(CompanyObjectiveController.class).one(entity.getCompanyObjective().getId())).withRel("company_objective"),
                linkTo(methodOn(CompanyObjectiveController.class).keyResultHistory(entity.getCompanyObjective().getId(), entity.getId())).withRel("history")
        );

    }
}

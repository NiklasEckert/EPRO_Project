package de.niklaseckert.epro_project.model.assembler;

import de.niklaseckert.epro_project.controller.CompanyObjectiveController;
import de.niklaseckert.epro_project.model.CompanyObjective;
import lombok.NonNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Class which constructs the Entity Model of a {@link CompanyObjective Company Objective} inclusive links.
 *
 * @author Niklas Eckert
 * @author Jakob Friedsam
 * @author Fabian Schulz
 */
@Component
public class CompanyObjectiveAssembler implements RepresentationModelAssembler<CompanyObjective, EntityModel<CompanyObjective>> {

    /**
     * Puts together the Entity Model of a {@link CompanyObjective Company Objective}.
     *
     * @param entity {@link CompanyObjective Company Objective} from which the model should be build of.
     * @return the Entity Model of the given {@link CompanyObjective Company Objective}.
     */
    @Override
    @NonNull
    public EntityModel<CompanyObjective> toModel(@NonNull CompanyObjective entity) {
        return EntityModel.of(entity,
                    linkTo(methodOn(CompanyObjectiveController.class).one(entity.getId())).withSelfRel(),
                    linkTo(methodOn(CompanyObjectiveController.class).all()).withRel("objectives"),
                    linkTo(methodOn(CompanyObjectiveController.class).allKeyResults(entity.getId())).withRel("key_results")
        );
    }
}

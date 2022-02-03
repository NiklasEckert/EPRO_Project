package de.niklaseckert.epro_project.controller;

import de.niklaseckert.epro_project.controller.exceptions.*;
import de.niklaseckert.epro_project.model.*;
import de.niklaseckert.epro_project.model.assembler.BusinessUnitAssembler;
import de.niklaseckert.epro_project.model.assembler.BusinessUnitKeyResultAssembler;
import de.niklaseckert.epro_project.model.assembler.BusinessUnitObjectiveAssembler;
import de.niklaseckert.epro_project.model.assembler.HistoryBusinessUnitObjectiveKeyResultAssembler;
import de.niklaseckert.epro_project.repos.BusinessUnitObjectiveRepository;
import de.niklaseckert.epro_project.repos.BusinessUnitRepository;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@AllArgsConstructor
@RequestMapping("/bu")
public class BusinessUnitController {

    private final BusinessUnitRepository repository;
    private final BusinessUnitObjectiveRepository objectiveRepository;
    private final BusinessUnitAssembler assembler;
    private final BusinessUnitObjectiveAssembler objectiveAssembler;
    private final BusinessUnitKeyResultAssembler keyResultAssembler;
    private final HistoryBusinessUnitObjectiveKeyResultAssembler historyBusinessUnitObjectiveKeyResultAssembler;

    @GetMapping("/{id}")
    public EntityModel<BusinessUnit> one(@PathVariable Long id) {
        BusinessUnit businessUnit = repository.findById(id).orElseThrow(() -> new BusinessUnitNotFoundException(id));

        return assembler.toModel(businessUnit);
    }

    @GetMapping
    public CollectionModel<EntityModel<BusinessUnit>> all() {
        List<EntityModel<BusinessUnit>> businessUnits = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(businessUnits,
                linkTo(methodOn(BusinessUnitController.class).all()).withSelfRel()
                );
    }

    @GetMapping("/{id}/objectives")
    public CollectionModel<EntityModel<BusinessUnitObjective>> allObjectives(@PathVariable Long id){
        BusinessUnit businessUnit = repository.findById(id).orElseThrow(() -> new BusinessUnitNotFoundException(id));
        List<EntityModel<BusinessUnitObjective>> businessUnitObjectives =businessUnit.getBusinessUnitObjectives().stream()
                .map(objectiveAssembler::toModel)
                .collect(Collectors.toList());

        return  CollectionModel.of(businessUnitObjectives,
                linkTo(methodOn(BusinessUnitController.class).allObjectives(id)).withSelfRel());

    }

    @GetMapping("/{id}/objectives/{oid}")
    public EntityModel<BusinessUnitObjective> oneObjective(@PathVariable Long id, @PathVariable Long oid){
        BusinessUnit businessUnit = repository.findById(id).orElseThrow(() -> new BusinessUnitNotFoundException(id));
        BusinessUnitObjective objective= businessUnit.getBusinessUnitObjectives().stream()
                .filter(o -> o.getId().equals(oid))
                .findFirst()
                .orElseThrow(() -> new BusinessUnitObjectiveNotFoundException(oid));

        return objectiveAssembler.toModel(objective);
    }

    @GetMapping("/{id}/objectives/{oid}/keyResults/{kid}")
    public EntityModel<BusinessUnitObjectiveKeyResult> oneKeyResult(@PathVariable Long id, @PathVariable Long oid, @PathVariable Long kid) {
        BusinessUnitObjective businessUnitObjective = objectiveRepository.findById(oid).orElseThrow(() -> new BusinessUnitNotFoundException(oid));
        BusinessUnitObjectiveKeyResult businessUnitObjectiveKeyResult= businessUnitObjective.getBusinessUnitObjectiveKeyResults().stream()
                .filter(o -> o.getId().equals(kid))
                .findFirst()
                .orElseThrow(() -> new BusinessUnitObjectivesKeyResultNotFoundException(kid));

        return keyResultAssembler.toModel(businessUnitObjectiveKeyResult);
    }

    @GetMapping("/{id}/objectives/{oid}/keyResults")
    public CollectionModel<EntityModel<BusinessUnitObjectiveKeyResult>> allKeyResults(@PathVariable Long id, @PathVariable Long oid) {
        BusinessUnitObjective businessUnitObjective = objectiveRepository.findById(oid).orElseThrow(() -> new BusinessUnitNotFoundException(oid));
        List<EntityModel<BusinessUnitObjectiveKeyResult>> businessUnitObjectiveKeyResults =businessUnitObjective.getBusinessUnitObjectiveKeyResults().stream()
                .map(keyResultAssembler::toModel)
                .collect(Collectors.toList());

        return  CollectionModel.of(businessUnitObjectiveKeyResults,
                linkTo(methodOn(BusinessUnitController.class).allKeyResults(id,oid)).withSelfRel());

    }

    @GetMapping("/{id}/objectives/{oid}/keyResults/{kid}/history")
    public CollectionModel<EntityModel<HistoryBusinessUnitObjectiveKeyResult>> keyResultHistory(@PathVariable Long id, @PathVariable Long oid, @PathVariable Long kid) {
        BusinessUnitObjective businessUnitObjective = objectiveRepository.findById(oid).orElseThrow(() -> new BusinessUnitNotFoundException(oid));
        BusinessUnitObjectiveKeyResult businessUnitObjectiveKeyResult= businessUnitObjective.getBusinessUnitObjectiveKeyResults().stream()
                .filter(o -> o.getId().equals(kid))
                .findFirst()
                .orElseThrow(() -> new BusinessUnitObjectivesKeyResultNotFoundException(kid));

        List<EntityModel<HistoryBusinessUnitObjectiveKeyResult>> historyBusinessUnitObjectiveKeyResults = businessUnitObjectiveKeyResult.getHistory().stream()
                .map(historyBusinessUnitObjectiveKeyResultAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(historyBusinessUnitObjectiveKeyResults, linkTo(methodOn(BusinessUnitController.class).keyResultHistory(id, oid,kid)).withSelfRel());
    }
}

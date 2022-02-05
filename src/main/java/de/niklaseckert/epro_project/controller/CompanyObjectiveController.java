package de.niklaseckert.epro_project.controller;


import de.niklaseckert.epro_project.controller.exceptions.BusinessUnitNotFoundException;
import de.niklaseckert.epro_project.controller.exceptions.BusinessUnitObjectiveNotFoundException;
import de.niklaseckert.epro_project.controller.exceptions.CompanyObjectiveKeyResultNotFoundException;
import de.niklaseckert.epro_project.controller.exceptions.CompanyObjectiveNotFoundException;
import de.niklaseckert.epro_project.model.*;
import de.niklaseckert.epro_project.model.assembler.CompanyObjectiveAssembler;
import de.niklaseckert.epro_project.model.assembler.CompanyObjectiveKeyResultAssembler;
import de.niklaseckert.epro_project.model.assembler.HistoryCompanyObjectiveKeyResultAssembler;
import de.niklaseckert.epro_project.repos.CompanyObjectiveRepository;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@AllArgsConstructor
@RequestMapping("/co")
public class CompanyObjectiveController {

    private final CompanyObjectiveRepository repository;
    private final CompanyObjectiveAssembler assembler;
    private final CompanyObjectiveKeyResultAssembler keyResultAssembler;
    private final HistoryCompanyObjectiveKeyResultAssembler historyCompanyObjectiveKeyResultAssembler;

    @GetMapping("/{id}")
    public EntityModel<CompanyObjective> one(@PathVariable Long id){
        CompanyObjective companyObjective = repository.findById(id).orElseThrow(() -> new CompanyObjectiveNotFoundException(id));

        return assembler.toModel(companyObjective);
    }

    @GetMapping
    public CollectionModel<EntityModel<CompanyObjective>> all(){
        List<EntityModel<CompanyObjective>> companyObjectives = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(companyObjectives,
                linkTo(methodOn(CompanyObjectiveController.class).all()).withSelfRel()
        );
    }

    @GetMapping("/{id}/keyResults")
    public CollectionModel<EntityModel<CompanyObjectiveKeyResult>> allKeyResults(@PathVariable Long id) {
        CompanyObjective companyObjective = repository.findById(id).orElseThrow(() -> new CompanyObjectiveNotFoundException(id));
        List<EntityModel<CompanyObjectiveKeyResult>> companyObjectiveKeyResults =companyObjective.getKeyResults().stream()
                .map(keyResultAssembler::toModel)
                .collect(Collectors.toList());

        return  CollectionModel.of(companyObjectiveKeyResults,
                linkTo(methodOn(CompanyObjectiveController.class).allKeyResults(id)).withSelfRel());

    }

    @GetMapping("/{id}/keyResults/{kid}")
    public EntityModel<CompanyObjectiveKeyResult> oneKeyResult(@PathVariable Long id, @PathVariable Long kid) {
        CompanyObjective companyObjective = repository.findById(id).orElseThrow(() -> new CompanyObjectiveNotFoundException(id) );
        CompanyObjectiveKeyResult companyObjectiveKeyResult= companyObjective.getKeyResults().stream()
                .filter(o -> o.getId().equals(kid))
                .findFirst()
                .orElseThrow(() -> new CompanyObjectiveKeyResultNotFoundException(kid));

        return keyResultAssembler.toModel(companyObjectiveKeyResult);
    }

    @GetMapping("/{id}/keyResults/{kid}/history")
    public CollectionModel<EntityModel<HistoryCompanyObjectiveKeyResult>> keyResultHistory(@PathVariable Long id, @PathVariable Long kid){
        CompanyObjective companyObjective = repository.findById(id).orElseThrow(() -> new CompanyObjectiveNotFoundException(id) );
        CompanyObjectiveKeyResult companyObjectiveKeyResult= companyObjective.getKeyResults().stream()
                .filter(o -> o.getId().equals(kid))
                .findFirst()
                .orElseThrow(() -> new CompanyObjectiveKeyResultNotFoundException(kid));

        List<EntityModel<HistoryCompanyObjectiveKeyResult>> historyCompanyObjectiveKeyResults = companyObjectiveKeyResult.getHistory().stream()
                .map(historyCompanyObjectiveKeyResultAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(historyCompanyObjectiveKeyResults, linkTo(methodOn(CompanyObjectiveController.class).keyResultHistory(id,kid)).withSelfRel());
    }

}

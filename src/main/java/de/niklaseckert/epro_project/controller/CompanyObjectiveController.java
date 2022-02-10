package de.niklaseckert.epro_project.controller;

import de.niklaseckert.epro_project.controller.exceptions.CompanyObjectiveKeyResultNotFoundException;
import de.niklaseckert.epro_project.controller.exceptions.CompanyObjectiveNotFoundException;
import de.niklaseckert.epro_project.model.*;
import de.niklaseckert.epro_project.model.assembler.CompanyObjectiveAssembler;
import de.niklaseckert.epro_project.model.assembler.CompanyObjectiveKeyResultAssembler;
import de.niklaseckert.epro_project.model.assembler.HistoryCompanyObjectiveKeyResultAssembler;
import de.niklaseckert.epro_project.repos.CompanyObjectiveKeyResultRepository;
import de.niklaseckert.epro_project.repos.CompanyObjectiveRepository;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@AllArgsConstructor
@RequestMapping("/co")
public class CompanyObjectiveController {

    private final CompanyObjectiveRepository repository;
    private final CompanyObjectiveKeyResultRepository keyResultRepository;
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

    @PostMapping
    public ResponseEntity<EntityModel<CompanyObjective>> createCompanyObjective(@RequestBody CompanyObjective companyObjective) {
        CompanyObjective newCompanyObjective = repository.save(companyObjective);
        return ResponseEntity
                .created(linkTo(methodOn(CompanyObjectiveController.class).one(newCompanyObjective.getId())).toUri())
                .body(assembler.toModel(newCompanyObjective));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<CompanyObjective>> replaceCompanyObjective(@RequestBody CompanyObjective newCompanyObjective, @PathVariable Long id) {
        CompanyObjective updatedCompanyObjective = repository.findById(id)
                .map(companyObjective -> {
                    companyObjective.setName(newCompanyObjective.getName());
                    companyObjective.setDescription(newCompanyObjective.getDescription());
                    return repository.save(companyObjective);
                })
                .orElseGet(() -> {
                    newCompanyObjective.setId(id);
                    return repository.save(newCompanyObjective);
                });
        EntityModel<CompanyObjective> entity = assembler.toModel(updatedCompanyObjective);
        return ResponseEntity
                .created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompanyObjective(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<CompanyObjective>> updateCompanyObjective(@RequestBody CompanyObjective newCompanyObjective, @PathVariable Long id) {
        CompanyObjective companyObjective = repository.findById(id).orElseThrow(() -> new CompanyObjectiveNotFoundException(id));

        CompanyObjective updatedCompanyObjective = repository.save(companyObjective.applyPatch(newCompanyObjective));

        EntityModel<CompanyObjective> entity = assembler.toModel(updatedCompanyObjective);
        return ResponseEntity
                .created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entity);
    }

    @PostMapping("/{id}/keyResults")
    public ResponseEntity<EntityModel<CompanyObjectiveKeyResult>> createKeyResult(@RequestBody CompanyObjectiveKeyResult newCompanyObjectiveKeyResult, @PathVariable Long id) {
        CompanyObjective companyObjective = repository.findById(id).orElseThrow(() -> new CompanyObjectiveNotFoundException(id));
        newCompanyObjectiveKeyResult.setCompanyObjective(companyObjective);
        CompanyObjectiveKeyResult keyResult = keyResultRepository.save(newCompanyObjectiveKeyResult);

        return ResponseEntity
                .created(linkTo(methodOn(CompanyObjectiveController.class).one(keyResult.getId())).toUri())
                .body(keyResultAssembler.toModel(keyResult));
    }

    @PutMapping("/{id}/keyResults/{kid}")
    public ResponseEntity<EntityModel<CompanyObjectiveKeyResult>> replaceKeyResult(@RequestBody CompanyObjectiveKeyResult newCompanyObjectiveKeyResult, @PathVariable Long id, @PathVariable Long kid) {
        CompanyObjective companyObjective = repository.findById(id).orElseThrow(() -> new CompanyObjectiveNotFoundException(id));
        CompanyObjectiveKeyResult companyObjectiveKeyResult = keyResultRepository.findById(kid)
                .map(keyResult -> {
                    keyResult.setName(newCompanyObjectiveKeyResult.getName());
                    keyResult.setDescription(newCompanyObjectiveKeyResult.getDescription());
                    keyResult.setCurrent(newCompanyObjectiveKeyResult.getCurrent());
                    keyResult.setGoal(newCompanyObjectiveKeyResult.getGoal());
                    keyResult.setConfidenceLevel(newCompanyObjectiveKeyResult.getConfidenceLevel());
                    keyResult.setComment(newCompanyObjectiveKeyResult.getComment());
                    return keyResultRepository.save(keyResult);
                })
                .orElseGet(() -> {
                    newCompanyObjectiveKeyResult.setId(kid);
                    newCompanyObjectiveKeyResult.setCompanyObjective(companyObjective);
                    return keyResultRepository.save(newCompanyObjectiveKeyResult);
                });

        EntityModel<CompanyObjectiveKeyResult> entity = keyResultAssembler.toModel(companyObjectiveKeyResult);
        return ResponseEntity
                .created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entity);
    }

    @DeleteMapping("/{id}/keyResults/{kid}")
    public ResponseEntity<?> deleteKeyResult(@PathVariable Long id, @PathVariable Long kid) {
        keyResultRepository.deleteById(kid);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/keyResults/{kid}")
    public ResponseEntity<EntityModel<CompanyObjectiveKeyResult>> updateKeyResult(@RequestBody CompanyObjectiveKeyResult newCompanyObjectiveKeyResult, @PathVariable Long id, @PathVariable Long kid) {
        CompanyObjective companyObjective = repository.findById(id).orElseThrow(() -> new CompanyObjectiveNotFoundException(id));
        CompanyObjectiveKeyResult keyResult = keyResultRepository.findById(kid).orElseThrow(() -> new CompanyObjectiveKeyResultNotFoundException(kid));

        CompanyObjectiveKeyResult updatedCompanyObjectiveKeyResult = keyResultRepository.save(keyResult.applyPatch(newCompanyObjectiveKeyResult));
        EntityModel<CompanyObjectiveKeyResult> entity = keyResultAssembler.toModel(updatedCompanyObjectiveKeyResult);
        return ResponseEntity
                .created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entity);
    }

}

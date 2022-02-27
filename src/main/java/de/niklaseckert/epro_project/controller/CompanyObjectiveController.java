package de.niklaseckert.epro_project.controller;

import de.niklaseckert.epro_project.controller.exceptions.CompanyObjectiveKeyResultNotFoundException;
import de.niklaseckert.epro_project.controller.exceptions.CompanyObjectiveNotFoundException;
import de.niklaseckert.epro_project.model.*;
import de.niklaseckert.epro_project.model.assembler.CompanyObjectiveAssembler;
import de.niklaseckert.epro_project.model.assembler.CompanyObjectiveKeyResultAssembler;
import de.niklaseckert.epro_project.model.assembler.HistoryCompanyObjectiveKeyResultAssembler;
import de.niklaseckert.epro_project.repos.CompanyObjectiveKeyResultRepository;
import de.niklaseckert.epro_project.repos.CompanyObjectiveRepository;
import de.niklaseckert.epro_project.repos.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Controller which handles all {@link CompanyObjective Company Objective} requests.
 *
 * @author Niklas Eckert
 * @author Jakob Friedsam
 * @author Fabian Schulz
 */
@RestController
@AllArgsConstructor
@RequestMapping("/co")
public class CompanyObjectiveController {

    /** Repository which contains the {@link CompanyObjective Company Objectives}. */
    private final CompanyObjectiveRepository repository;

    /** Repository which contains the {@link CompanyObjectiveKeyResult Company Objective Key Results}. */
    private final CompanyObjectiveKeyResultRepository keyResultRepository;

    /** Assembler which constructs the {@link CompanyObjective Company Objective}. */
    private final CompanyObjectiveAssembler assembler;

    /** Assembler which constructs the {@link CompanyObjectiveKeyResult Company Objective Key Result}. */
    private final CompanyObjectiveKeyResultAssembler keyResultAssembler;

    /** Assembler which constructs the {@link HistoryCompanyObjectiveKeyResult History of Company Objective Key Results}. */
    private final HistoryCompanyObjectiveKeyResultAssembler historyCompanyObjectiveKeyResultAssembler;

    /** Repository which contains the {@link User Users}. */
    private final UserRepository userRepository;

    /**
     * Get Mapping for a specific {@link CompanyObjective Company Objective}.
     *
     * @param id contains the ID of a {@link CompanyObjective Company Objective}.
     * @return an Entity Model of a {@link CompanyObjective Company Objective}.
     */
    @GetMapping("/{id}")
    public EntityModel<CompanyObjective> one(@PathVariable Long id){
        CompanyObjective companyObjective = repository.findById(id).orElseThrow(() -> new CompanyObjectiveNotFoundException(id));

        return assembler.toModel(companyObjective);
    }

    /**
     * Get Mapping for all {@link CompanyObjective Company Objectives}.
     *
     * @return a Collection Model of all {@link CompanyObjective Company Objectives}.
     */
    @GetMapping
    public CollectionModel<EntityModel<CompanyObjective>> all(){
        List<EntityModel<CompanyObjective>> companyObjectives = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(companyObjectives,
                linkTo(methodOn(CompanyObjectiveController.class).all()).withSelfRel()
        );
    }

    /**
     * Get Mapping for all {@link CompanyObjectiveKeyResult Key Results} of a specific {@link CompanyObjective Company Objective}.
     *
     * @param id contains the ID of a {@link CompanyObjective Company Objective}.
     * @return a Collection Model of all {@link CompanyObjectiveKeyResult Key Results} of the specific {@link CompanyObjective Company Objective}.
     */
    @GetMapping("/{id}/keyResults")
    public CollectionModel<EntityModel<CompanyObjectiveKeyResult>> allKeyResults(@PathVariable Long id) {
        CompanyObjective companyObjective = repository.findById(id).orElseThrow(() -> new CompanyObjectiveNotFoundException(id));
        List<EntityModel<CompanyObjectiveKeyResult>> companyObjectiveKeyResults = companyObjective.getKeyResults().stream()
                .map(keyResultAssembler::toModel)
                .collect(Collectors.toList());

        return  CollectionModel.of(companyObjectiveKeyResults,
                linkTo(methodOn(CompanyObjectiveController.class).allKeyResults(id)).withSelfRel());

    }

    /**
     * Get Mapping for a specific {@link CompanyObjectiveKeyResult Key Result} of specific {@link CompanyObjective Company Objective}.
     *
     * @param id contains the ID of a {@link CompanyObjective Company Objective}.
     * @param kid contains the ID of a {@link CompanyObjectiveKeyResult Company Objective Key Result}.
     * @return an Entity Model of a {@link CompanyObjectiveKeyResult Company Objective Key Result}.
     */
    @GetMapping("/{id}/keyResults/{kid}")
    public EntityModel<CompanyObjectiveKeyResult> oneKeyResult(@PathVariable Long id, @PathVariable Long kid) {
        CompanyObjective companyObjective = repository.findById(id).orElseThrow(() -> new CompanyObjectiveNotFoundException(id) );
        CompanyObjectiveKeyResult companyObjectiveKeyResult= companyObjective.getKeyResults().stream()
                .filter(o -> o.getId().equals(kid))
                .findFirst()
                .orElseThrow(() -> new CompanyObjectiveKeyResultNotFoundException(kid));

        return keyResultAssembler.toModel(companyObjectiveKeyResult);
    }

    /**
     * Get Mapping for the complete {@link HistoryCompanyObjectiveKeyResult History} of a specific {@link CompanyObjectiveKeyResult Company Objective Key Result}.
     *
     * @param id contains the ID of a {@link CompanyObjective Company Objective}.
     * @param kid contains the ID of a {@link CompanyObjectiveKeyResult Company Objective Key Result}.
     * @return a Collection Model of the {@link HistoryCompanyObjectiveKeyResult History} of a specific {@link CompanyObjectiveKeyResult Company Objective Key Result}.
     */
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

    /**
     * Post Mapping to create a new {@link CompanyObjective Company Objective}.
     *
     * @param companyObjective contains the {@link CompanyObjective Company Objective} which should be created.
     * @return a Response Entity of the new {@link CompanyObjective Company Objective}.
     */
    @PostMapping
    public ResponseEntity<EntityModel<CompanyObjective>> createCompanyObjective(@RequestBody CompanyObjective companyObjective) {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new UsernameNotFoundException(""));
        companyObjective.setUser(user);
        CompanyObjective newCompanyObjective = repository.save(companyObjective);

        return ResponseEntity
                .created(linkTo(methodOn(CompanyObjectiveController.class).one(newCompanyObjective.getId())).toUri())
                .body(assembler.toModel(newCompanyObjective));
    }

    /**
     * Put Mapping for a specific {@link CompanyObjective Company Objective}.
     *
     * @param newCompanyObjective contains a new {@link CompanyObjective Company Objective}.
     * @param id contains the ID of the {@link CompanyObjective Company Objective} which should be replaced.
     * @return a Response Entity of a {@link CompanyObjective Company Objective}.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<CompanyObjective>> replaceCompanyObjective(@RequestBody CompanyObjective newCompanyObjective, @PathVariable Long id) {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new UsernameNotFoundException(""));
        newCompanyObjective.setUser(user);
        CompanyObjective updatedCompanyObjective = repository.findById(id)
                .map(companyObjective -> {
                    companyObjective.setName(newCompanyObjective.getName());
                    companyObjective.setDescription(newCompanyObjective.getDescription());
                    return repository.save(companyObjective);
                })
                .orElseGet(() -> {
                    return repository.save(newCompanyObjective);
                });
        EntityModel<CompanyObjective> entity = assembler.toModel(updatedCompanyObjective);
        return ResponseEntity
                .created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entity);
    }

    /**
     * Delete Mapping for a specific {@link CompanyObjective Company Objective}.
     *
     * @param id contains the ID of the {@link CompanyObjective Company Objective} which should be deleted.
     * @return an Empty Response Entity.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompanyObjective(@PathVariable Long id) {
        if(repository.findById(id).isEmpty()) {
            throw new CompanyObjectiveNotFoundException(id);
        }
        repository.deleteById(id);
        return ResponseEntity.accepted().build();
    }

    /**
     * Patch Mapping for a specific {@link CompanyObjective Company Objective}.
     *
     * @param updates contains a Map with the updated which should be applied.
     * @param id contains the ID of the {@link CompanyObjective Company Objective} which should be updated.
     * @return a Response Entity of a {@link CompanyObjective Company Objective}.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<CompanyObjective>> updateCompanyObjective(@RequestBody Map<String, Object> updates, @PathVariable Long id) {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new UsernameNotFoundException(""));
        CompanyObjective companyObjective = repository.findById(id).orElseThrow(() -> new CompanyObjectiveNotFoundException(id));
        updates.put("user", user);
        CompanyObjective updatedCompanyObjective = repository.save(companyObjective.applyPatch(updates));

        EntityModel<CompanyObjective> entity = assembler.toModel(updatedCompanyObjective);
        return ResponseEntity
                .created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entity);
    }

    /**
     * Post Mapping to create a new {@link CompanyObjectiveKeyResult Company Objective Key Result}.
     *
     * @param newCompanyObjectiveKeyResult contains the {@link CompanyObjectiveKeyResult Company Objective Key Result} which should be created.
     * @param id contains the ID of a {@link CompanyObjective Company Objective}.
     * @return a Response Entity of the new {@link CompanyObjectiveKeyResult Company Objective Key Result}.
     */
    @PostMapping("/{id}/keyResults")
    public ResponseEntity<EntityModel<CompanyObjectiveKeyResult>> createKeyResult(@RequestBody CompanyObjectiveKeyResult newCompanyObjectiveKeyResult, @PathVariable Long id) {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new UsernameNotFoundException(""));
        newCompanyObjectiveKeyResult.setUser(user);
        CompanyObjective companyObjective = repository.findById(id).orElseThrow(() -> new CompanyObjectiveNotFoundException(id));
        newCompanyObjectiveKeyResult.setCompanyObjective(companyObjective);
        CompanyObjectiveKeyResult keyResult = keyResultRepository.save(newCompanyObjectiveKeyResult);

        return ResponseEntity
                .created(linkTo(methodOn(CompanyObjectiveController.class).one(keyResult.getId())).toUri())
                .body(keyResultAssembler.toModel(keyResult));
    }

    /**
     * Put Mapping for a specific {@link CompanyObjectiveKeyResult Company Objective Key Result}.
     *
     * @param newCompanyObjectiveKeyResult contains a new {@link CompanyObjectiveKeyResult Company Objective Key Result}.
     * @param id contains the ID of a {@link CompanyObjective Company Objective}.
     * @param kid contains the ID of the {@link CompanyObjectiveKeyResult Company Objective Key Result} which should be replaced.
     * @return a Response Entity of a {@link CompanyObjectiveKeyResult Company Objective Key Result}.
     */
    @PutMapping("/{id}/keyResults/{kid}")
    public ResponseEntity<EntityModel<CompanyObjectiveKeyResult>> replaceKeyResult(@RequestBody CompanyObjectiveKeyResult newCompanyObjectiveKeyResult, @PathVariable Long id, @PathVariable Long kid) {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new UsernameNotFoundException(""));
        newCompanyObjectiveKeyResult.setUser(user);
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
                    newCompanyObjectiveKeyResult.setCompanyObjective(companyObjective);
                    return keyResultRepository.save(newCompanyObjectiveKeyResult);
                });

        EntityModel<CompanyObjectiveKeyResult> entity = keyResultAssembler.toModel(companyObjectiveKeyResult);
        return ResponseEntity
                .created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entity);
    }

    /**
     * Delete Mapping for a specific {@link CompanyObjectiveKeyResult Company Objective Key Result}.
     *
     * @param id contains the ID of a {@link CompanyObjective Company Objective}.
     * @param kid contains the ID of the {@link CompanyObjectiveKeyResult Company Objective Key Result} which should be deleted.
     * @return an empty Response Entity.
     */
    @DeleteMapping("/{id}/keyResults/{kid}")
    public ResponseEntity<?> deleteKeyResult(@PathVariable Long id, @PathVariable Long kid) {
        if(keyResultRepository.findById(id).isEmpty()) {
            throw new CompanyObjectiveKeyResultNotFoundException(kid);
        }
        keyResultRepository.deleteById(kid);
        return ResponseEntity.accepted().build();
    }

    /**
     * Patch Mapping for a specific {@link CompanyObjectiveKeyResult Company Objective Key Result}.
     *
     * @param updates contains a Map with the updates that should be applied.
     * @param id contains the ID of a {@link CompanyObjective Company Objective}.
     * @param kid contains the ID of the {@link CompanyObjectiveKeyResult Company Objective Key Result} which should be updated.
     * @return a Response Entity of a {@link CompanyObjectiveKeyResult Company Objective Key Result}.
     */
    @PatchMapping("/{id}/keyResults/{kid}")
    public ResponseEntity<EntityModel<CompanyObjectiveKeyResult>> updateKeyResult(@RequestBody Map<String, Object> updates, @PathVariable Long id, @PathVariable Long kid) {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new UsernameNotFoundException(""));
        updates.put("user", user);
        CompanyObjective companyObjective = repository.findById(id).orElseThrow(() -> new CompanyObjectiveNotFoundException(id));
        CompanyObjectiveKeyResult keyResult = keyResultRepository.findById(kid).orElseThrow(() -> new CompanyObjectiveKeyResultNotFoundException(kid));

        CompanyObjectiveKeyResult updatedCompanyObjectiveKeyResult = keyResultRepository.save(keyResult.applyPatch(updates));
        EntityModel<CompanyObjectiveKeyResult> entity = keyResultAssembler.toModel(updatedCompanyObjectiveKeyResult);
        return ResponseEntity
                .created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entity);
    }

}

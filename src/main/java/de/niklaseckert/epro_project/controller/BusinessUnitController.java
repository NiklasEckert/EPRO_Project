package de.niklaseckert.epro_project.controller;

import de.niklaseckert.epro_project.controller.exceptions.*;
import de.niklaseckert.epro_project.model.*;
import de.niklaseckert.epro_project.model.assembler.BusinessUnitAssembler;
import de.niklaseckert.epro_project.model.assembler.BusinessUnitKeyResultAssembler;
import de.niklaseckert.epro_project.model.assembler.BusinessUnitObjectiveAssembler;
import de.niklaseckert.epro_project.model.assembler.HistoryBusinessUnitObjectiveKeyResultAssembler;
import de.niklaseckert.epro_project.repos.BusinessUnitObjectiveKeyResultRepository;
import de.niklaseckert.epro_project.repos.BusinessUnitObjectiveRepository;
import de.niklaseckert.epro_project.repos.BusinessUnitRepository;
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

@RestController
@AllArgsConstructor
@RequestMapping("/bu")
public class BusinessUnitController {

    private final BusinessUnitRepository repository;
    private final BusinessUnitObjectiveRepository objectiveRepository;
    private final BusinessUnitObjectiveKeyResultRepository keyResultRepository;
    private final BusinessUnitAssembler assembler;
    private final BusinessUnitObjectiveAssembler objectiveAssembler;
    private final BusinessUnitKeyResultAssembler keyResultAssembler;
    private final HistoryBusinessUnitObjectiveKeyResultAssembler historyBusinessUnitObjectiveKeyResultAssembler;
    private final UserRepository userRepository;

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
    public CollectionModel<EntityModel<BusinessUnitObjective>> allObjectives(@PathVariable Long id) {
        BusinessUnit businessUnit = repository.findById(id).orElseThrow(() -> new BusinessUnitNotFoundException(id));
        List<EntityModel<BusinessUnitObjective>> businessUnitObjectives = businessUnit.getBusinessUnitObjectives().stream()
                .filter(o -> (o.getUser().getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName()) ||
                        SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toList()).contains("ROLE_READ_ONLY")))
                .map(objectiveAssembler::toModel)
                .collect(Collectors.toList());

        return  CollectionModel.of(businessUnitObjectives,
                linkTo(methodOn(BusinessUnitController.class).allObjectives(id)).withSelfRel());

    }

    @GetMapping("/{id}/objectives/{oid}")
    public EntityModel<BusinessUnitObjective> oneObjective(@PathVariable Long id, @PathVariable Long oid) {
        BusinessUnit businessUnit = repository.findById(id).orElseThrow(() -> new BusinessUnitNotFoundException(id));
        BusinessUnitObjective objective= businessUnit.getBusinessUnitObjectives().stream()
                .filter(o -> (o.getId().equals(oid) && o.getUser().getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName()) ||
                        SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toList()).contains("ROLE_READ_ONLY")))
                .findFirst()
                .orElseThrow(() -> new BusinessUnitObjectiveNotFoundException(oid));

        return objectiveAssembler.toModel(objective);
    }

    @GetMapping("/{id}/objectives/{oid}/keyResults/{kid}")
    public EntityModel<BusinessUnitObjectiveKeyResult> oneKeyResult(@PathVariable Long id, @PathVariable Long oid, @PathVariable Long kid) {
        BusinessUnitObjective businessUnitObjective = objectiveRepository.findById(oid).orElseThrow(() -> new BusinessUnitNotFoundException(oid));
        BusinessUnitObjectiveKeyResult businessUnitObjectiveKeyResult= businessUnitObjective.getBusinessUnitObjectiveKeyResults().stream()
                .filter(o -> (o.getId().equals(kid) && o.getUser().getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName()) ||
                        SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toList()).contains("ROLE_READ_ONLY")))
                .findFirst()
                .orElseThrow(() -> new BusinessUnitObjectivesKeyResultNotFoundException(kid));

        return keyResultAssembler.toModel(businessUnitObjectiveKeyResult);
    }

    @GetMapping("/{id}/objectives/{oid}/keyResults")
    public CollectionModel<EntityModel<BusinessUnitObjectiveKeyResult>> allKeyResults(@PathVariable Long id, @PathVariable Long oid) {
        BusinessUnitObjective businessUnitObjective = objectiveRepository.findById(oid).orElseThrow(() -> new BusinessUnitNotFoundException(oid));
        List<EntityModel<BusinessUnitObjectiveKeyResult>> businessUnitObjectiveKeyResults =businessUnitObjective.getBusinessUnitObjectiveKeyResults().stream()
                .filter(o -> (o.getUser().getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName()) ||
                        SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toList()).contains("ROLE_READ_ONLY")))
                .map(keyResultAssembler::toModel)
                .collect(Collectors.toList());

        return  CollectionModel.of(businessUnitObjectiveKeyResults,
                linkTo(methodOn(BusinessUnitController.class).allKeyResults(id,oid)).withSelfRel());

    }

    @GetMapping("/{id}/objectives/{oid}/keyResults/{kid}/history")
    public CollectionModel<EntityModel<HistoryBusinessUnitObjectiveKeyResult>> keyResultHistory(@PathVariable Long id, @PathVariable Long oid, @PathVariable Long kid) {
        BusinessUnitObjective businessUnitObjective = objectiveRepository.findById(oid).orElseThrow(() -> new BusinessUnitNotFoundException(oid));
        BusinessUnitObjectiveKeyResult businessUnitObjectiveKeyResult= businessUnitObjective.getBusinessUnitObjectiveKeyResults().stream()
                .filter(o -> (o.getId().equals(kid) && o.getUser().getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName()) ||
                        SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toList()).contains("ROLE_READ_ONLY")))
                .findFirst()
                .orElseThrow(() -> new BusinessUnitObjectivesKeyResultNotFoundException(kid));

        List<EntityModel<HistoryBusinessUnitObjectiveKeyResult>> historyBusinessUnitObjectiveKeyResults = businessUnitObjectiveKeyResult.getHistory().stream()
                .map(historyBusinessUnitObjectiveKeyResultAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(historyBusinessUnitObjectiveKeyResults, linkTo(methodOn(BusinessUnitController.class).keyResultHistory(id, oid,kid)).withSelfRel());
    }

    @PostMapping
    public ResponseEntity<EntityModel<BusinessUnit>> createBusinessUnit(@RequestBody BusinessUnit businessUnit) {
        BusinessUnit newBusinessUnit = repository.save(businessUnit);
        return ResponseEntity
                .created(linkTo(methodOn(BusinessUnitController.class).one(newBusinessUnit.getId())).toUri())
                .body(assembler.toModel(newBusinessUnit));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<BusinessUnit>> replaceBusinessUnit(@RequestBody BusinessUnit newBusinessUnit, @PathVariable Long id) {
        BusinessUnit updatedBusinessUnit = repository.findById(id)
                .map(businessUnit -> {
                    businessUnit.setName(newBusinessUnit.getName());
                    return repository.save(businessUnit);
                })
                .orElseGet(() -> {
                    return repository.save(newBusinessUnit);
                });
        EntityModel<BusinessUnit> entity = assembler.toModel(updatedBusinessUnit);
        return ResponseEntity
                .created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBusinessUnit(@PathVariable Long id) {
        if (repository.findById(id).isEmpty()) {
            throw new BusinessUnitNotFoundException(id);
        }
        repository.deleteById(id);
        return ResponseEntity.accepted().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<BusinessUnit>> updateBusinessUnit(@RequestBody Map<String, Object> updates, @PathVariable Long id) {
        BusinessUnit businessUnit = repository.findById(id).orElseThrow(() -> new BusinessUnitNotFoundException(id));
        BusinessUnit updatedBusinessUnit = repository.save(businessUnit.applyPatch(updates));

        EntityModel<BusinessUnit> entity = assembler.toModel(updatedBusinessUnit);
        return ResponseEntity
                .created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entity);
    }

    @PostMapping("/{id}/objectives")
    public ResponseEntity<EntityModel<BusinessUnitObjective>> createBusinessUnitObjective(@RequestBody BusinessUnitObjective newBusinessUnitObjective, @PathVariable Long id) {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new UsernameNotFoundException(""));
        newBusinessUnitObjective.setUser(user);
        BusinessUnit businessUnit = repository.findById(id).orElseThrow(() -> new BusinessUnitNotFoundException(id));
        newBusinessUnitObjective.setBusinessUnit(businessUnit);
        BusinessUnitObjective objective = objectiveRepository.save(newBusinessUnitObjective);

        return ResponseEntity
                .created(linkTo(methodOn(BusinessUnitController.class).one(objective.getId())).toUri())
                .body(objectiveAssembler.toModel(objective));
    }

    @PutMapping("/{id}/objectives/{oid}")
    public ResponseEntity<EntityModel<BusinessUnitObjective>> replaceBusinessUnitObjective(@RequestBody BusinessUnitObjective newBusinessUnitObjective, @PathVariable Long id, @PathVariable Long oid) {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new UsernameNotFoundException(""));
        newBusinessUnitObjective.setUser(user);
        BusinessUnit businessUnit = repository.findById(id).orElseThrow(() -> new BusinessUnitNotFoundException(id));
        BusinessUnitObjective businessUnitObjective = objectiveRepository.findById(oid)
                .map(objective -> {
                    objective.setName(newBusinessUnitObjective.getName());
                    objective.setDescription(newBusinessUnitObjective.getDescription());
                    return objectiveRepository.save(objective);
                })
                .orElseGet(() -> {
                    newBusinessUnitObjective.setId(oid);
                    newBusinessUnitObjective.setBusinessUnit(businessUnit);
                    return objectiveRepository.save(newBusinessUnitObjective);
                });

        EntityModel<BusinessUnitObjective> entity = objectiveAssembler.toModel(businessUnitObjective);
        return ResponseEntity
                .created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entity);
    }

    @DeleteMapping("/{id}/objectives/{oid}")
    public ResponseEntity<?> deleteBusinessUnitObjective(@PathVariable Long id, @PathVariable Long oid) {
        objectiveRepository.deleteById(oid);
        return ResponseEntity.noContent().build();
    }

    //TODO:Request Body als Map
    @PatchMapping("/{id}/objectives/{oid}")
    public ResponseEntity<EntityModel<BusinessUnitObjective>> updatedBusinessUnitObjective(@RequestBody Map<String, Object> updates, @PathVariable Long id, @PathVariable Long oid) {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new UsernameNotFoundException(""));
        updates.put("user", user);
        BusinessUnit businessUnit = repository.findById(id).orElseThrow(() -> new BusinessUnitNotFoundException(id));
        BusinessUnitObjective objective = objectiveRepository.findById(oid).orElseThrow(() -> new BusinessUnitObjectiveNotFoundException(oid));

        BusinessUnitObjective updatedBusinessUnitObjective = objectiveRepository.save(objective.applyPatch(updates));
        EntityModel<BusinessUnitObjective> entity = objectiveAssembler.toModel(updatedBusinessUnitObjective);
        return ResponseEntity
                .created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entity);
    }

    @PostMapping("/{id}/objectives/{oid}/keyResults")
    public ResponseEntity<EntityModel<BusinessUnitObjectiveKeyResult>> createBusinessUnitObjectiveKeyResult(@RequestBody BusinessUnitObjectiveKeyResult newBusinessUnitObjectiveKeyResult, @PathVariable Long id, @PathVariable Long oid) {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new UsernameNotFoundException(""));
        newBusinessUnitObjectiveKeyResult.setUser(user);
        BusinessUnit businessUnit = repository.findById(id).orElseThrow(() -> new BusinessUnitNotFoundException(id));
        BusinessUnitObjective businessUnitObjective = objectiveRepository.findById(oid).orElseThrow(() -> new BusinessUnitObjectiveNotFoundException(oid));

        newBusinessUnitObjectiveKeyResult.setBusinessUnitObjective(businessUnitObjective);
        BusinessUnitObjectiveKeyResult keyResult = keyResultRepository.save(newBusinessUnitObjectiveKeyResult);

        return ResponseEntity
                .created(linkTo(methodOn(BusinessUnitController.class).one(keyResult.getId())).toUri())
                .body(keyResultAssembler.toModel(keyResult));
    }

    @PutMapping("/{id}/objectives/{oid}/keyResults/{kid}")
    public ResponseEntity<EntityModel<BusinessUnitObjectiveKeyResult>> replaceBusinessUnitObjectiveKeyResult(@RequestBody BusinessUnitObjectiveKeyResult newBusinessUnitObjectiveKeyResult, @PathVariable Long id, @PathVariable Long oid, @PathVariable Long kid) {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new UsernameNotFoundException(""));
        newBusinessUnitObjectiveKeyResult.setUser(user);
        BusinessUnit businessUnit = repository.findById(id).orElseThrow(() -> new BusinessUnitNotFoundException(id));
        BusinessUnitObjective businessUnitObjective = objectiveRepository.findById(oid).orElseThrow(() -> new BusinessUnitObjectiveNotFoundException(oid));
        BusinessUnitObjectiveKeyResult businessUnitObjectiveKeyResult = keyResultRepository.findById(kid)
                .map(keyResult -> {
                    keyResult.setName(newBusinessUnitObjectiveKeyResult.getName());
                    keyResult.setDescription(newBusinessUnitObjectiveKeyResult.getDescription());
                    keyResult.setCurrent(newBusinessUnitObjectiveKeyResult.getCurrent());
                    keyResult.setGoal(newBusinessUnitObjectiveKeyResult.getGoal());
                    keyResult.setConfidenceLevel(newBusinessUnitObjectiveKeyResult.getConfidenceLevel());
                    keyResult.setComment(newBusinessUnitObjectiveKeyResult.getComment());
                    keyResult.setBusinessUnitObjective(businessUnitObjective);
                    return keyResultRepository.save(keyResult);
                })
                .orElseGet(() -> {
                    newBusinessUnitObjectiveKeyResult.setId(kid);
                    newBusinessUnitObjectiveKeyResult.setBusinessUnitObjective(businessUnitObjective);
                    return keyResultRepository.save(newBusinessUnitObjectiveKeyResult);
                });


        EntityModel<BusinessUnitObjectiveKeyResult> entity = keyResultAssembler.toModel(businessUnitObjectiveKeyResult);
        return ResponseEntity
                .created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entity);
    }

    @DeleteMapping("/{id}/objectives/{oid}/keyResults/{kid}")
    public ResponseEntity<?> deleteBusinessUnitObjectiveKeyResult(@RequestBody BusinessUnitObjectiveKeyResult newBusinessUnitObjectiveKeyResult, @PathVariable Long id, @PathVariable Long oid, @PathVariable Long kid) {
        keyResultRepository.deleteById(kid);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/objectives/{oid}/keyResults/{kid}")
    public ResponseEntity<EntityModel<BusinessUnitObjectiveKeyResult>> updateBusinessUnitObjectiveKeyResult(@RequestBody Map<String, Object> updates, @PathVariable Long id, @PathVariable Long oid, @PathVariable Long kid) {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new UsernameNotFoundException(""));
        updates.put("user",user);
        BusinessUnit businessUnit = repository.findById(id).orElseThrow(() -> new BusinessUnitNotFoundException(id));
        BusinessUnitObjective businessUnitObjective = objectiveRepository.findById(oid).orElseThrow(() -> new BusinessUnitObjectiveNotFoundException(oid));
        BusinessUnitObjectiveKeyResult keyResult = keyResultRepository.findById(kid).orElseThrow(() -> new BusinessUnitObjectivesKeyResultNotFoundException(kid));

        BusinessUnitObjectiveKeyResult updatedBusinessUnitObjectiveKeyResult = keyResultRepository.save(keyResult.applyPatch(updates));
        EntityModel<BusinessUnitObjectiveKeyResult> entity = keyResultAssembler.toModel(updatedBusinessUnitObjectiveKeyResult);
        return ResponseEntity
                .created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entity);
    }
}

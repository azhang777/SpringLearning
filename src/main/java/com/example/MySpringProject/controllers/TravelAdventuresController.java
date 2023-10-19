package com.example.MySpringProject.controllers;

import com.example.MySpringProject.entities.Adventure;
import com.example.MySpringProject.repositories.AdventureRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/traveladventures")
public class TravelAdventuresController {

    private final AdventureRepository adventureRepository;

    public TravelAdventuresController(AdventureRepository adventureRepo) {
        this.adventureRepository = adventureRepo;
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<Adventure>> findAllAdventures() {
        Iterable<Adventure> adventures = this.adventureRepository.findAll();
        return ResponseEntity.ok(adventures);
    }

    @GetMapping("/bycountry/{country}")
    public ResponseEntity<List<Adventure>> findByCountry(@PathVariable String country) {
        List<Adventure> adventuresByCountry = adventureRepository.findByCountry(country);
        return ResponseEntity.ok(adventuresByCountry);
    }

    @GetMapping("/bystate")
    public ResponseEntity<List<Adventure>> findByState(@RequestParam String state) {
        List<Adventure> adventuresByState = adventureRepository.findByState(state);
        return ResponseEntity.ok(adventuresByState);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addVenture(@RequestBody Adventure adventure) {
        try {
            adventureRepository.save(adventure);
            return ResponseEntity.status(HttpStatus.CREATED).body("Adventure has been successfully added");
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add to adventures: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> editAdventure(@PathVariable int id, @RequestBody Adventure adventure) {
        Optional<Adventure> adventureOptional = this.adventureRepository.findById(id);
        if (!adventureOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "id is not valid");
        }

        Adventure thisAdventure = adventureOptional.get();
        thisAdventure.setBlogCompleted(true);
        return ResponseEntity.status(HttpStatus.OK).body("Blog set to completed for " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdventure(@PathVariable int id) {
        Optional<Adventure> adventureOptional = this.adventureRepository.findById(id);
        if (!adventureOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "id is not valid");
        }
        Adventure thisAdventure = adventureOptional.get();
        adventureRepository.delete(thisAdventure);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

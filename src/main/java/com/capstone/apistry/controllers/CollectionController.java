package com.capstone.apistry.controllers;

import com.capstone.apistry.entities.Collection;
import com.capstone.apistry.services.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collections")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;

    @PostMapping
    public Collection create(@RequestBody Collection collection) {
        return collectionService.saveCollection(collection);
    }

    @GetMapping("/{id}")
    public Collection getById(@PathVariable Long id) {
        return collectionService.getCollectionById(id);
    }

    @GetMapping
    public List<Collection> getAll() {
        return collectionService.getAllCollections();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        collectionService.deleteCollection(id);
    }
}

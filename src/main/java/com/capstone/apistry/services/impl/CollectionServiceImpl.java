package com.capstone.apistry.services.impl;

import com.capstone.apistry.entities.Collection;
import com.capstone.apistry.repositories.CollectionRepository;
import com.capstone.apistry.services.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {

    private final CollectionRepository collectionRepository;

    @Override
    public Collection saveCollection(Collection collection) {
        return collectionRepository.save(collection);
    }

    @Override
    public Collection getCollectionById(Long id) {
        return collectionRepository.findById(id).orElse(null);
    }

    @Override
    public List<Collection> getAllCollections() {
        return collectionRepository.findAll();
    }

    @Override
    public void deleteCollection(Long id) {
        collectionRepository.deleteById(id);
    }
}

package com.capstone.apistry.services;

import com.capstone.apistry.entities.Collection;
import java.util.List;

public interface CollectionService {
    Collection saveCollection(Collection collection);
    Collection getCollectionById(Long id);
    List<Collection> getAllCollections();
    void deleteCollection(Long id);
}

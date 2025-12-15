package com.capstone.apistry.services.impl;

import com.capstone.apistry.entities.Workspace;
import com.capstone.apistry.repositories.WorkspaceRepository;
import com.capstone.apistry.services.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;

    @Override
    public Workspace saveWorkspace(Workspace workspace) {
        return workspaceRepository.save(workspace);
    }

    @Override
    public Workspace getWorkspaceById(Long id) {
        return workspaceRepository.findById(id).orElse(null);
    }

    @Override
    public List<Workspace> getAllWorkspaces() {
        return workspaceRepository.findAll();
    }

    @Override
    public void deleteWorkspace(Long id) {
        workspaceRepository.deleteById(id);
    }
}

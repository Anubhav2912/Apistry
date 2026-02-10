package com.capstone.apistry.services;

import com.capstone.apistry.entities.Workspace;
import java.util.List;

public interface WorkspaceService {
    Workspace saveWorkspace(Workspace workspace);
    Workspace getWorkspaceById(Long id);
    List<Workspace> getAllWorkspaces();
    void deleteWorkspace(Long id);
}

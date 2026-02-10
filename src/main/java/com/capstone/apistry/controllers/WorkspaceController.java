package com.capstone.apistry.controllers;

import com.capstone.apistry.entities.Workspace;
import com.capstone.apistry.services.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping
    public Workspace create(@RequestBody Workspace workspace) {
        return workspaceService.saveWorkspace(workspace);
    }

    @GetMapping("/{id}")
    public Workspace getById(@PathVariable Long id) {
        return workspaceService.getWorkspaceById(id);
    }

    @GetMapping
    public List<Workspace> getAll() {
        return workspaceService.getAllWorkspaces();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        workspaceService.deleteWorkspace(id);
    }
}

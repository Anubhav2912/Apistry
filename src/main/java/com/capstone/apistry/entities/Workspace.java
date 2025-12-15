package com.capstone.apistry.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "workspaces")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    
    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL)
    private List<Collection> collections;
    
    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL)
    private List<Environment> environments;
}

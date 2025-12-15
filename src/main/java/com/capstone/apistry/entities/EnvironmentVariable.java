package com.capstone.apistry.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "environment_variables")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvironmentVariable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String keyName;
    private String value;
    
    @ManyToOne
    @JoinColumn(name = "environment_id")
    private Environment environment;
}

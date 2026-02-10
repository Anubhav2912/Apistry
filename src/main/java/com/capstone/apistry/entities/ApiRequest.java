package com.capstone.apistry.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "api_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String method;
    private String url;

    @Lob
    private String headers;

    @Lob
    private String body;

    /**
     * Whether the request was explicitly saved by the user (like "Save" in Postman),
     * as opposed to only appearing in transient history.
     */
    private boolean saved;

    /**
     * Owner of this request so each user's history and saved list are scoped
     * to their own account.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;
    
    @ManyToOne
    @JoinColumn(name = "collection_id")
    private Collection collection;
}

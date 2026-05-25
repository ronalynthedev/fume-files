package com.fumefiles.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Entity
@Table(name = "fragrances")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fragrance {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brand;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String family;

    private String intensity;

    @ElementCollection
    @CollectionTable(
        name = "fragrance_seasons",
        joinColumns = @JoinColumn(name = "fragrance_id")
    )
    @Column(name = "season")
    private List<String> seasons;

    @ElementCollection
    @CollectionTable(
        name = "fragrance_accords",
        joinColumns = @JoinColumn(name = "fragrance_id")
    )
    @Column(name = "accord")
    private List<String> accords;
}
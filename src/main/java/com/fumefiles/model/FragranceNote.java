package com.fumefiles.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "fragrance_notes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FragranceNote {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "fragrance_id", nullable = false)
    private Fragrance fragrance;

    @ManyToOne
    @JoinColumn(name = "note_id", nullable = false)
    private Note note;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotePosition position;

    public enum NotePosition {
        TOP, HEART, BASE
    }
}
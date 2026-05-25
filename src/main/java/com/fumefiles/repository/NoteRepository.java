package com.fumefiles.repository;

import com.fumefiles.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, String> {

    Optional<Note> findByNameIgnoreCase(String name);

    List<Note> findByFamily(String family);
}
package com.fumefiles.repository;

import com.fumefiles.model.FragranceNote;
import com.fumefiles.model.FragranceNote.NotePosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FragranceNoteRepository extends JpaRepository<FragranceNote, String> {

    List<FragranceNote> findByFragranceId(String fragranceId);

    List<FragranceNote> findByNoteIdAndPosition(String noteId, NotePosition position);
}
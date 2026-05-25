package com.fumefiles.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fumefiles.model.Fragrance;
import com.fumefiles.model.FragranceNote;
import com.fumefiles.model.FragranceNote.NotePosition;
import com.fumefiles.model.Note;
import com.fumefiles.repository.FragranceNoteRepository;
import com.fumefiles.repository.FragranceRepository;
import com.fumefiles.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final FragranceRepository fragranceRepository;
    private final NoteRepository noteRepository;
    private final FragranceNoteRepository fragranceNoteRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {

        if (fragranceRepository.count() > 0) {
            log.info("Database already seeded, skipping...");
            return;
        }

        log.info("Loading fragrances from JSON...");

        var resource = new ClassPathResource("data/fragrances.json");
        List<Map<String, Object>> fragranceData = objectMapper.readValue(
            resource.getInputStream(),
            new TypeReference<>() {}
        );

        for (Map<String, Object> data : fragranceData) {
            Fragrance fragrance = fragranceRepository.save(
                Fragrance.builder()
                    .name((String) data.get("name"))
                    .brand((String) data.get("brand"))
                    .description((String) data.get("description"))
                    .family((String) data.get("family"))
                    .intensity((String) data.get("intensity"))
                    .seasons((List<String>) data.get("seasons"))
                    .accords((List<String>) data.get("accords"))
                    .build()
            );

            Map<String, List<String>> notes = (Map<String, List<String>>) data.get("notes");

            saveNotes(fragrance, notes.get("top"), NotePosition.TOP);
            saveNotes(fragrance, notes.get("heart"), NotePosition.HEART);
            saveNotes(fragrance, notes.get("base"), NotePosition.BASE);
        }

        log.info("Seeding complete! {} fragrances loaded.", fragranceRepository.count());
    }

    private void saveNotes(Fragrance fragrance, List<String> noteNames, NotePosition position) {
        if (noteNames == null) return;
        for (String noteName : noteNames) {
            Note note = noteRepository.findByNameIgnoreCase(noteName)
                .orElseGet(() -> noteRepository.save(
                    Note.builder()
                        .name(noteName)
                        .family(inferFamily(noteName))
                        .subFamily("")
                        .build()
                ));

            fragranceNoteRepository.save(
                FragranceNote.builder()
                    .fragrance(fragrance)
                    .note(note)
                    .position(position)
                    .build()
            );
        }
    }

    private String inferFamily(String noteName) {
        Map<String, String> families = Map.ofEntries(
            Map.entry("Vanilla", "Gourmand"), Map.entry("Marshmallow", "Gourmand"),
            Map.entry("Sugar", "Gourmand"), Map.entry("Brown Sugar", "Gourmand"),
            Map.entry("Tonka Bean", "Gourmand"), Map.entry("Buttercream", "Gourmand"),
            Map.entry("Sandalwood", "Woody"), Map.entry("Vetiver", "Woody"),
            Map.entry("Cedar", "Woody"), Map.entry("Patchouli", "Woody"),
            Map.entry("Guaiac Wood", "Woody"), Map.entry("Cashmeran", "Woody"),
            Map.entry("Jasmine", "Floral"), Map.entry("Rose", "Floral"),
            Map.entry("Iris", "Floral"), Map.entry("Magnolia", "Floral"),
            Map.entry("Tuberose", "Floral"), Map.entry("Gardenia", "Floral"),
            Map.entry("Bergamot", "Citrus"), Map.entry("Pink Pepper", "Spicy"),
            Map.entry("Amber", "Amber"), Map.entry("Musk", "Musky"),
            Map.entry("Ambroxan", "Musky"), Map.entry("Ambrette", "Musky"),
            Map.entry("Coconut", "Tropical"), Map.entry("Fig", "Fruity"),
            Map.entry("Cassis", "Fruity"), Map.entry("Blackcurrant", "Fruity"),
            Map.entry("Rum Accord", "Spicy"), Map.entry("Chestnut", "Gourmand"),
            Map.entry("Cocoa", "Gourmand"), Map.entry("Mimosa", "Floral")
        );
        return families.getOrDefault(noteName, "Other");
    }
}
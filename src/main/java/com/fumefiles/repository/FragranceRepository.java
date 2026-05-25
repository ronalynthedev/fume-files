package com.fumefiles.repository;

import com.fumefiles.model.Fragrance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FragranceRepository extends JpaRepository<Fragrance, String> {

    List<Fragrance> findByFamily(String family);

    List<Fragrance> findByIntensity(String intensity);

    List<Fragrance> findByNameContainingIgnoreCase(String name);
}
package dev.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.domain.Session;

public interface SessionRepo extends JpaRepository<Session, Long> {

    Optional<Session> findByNom(String nom);

	List<Session> findByNomStartingWith(String formation);

    
}

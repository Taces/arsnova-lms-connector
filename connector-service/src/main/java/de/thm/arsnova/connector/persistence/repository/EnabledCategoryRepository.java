package de.thm.arsnova.connector.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.thm.arsnova.connector.persistence.domain.EnabledCategory;

@Repository
public interface EnabledCategoryRepository extends JpaRepository<EnabledCategory, Integer> {
}

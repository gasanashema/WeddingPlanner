package rw.ac.auca.template;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanningTemplateRepository extends JpaRepository<PlanningTemplate, Long> {

    @EntityGraph(attributePaths = {"items"})
    List<PlanningTemplate> findByIsActiveTrue();

    @EntityGraph(attributePaths = {"items"})
    List<PlanningTemplate> findByCategoryAndIsActiveTrue(TemplateCategory category);
}


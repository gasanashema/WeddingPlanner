package rw.ac.auca.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.wedding.id = :weddingId AND t.visibilityScope IN :scopes AND t.deletedAt IS NULL")
    List<Task> findByWeddingIdAndVisibilityScopeInAndDeletedAtIsNull(
            @Param("weddingId") Long weddingId,
            @Param("scopes") Collection<VisibilityScope> scopes);

    @Query("SELECT t FROM Task t WHERE t.id = :id AND t.deletedAt IS NULL")
    Optional<Task> findByIdAndDeletedAtIsNull(@Param("id") Long id);

    @Query("SELECT t FROM Task t WHERE t.wedding.id = :weddingId AND t.deletedAt IS NOT NULL")
    List<Task> findDeletedTasksByWeddingId(@Param("weddingId") Long weddingId);
}

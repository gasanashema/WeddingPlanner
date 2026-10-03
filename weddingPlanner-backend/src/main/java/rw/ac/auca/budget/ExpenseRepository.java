package rw.ac.auca.budget;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rw.ac.auca.task.VisibilityScope;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByWeddingIdAndVisibilityScopeInAndDeletedAtIsNull(Long weddingId, List<VisibilityScope> scopes);

    Optional<Expense> findByIdAndDeletedAtIsNull(Long id);

    @Query("SELECT e FROM Expense e WHERE e.wedding.id = :weddingId AND e.deletedAt IS NOT NULL")
    List<Expense> findDeletedExpensesByWeddingId(@Param("weddingId") Long weddingId);

    List<Expense> findByWeddingIdAndDeletedAtIsNull(Long weddingId);
}

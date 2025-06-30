package se.lexicon.todo_api.repository;

import org.springframework.data.repository.CrudRepository;
import se.lexicon.todo_api.entity.Todo;

import java.time.LocalDateTime;
import java.util.List;

public interface TodoRepository extends CrudRepository<Todo, Long> {

    // 🔍 Find todos by title keyword (case-insensitive contains)
    List<Todo> findByTitleContainingIgnoreCase(String title);
    // select * from todo where lower(title) like lower(concat('%', :title ,'%'));

    // 👤 Find todos by person ID
    List<Todo> findByPersonId(Long personId);
    // select * from todo where person_id = :personId;

    // ✅ Find todos by completed status
    List<Todo> findByCompleted(boolean completed);
    // select * from todo where completed = :completed;

    // ✅ Find todos by completed true
    List<Todo> findByCompletedTrue();
    // select * from todo where completed = true;

    // 🗓️ Find todos between two due dates
    List<Todo> findByDueDateBetween(LocalDateTime start, LocalDateTime end);
    // select * from todo where due_date between :start and :end;
    // select * from todo where due_date >= :start and due_date <= :end;

    // 🗓️ Find todos due before a specific date and not completed
    List<Todo> findByDueDateBeforeAndCompletedFalse(LocalDateTime dateTime);
    // select * from todo where due_date < :dateTime and completed = false;

    // ❌ Find unassigned todos (person is null)
    List<Todo> findByPersonIsNull();
    // select * from todo where person_id is null;

    // 🔥 Find unfinished & overdue tasks (custom query)
    List<Todo> findByCompletedFalseAndDueDateBefore(LocalDateTime dateTime);
    // select * from todo where completed = false and due_date < :dateTime;

    // ✅ Find completed tasks assigned to a specific person
    List<Todo> findByPersonIdAndCompletedTrue(Long personId);
    // select * from todo where person_id = :personId and completed = true;

    // 📅 Find all with no due date
    List<Todo> findByDueDateIsNull();
    // select * from todo where due_date is null;

    // 📌 Count all tasks assigned to a person
    long countByPersonId(Long personId);
    // select count(*) from todo where person_id = :personId;

    @Override
    List<Todo> findAll();
}

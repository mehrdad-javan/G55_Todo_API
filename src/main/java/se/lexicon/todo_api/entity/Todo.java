package se.lexicon.todo_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@ToString(exclude = "attachments")
//@EqualsAndHashCode(exclude = "attachments")

@Entity
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, length = 500)
    private String description;
    private boolean completed;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime dueDate;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // orphanRemoval = true means that if an attachment is removed from the todo, it will also be deleted from the database.
    @Setter(AccessLevel.NONE) // Prevents direct modification of attachments set
    private Set<Attachment> attachments = new HashSet<>(); // 2

    @Transient
    // transient annotation is used to indicate that a field should not be persisted in the database.
    private boolean isAssigned;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false, unique = true)
    private Person person; // person_id

    public Todo(String title, String description, boolean completed, LocalDateTime dueDate) {
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.dueDate = dueDate;
    }


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


    // helper methods for managing attachments

    public void addAttachment(Attachment attachment) {
        if (attachments == null) {
            attachments = new HashSet<>();
        }
        attachments.add(attachment);
        // sync the other side
        attachment.setTodo(this);
    }

    public void removeAttachment(Attachment attachment) {
        attachments.remove(attachment);
        // disconnect the other side
        attachment.setTodo(null);
    }

}

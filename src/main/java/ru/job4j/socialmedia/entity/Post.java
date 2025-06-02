package ru.job4j.socialmedia.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "title"})
@Table (name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private String title;

    private String content;

    @NotNull
    private LocalDateTime created;

    /**
     * Файлы с фотографиями автомобиля
     */
    @OneToMany
    private Set<File> files = new HashSet<>();

    public void addFile(File file) {
        file.setPost(this);
        this.files.add(file);
    }

    public void removeFile(File file) {
        file.setPost(null);
        this.files.remove(file);
    }

    public Post(@NonNull User user, @NonNull String title, @NonNull LocalDateTime created) {
        this.user = user;
        this.title = title;
        this.created = created;
    }
}

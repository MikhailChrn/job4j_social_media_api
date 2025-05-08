package ru.job4j.socialmedia.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.NonNull;

@Entity
@Table(name = "files")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = {"id"})
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    private String title;

    @NonNull
    private String path;

    /**
     * Ссылка на объявление
     */
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public File(String title, String path) {
        this.title = title;
        this.path = path;
    }
}

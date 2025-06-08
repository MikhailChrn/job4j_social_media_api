package ru.job4j.socialmedia.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "email"})
@Table (name = "users")
@Schema(description = "User Model Information")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(unique = true, nullable = false)
    @Schema(description = "user email", example = "email@email.com")
    private String email;

    @NotNull
    @Schema(description = "user password", example = "password")
    private String password;

    @Schema(description = "Date of creation", example = "2023-10-15T15:15:15")
    private LocalDateTime create;

}

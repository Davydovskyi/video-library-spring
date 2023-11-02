package edu.jcourse.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@ToString(exclude = {"movie.reviews"})
@EqualsAndHashCode(exclude = {"movie.reviews"})
@AllArgsConstructor
@Entity
@Builder
@Table(name = "users")
public class User implements BaseEntity<Long> {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_birth_date")
    private LocalDate birthDate;

    @Column(name = "user_password", nullable = false)
    private String password;

    @Column(name = "user_email", nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "user_image")
    private String userImage;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.REFRESH)
    private List<Review> reviews = new ArrayList<>();
}
package edu.jcourse.database.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.util.ArrayList;
import java.util.List;

@FieldNameConstants
@Data
@AllArgsConstructor
@ToString(exclude = {"reviews", "moviePersons"})
@EqualsAndHashCode(exclude = {"reviews", "moviePersons"}, callSuper = false)
@NoArgsConstructor
@Builder
@Entity
@Table(name = "movie")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Movie extends AuditingEntity<Integer> {

    @Id
    @Column(name = "movie_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "movie_title", nullable = false)
    private String title;

    @Column(name = "release_year", nullable = false)
    private Short releaseYear;

    @Column(name = "movie_country", nullable = false)
    private String country;

    @Enumerated(EnumType.STRING)
    @Column(name = "movie_genre", nullable = false)
    private Genre genre;

    @Column(name = "movie_description", nullable = false)
    private String description;

    @NotAudited
    @Builder.Default
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<MoviePerson> moviePersons = new ArrayList<>();

    @NotAudited
    @Builder.Default
    @OneToMany(mappedBy = "movie", cascade = CascadeType.REFRESH)
    private List<Review> reviews = new ArrayList<>();
}
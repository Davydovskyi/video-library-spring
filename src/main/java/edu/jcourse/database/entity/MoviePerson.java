package edu.jcourse.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "movie_person")
public class MoviePerson implements BaseEntity<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "movie_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private Movie movie;

    @JoinColumn(name = "person_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private Person person;

    @Enumerated(EnumType.STRING)
    @Column(name = "person_role", nullable = false)
    private PersonRole personRole;

    public void setMovie(Movie movie) {
        this.movie = movie;
        movie.getMoviePersons().add(this);
    }

    public void setPerson(Person person) {
        this.person = person;
        person.getMoviePersons().add(this);
    }
}
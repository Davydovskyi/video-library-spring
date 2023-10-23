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
@Table(name = "review")
public class Review implements BaseEntity<Integer> {

    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(name = "movie_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Movie movie;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private User user;

    @Column(name = "review_text", nullable = false)
    private String reviewText;

    @Column(name = "rate", nullable = false)
    private Short rate;

    public void setUser(User user) {
        this.user = user;
        user.getReviews().add(this);
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
        movie.getReviews().add(this);
    }
}
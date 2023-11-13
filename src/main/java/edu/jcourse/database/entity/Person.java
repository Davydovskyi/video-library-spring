package edu.jcourse.database.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@FieldNameConstants
@Data
@AllArgsConstructor
@ToString(exclude = {"moviePersons"})
@EqualsAndHashCode(exclude = {"moviePersons"})
@NoArgsConstructor
@Builder
@Entity
@Table(name = "person")
public class Person implements BaseEntity<Integer> {

    @Id
    @Column(name = "person_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "person_name", nullable = false)
    private String name;

    @Column(name = "person_birth_date", nullable = false)
    private LocalDate birthDate;

    @Builder.Default
    @OneToMany(mappedBy = "person", cascade = CascadeType.REFRESH)
    private List<MoviePerson> moviePersons = new ArrayList<>();
}
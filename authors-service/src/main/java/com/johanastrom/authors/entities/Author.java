package com.johanastrom.authors.entities;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "Authors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "FirstName")
    private String firstName;
    @Column(name = "LastName")
    private String lastName;
    @Column(name = "BirthDate")
    private Timestamp birthDate;


    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != getClass()) return false;
        Author author = (Author) obj;
        return Objects.equals(author.getFirstName(), this.firstName) &&
                Objects.equals(author.getLastName(), this.getLastName()) &&
                Objects.equals(author.getBirthDate(), (this.getBirthDate())) ||
                author.getId() == this.id;
    }

}

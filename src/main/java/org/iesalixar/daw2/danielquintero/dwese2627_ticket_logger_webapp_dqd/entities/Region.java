package org.iesalixar.daw2.danielquintero
        .dwese2627_ticket_logger_webapp_dqd.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "regions")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "code",
            nullable = false,
            unique = true,
            length = 2
    )
    private String code;

    @Column(
            name = "name",
            nullable = false,
            length = 100
    )
    private String name;

    @OneToMany(mappedBy = "region")
    private List<Province> provinces =
            new ArrayList<>();

    public Region(
            String code,
            String name) {

        this.code = code;
        this.name = name;
    }
}
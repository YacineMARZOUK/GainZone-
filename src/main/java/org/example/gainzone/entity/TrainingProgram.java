package org.example.gainzone.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "training_program")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingProgram extends BaseEntity {

    private String name;
    private String description;
    private String frequency;
    private Integer durationWeeks;

    @ManyToOne
    private User coach;

    @ManyToOne
    private User member; // Optional

    // Exercises stored as JSON string for simplicity as per plan
    private String exercisesJson;
}

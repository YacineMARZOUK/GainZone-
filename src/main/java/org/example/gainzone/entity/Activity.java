package org.example.gainzone.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "activity")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Activity extends BaseEntity {

    private String name;
    private String description;
    private String type;
    private LocalDateTime dateTime;
    private Integer durationMinutes;
    private Integer maxParticipants;

    @ManyToOne
    private User coach;
}

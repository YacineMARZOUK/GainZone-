package org.example.gainzone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.gainzone.enums.Goal; // I might need a Goal enum too based on plan "goal (WEIGHT_LOSS...)"

@Entity
@Table(name = "member_profile")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MemberProfile extends BaseEntity {

    private Integer age;
    private String gender;
    private Double weight;
    private Double height;

    @Enumerated(EnumType.STRING)
    private Goal goal;

    private String fitnessLevel;
    private String profileImageUrl;

    @OneToOne(mappedBy = "memberProfile")
    private User user;
}

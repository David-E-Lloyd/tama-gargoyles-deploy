package com.example.tama_gargoyles.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "gargoyles")
public class Gargoyle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private enum Type {
        BAD,
        GOOD,
        CHILD
    }

    @ManyToOne(optional = false)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    private Integer age = 0;

    private enum Status {
        ACTIVE,
        RETIRED
    }

    @Enumerated(EnumType.STRING)
    private Type type = Type.CHILD;

    @Enumerated(EnumType.STRING)
    private Status status =Status.ACTIVE;

    private Integer hunger = 100;
    private Integer happiness = 100;
    private Integer health = 100;
    private Integer experience = 0;
    private Integer strength = 25;
    private Integer speed = 10;
    private Integer intelligence = 10;
    private Float last_fed = 6.5F;
    private Float last_played = 19.25F;
    private Float left_at = 21.75F;

    public Gargoyle() {}

//    public Gargoyle(User user, Type type, Status status) {
//        this.user = user;
//        this.type = Type.CHILD;
//        this.status = Status.ACTIVE;
//    }
}

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

    public void addAttribute(String gargoyles, Iterable<Gargoyle> all) {
    }

    private enum Type {
        BAD,
        GOOD,
        CHILD
    }

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    private Integer age;

    private enum Status {
        ACTIVE,
        RETIRED
    }

    @Enumerated(EnumType.STRING)
    private Type type;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Integer hunger;
    private Integer happiness;
    private Integer health;
    private Integer experience;
    private Integer strength;
    private Integer speed;
    private Integer intelligence;
    private Float last_fed;
    private Float last_played;
    private Float left_at;

    public Gargoyle() {}

    public Gargoyle(User user, Type type, Status status) {
        this.user = user;
        this.type = Type.CHILD;
        this.status = Status.ACTIVE;
//        this.hunger = 100;
//        this.happiness = 100;
//        this.health = 100;
//        this.experience = 100;
//        this.strength = 100;
//        this.speed = 100;
//        this.intelligence = 100;
//        this.last_fed = 6.5F;
//        this.last_played = 19.25F;
//        this.left_at = 21.75F;
    }
}

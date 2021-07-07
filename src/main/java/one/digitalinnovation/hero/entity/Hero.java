package one.digitalinnovation.hero.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import one.digitalinnovation.hero.enums.HeroType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data //getter, setter, etc
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Hero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String superPower;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HeroType type;


}

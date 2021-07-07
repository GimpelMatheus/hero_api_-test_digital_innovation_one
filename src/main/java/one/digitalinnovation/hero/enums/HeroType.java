package one.digitalinnovation.hero.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HeroType {

    MARVEL("MARVEL"),
    DC("DC"),
    ASGARD("ASRGARD");

    private final String description;
}

package one.digitalinnovation.hero.builder;

import lombok.Builder;
import one.digitalinnovation.hero.dto.HeroDTO;
import one.digitalinnovation.hero.enums.HeroType;

@Builder
public class HeroDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "FLASH";

    @Builder.Default
    private String superPower = "VELOCITY";

    @Builder.Default
    private HeroType type = HeroType.DC;

    public HeroDTO toHeroDTO() {
        return new HeroDTO(id,
                name,
                superPower,
                type);
    }

}

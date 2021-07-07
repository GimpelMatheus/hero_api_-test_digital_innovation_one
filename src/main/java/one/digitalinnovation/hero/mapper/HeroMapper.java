package one.digitalinnovation.hero.mapper;

import one.digitalinnovation.hero.dto.HeroDTO;
import one.digitalinnovation.hero.entity.Hero;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface HeroMapper {

    HeroMapper INSTANCE = Mappers.getMapper(HeroMapper.class);

    Hero toModel(HeroDTO heroDTO);

    HeroDTO toDTO(Hero hero);
}

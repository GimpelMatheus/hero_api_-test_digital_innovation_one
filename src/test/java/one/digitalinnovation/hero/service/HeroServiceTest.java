package one.digitalinnovation.hero.service;

import one.digitalinnovation.hero.builder.HeroDTOBuilder;
import one.digitalinnovation.hero.dto.HeroDTO;
import one.digitalinnovation.hero.entity.Hero;
import one.digitalinnovation.hero.exception.HeroAlreadyRegisteredException;
import one.digitalinnovation.hero.exception.HeroNotFoundException;
import one.digitalinnovation.hero.mapper.HeroMapper;
import one.digitalinnovation.hero.repository.HeroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)//fala pra classe que vai add a extensão
//do mockito
public class HeroServiceTest {

    private static final long INVALID_HERO_ID = 1L;

    @Mock
    private HeroRepository heroRepository;

    private HeroMapper heroMapper = HeroMapper.INSTANCE;

    @InjectMocks
    private HeroService heroService;

    @Test
    void whenHeroInformedThenItShouldBeCreated() throws HeroAlreadyRegisteredException {
        // given
        //OBJETO JÁ PRONTO PARA CASOS DE TESTE
        HeroDTO expectedHeroDTO = HeroDTOBuilder.builder().build().toHeroDTO();
        Hero expectedSavedHero = heroMapper.toModel(expectedHeroDTO);

        // when
        when(heroRepository.findByName(expectedHeroDTO.getName())).thenReturn(Optional.empty());
        when(heroRepository.save(expectedSavedHero)).thenReturn(expectedSavedHero);

        //then
        HeroDTO createdHeroDTO = heroService.createHero(expectedHeroDTO);

        //Hamcrest Validation
        assertThat(createdHeroDTO.getId(), is(equalTo(expectedHeroDTO.getId())));
        assertThat(createdHeroDTO.getName(), is(equalTo(expectedHeroDTO.getName())));

    }

    @Test
    void whenAlreadyRegisteredHeroInformedThenAnExceptionShouldBeThrown() {
        // given
        HeroDTO expectedHeroDTO = HeroDTOBuilder.builder().build().toHeroDTO();
        Hero duplicatedHero = heroMapper.toModel(expectedHeroDTO);

        // when
        when(heroRepository.findByName(expectedHeroDTO.getName())).thenReturn(Optional.of(duplicatedHero));

        // then
        assertThrows(HeroAlreadyRegisteredException.class, () -> heroService.createHero(expectedHeroDTO));
    }

    @Test
    void whenValidHeroNameIsGivenThenReturnAHero() throws HeroNotFoundException {
        // given
        HeroDTO expectedFoundHeroDTO = HeroDTOBuilder.builder().build().toHeroDTO();
        Hero expectedFoundHero = heroMapper.toModel(expectedFoundHeroDTO);

        // when
        when(heroRepository.findByName(expectedFoundHero.getName())).thenReturn(Optional.of(expectedFoundHero));

        // then
        HeroDTO foundHeroDTO = heroService.findByName(expectedFoundHeroDTO.getName());

        assertThat(foundHeroDTO, is(equalTo(expectedFoundHeroDTO)));
    }

    @Test
    void whenNotRegisteredHeroNameIsGivenThenThrowAnException() {
        // given
        HeroDTO expectedFoundHeroDTO = HeroDTOBuilder.builder().build().toHeroDTO();

        // when
        //acontece pois retornamos um empty, sendo assim, indica que não achou nada
        when(heroRepository.findByName(expectedFoundHeroDTO.getName())).thenReturn(Optional.empty());

        // then
        assertThrows(HeroNotFoundException.class, () -> heroService.findByName(expectedFoundHeroDTO.getName()));
    }

    @Test
    void whenListHeroIsCalledThenReturnAListOfHeros() {
        // given
        HeroDTO expectedFoundHeroDTO = HeroDTOBuilder.builder().build().toHeroDTO();
        Hero expectedFoundHero = heroMapper.toModel(expectedFoundHeroDTO);

        //when
        when(heroRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundHero));

        //then
        List<HeroDTO> foundListHerosDTO = heroService.listAll();

        assertThat(foundListHerosDTO, is(not(empty())));
        assertThat(foundListHerosDTO.get(0), is(equalTo(expectedFoundHeroDTO)));
    }

    @Test
    void whenListHeroIsCalledThenReturnAnEmptyListOfHeros() {
        //when
        when(heroRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        //then
        List<HeroDTO> foundListHerosDTO = heroService.listAll();

        assertThat(foundListHerosDTO, is(empty()));
    }

    @Test
    void whenExclusionIsCalledWithValidIdThenAHeroShouldBeDeleted() throws HeroNotFoundException {
        // given
        HeroDTO expectedDeletedHeroDTO = HeroDTOBuilder.builder().build().toHeroDTO();
        Hero expectedDeletedHero = heroMapper.toModel(expectedDeletedHeroDTO);

        // when
        when(heroRepository.findById(expectedDeletedHeroDTO.getId())).thenReturn(Optional.of(expectedDeletedHero));
        doNothing().when(heroRepository).deleteById(expectedDeletedHeroDTO.getId());

        // then
        heroService.deleteById(expectedDeletedHeroDTO.getId());

        verify(heroRepository, times(1)).findById(expectedDeletedHeroDTO.getId());
        verify(heroRepository, times(1)).deleteById(expectedDeletedHeroDTO.getId());
    }

}

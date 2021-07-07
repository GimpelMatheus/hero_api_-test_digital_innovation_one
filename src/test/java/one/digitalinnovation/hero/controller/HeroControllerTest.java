package one.digitalinnovation.hero.controller;

import one.digitalinnovation.hero.builder.HeroDTOBuilder;
import one.digitalinnovation.hero.dto.HeroDTO;
import one.digitalinnovation.hero.exception.HeroNotFoundException;
import one.digitalinnovation.hero.service.HeroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;

import static one.digitalinnovation.hero.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class HeroControllerTest {

    private static final String HERO_API_URL_PATH = "/api/v1/hero";
    private static final long VALID_HERO_ID = 1L;
    private static final long INVALID_HERO_ID = 2l;
    private MockMvc mockMvc;

    @Mock
    private HeroService heroService;

    @InjectMocks
    private HeroController heroController;

    @BeforeEach
    void setUp() {
        //Antes de cada teste precisamos fazer a compilação do nosso mockMvc
        //vamos fazer o setup somente para heroController
        mockMvc = MockMvcBuilders.standaloneSetup(heroController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()) //obj paginável
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView()) //json
                .build();
    }

    @Test
    void whenPOSTIsCalledThenAHeroIsCreated() throws Exception {
        // given
        HeroDTO heroDTO = HeroDTOBuilder.builder().build().toHeroDTO();

        // when
        when(heroService.createHero(heroDTO)).thenReturn(heroDTO);

        // then
        mockMvc.perform(post(HERO_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(heroDTO)))//vamos fazer o parse para Json
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(heroDTO.getName())))
                .andExpect(jsonPath("$.superPower", is(heroDTO.getSuperPower())))
                .andExpect(jsonPath("$.type", is(heroDTO.getType().toString())));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        HeroDTO heroDTO = HeroDTOBuilder.builder().build().toHeroDTO();
        heroDTO.setSuperPower(null);

        // then
        mockMvc.perform(post(HERO_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(heroDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETIsCalledWithValidNameThenOkStatusIsReturned() throws Exception {
        // given
        HeroDTO heroDTO = HeroDTOBuilder.builder().build().toHeroDTO();

        //when
        when(heroService.findByName(heroDTO.getName())).thenReturn(heroDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(HERO_API_URL_PATH + "/" + heroDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(heroDTO.getName())))
                .andExpect(jsonPath("$.brand", is(heroDTO.getSuperPower())))
                .andExpect(jsonPath("$.type", is(heroDTO.getType().toString())));
    }

    @Test
    void whenGETIsCalledWithoutRegisteredNameThenNotFoundStatusIsReturned() throws Exception {
        // given
        HeroDTO heroDTO = HeroDTOBuilder.builder().build().toHeroDTO();

        //when
        when(heroService.findByName(heroDTO.getName())).thenThrow(HeroNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(HERO_API_URL_PATH + "/" + heroDTO.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETListWithHerosIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        HeroDTO heroDTO = HeroDTOBuilder.builder().build().toHeroDTO();

        //when
        when(heroService.listAll()).thenReturn(Collections.singletonList(heroDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(HERO_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(heroDTO.getName())))
                .andExpect(jsonPath("$[0].superPower", is(heroDTO.getSuperPower())))
                .andExpect(jsonPath("$[0].type", is(heroDTO.getType().toString())));
    }

    @Test
    void whenGETListWithoutHerosIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        HeroDTO heroDTO = HeroDTOBuilder.builder().build().toHeroDTO();

        //when
        when(heroService.listAll()).thenReturn(Collections.singletonList(heroDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(HERO_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        // given
        HeroDTO heroDTO = HeroDTOBuilder.builder().build().toHeroDTO();

        //when
        doNothing().when(heroService).deleteById(heroDTO.getId());

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(HERO_API_URL_PATH + "/" + heroDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDELETEIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception {
        //when
        doThrow(HeroNotFoundException.class).when(heroService).deleteById(INVALID_HERO_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(HERO_API_URL_PATH + "/" + INVALID_HERO_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

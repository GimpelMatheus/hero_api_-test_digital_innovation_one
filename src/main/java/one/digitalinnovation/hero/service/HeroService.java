package one.digitalinnovation.hero.service;

import lombok.AllArgsConstructor;
import one.digitalinnovation.hero.dto.HeroDTO;
import one.digitalinnovation.hero.entity.Hero;
import one.digitalinnovation.hero.exception.HeroAlreadyRegisteredException;
import one.digitalinnovation.hero.exception.HeroNotFoundException;
import one.digitalinnovation.hero.mapper.HeroMapper;
import one.digitalinnovation.hero.repository.HeroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//
@Service // Todo ciclo de vida vai ser gerenciado pelo spring
@AllArgsConstructor(onConstructor = @__(@Autowired))// anotação dada pelo lombok
//testes unitários
public class HeroService {

    private final HeroRepository heroRepository;
    private final HeroMapper heroMapper = HeroMapper.INSTANCE;

    public HeroDTO createHero(HeroDTO heroDTO) throws HeroAlreadyRegisteredException {
        verifyIfIsAlreadyRegistered(heroDTO.getName());
        Hero hero = heroMapper.toModel(heroDTO);
        Hero savedHero = heroRepository.save(hero);
        return heroMapper.toDTO(savedHero);
    }

    public HeroDTO findByName(String name) throws HeroNotFoundException {
        Hero foundHero = heroRepository.findByName(name)
                .orElseThrow(() -> new HeroNotFoundException(name));
        return heroMapper.toDTO(foundHero);
    }

    public List<HeroDTO> listAll() {
        return heroRepository.findAll()
                .stream()
                .map(heroMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) throws HeroNotFoundException {
        verifyIfExists(id);
        heroRepository.deleteById(id);
    }

    private void verifyIfIsAlreadyRegistered(String name) throws HeroAlreadyRegisteredException {
        Optional<Hero> optSavedBeer = heroRepository.findByName(name);
        if (optSavedBeer.isPresent()) {
            throw new HeroAlreadyRegisteredException(name);
        }
    }

    private Hero verifyIfExists(Long id) throws HeroNotFoundException {
        return heroRepository.findById(id)
                .orElseThrow(() -> new HeroNotFoundException(id));
    }

}

package one.digitalinnovation.hero.controller;

import lombok.AllArgsConstructor;
import one.digitalinnovation.hero.dto.HeroDTO;
import one.digitalinnovation.hero.exception.HeroAlreadyRegisteredException;
import one.digitalinnovation.hero.exception.HeroNotFoundException;
import one.digitalinnovation.hero.exception.HeroExceededException;
import one.digitalinnovation.hero.service.HeroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

//Aqui que rola toda a operação inicial do padrão REST
@RestController
@RequestMapping("/api/v1/beers")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class HeroController implements BeerControllerDocs {

    private final HeroService heroService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HeroDTO createBeer(@RequestBody @Valid HeroDTO heroDTO) throws HeroAlreadyRegisteredException {
        return heroService.createBeer(heroDTO);
    }

    @GetMapping("/{name}")
    public HeroDTO findByName(@PathVariable String name) throws HeroNotFoundException {
        return heroService.findByName(name);
    }

    @GetMapping
    public List<HeroDTO> listBeers() {
        return heroService.listAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws HeroNotFoundException {
        heroService.deleteById(id);
    }

    @PatchMapping("/{id}/increment")
    public HeroDTO increment(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO) throws HeroNotFoundException, HeroExceededException {
        return heroService.increment(id, quantityDTO.getQuantity());
    }
}

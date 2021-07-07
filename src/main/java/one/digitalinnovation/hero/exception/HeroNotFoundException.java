package one.digitalinnovation.hero.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class HeroNotFoundException extends Exception {

    public HeroNotFoundException(String beerName) {
        super(String.format("Hero with name %s not found in the system.", beerName));
    }

    public HeroNotFoundException(Long id) {
        super(String.format("Hero with id %s not found in the system.", id));
    }
}

package one.digitalinnovation.hero.repository;

import one.digitalinnovation.hero.entity.Hero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeroRepository extends JpaRepository<Hero, Long> {
    //RELACIONAR COM O BANCO DE DADOS
    Optional<Hero> findByName(String name);
}

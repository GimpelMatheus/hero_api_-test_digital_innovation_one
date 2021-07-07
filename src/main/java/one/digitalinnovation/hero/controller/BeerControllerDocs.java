package one.digitalinnovation.hero.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import one.digitalinnovation.hero.dto.HeroDTO;
import one.digitalinnovation.hero.exception.HeroAlreadyRegisteredException;
import one.digitalinnovation.hero.exception.HeroNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Api("Manages beer stock")
public interface BeerControllerDocs {

    @ApiOperation(value = "Hero creation operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success beer creation"),
            @ApiResponse(code = 400, message = "Missing required fields or wrong field range value.")
    })
    HeroDTO createBeer(HeroDTO heroDTO) throws HeroAlreadyRegisteredException;

    @ApiOperation(value = "Returns beer found by a given name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success beer found in the system"),
            @ApiResponse(code = 404, message = "Hero with given name not found.")
    })
    HeroDTO findByName(@PathVariable String name) throws HeroNotFoundException;

    @ApiOperation(value = "Returns a list of all beers registered in the system")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all beers registered in the system"),
    })
    List<HeroDTO> listBeers();

    @ApiOperation(value = "Delete a beer found by a given valid Id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Success beer deleted in the system"),
            @ApiResponse(code = 404, message = "Hero with given id not found.")
    })
    void deleteById(@PathVariable Long id) throws HeroNotFoundException;
}

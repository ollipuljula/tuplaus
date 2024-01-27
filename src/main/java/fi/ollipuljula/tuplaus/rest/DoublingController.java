package fi.ollipuljula.tuplaus.rest;

import fi.ollipuljula.tuplaus.db.entity.GameTransaction;
import fi.ollipuljula.tuplaus.domain.DoublingService;
import fi.ollipuljula.tuplaus.rest.model.DoubleDto;
import fi.ollipuljula.tuplaus.rest.model.DoubleResultDto;
import fi.ollipuljula.tuplaus.rest.model.PlayDto;
import fi.ollipuljula.tuplaus.rest.model.PlayResultDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/game/doubling")
public class DoublingController {
    private final DoublingService doublingService;

    @Autowired
    public DoublingController(DoublingService doublingService) {
        this.doublingService = doublingService;
    }

    @GetMapping("")
    public List<GameTransaction> fetchGameTransactions() {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "TODO");
    }

    @GetMapping("/{id}")
    public GameTransaction fetchGameTransaction(@PathVariable Long id) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "TODO");
    }

    @Operation(summary = "Play the first round of doubling game")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "no balance or provided arguments are invalid",
                    content = @Content(schema = @Schema(implementation = PlayDto.class))),
            @ApiResponse(responseCode = "200", description = "first round played, results returned")
    })
    @PostMapping("/play")
    public PlayResultDto play(@RequestBody @Valid PlayDto dto) {
        if(!(dto.getBet() > 0) || dto.getChoice() == null || dto.getUserId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data validation errors");
        }
        GameTransaction gameTransaction = doublingService.play(dto);
        PlayResultDto result = new PlayResultDto();
        BeanUtils.copyProperties(gameTransaction, result);
        return result;
    }

    @PostMapping("/double")
    public DoubleResultDto play(@RequestBody DoubleDto dto) {
        if((dto.isPlay() && dto.getChoice() == null) || dto.getGameTransactionId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data validation errors");
        }
        GameTransaction gameTransaction = doublingService.playDouble(dto);
        DoubleResultDto result = new DoubleResultDto();
        BeanUtils.copyProperties(gameTransaction, result);
        return result;
    }
}

package fi.ollipuljula.tuplaus.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "No balance to play")
public class NoBalanceException extends RuntimeException {
}

package fi.ollipuljula.tuplaus.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Incorrect game event")
public class IncorrectGameEvent extends RuntimeException {
}

package warehouse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Archived parameter can only be true or false")
public class ArchivedNotFoundException extends RuntimeException {
}

package ru.clevertec.ecl.giftcertificates.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.clevertec.ecl.giftcertificates.exception.NoTagWithTheSameNameException;
import ru.clevertec.ecl.giftcertificates.exception.NotFoundException;

/**
 * This GiftCertificatesExceptionHandler class handles exceptions and returns appropriate error responses.
 */
@Slf4j
@ControllerAdvice
public class GiftCertificatesExceptionHandler {

    private final IncorrectData incorrectData = new IncorrectData();

    /**
     * Handles {@link NotFoundException} and returns a 404 Not Found response with an error message.
     *
     * @param exception The NotFoundException to handle.
     * @return A ResponseEntity containing an {@link IncorrectData} object and a 404 status code.
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<IncorrectData> serviceException(NotFoundException exception) {
        incorrectData.setErrorMessage(exception.getMessage());
        incorrectData.setErrorCode(HttpStatus.NOT_FOUND.toString());
        log.error(incorrectData.toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(incorrectData);
    }

    /**
     * Handles {@link NoTagWithTheSameNameException} and returns a 406 Not Acceptable response with an error message.
     *
     * @param exception The NoTagWithTheSameNameException to handle.
     * @return A ResponseEntity containing an {@link IncorrectData} object and a 406 status code.
     */
    @ExceptionHandler(NoTagWithTheSameNameException.class)
    public ResponseEntity<IncorrectData> noTagWithTheSameNameException(NoTagWithTheSameNameException exception) {
        incorrectData.setErrorMessage(exception.getMessage());
        incorrectData.setErrorCode(HttpStatus.NOT_ACCEPTABLE.toString());
        log.error(incorrectData.toString());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(incorrectData);
    }

}

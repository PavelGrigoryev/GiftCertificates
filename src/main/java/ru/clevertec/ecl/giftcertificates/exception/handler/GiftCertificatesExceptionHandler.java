package ru.clevertec.ecl.giftcertificates.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.clevertec.ecl.giftcertificates.exception.NoSuchGiftCertificateException;
import ru.clevertec.ecl.giftcertificates.exception.NoSuchTagException;

/**
 * This GiftCertificatesExceptionHandler class handles exceptions and returns appropriate error responses.
 */
@Slf4j
@ControllerAdvice
public class GiftCertificatesExceptionHandler {

    private final IncorrectData incorrectData = new IncorrectData();

    /**
     * Handles {@link NoSuchGiftCertificateException} and returns a 404 Not Found response with an error message.
     *
     * @param exception The NoSuchGiftCertificateException to handle.
     * @return A ResponseEntity containing an {@link IncorrectData} object and a 404 status code.
     */
    @ExceptionHandler(NoSuchGiftCertificateException.class)
    public ResponseEntity<IncorrectData> noSuchGiftCertificateException(NoSuchGiftCertificateException exception) {
        incorrectData.setErrorMessage(exception.getMessage());
        incorrectData.setErrorCode(HttpStatus.NOT_FOUND.toString());
        log.error(incorrectData.toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(incorrectData);
    }

    /**
     * Handles {@link NoSuchTagException} and returns a 404 Not Found response with an error message.
     *
     * @param exception The NoSuchTagException to handle.
     * @return A ResponseEntity containing an {@link IncorrectData} object and a 404 status code.
     */
    @ExceptionHandler(NoSuchTagException.class)
    public ResponseEntity<IncorrectData> noSuchTagException(NoSuchTagException exception) {
        incorrectData.setErrorMessage(exception.getMessage());
        incorrectData.setErrorCode(HttpStatus.NOT_FOUND.toString());
        log.error(incorrectData.toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(incorrectData);
    }

}

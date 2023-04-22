package ru.clevertec.ecl.giftcertificates.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.clevertec.ecl.giftcertificates.exception.CannotDeleteTagException;
import ru.clevertec.ecl.giftcertificates.exception.NoSuchGiftCertificateException;
import ru.clevertec.ecl.giftcertificates.exception.NoSuchTagException;

@Slf4j
@ControllerAdvice
public class GiftCertificatesExceptionHandler {

    private final IncorrectData incorrectData = new IncorrectData();

    @ExceptionHandler(NoSuchGiftCertificateException.class)
    public ResponseEntity<IncorrectData> noSuchGiftCertificateException(NoSuchGiftCertificateException exception) {
        incorrectData.setErrorMessage(exception.getMessage());
        incorrectData.setErrorCode(HttpStatus.NOT_FOUND.toString());
        log.error(incorrectData.toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(incorrectData);
    }

    @ExceptionHandler(NoSuchTagException.class)
    public ResponseEntity<IncorrectData> noSuchTagException(NoSuchTagException exception) {
        incorrectData.setErrorMessage(exception.getMessage());
        incorrectData.setErrorCode(HttpStatus.NOT_FOUND.toString());
        log.error(incorrectData.toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(incorrectData);
    }

    @ExceptionHandler(CannotDeleteTagException.class)
    public ResponseEntity<IncorrectData> cannotDeleteTagException(CannotDeleteTagException exception) {
        incorrectData.setErrorMessage(exception.getMessage());
        incorrectData.setErrorCode(HttpStatus.NOT_ACCEPTABLE.toString());
        log.error(incorrectData.toString());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(incorrectData);
    }

}

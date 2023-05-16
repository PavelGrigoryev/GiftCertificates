package ru.clevertec.ecl.giftcertificates.exception;

public class NoSuchUserException extends NotFoundException {

    public NoSuchUserException(String message) {
        super(message);
    }

}

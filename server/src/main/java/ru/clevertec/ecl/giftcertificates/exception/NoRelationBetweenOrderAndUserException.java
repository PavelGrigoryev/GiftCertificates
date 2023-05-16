package ru.clevertec.ecl.giftcertificates.exception;

public class NoRelationBetweenOrderAndUserException extends NotFoundException {

    public NoRelationBetweenOrderAndUserException(String message) {
        super(message);
    }

}

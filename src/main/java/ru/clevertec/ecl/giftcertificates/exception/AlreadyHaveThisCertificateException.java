package ru.clevertec.ecl.giftcertificates.exception;

public class AlreadyHaveThisCertificateException extends NotFoundException {

    public AlreadyHaveThisCertificateException(String message) {
        super(message);
    }

}

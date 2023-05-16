package ru.clevertec.ecl.giftcertificates.exception;

public class AlreadyHaveThisCertificateException extends RuntimeException {

    public AlreadyHaveThisCertificateException(String message) {
        super(message);
    }

}

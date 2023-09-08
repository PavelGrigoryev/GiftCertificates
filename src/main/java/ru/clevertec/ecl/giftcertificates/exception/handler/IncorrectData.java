package ru.clevertec.ecl.giftcertificates.exception.handler;

import lombok.Data;

@Data
public class IncorrectData {

    private String errorMessage;
    private String errorCode;

}

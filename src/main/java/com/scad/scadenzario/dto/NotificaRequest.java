package com.scad.scadenzario.dto;

import lombok.Data;

@Data
public class NotificaRequest {
    private Integer numGgPreNotifica; // obbligatorio
    private String tipoNotifica;
    private Boolean flagSms;
    private Boolean flagEmail;
    private String oggetto;
    private String corpoTesto;
}

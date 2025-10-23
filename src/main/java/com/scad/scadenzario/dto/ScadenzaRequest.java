package com.scad.scadenzario.dto;

import java.sql.Date;
import java.util.List;

import lombok.Data;

@Data
public class ScadenzaRequest {
    private Long idTs;
    private String codiceTipologia;
    private String descrizioneTipologia;
    private Long idCliente;
    private Long idUtente;
    private Boolean flagPrivato;
    private String descrizione;
    private Date dataScadenza;
    private String stato;
    private String livelloCriticita;
    private String nome;
    private String cognome;
    private List<NotificaRequest> notifiche; // lista di NotificaRequest
}

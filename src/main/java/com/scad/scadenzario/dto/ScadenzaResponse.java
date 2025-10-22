package com.scad.scadenzario.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ScadenzaResponse {
	 private Long idSz;
	    private Long idTs;
	    private String codiceTipologia;
	    private String descrizioneTipologia;
	    private Long idCliente;
	    private Boolean flagPrivato;
	    private String descrizione;
	    private Date dataScadenza;
	    private String stato;
	    private String livelloCriticita;
	    private String nome;
	    private String cognome;

	    private List<NotificaResponse> notifiche;
}

package com.scad.scadenzario.dto;

import lombok.Data;

@Data
public class TipologieDTO {
	   private Long idTs;             // bigint(20), PK
	    private String codice;         // varchar(50), non null
	    private String descrizione;    // varchar(255), non null
}

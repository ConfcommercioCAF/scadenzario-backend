package com.scad.scadenzario.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tipologie {
	   private Long idTs;             // bigint(20), PK
	    private String codice;         // varchar(50), non null
	    private String descrizione;    // varchar(255), non null
	    private Boolean flagAttivo;    // tinyint(1), default 1
	    private Date dataCreazione;    // datetime, default current_timestamp
	    private Date dataModifica;     // datetime, nullable
	    private Date dataEliminazione; // datetime, nullable
	    private Boolean flagEliminato; // tinyint(1), nullable
}

package com.scad.scadenzario.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegolaNotifica {
		private Long idRn;                  // bigint(20), PK
	    private Date dataCreazione;         // datetime
	    private Date dataModifica;          // datetime, nullable
	    private Date dataNotifica;          // date, nullable
	    private Integer numGgPreNotifica;   // int, nullable
	    private String tipoNotifica;        // varchar(20), nullable
	    private Boolean flagSms;            // tinyint(1), default 0
	    private Boolean flagEmail;          // tinyint(1), default 0
	    private Long idNt;                  // bigint(20), nullable
	    private Long idTs;                  // bigint(20), nullable
	    private Long idSz;                  // bigint(20), nullable
}

package com.scad.scadenzario.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificaTemplate {

		private Long idNt;          // bigint(20), PK
	    private String canale;      // varchar(20), nullable
	    private String oggetto;     // varchar(255), nullable
	    private String corpoTesto;  // text, nullable
	    private Date dataCreazione; // datetime
}

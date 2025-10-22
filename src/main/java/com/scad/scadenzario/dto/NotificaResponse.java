package com.scad.scadenzario.dto;

import lombok.Data;
import java.util.Date;

@Data
public class NotificaResponse {
	   private Long idRn;
	    private Integer numGgPreNotifica;
	    private Date dataNotifica;
	    private String tipoNotifica;
	    private Boolean flagSms;
	    private Boolean flagEmail;
	    private String oggetto;
	    private String corpoTesto;
}

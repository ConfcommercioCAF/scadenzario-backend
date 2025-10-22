package com.scad.scadenzario.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Scadenza {
    private Long idSz;
    private Long idUtente;
    private Long idTs;                  // nullable
    private Long idCliente;             // nullable
    private Date dataInserimento;       // default current_timestamp
    private Date dataModifica;          // nullable
    private Date dataEliminazione;      // nullable
    private Boolean flagEliminato;      // default false
    private Boolean flagPrivato;
    private String descrizione;
    private Date dataScadenza;
    private Date dataRinnovo;           // nullable
    private String stato;               // default 'ATTIVA'
    private String livelloCriticita;    // nullable
    private String nome;                // nullable
    private String cognome;             // nullable
}

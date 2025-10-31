package com.scad.scadenzario.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scad.scadenzario.dto.NotificaResponse;
import com.scad.scadenzario.dto.ScadenzaRequest;
import com.scad.scadenzario.dto.ScadenzaResponse;
import com.scad.scadenzario.model.Tipologie;
import com.scad.scadenzario.repository.ScadenzeRepository;

@Service
public class ScadenzeService {
	  private static final Logger log = LoggerFactory.getLogger(ScadenzeRepository.class);
	@Autowired
    private ScadenzeRepository scadenzaRepository;
	@Autowired
	private TipologieService tipologieService;

    /**
     * Restituisce tutte le scadenze non eliminate.
     */
    public List<ScadenzaResponse> getAllScadenze() {
        return scadenzaRepository.findAll();
    }

    /**
     * Restituisce i dettagli di una singola scadenza.
     * @param idSz ID della scadenza
     * @return ScadenzaResponse o null se non trovata
     */
    public ScadenzaResponse getScadenzaById(Long idSz) {
        return scadenzaRepository.findById(idSz);
    }
    
    /**
     * Restituisce i dettagli di una singola scadenza.
     * @param idSz ID del cliente
     * @return ScadenzaResponse o null se non trovata
     */
    public List<ScadenzaResponse> getScadenzaByIdCliente(Long idCliente) {
        return scadenzaRepository.findByIdCliente(idCliente);
    }
    
    /**
     * Inserisce una nuova scadenza con eventuali notifiche associate.
     * @param scadenza dati della scadenza da inserire
     * @return la scadenza inserita (con id generato)
     */
 
    @Transactional
    public ScadenzaResponse inserisciScadenzaConNotifiche(ScadenzaRequest request) {
        if (request.getIdUtente() == null) {
            throw new IllegalArgumentException("idUtente è obbligatorio");
        }

        log.info("Inserimento scadenza per utente {}, descrizione: {}",
                 request.getIdUtente(), request.getDescrizione());

        // Creiamo un oggetto ScadenzaResponse da passare al repository
        ScadenzaResponse scadenza = new ScadenzaResponse();
        BeanUtils.copyProperties(request, scadenza, 
        	    "codiceTipologia", "descrizioneTipologia", "idSz"); 
        	// 👉 escludi i campi che non esistono nella request, per evitare warning/reflection error

        	// Recupera dal servizio la tipologia associata all'idTs
        	tipologieService.getTipologiaById(request.getIdTs()).ifPresentOrElse(
        	    tipologia -> {
        	        scadenza.setCodiceTipologia(tipologia.getCodice());
        	        scadenza.setDescrizioneTipologia(tipologia.getDescrizione());
        	    },
        	    () -> {
        	        throw new IllegalArgumentException("Tipologia non trovata per idTs: " + request.getIdTs());
        	    }
        	);

        // Copia le notifiche dal request al response
        if (request.getNotifiche() != null && !request.getNotifiche().isEmpty()) {
            List<NotificaResponse> notificheResponse = request.getNotifiche().stream().map(nr -> {
                NotificaResponse n = new NotificaResponse();
                n.setNumGgPreNotifica(nr.getNumGgPreNotifica());
                n.setTipoNotifica(nr.getTipoNotifica());
                n.setFlagSms(nr.getFlagSms());
                n.setFlagEmail(nr.getFlagEmail());
                n.setOggetto(nr.getOggetto());
                n.setCorpoTesto(nr.getCorpoTesto());
                return n;
            }).collect(Collectors.toList());
            scadenza.setNotifiche(notificheResponse);
        }

        // ✅ Chiamata corretta al repository NON statico
        Long idSz = scadenzaRepository.insertScadenzaConNotifiche(scadenza);
        scadenza.setIdSz(idSz);

        log.info("Scadenza inserita con successo (id_sz = {})", idSz);
        return scadenza;
    }
}

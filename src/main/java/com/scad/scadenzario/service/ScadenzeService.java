package com.scad.scadenzario.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scad.scadenzario.dto.ScadenzaResponse;
import com.scad.scadenzario.repository.ScadenzeRepository;

@Service
public class ScadenzeService {

	@Autowired
    private ScadenzeRepository scadenzaRepository;

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
}

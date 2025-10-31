package com.scad.scadenzario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scad.scadenzario.dto.TipologieDTO;

import com.scad.scadenzario.repository.TipologieRepository;

@Service
public class TipologieService {

	@Autowired
    private TipologieRepository tipologieRepository;

    /**
     * Restituisce tutte le scadenze non eliminate.
     */
    public List<TipologieDTO> getAllTipologie() {
        return tipologieRepository.findAll();
    }
    

    /**
     * Restituisce tutte le scadenze non eliminate.
     */
    public Optional<TipologieDTO> getTipologiaById(Long idTs) {
        return tipologieRepository.getTipologiaById(idTs);
    }
}

package com.scad.scadenzario.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scad.scadenzario.dto.ScadenzaResponse;
import com.scad.scadenzario.dto.TipologieDTO;
import com.scad.scadenzario.service.ScadenzeService;
import com.scad.scadenzario.service.TipologieService;

@RestController
@RequestMapping("/api")
public class TipologieController {

	@Autowired
	    private TipologieService tipologieService;
	 
	 
	@GetMapping("/tipologie")
    public ResponseEntity<?>  getAllTipologie() {
		 Map<String, Object> response = new HashMap<>();

		    try {
		        List<TipologieDTO> tipologie = tipologieService.getAllTipologie();

		        if (tipologie.isEmpty()) {
		            response.put("res", true);
		            response.put("data", Collections.emptyList());
		            response.put("err", "");
		        } else {
		            response.put("res", true);
		            response.put("data", tipologie);
		            response.put("err", "");
		        }

		        return ResponseEntity.ok(response);

		    } catch (Exception e) {
		        response.put("res", false);
		        response.put("data", Collections.emptyList());
		        response.put("err", e.getMessage());
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		    }

 		
	}
}

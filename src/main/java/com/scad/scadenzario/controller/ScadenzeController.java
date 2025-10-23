package com.scad.scadenzario.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scad.scadenzario.dto.ScadenzaRequest;
import com.scad.scadenzario.dto.ScadenzaResponse;
import com.scad.scadenzario.service.ScadenzeService;

@RestController
@RequestMapping("/api")
public class ScadenzeController {

	 @Autowired
	    private ScadenzeService scadenzaService;

	 @PostMapping("/insert-scadenza")
	    public ResponseEntity<Map<String, Object>> creaScadenza(@RequestBody ScadenzaRequest scadenzaRequest) {
	        Map<String, Object> response = new HashMap<>();
	        try {
	            ScadenzaResponse scadenzaInserita = scadenzaService.inserisciScadenzaConNotifiche(scadenzaRequest);

	            response.put("res", true);
	            response.put("data", scadenzaInserita);
	            response.put("err", "");
	            return ResponseEntity.status(HttpStatus.CREATED).body(response);

	        } catch (Exception e) {
	            response.put("res", false);
	            response.put("data", null);
	            response.put("err", e.getMessage());
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	        }
	    }
	    
	 	@GetMapping("/scadenze")
	    public ResponseEntity<?>  getAllScadenze() {
	 		
	 		  Map<String, Object> response = new HashMap<>();

	 		    try {
	 		        List<ScadenzaResponse> scadenze = scadenzaService.getAllScadenze();

	 		        if (scadenze.isEmpty()) {
	 		            response.put("res", true);
	 		            response.put("data", Collections.emptyList());
	 		            response.put("err", "");
	 		        } else {
	 		            response.put("res", true);
	 		            response.put("data", scadenze);
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

	 	@GetMapping("/scadenza/{idSz}")
	 	public ResponseEntity<?> getScadenzaById(@PathVariable Long idSz) {
	 	    Map<String, Object> response = new HashMap<>();

	 	    try {
	 	        ScadenzaResponse scadenza = scadenzaService.getScadenzaById(idSz);

	 	        if (scadenza == null) {
	 	            response.put("res", true);
	 	            response.put("data", null);
	 	            response.put("err", "");
	 	            return ResponseEntity.ok(response);
	 	        } else {
	 	            response.put("res", true);
	 	            response.put("data", scadenza);
	 	            response.put("err", "");
	 	            return ResponseEntity.ok(response);
	 	        }
	 	    } catch (Exception e) {
	 	        response.put("res", false);
	 	        response.put("data", null);
	 	        response.put("err", e.getMessage());
	 	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	 	    }
	 	}
	 	
	 	@GetMapping("/scadenza-cliente/{idCliente}")
	 	public ResponseEntity<?> getScadenzaByIdCliente(@PathVariable Long idCliente) {
	 	    Map<String, Object> response = new HashMap<>();

	 	    try {
	 	    	 List<ScadenzaResponse> scadenze = scadenzaService.getScadenzaByIdCliente(idCliente);

	 	    	  if (scadenze.isEmpty()) {
	 		            response.put("res", true);
	 		            response.put("data", Collections.emptyList());
	 		            response.put("err", "");
	 		        } else {
	 		            response.put("res", true);
	 		            response.put("data", scadenze);
	 		            response.put("err", "");
	 		        }

	 		        return ResponseEntity.ok(response);

	 	    }catch (Exception e) {
 		        response.put("res", false);
 		        response.put("data", Collections.emptyList());
 		        response.put("err", e.getMessage());
 		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
 		    }
	 	}


}

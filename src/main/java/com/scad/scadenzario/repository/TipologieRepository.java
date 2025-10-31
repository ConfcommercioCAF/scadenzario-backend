package com.scad.scadenzario.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.scad.scadenzario.dto.NotificaResponse;
import com.scad.scadenzario.dto.ScadenzaResponse;
import com.scad.scadenzario.dto.TipologieDTO;

@Repository
public class TipologieRepository {
	  @Autowired
	    private JdbcTemplate jdbcTemplate;
	  
	    public List<TipologieDTO> findAll() {
	    	
	        String sql = """
	        		SELECT
	        		 scad_tipologie.id_ts,
	        		 scad_tipologie.codice,
	        		 scad_tipologie.descrizione
	        		FROM 
	        		 scad_tipologie
	        		WHERE 
	        		 scad_tipologie.flag_eliminato = 0 
	        		AND scad_tipologie.flag_attivo = 1;
	        """;
	        
	        
	        return jdbcTemplate.query(sql, (rs, rowNum) -> {
	            TipologieDTO dto = new TipologieDTO();
	            dto.setIdTs(rs.getLong("id_ts"));
	            dto.setCodice(rs.getString("codice"));
	            dto.setDescrizione(rs.getString("descrizione"));
	            return dto;
	        });
	        
	    }

		public Optional<TipologieDTO> getTipologiaById(Long idTs) {
			 String sql = """
		        		SELECT
		        		 scad_tipologie.id_ts,
		        		 scad_tipologie.codice,
		        		 scad_tipologie.descrizione
		        		FROM 
		        		 scad_tipologie
		        		WHERE 
		        		 scad_tipologie.flag_eliminato = 0 
		        		AND scad_tipologie.flag_attivo = 1 
		        		AND scad_tipologie.id_ts = ?;
		        """;
		        
		        
			 List<TipologieDTO> list = jdbcTemplate.query(sql, (rs, rowNum) -> {
				    TipologieDTO dto = new TipologieDTO();
				    dto.setIdTs(rs.getLong("id_ts"));
				    dto.setCodice(rs.getString("codice"));
				    dto.setDescrizione(rs.getString("descrizione"));
				    return dto;
				}, idTs);

				return list.stream().findFirst();
		        
		}
		
		
		
}

package com.scad.scadenzario.repository;

import com.scad.scadenzario.dto.NotificaResponse;
import com.scad.scadenzario.dto.ScadenzaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Repository
public class ScadenzeRepository {

    private static final Logger log = LoggerFactory.getLogger(ScadenzeRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ScadenzaResponse> findAll() {
        String sql = """
            SELECT 
                s.id_sz,
                s.id_ts,
                t.codice AS codice_tipologia,
                t.descrizione AS descrizione_tipologia,
                s.id_cliente,
                s.flag_privato,
                s.descrizione,
                s.data_scadenza,
                s.stato,
                s.livello_criticita,
                s.nome,
                s.cognome,
                rn.id_rn,
                rn.num_gg_pre_notifica,
                rn.data_notifica,
                rn.tipo_notifica,
                rn.flag_sms,
                rn.flag_email,
                nt.oggetto,
                nt.corpo_testo
            FROM scad_scadenze s
            LEFT JOIN scad_tipologie t ON s.id_ts = t.id_ts
            LEFT JOIN scad_regole_notifica rn ON s.id_sz = rn.id_sz
            LEFT JOIN scad_notifica_template nt ON rn.id_nt = nt.id_nt
            WHERE s.flag_eliminato = 0
            ORDER BY s.data_scadenza ASC
        """;

        try {
            return jdbcTemplate.query(con -> con.prepareStatement(sql), rs -> {
                Map<Long, ScadenzaResponse> scadenzeMap = new LinkedHashMap<>();
                while (rs.next()) {
                    Long idSz = rs.getLong("id_sz");

                    ScadenzaResponse scadenza = scadenzeMap.computeIfAbsent(idSz, k -> {
                        ScadenzaResponse s = new ScadenzaResponse();
                        try {
                            s.setIdSz(idSz);
                            s.setIdTs(rs.getObject("id_ts", Long.class));
                            s.setCodiceTipologia(rs.getString("codice_tipologia"));
                            s.setDescrizioneTipologia(rs.getString("descrizione_tipologia"));
                            s.setIdCliente(rs.getObject("id_cliente", Long.class));
                            s.setFlagPrivato(rs.getBoolean("flag_privato"));
                            s.setDescrizione(rs.getString("descrizione"));
                            s.setDataScadenza(rs.getTimestamp("data_scadenza"));
                            s.setStato(rs.getString("stato"));
                            s.setLivelloCriticita(rs.getString("livello_criticita"));
                            s.setNome(rs.getString("nome"));
                            s.setCognome(rs.getString("cognome"));
                        } catch (SQLException e) {
                            log.error("Errore durante il mapping di ScadenzaResponse: {}", e.getMessage(), e);
                        }
                        s.setNotifiche(new ArrayList<>());
                        return s;
                    });

                    Long idRn = rs.getObject("id_rn", Long.class);
                    if (idRn != null) {
                        NotificaResponse n = new NotificaResponse();
                        try {
                            n.setIdRn(idRn);
                            n.setNumGgPreNotifica(rs.getObject("num_gg_pre_notifica", Integer.class));
                            n.setDataNotifica(rs.getTimestamp("data_notifica"));
                            n.setTipoNotifica(rs.getString("tipo_notifica"));
                            n.setFlagSms(rs.getBoolean("flag_sms"));
                            n.setFlagEmail(rs.getBoolean("flag_email"));
                            n.setOggetto(rs.getString("oggetto"));
                            n.setCorpoTesto(rs.getString("corpo_testo"));
                        } catch (SQLException e) {
                            log.error("Errore durante il mapping di NotificaResponse: {}", e.getMessage(), e);
                        }
                        scadenza.getNotifiche().add(n);
                    }
                }
                return new ArrayList<>(scadenzeMap.values());
            });
        } catch (DataAccessException e) {
            log.error("Errore durante l’esecuzione di findAll(): {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }


    public ScadenzaResponse findById(Long idSz) {
        String sql = """
            SELECT 
                s.id_sz,
                s.id_ts,
                t.codice AS codice_tipologia,
                t.descrizione AS descrizione_tipologia,
                s.id_cliente,
                s.flag_privato,
                s.descrizione,
                s.data_scadenza,
                s.stato,
                s.livello_criticita,
                s.nome,
                s.cognome,
                rn.id_rn,
                rn.num_gg_pre_notifica,
                rn.data_notifica,
                rn.tipo_notifica,
                rn.flag_sms,
                rn.flag_email,
                nt.oggetto,
                nt.corpo_testo
            FROM scad_scadenze s
            LEFT JOIN scad_tipologie t ON s.id_ts = t.id_ts
            LEFT JOIN scad_regole_notifica rn ON s.id_sz = rn.id_sz
            LEFT JOIN scad_notifica_template nt ON rn.id_nt = nt.id_nt
            WHERE s.flag_eliminato = 0 AND s.id_sz = ?
        """;

        try {
            return jdbcTemplate.query(con -> {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setLong(1, idSz);
                return ps;
            }, rs -> {
                Map<Long, ScadenzaResponse> scadenzeMap = new LinkedHashMap<>();
                while (rs.next()) {
                    Long id = rs.getLong("id_sz");

                    ScadenzaResponse scadenza = scadenzeMap.computeIfAbsent(id, k -> {
                        ScadenzaResponse s = new ScadenzaResponse();
                        try {
                            s.setIdSz(id);
                            s.setIdTs(rs.getObject("id_ts", Long.class));
                            s.setCodiceTipologia(rs.getString("codice_tipologia"));
                            s.setDescrizioneTipologia(rs.getString("descrizione_tipologia"));
                            s.setIdCliente(rs.getObject("id_cliente", Long.class));
                            s.setFlagPrivato(rs.getBoolean("flag_privato"));
                            s.setDescrizione(rs.getString("descrizione"));
                            s.setDataScadenza(rs.getTimestamp("data_scadenza"));
                            s.setStato(rs.getString("stato"));
                            s.setLivelloCriticita(rs.getString("livello_criticita"));
                            s.setNome(rs.getString("nome"));
                            s.setCognome(rs.getString("cognome"));
                        } catch (SQLException e) {
                            log.error("Errore durante il mapping di ScadenzaResponse: {}", e.getMessage(), e);
                        }
                        s.setNotifiche(new ArrayList<>());
                        return s;
                    });

                    Long idRn = rs.getObject("id_rn", Long.class);
                    if (idRn != null) {
                        NotificaResponse n = new NotificaResponse();
                        try {
                            n.setIdRn(idRn);
                            n.setNumGgPreNotifica(rs.getObject("num_gg_pre_notifica", Integer.class));
                            n.setDataNotifica(rs.getTimestamp("data_notifica"));
                            n.setTipoNotifica(rs.getString("tipo_notifica"));
                            n.setFlagSms(rs.getBoolean("flag_sms"));
                            n.setFlagEmail(rs.getBoolean("flag_email"));
                            n.setOggetto(rs.getString("oggetto"));
                            n.setCorpoTesto(rs.getString("corpo_testo"));
                        } catch (SQLException e) {
                            log.error("Errore durante il mapping di NotificaResponse: {}", e.getMessage(), e);
                        }
                        scadenza.getNotifiche().add(n);
                    }
                }
                return scadenzeMap.values().stream().findFirst().orElse(null);
            });
        } catch (DataAccessException e) {
            log.error("Errore durante findById({}): {}", idSz, e.getMessage(), e);
            return null;
        }
    }

}

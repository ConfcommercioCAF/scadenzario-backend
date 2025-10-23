package com.scad.scadenzario.repository;

import com.scad.scadenzario.dto.NotificaResponse;
import com.scad.scadenzario.dto.ScadenzaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
                s.id_utente,
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
                            s.setIdUtente(rs.getLong("id_utente")); // NUOVO CAMPO
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
                s.id_utente,
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
                            s.setIdUtente(rs.getLong("id_utente")); // NUOVO CAMPO
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

    public List<ScadenzaResponse> findByIdCliente(Long idCliente) {
        String sql = """
            SELECT 
                s.id_sz,
                s.id_ts,
                s.id_utente,
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
            WHERE s.flag_eliminato = 0 AND s.id_cliente = ?
        """;

        try {
            return jdbcTemplate.query(con -> {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setLong(1, idCliente);
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
                            s.setIdUtente(rs.getLong("id_utente")); // NUOVO CAMPO
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
            log.error("Errore durante findByIdCliente({}): {}", idCliente, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Transactional
    public Long insertScadenzaConNotifiche(ScadenzaResponse scadenza) {
        String insertScadenzaSql = """
            INSERT INTO scad_scadenze (
                id_ts,
                id_utente,
                id_cliente,
                flag_privato,
                descrizione,
                data_scadenza,
                stato,
                livello_criticita,
                nome,
                cognome,
                flag_eliminato
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0)
        """;

        KeyHolder scadenzaKeyHolder = new GeneratedKeyHolder();

        try {
            // 1️⃣ Inserimento scadenza principale
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(insertScadenzaSql, Statement.RETURN_GENERATED_KEYS);
                ps.setObject(1, scadenza.getIdTs(), Types.BIGINT);
                ps.setObject(2, scadenza.getIdUtente(), Types.BIGINT);
                ps.setObject(3, scadenza.getIdCliente(), Types.BIGINT);
                ps.setObject(4, scadenza.getFlagPrivato(), Types.BOOLEAN);
                ps.setObject(5, scadenza.getDescrizione(), Types.VARCHAR);
                ps.setObject(6, scadenza.getDataScadenza(), Types.TIMESTAMP);
                ps.setObject(7, scadenza.getStato(), Types.VARCHAR);
                ps.setObject(8, scadenza.getLivelloCriticita(), Types.VARCHAR);
                ps.setObject(9, scadenza.getNome(), Types.VARCHAR);
                ps.setObject(10, scadenza.getCognome(), Types.VARCHAR);
                return ps;
            }, scadenzaKeyHolder);

            Long idSz = Objects.requireNonNull(scadenzaKeyHolder.getKey()).longValue();
            log.info("✅ Inserita nuova scadenza con id_sz = {}", idSz);

            // 2️⃣ Inserimento notifiche collegate
            if (scadenza.getNotifiche() != null && !scadenza.getNotifiche().isEmpty()) {
                for (NotificaResponse notifica : scadenza.getNotifiche()) {

                    // 2a. Inserisci il template della notifica
                    String insertTemplateSql = """
                        INSERT INTO scad_notifica_template (oggetto, corpo_testo)
                        VALUES (?, ?)
                    """;

                    KeyHolder templateKeyHolder = new GeneratedKeyHolder();
                    jdbcTemplate.update(con -> {
                        PreparedStatement ps = con.prepareStatement(insertTemplateSql, Statement.RETURN_GENERATED_KEYS);
                        ps.setObject(1, notifica.getOggetto(), Types.VARCHAR);
                        ps.setObject(2, notifica.getCorpoTesto(), Types.VARCHAR);
                        return ps;
                    }, templateKeyHolder);

                    Long idNt = Objects.requireNonNull(templateKeyHolder.getKey()).longValue();

                    // 2b. Calcolo dataNotifica (solo DATE)
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(scadenza.getDataScadenza());
                    cal.add(Calendar.DAY_OF_MONTH, -notifica.getNumGgPreNotifica());
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    Date dataNotifica = cal.getTime();

                    // 2c. Inserisci la regola di notifica
                    String insertRegolaSql = """
                        INSERT INTO scad_regole_notifica (
                            id_sz,
                            id_nt,
                            num_gg_pre_notifica,
                            data_notifica,
                            tipo_notifica,
                            flag_sms,
                            flag_email
                        ) VALUES (?, ?, ?, ?, ?, ?, ?)
                    """;

                    jdbcTemplate.update(con -> {
                        PreparedStatement ps = con.prepareStatement(insertRegolaSql);
                        ps.setObject(1, idSz, Types.BIGINT);
                        ps.setObject(2, idNt, Types.BIGINT);
                        ps.setObject(3, notifica.getNumGgPreNotifica(), Types.INTEGER);
                        ps.setObject(4, dataNotifica, Types.DATE); // usa DATE
                        ps.setObject(5, notifica.getTipoNotifica(), Types.VARCHAR);
                        ps.setObject(6, notifica.getFlagSms(), Types.BOOLEAN);
                        ps.setObject(7, notifica.getFlagEmail(), Types.BOOLEAN);
                        return ps;
                    });

                    log.info("   ↳ Inserita notifica per scadenza {} con id_nt = {}", idSz, idNt);
                }
            }

            return idSz;

        } catch (DataAccessException e) {
            log.error("❌ Errore durante insertScadenzaConNotifiche: {}", e.getMessage(), e);
            throw e; // rollback automatico grazie a @Transactional
        }
    }



}

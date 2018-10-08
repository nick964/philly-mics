package org.nick.mics.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.nick.mics.domain.Mic;
import org.nick.mics.repository.MicRepository;
import org.nick.mics.service.MicService;
import org.nick.mics.web.rest.errors.BadRequestAlertException;
import org.nick.mics.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Mic.
 */
@RestController
@RequestMapping("/api")
public class MicResource {

    private final Logger log = LoggerFactory.getLogger(MicResource.class);

    private static final String ENTITY_NAME = "mic";

    private final MicRepository micRepository;
    private final MicService micService;

    public MicResource(MicRepository micRepository, MicService micService) {
        this.micRepository = micRepository;
        this.micService = micService;
    }

    /**
     * POST  /mics : Create a new mic.
     *
     * @param mic the mic to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mic, or with status 400 (Bad Request) if the mic has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mics")
    @Timed
    public ResponseEntity<Mic> createMic(@Valid @RequestBody Mic mic) throws URISyntaxException {
        log.debug("REST request to save Mic : {}", mic);
        if (mic.getId() != null) {
            throw new BadRequestAlertException("A new mic cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Mic result = micRepository.save(mic);
        return ResponseEntity.created(new URI("/api/mics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mics : Updates an existing mic.
     *
     * @param mic the mic to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mic,
     * or with status 400 (Bad Request) if the mic is not valid,
     * or with status 500 (Internal Server Error) if the mic couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mics")
    @Timed
    public ResponseEntity<Mic> updateMic(@Valid @RequestBody Mic mic) throws URISyntaxException {
        log.debug("REST request to update Mic : {}", mic);
        if (mic.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Mic result = micRepository.save(mic);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mic.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mics : get all the mics.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of mics in body
     */
    @GetMapping("/mics")
    @Timed
    public List<Mic> getAllWeeklyMicsOfType(@RequestParam("micType") String micType, @RequestParam("date") String weekOf) {
        log.debug("REST request to get all weekly mics with micType: " +micType + " and Date of " + weekOf);
        return micService.getWeeklyMics(micType, weekOf);
    }

    /**
     * GET  /weeklymics : get all the weekly mics based on Type and
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of mics in body
     */
    @GetMapping("/mics")
    @Timed
    public List<Mic> getAllMics(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Mics");
        return micRepository.findAllWithEagerRelationships();
    }

    /**
     * GET  /mics/:id : get the "id" mic.
     *
     * @param id the id of the mic to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mic, or with status 404 (Not Found)
     */
    @GetMapping("/mics/{id}")
    @Timed
    public ResponseEntity<Mic> getMic(@PathVariable Long id) {
        log.debug("REST request to get Mic : {}", id);
        Optional<Mic> mic = micRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(mic);
    }

    /**
     * DELETE  /mics/:id : delete the "id" mic.
     *
     * @param id the id of the mic to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mics/{id}")
    @Timed
    public ResponseEntity<Void> deleteMic(@PathVariable Long id) {
        log.debug("REST request to delete Mic : {}", id);

        micRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

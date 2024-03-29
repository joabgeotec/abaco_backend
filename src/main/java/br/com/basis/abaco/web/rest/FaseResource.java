package br.com.basis.abaco.web.rest;

import br.com.basis.abaco.domain.Fase;
import br.com.basis.abaco.repository.FaseRepository;
import br.com.basis.abaco.repository.search.FaseSearchRepository;
import br.com.basis.abaco.service.exception.RelatorioException;
import br.com.basis.abaco.service.relatorio.RelatorioFaseColunas;
import br.com.basis.abaco.utils.AbacoUtil;
import br.com.basis.abaco.utils.PageUtils;
import br.com.basis.abaco.web.rest.util.HeaderUtil;
import br.com.basis.abaco.web.rest.util.PaginationUtil;
import br.com.basis.dynamicexports.service.DynamicExportsService;
import br.com.basis.dynamicexports.util.DynamicExporter;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing Fase.
 */
@RestController
@RequestMapping("/api")
public class FaseResource {

    private final Logger log = LoggerFactory.getLogger(FaseResource.class);

    private static final String ENTITY_NAME = "fase";

    private final FaseRepository faseRepository;

    private final FaseSearchRepository faseSearchRepository;

    private final DynamicExportsService dynamicExportsService;

    public FaseResource(FaseRepository faseRepository, FaseSearchRepository faseSearchRepository, DynamicExportsService dynamicExportsService) {
        this.faseRepository = faseRepository;
        this.faseSearchRepository = faseSearchRepository;
        this.dynamicExportsService = dynamicExportsService;
    }

    /**
     * POST  /fases : Create a new fase.
     *
     * @param fase the fase to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fase, or with status 400 (Bad Request) if the fase has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/fases")
    @Timed
    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE_GESTOR"})
    public ResponseEntity<Fase> createFase(@RequestBody Fase fase) throws URISyntaxException {
        log.debug("REST request to save Fase : {}", fase);
        if (fase.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new fase cannot already have an ID")).body(null);
        }
        Fase result = faseRepository.save(fase);
        faseSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/fases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fases : Updates an existing fase.
     *
     * @param fase the fase to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fase,
     * or with status 400 (Bad Request) if the fase is not valid,
     * or with status 500 (Internal Server Error) if the fase couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/fases")
    @Timed
    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE_GESTOR"})
    public ResponseEntity<Fase> updateFase(@RequestBody Fase fase) throws URISyntaxException {
        log.debug("REST request to update Fase : {}", fase);
        if (fase.getId() == null) {
            return createFase(fase);
        }
        Fase result = faseRepository.save(fase);
        faseSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fase.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fases : get all the fases.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of fases in body
     */
    @GetMapping("/fases")
    @Timed
    public List<Fase> getAllFases() {
        log.debug("REST request to get all Fases");
        return faseRepository.findAll();
    }

    /**
     * GET  /fases/:id : get the "id" fase.
     *
     * @param id the id of the fase to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fase, or with status 404 (Not Found)
     */
    @GetMapping("/fases/{id}")
    @Timed
    public ResponseEntity<Fase> getFase(@PathVariable Long id) {
        log.debug("REST request to get Fase : {}", id);
        Fase fase = faseRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fase));
    }

    /**
     * DELETE  /fases/:id : delete the "id" fase.
     *
     * @param id the id of the fase to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/fases/{id}")
    @Timed
    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE_GESTOR"})
    public ResponseEntity<Void> deleteFase(@PathVariable Long id) {
        log.debug("REST request to delete Fase : {}", id);
        faseRepository.delete(id);
        faseSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/fases?query=:query : search for the fase corresponding
     * to the query.
     *
     * @param query the query of the fase search
     * @return the result of the search
     * @throws URISyntaxException
     */
    @GetMapping("/_search/fases")
    @Timed
    public ResponseEntity<List<Fase>> searchFases(@RequestParam(defaultValue = "*") String query, @RequestParam String order, @RequestParam(name="page") int pageNumber, @RequestParam int size, @RequestParam(defaultValue="id") String sort) throws URISyntaxException {
        log.debug("REST request to search Fases for query {}", query);
        Sort.Direction sortOrder = PageUtils.getSortDirection(order);
        Pageable newPageable = new PageRequest(pageNumber, size, sortOrder, sort);

        Page<Fase> page = faseSearchRepository.search(queryStringQuery(query), newPageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/fases");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping(value = "/tipoFase/exportacao/{tipoRelatorio}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Timed

    public ResponseEntity<InputStreamResource> gerarRelatorioExportacao(@PathVariable String tipoRelatorio, @RequestParam(defaultValue = "*") String query) throws RelatorioException {
        ByteArrayOutputStream byteArrayOutputStream;
        try {
            new NativeSearchQueryBuilder().withQuery(multiMatchQuery(query)).build();
            Page<Fase> result =  faseSearchRepository.search(queryStringQuery(query), dynamicExportsService.obterPageableMaximoExportacao());
            byteArrayOutputStream = dynamicExportsService.export(new RelatorioFaseColunas(), result, tipoRelatorio, Optional.empty(), Optional.ofNullable(AbacoUtil.REPORT_LOGO_PATH), Optional.ofNullable(AbacoUtil.getReportFooter()));
        } catch (DRException | ClassNotFoundException | JRException | NoClassDefFoundError e) {
            log.error(e.getMessage(), e);
            throw new RelatorioException(e);
        }
        return DynamicExporter.output(byteArrayOutputStream,
            "relatorio." + tipoRelatorio);
    }


}

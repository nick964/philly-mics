package org.nick.mics.web.rest;

import org.nick.mics.OpenmicsApp;

import org.nick.mics.domain.Host;
import org.nick.mics.repository.HostRepository;
import org.nick.mics.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static org.nick.mics.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the HostResource REST controller.
 *
 * @see HostResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OpenmicsApp.class)
public class HostResourceIntTest {

    private static final Long DEFAULT_HOST_ID = 1L;
    private static final Long UPDATED_HOST_ID = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHostMockMvc;

    private Host host;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HostResource hostResource = new HostResource(hostRepository);
        this.restHostMockMvc = MockMvcBuilders.standaloneSetup(hostResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Host createEntity(EntityManager em) {
        Host host = new Host()
            .hostId(DEFAULT_HOST_ID)
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER);
        return host;
    }

    @Before
    public void initTest() {
        host = createEntity(em);
    }

    @Test
    @Transactional
    public void createHost() throws Exception {
        int databaseSizeBeforeCreate = hostRepository.findAll().size();

        // Create the Host
        restHostMockMvc.perform(post("/api/hosts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(host)))
            .andExpect(status().isCreated());

        // Validate the Host in the database
        List<Host> hostList = hostRepository.findAll();
        assertThat(hostList).hasSize(databaseSizeBeforeCreate + 1);
        Host testHost = hostList.get(hostList.size() - 1);
        assertThat(testHost.getHostId()).isEqualTo(DEFAULT_HOST_ID);
        assertThat(testHost.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHost.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testHost.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void createHostWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hostRepository.findAll().size();

        // Create the Host with an existing ID
        host.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHostMockMvc.perform(post("/api/hosts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(host)))
            .andExpect(status().isBadRequest());

        // Validate the Host in the database
        List<Host> hostList = hostRepository.findAll();
        assertThat(hostList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = hostRepository.findAll().size();
        // set the field null
        host.setName(null);

        // Create the Host, which fails.

        restHostMockMvc.perform(post("/api/hosts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(host)))
            .andExpect(status().isBadRequest());

        List<Host> hostList = hostRepository.findAll();
        assertThat(hostList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = hostRepository.findAll().size();
        // set the field null
        host.setEmail(null);

        // Create the Host, which fails.

        restHostMockMvc.perform(post("/api/hosts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(host)))
            .andExpect(status().isBadRequest());

        List<Host> hostList = hostRepository.findAll();
        assertThat(hostList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHosts() throws Exception {
        // Initialize the database
        hostRepository.saveAndFlush(host);

        // Get all the hostList
        restHostMockMvc.perform(get("/api/hosts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(host.getId().intValue())))
            .andExpect(jsonPath("$.[*].hostId").value(hasItem(DEFAULT_HOST_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())));
    }
    
    @Test
    @Transactional
    public void getHost() throws Exception {
        // Initialize the database
        hostRepository.saveAndFlush(host);

        // Get the host
        restHostMockMvc.perform(get("/api/hosts/{id}", host.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(host.getId().intValue()))
            .andExpect(jsonPath("$.hostId").value(DEFAULT_HOST_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHost() throws Exception {
        // Get the host
        restHostMockMvc.perform(get("/api/hosts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHost() throws Exception {
        // Initialize the database
        hostRepository.saveAndFlush(host);

        int databaseSizeBeforeUpdate = hostRepository.findAll().size();

        // Update the host
        Host updatedHost = hostRepository.findById(host.getId()).get();
        // Disconnect from session so that the updates on updatedHost are not directly saved in db
        em.detach(updatedHost);
        updatedHost
            .hostId(UPDATED_HOST_ID)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restHostMockMvc.perform(put("/api/hosts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHost)))
            .andExpect(status().isOk());

        // Validate the Host in the database
        List<Host> hostList = hostRepository.findAll();
        assertThat(hostList).hasSize(databaseSizeBeforeUpdate);
        Host testHost = hostList.get(hostList.size() - 1);
        assertThat(testHost.getHostId()).isEqualTo(UPDATED_HOST_ID);
        assertThat(testHost.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHost.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testHost.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingHost() throws Exception {
        int databaseSizeBeforeUpdate = hostRepository.findAll().size();

        // Create the Host

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHostMockMvc.perform(put("/api/hosts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(host)))
            .andExpect(status().isBadRequest());

        // Validate the Host in the database
        List<Host> hostList = hostRepository.findAll();
        assertThat(hostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHost() throws Exception {
        // Initialize the database
        hostRepository.saveAndFlush(host);

        int databaseSizeBeforeDelete = hostRepository.findAll().size();

        // Get the host
        restHostMockMvc.perform(delete("/api/hosts/{id}", host.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Host> hostList = hostRepository.findAll();
        assertThat(hostList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Host.class);
        Host host1 = new Host();
        host1.setId(1L);
        Host host2 = new Host();
        host2.setId(host1.getId());
        assertThat(host1).isEqualTo(host2);
        host2.setId(2L);
        assertThat(host1).isNotEqualTo(host2);
        host1.setId(null);
        assertThat(host1).isNotEqualTo(host2);
    }
}

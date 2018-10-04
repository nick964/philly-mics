package org.nick.mics.web.rest;

import org.nick.mics.OpenmicsApp;

import org.nick.mics.domain.Mic;
import org.nick.mics.repository.MicRepository;
import org.nick.mics.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


import static org.nick.mics.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.nick.mics.domain.enumeration.MicType;
/**
 * Test class for the MicResource REST controller.
 *
 * @see MicResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OpenmicsApp.class)
public class MicResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_MIC_TIME = 1L;
    private static final Long UPDATED_MIC_TIME = 2L;

    private static final Long DEFAULT_DURATION = 1L;
    private static final Long UPDATED_DURATION = 2L;

    private static final Boolean DEFAULT_IS_RECURRING = false;
    private static final Boolean UPDATED_IS_RECURRING = true;

    private static final String DEFAULT_RECURRENCE_PATTERN = "AAAAAAAAAA";
    private static final String UPDATED_RECURRENCE_PATTERN = "BBBBBBBBBB";

    private static final String DEFAULT_STREET_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_STREET_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final MicType DEFAULT_MIC_TYPE = MicType.COMEDY;
    private static final MicType UPDATED_MIC_TYPE = MicType.MUSIC;

    @Autowired
    private MicRepository micRepository;

    @Mock
    private MicRepository micRepositoryMock;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMicMockMvc;

    private Mic mic;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MicResource micResource = new MicResource(micRepository);
        this.restMicMockMvc = MockMvcBuilders.standaloneSetup(micResource)
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
    public static Mic createEntity(EntityManager em) {
        Mic mic = new Mic()
            .name(DEFAULT_NAME)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .micTime(DEFAULT_MIC_TIME)
            .duration(DEFAULT_DURATION)
            .isRecurring(DEFAULT_IS_RECURRING)
            .recurrencePattern(DEFAULT_RECURRENCE_PATTERN)
            .streetAddress(DEFAULT_STREET_ADDRESS)
            .postalCode(DEFAULT_POSTAL_CODE)
            .city(DEFAULT_CITY)
            .notes(DEFAULT_NOTES)
            .micType(DEFAULT_MIC_TYPE);
        return mic;
    }

    @Before
    public void initTest() {
        mic = createEntity(em);
    }

    @Test
    @Transactional
    public void createMic() throws Exception {
        int databaseSizeBeforeCreate = micRepository.findAll().size();

        // Create the Mic
        restMicMockMvc.perform(post("/api/mics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mic)))
            .andExpect(status().isCreated());

        // Validate the Mic in the database
        List<Mic> micList = micRepository.findAll();
        assertThat(micList).hasSize(databaseSizeBeforeCreate + 1);
        Mic testMic = micList.get(micList.size() - 1);
        assertThat(testMic.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMic.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testMic.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testMic.getMicTime()).isEqualTo(DEFAULT_MIC_TIME);
        assertThat(testMic.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testMic.isIsRecurring()).isEqualTo(DEFAULT_IS_RECURRING);
        assertThat(testMic.getRecurrencePattern()).isEqualTo(DEFAULT_RECURRENCE_PATTERN);
        assertThat(testMic.getStreetAddress()).isEqualTo(DEFAULT_STREET_ADDRESS);
        assertThat(testMic.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testMic.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testMic.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testMic.getMicType()).isEqualTo(DEFAULT_MIC_TYPE);
    }

    @Test
    @Transactional
    public void createMicWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = micRepository.findAll().size();

        // Create the Mic with an existing ID
        mic.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMicMockMvc.perform(post("/api/mics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mic)))
            .andExpect(status().isBadRequest());

        // Validate the Mic in the database
        List<Mic> micList = micRepository.findAll();
        assertThat(micList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = micRepository.findAll().size();
        // set the field null
        mic.setName(null);

        // Create the Mic, which fails.

        restMicMockMvc.perform(post("/api/mics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mic)))
            .andExpect(status().isBadRequest());

        List<Mic> micList = micRepository.findAll();
        assertThat(micList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = micRepository.findAll().size();
        // set the field null
        mic.setStartDate(null);

        // Create the Mic, which fails.

        restMicMockMvc.perform(post("/api/mics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mic)))
            .andExpect(status().isBadRequest());

        List<Mic> micList = micRepository.findAll();
        assertThat(micList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = micRepository.findAll().size();
        // set the field null
        mic.setEndDate(null);

        // Create the Mic, which fails.

        restMicMockMvc.perform(post("/api/mics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mic)))
            .andExpect(status().isBadRequest());

        List<Mic> micList = micRepository.findAll();
        assertThat(micList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsRecurringIsRequired() throws Exception {
        int databaseSizeBeforeTest = micRepository.findAll().size();
        // set the field null
        mic.setIsRecurring(null);

        // Create the Mic, which fails.

        restMicMockMvc.perform(post("/api/mics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mic)))
            .andExpect(status().isBadRequest());

        List<Mic> micList = micRepository.findAll();
        assertThat(micList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStreetAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = micRepository.findAll().size();
        // set the field null
        mic.setStreetAddress(null);

        // Create the Mic, which fails.

        restMicMockMvc.perform(post("/api/mics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mic)))
            .andExpect(status().isBadRequest());

        List<Mic> micList = micRepository.findAll();
        assertThat(micList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMics() throws Exception {
        // Initialize the database
        micRepository.saveAndFlush(mic);

        // Get all the micList
        restMicMockMvc.perform(get("/api/mics?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mic.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].micTime").value(hasItem(DEFAULT_MIC_TIME.intValue())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].isRecurring").value(hasItem(DEFAULT_IS_RECURRING.booleanValue())))
            .andExpect(jsonPath("$.[*].recurrencePattern").value(hasItem(DEFAULT_RECURRENCE_PATTERN.toString())))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].micType").value(hasItem(DEFAULT_MIC_TYPE.toString())));
    }
    
    public void getAllMicsWithEagerRelationshipsIsEnabled() throws Exception {
        MicResource micResource = new MicResource(micRepositoryMock);
        when(micRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restMicMockMvc = MockMvcBuilders.standaloneSetup(micResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restMicMockMvc.perform(get("/api/mics?eagerload=true"))
        .andExpect(status().isOk());

        verify(micRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllMicsWithEagerRelationshipsIsNotEnabled() throws Exception {
        MicResource micResource = new MicResource(micRepositoryMock);
            when(micRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restMicMockMvc = MockMvcBuilders.standaloneSetup(micResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restMicMockMvc.perform(get("/api/mics?eagerload=true"))
        .andExpect(status().isOk());

            verify(micRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getMic() throws Exception {
        // Initialize the database
        micRepository.saveAndFlush(mic);

        // Get the mic
        restMicMockMvc.perform(get("/api/mics/{id}", mic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(mic.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.micTime").value(DEFAULT_MIC_TIME.intValue()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.intValue()))
            .andExpect(jsonPath("$.isRecurring").value(DEFAULT_IS_RECURRING.booleanValue()))
            .andExpect(jsonPath("$.recurrencePattern").value(DEFAULT_RECURRENCE_PATTERN.toString()))
            .andExpect(jsonPath("$.streetAddress").value(DEFAULT_STREET_ADDRESS.toString()))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()))
            .andExpect(jsonPath("$.micType").value(DEFAULT_MIC_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMic() throws Exception {
        // Get the mic
        restMicMockMvc.perform(get("/api/mics/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMic() throws Exception {
        // Initialize the database
        micRepository.saveAndFlush(mic);

        int databaseSizeBeforeUpdate = micRepository.findAll().size();

        // Update the mic
        Mic updatedMic = micRepository.findById(mic.getId()).get();
        // Disconnect from session so that the updates on updatedMic are not directly saved in db
        em.detach(updatedMic);
        updatedMic
            .name(UPDATED_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .micTime(UPDATED_MIC_TIME)
            .duration(UPDATED_DURATION)
            .isRecurring(UPDATED_IS_RECURRING)
            .recurrencePattern(UPDATED_RECURRENCE_PATTERN)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .city(UPDATED_CITY)
            .notes(UPDATED_NOTES)
            .micType(UPDATED_MIC_TYPE);

        restMicMockMvc.perform(put("/api/mics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMic)))
            .andExpect(status().isOk());

        // Validate the Mic in the database
        List<Mic> micList = micRepository.findAll();
        assertThat(micList).hasSize(databaseSizeBeforeUpdate);
        Mic testMic = micList.get(micList.size() - 1);
        assertThat(testMic.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMic.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testMic.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testMic.getMicTime()).isEqualTo(UPDATED_MIC_TIME);
        assertThat(testMic.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testMic.isIsRecurring()).isEqualTo(UPDATED_IS_RECURRING);
        assertThat(testMic.getRecurrencePattern()).isEqualTo(UPDATED_RECURRENCE_PATTERN);
        assertThat(testMic.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testMic.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testMic.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testMic.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testMic.getMicType()).isEqualTo(UPDATED_MIC_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingMic() throws Exception {
        int databaseSizeBeforeUpdate = micRepository.findAll().size();

        // Create the Mic

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMicMockMvc.perform(put("/api/mics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mic)))
            .andExpect(status().isBadRequest());

        // Validate the Mic in the database
        List<Mic> micList = micRepository.findAll();
        assertThat(micList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMic() throws Exception {
        // Initialize the database
        micRepository.saveAndFlush(mic);

        int databaseSizeBeforeDelete = micRepository.findAll().size();

        // Get the mic
        restMicMockMvc.perform(delete("/api/mics/{id}", mic.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Mic> micList = micRepository.findAll();
        assertThat(micList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mic.class);
        Mic mic1 = new Mic();
        mic1.setId(1L);
        Mic mic2 = new Mic();
        mic2.setId(mic1.getId());
        assertThat(mic1).isEqualTo(mic2);
        mic2.setId(2L);
        assertThat(mic1).isNotEqualTo(mic2);
        mic1.setId(null);
        assertThat(mic1).isNotEqualTo(mic2);
    }
}

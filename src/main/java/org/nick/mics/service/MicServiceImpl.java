package org.nick.mics.service;

import com.google.ical.values.RRule;
import org.apache.commons.lang3.time.DateUtils;
import org.nick.mics.domain.Mic;
import org.nick.mics.repository.MicRepository;
import org.nick.mics.web.rest.MicResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MicServiceImpl implements MicService {

    private final Logger log = LoggerFactory.getLogger(MicResource.class);

    @Autowired
    MicRepository micRepository;

    public List<Mic> getWeeklyMics(String mictype, String weekOf) {

        Date week = null;
        try {
            week = DateUtils.parseDate(weekOf);

            List<Mic> weeklyMics = micRepository.findMicsbyTypeAndDate(mictype, week);

            for(Mic mic : weeklyMics) {
                RRule rule = new RRule(mic.getRecurrencePattern());


            }



        } catch(Exception e) {
            log.error("Error parsign date");
        }


        return null;
    }



}

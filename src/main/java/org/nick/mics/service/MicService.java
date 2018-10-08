package org.nick.mics.service;

import org.nick.mics.domain.Mic;

import java.util.List;

public interface MicService {

    List<Mic> getWeeklyMics(String mictype, String weekOf);

}

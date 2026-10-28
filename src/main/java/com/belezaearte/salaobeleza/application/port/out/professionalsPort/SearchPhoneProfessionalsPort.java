package com.belezaearte.salaobeleza.application.port.out.professionalsPort;

import com.belezaearte.salaobeleza.adapter.out.persistence.Professionals;

import java.util.List;

public interface SearchPhoneProfessionalsPort {
    List<Professionals> searchPhone (String phone);
}

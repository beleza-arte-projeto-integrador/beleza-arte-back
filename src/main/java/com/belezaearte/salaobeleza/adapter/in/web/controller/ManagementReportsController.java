package com.belezaearte.salaobeleza.adapter.in.web.controller;

import com.belezaearte.salaobeleza.application.domain.dto.ManagementReportDto;
import com.belezaearte.salaobeleza.application.port.in.ManagementReports.SearchManagementReportUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

// Relatorios gerenciais do salao.
// Responsavel: Matheus
@RestController
public class ManagementReportsController {

    @Autowired
    private SearchManagementReportUseCase searchManagementReportUseCase;

    // Ex: /searchManagementReport?startDate=2026-11-01&endDate=2026-11-30
    //     /searchManagementReport?startDate=2026-11-01&endDate=2026-11-30&idProfessional=1
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchManagementReport",
            produces = {"application/json"}
    )
    public ResponseEntity<ManagementReportDto> searchManagementReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Integer idProfessional) {
        return ResponseEntity.ok(searchManagementReportUseCase.searchManagementReport(startDate, endDate, idProfessional));
    }
}

package com.belezaearte.salaobeleza.adapter.in.web.controller;

import com.belezaearte.salaobeleza.application.domain.dto.PaymentsDto;
import com.belezaearte.salaobeleza.application.domain.dto.PaymentsListDto;
import com.belezaearte.salaobeleza.application.domain.dto.PaymentsRevenueDto;
import com.belezaearte.salaobeleza.application.domain.dto.PaymentsSummaryDto;
import com.belezaearte.salaobeleza.application.port.in.Payments.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
public class PaymentsController {

    @Autowired
    private PayPaymentsUseCase payPaymentsUseCase;

    @RequestMapping(
            method = RequestMethod.PUT,
            value = "/payPayments",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    public PaymentsDto payPayments(@RequestBody PaymentsDto paymentsDto) {
        return payPaymentsUseCase.payPayments(paymentsDto);
    }

    @Autowired
    private ListPaymentsUseCase listPaymentsUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/listPayments",
            produces = {"application/json"}
    )
    public ResponseEntity<List<PaymentsListDto>> listPayments(@RequestParam(required = false) String status) {
        return ResponseEntity.ok(listPaymentsUseCase.listPayments(status));
    }

    @Autowired
    private SearchSummaryPaymentsUseCase searchSummaryPaymentsUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchSummaryPayments",
            produces = {"application/json"}
    )
    public ResponseEntity<PaymentsSummaryDto> searchSummary(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        return ResponseEntity.ok(searchSummaryPaymentsUseCase.searchSummary(month));
    }

    @Autowired
    private SearchRevenuePaymentsUseCase searchRevenuePaymentsUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchRevenuePayments",
            produces = {"application/json"}
    )
    public ResponseEntity<PaymentsRevenueDto> searchRevenue(
            @RequestParam String period,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(searchRevenuePaymentsUseCase.searchRevenue(period, date));
    }

    @Autowired
    private SearchAppointmentPaymentsUseCase searchAppointmentPaymentsUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchAppointmentPayments",
            produces = {"application/json"}
    )
    public ResponseEntity<List<PaymentsDto>> searchAppointment(@RequestParam Integer idAppointment) {
        return ResponseEntity.ok(searchAppointmentPaymentsUseCase.searchAppointment(idAppointment));
    }

    @Autowired
    private SearchStatusPaymentsUseCase searchStatusPaymentsUseCase;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/searchStatusPayments",
            produces = {"application/json"}
    )
    public ResponseEntity<List<PaymentsDto>> searchStatus(@RequestParam String status) {
        return ResponseEntity.ok(searchStatusPaymentsUseCase.searchStatus(status));
    }
}

package com.prgmrs.voucher.controller;

import com.prgmrs.voucher.dto.VoucherListResponse;
import com.prgmrs.voucher.dto.VoucherRequest;
import com.prgmrs.voucher.dto.VoucherResponse;
import com.prgmrs.voucher.exception.WrongRangeFormatException;
import com.prgmrs.voucher.model.Voucher;
import com.prgmrs.voucher.service.VoucherService;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class VoucherController {

    private final VoucherService voucherService;

    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    public VoucherResponse createVoucher(VoucherRequest voucherRequest) throws WrongRangeFormatException {
        return voucherService.createVoucher(voucherRequest);
    }

    public VoucherListResponse findAll() {
        return voucherService.findAll();
    }

    public Voucher findVoucherById(UUID uuid) {
        return voucherService.findVoucherById(uuid);
    }

    public VoucherListResponse findByUsername(String name) {
        return voucherService.findByUsername();
    }
}

package com.prgmrs.voucher.service;

import com.prgmrs.voucher.dto.request.VoucherRequest;
import com.prgmrs.voucher.dto.response.VoucherListResponse;
import com.prgmrs.voucher.dto.response.VoucherResponse;
import com.prgmrs.voucher.enums.VoucherSelectionType;
import com.prgmrs.voucher.exception.NoSuchVoucherTypeException;
import com.prgmrs.voucher.exception.WrongRangeFormatException;
import com.prgmrs.voucher.model.Voucher;
import com.prgmrs.voucher.model.strategy.FixedAmountDiscountStrategy;
import com.prgmrs.voucher.model.strategy.PercentDiscountStrategy;
import com.prgmrs.voucher.model.validator.VoucherValidator;
import com.prgmrs.voucher.model.vo.Amount;
import com.prgmrs.voucher.model.vo.DiscountValue;
import com.prgmrs.voucher.model.vo.Percent;
import com.prgmrs.voucher.repository.VoucherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class VoucherService {
    private static final Logger logger = LoggerFactory.getLogger(VoucherService.class);
    private final VoucherRepository voucherRepository;
    private final VoucherValidator voucherValidator;

    public VoucherService(VoucherRepository voucherRepository, VoucherValidator voucherValidator) {
        this.voucherRepository = voucherRepository;
        this.voucherValidator = voucherValidator;
    }

    public VoucherResponse createVoucher(VoucherRequest voucherRequest) {
        String token = voucherRequest.token();
        Optional<Long> convertedValue = voucherValidator.stringToLongConverter(token);
        VoucherSelectionType voucherSelectionType = voucherRequest.voucherSelectionType();
        UUID uuid = UUID.randomUUID();
        Voucher voucher;

        if (convertedValue.isEmpty()) {
            throw new WrongRangeFormatException("incorrect token format");
        }

        DiscountValue discountValue = new DiscountValue(convertedValue.get());

        voucherValidator.isAmountValid(voucherSelectionType, discountValue);

        switch (voucherSelectionType) {
            case FIXED_AMOUNT_VOUCHER -> {
                Amount amount = new Amount(discountValue.value());
                FixedAmountDiscountStrategy fixedAmountDiscountStrategy = new FixedAmountDiscountStrategy(amount);
                voucher = new Voucher(uuid, fixedAmountDiscountStrategy);
            }
            case PERCENT_DISCOUNT_VOUCHER -> {
                Percent percent = new Percent(discountValue.value());
                PercentDiscountStrategy percentDiscountStrategy = new PercentDiscountStrategy(percent);
                voucher = new Voucher(uuid, percentDiscountStrategy);
            }
            default -> {
                logger.error("unexpected error occurred: unexpected voucher type");
                throw new NoSuchVoucherTypeException("unexpected voucher type");
            }
        }

        voucherRepository.save(voucher);

        return new VoucherResponse(voucher);
    }

    public VoucherListResponse findAll() {
        return new VoucherListResponse(voucherRepository.findAll());
    }

    public VoucherListResponse getAssignedVoucherListByUsername(String username) {
        return new VoucherListResponse(voucherRepository.getAssignedVoucherListByUsername(username));
    }

    public VoucherListResponse getNotAssignedVoucher() {
        return new VoucherListResponse(voucherRepository.getNotAssignedVoucherList());
    }

    public VoucherListResponse getAssignedVoucherList() {
        return new VoucherListResponse(voucherRepository.getAssignedVoucherList());

    }
}

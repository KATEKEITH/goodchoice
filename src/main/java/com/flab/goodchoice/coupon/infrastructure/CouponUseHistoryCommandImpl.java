package com.flab.goodchoice.coupon.infrastructure;

import com.flab.goodchoice.coupon.application.CouponUseHistoryCommand;
import com.flab.goodchoice.coupon.domain.CouponUseHistory;
import com.flab.goodchoice.coupon.infrastructure.entity.CouponEntity;
import com.flab.goodchoice.coupon.infrastructure.entity.CouponUseHistoryEntity;
import com.flab.goodchoice.coupon.infrastructure.entity.MemberEntity;
import com.flab.goodchoice.coupon.infrastructure.repositories.CouponUseHistoryRepository;
import org.springframework.stereotype.Component;

@Component
public class CouponUseHistoryCommandImpl implements CouponUseHistoryCommand {

    private final CouponUseHistoryRepository couponUseHistoryRepository;

    public CouponUseHistoryCommandImpl(CouponUseHistoryRepository couponUseHistoryRepository) {
        this.couponUseHistoryRepository = couponUseHistoryRepository;
    }

    @Override
    public CouponUseHistory save(CouponUseHistory couponUseHistory) {
        CouponUseHistoryEntity couponUseHistoryEntity = CouponUseHistoryEntity.builder()
                .member(MemberEntity.of(couponUseHistory.getMember()))
                .couponEntity(CouponEntity.of(couponUseHistory.getCoupon()))
                .price(couponUseHistory.getPrice())
                .discountPrice(couponUseHistory.getDiscountPrice())
                .useState(couponUseHistory.getUseState())
                .build();

        return couponUseHistoryRepository.save(couponUseHistoryEntity).toCouponUseHistory();
    }
}

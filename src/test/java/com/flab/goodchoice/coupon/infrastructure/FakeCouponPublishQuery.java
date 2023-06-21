package com.flab.goodchoice.coupon.infrastructure;

import com.flab.goodchoice.coupon.application.CouponPublishQuery;
import com.flab.goodchoice.coupon.domain.CouponPublish;
import com.flab.goodchoice.coupon.exception.CouponError;
import com.flab.goodchoice.coupon.exception.CouponException;
import com.flab.goodchoice.coupon.infrastructure.entity.CouponPublishEntity;
import com.flab.goodchoice.coupon.infrastructure.repositories.CouponPublishRepository;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

public class FakeCouponPublishQuery implements CouponPublishQuery {

    private final CouponPublishRepository couponPublishRepository;

    public FakeCouponPublishQuery(CouponPublishRepository couponPublishRepository) {
        this.couponPublishRepository = couponPublishRepository;
    }

    @Override
    public List<CouponPublish> getCouponIssue(Long memberId) {
        return couponPublishRepository.findCouponHistoryFetchByMemberId(memberId).stream()
                .map(CouponPublishEntity::toCouponPublish)
                .collect(toList());
    }

    @Override
    public CouponPublish getCouponPublishInfo(UUID couponPublishToken, Long memberId) {
        CouponPublishEntity couponPublishEntity = couponPublishRepository.findByCouponPublishTokenAndMemberEntityId(couponPublishToken, memberId).orElseThrow(() -> new CouponException(CouponError.NOT_FOUND_COUPON));
        return couponPublishEntity.toCouponPublish();
    }
}

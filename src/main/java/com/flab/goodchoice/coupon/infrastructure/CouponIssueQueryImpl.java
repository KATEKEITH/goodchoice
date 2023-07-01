package com.flab.goodchoice.coupon.infrastructure;

import com.flab.goodchoice.coupon.application.CouponIssueQuery;
import com.flab.goodchoice.coupon.domain.CouponIssue;
import com.flab.goodchoice.coupon.exception.CouponError;
import com.flab.goodchoice.coupon.exception.CouponException;
import com.flab.goodchoice.coupon.infrastructure.entity.CouponIssueEntity;
import com.flab.goodchoice.coupon.infrastructure.repositories.CouponIssueRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Transactional(readOnly = true)
@Component
public class CouponIssueQueryImpl implements CouponIssueQuery {

    private final CouponIssueRepository couponPublishRepository;

    public CouponIssueQueryImpl(CouponIssueRepository couponPublishRepository) {
        this.couponPublishRepository = couponPublishRepository;
    }

    @Override
    public List<CouponIssue> getCouponIssues(Long memberId) {
        return couponPublishRepository.findCouponHistoryFetchByMemberId(memberId).stream()
                .map(CouponIssueEntity::toCouponIssue)
                .collect(toList());
    }

    @Override
    public CouponIssue getCouponIssue(UUID couponIssueToken, Long memberId) {
        CouponIssueEntity couponPublishEntity = couponPublishRepository.findByCouponIssueTokenAndMemberEntityId(couponIssueToken, memberId)
                .orElseThrow(() -> new CouponException(CouponError.NOT_FOUND_COUPON_PUBLISH));
        return couponPublishEntity.toCouponIssue();
    }
}

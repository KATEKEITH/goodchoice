package com.flab.goodchoice.coupon.application;

import com.flab.goodchoice.coupon.infrastructure.*;
import com.flab.goodchoice.coupon.infrastructure.entity.CouponEntity;
import com.flab.goodchoice.coupon.domain.CouponType;
import com.flab.goodchoice.coupon.domain.State;
import com.flab.goodchoice.coupon.infrastructure.repositories.CouponRepository;
import com.flab.goodchoice.coupon.infrastructure.repositories.InMemoryCouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class CouponCommandServiceTest {

    private CouponCommandService couponCommandService;
    private CouponQuery couponQuery;
    private CouponCommand couponCommand;
    private CouponRepository couponRepository;

    CouponEntity coupon;

    @BeforeEach
    void setUp() {
        couponRepository = new InMemoryCouponRepository();
        couponQuery = new FakeCouponQuery(couponRepository);
        couponCommand = new FakeCouponCommand(couponRepository);

        couponCommandService = new CouponCommandService(couponQuery, couponCommand);

        coupon = new CouponEntity(UUID.randomUUID(), "10%할인", 100, CouponType.DISCOUNT, 10, State.ACTIVITY);
        couponRepository.save(coupon);
    }

    @DisplayName("쿠폰 생성")
    @Test
    void createCoupon() {
        final String couponName = "10%할인";
        final int stock = 100;

        couponCommandService.create(couponName, stock, CouponType.DISCOUNT, 10);

        CouponEntity coupon = couponRepository.findById(1L).get();

        assertAll(
                () -> assertThat(coupon.getCouponName()).isEqualTo(couponName),
                () -> assertThat(coupon.getStock()).isEqualTo(stock)
        );
    }

    @DisplayName("쿠폰명 미입력시 에러")
    @Test
    void notInputCouponName() {
        final String couponName = "";
        final int stock = 100;

        assertThatThrownBy(() -> couponCommandService.create(couponName, stock, CouponType.DISCOUNT, 10))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("쿠폰 갯수가 음수이면 에러")
    @Test
    void negativeStock() {
        final String couponName = "10%할인";
        final int stock = -1;

        assertThatThrownBy(() -> couponCommandService.create(couponName, stock, CouponType.DISCOUNT, 10))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("쿠폰 정보 수정")
    @Test
    void modifyCoupon() {
        couponCommandService.modifyCoupon(coupon.getCouponToken(), "15%할인", 200);

        CouponEntity findCouponEntity = couponRepository.findByCouponToken(coupon.getCouponToken()).orElseThrow();

        assertAll(
                () -> assertThat(findCouponEntity.getCouponName()).isEqualTo("15%할인"),
                () -> assertThat(findCouponEntity.getStock()).isEqualTo(200)
        );
    }

    @DisplayName("쿠폰 정보 수정시 쿠폰명 미입력 에러")
    @Test
    void modifyNotInputCouponName() {
        final String couponName = "";
        final int stock = 200;

        assertThatThrownBy(() -> couponCommandService.modifyCoupon(coupon.getCouponToken(), couponName, stock))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("쿠폰 정보 수정시 쿠폰 갯수가 음수이면 에러")
    @Test
    void modifyNegativeStock() {
        final String couponName = "15%할인";
        final int stock = -1;

        assertThatThrownBy(() -> couponCommandService.modifyCoupon(coupon.getCouponToken(), couponName, stock))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("쿠폰 삭제")
    @Test
    void deleteCoupon() {
        couponCommandService.deleteCoupon(coupon.getCouponToken());

        CouponEntity findCouponEntity = couponRepository.findByCouponToken(coupon.getCouponToken()).orElseThrow();

        assertThat(findCouponEntity.getState()).isEqualTo(State.INACTIVITY);
    }
}

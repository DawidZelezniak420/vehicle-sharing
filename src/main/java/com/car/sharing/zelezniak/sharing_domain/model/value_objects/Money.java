package com.car.sharing.zelezniak.sharing_domain.model.value_objects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor(force = true)
public class Money {

    private static final BigDecimal ZERO = BigDecimal.ZERO;
    private final BigDecimal money;

    public Money(BigDecimal money) {
        validate(money);
        this.money = format(money);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money1 = (Money) o;
        return Objects.equals(money, money1.money);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(money);
    }

    @Override
    public String toString() {
        return "Money{" +
                "money=" + money +
                '}';
    }

    private void validate(BigDecimal money) {
        if (money == null || isLowerThanZero(money)) {
            throwException();
        }
    }

    private boolean isLowerThanZero(BigDecimal money) {
        return money.compareTo(ZERO) < 0;
    }

    private void throwException() {
        throw new IllegalArgumentException(
                "Money value in incorrect");
    }

    private BigDecimal format(BigDecimal money) {
        return money.setScale(
                2, RoundingMode.HALF_UP);
    }
}

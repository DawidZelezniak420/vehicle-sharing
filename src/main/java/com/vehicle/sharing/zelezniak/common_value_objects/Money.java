package com.vehicle.sharing.zelezniak.common_value_objects.address;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.math.*;

@Embeddable
@Getter
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@ToString
public class Money {

    private static final BigDecimal ZERO = BigDecimal.ZERO;
    private final BigDecimal value;

    public Money(BigDecimal money) {
        validate(money);
        this.value = format(money);
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
                "Money value is incorrect");
    }

    private BigDecimal format(BigDecimal money) {
        return money.setScale(
                2, RoundingMode.HALF_UP);
    }
}

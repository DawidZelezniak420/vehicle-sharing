package com.car.sharing.zelezniak;


import com.car.sharing.zelezniak.util.validation.DataValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = CarSharingApplication.class)
class DataValidatorTest {

    @Autowired
    private DataValidator dataValidator;

    @Test
    void shouldThrowException() {
        String given = null;
        assertThrows(IllegalArgumentException.class, () ->
                dataValidator.throwExceptionIfObjectIsNull(
                        given,"Should not be a null."));
    }

    @Test
    void shouldNotThrowException() {
        String given = "string";
        assertDoesNotThrow(() ->
                dataValidator.throwExceptionIfObjectIsNull(
                        given,"Should not be a null."));
    }
}

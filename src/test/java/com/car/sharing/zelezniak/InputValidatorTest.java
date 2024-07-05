package com.car.sharing.zelezniak;


import com.car.sharing.zelezniak.util.validation.InputValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CarSharingApplication.class)
class InputValidatorTest {

    @Autowired
    private InputValidator inputValidator;

    @Test
    void shouldThrowException() {
        String given = null;
        assertThrows(IllegalArgumentException.class, () ->
                inputValidator.throwExceptionIfObjectIsNull(
                        given,"Should not be a null."));
    }

    @Test
    void shouldNotThrowException() {
        String given = "string";
        assertDoesNotThrow(() ->
                inputValidator.throwExceptionIfObjectIsNull(
                        given,"Should not be a null."));
    }
}

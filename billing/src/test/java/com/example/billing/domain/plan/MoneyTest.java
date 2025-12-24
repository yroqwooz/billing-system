package com.example.billing.domain.plan;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Money value object")
class MoneyTest {

    private static final Currency USD = Currency.getInstance("USD");
    private static final Currency EUR = Currency.getInstance("EUR");

    @Nested
    @DisplayName("Creation")
    class Creation {

        @Test
        @DisplayName("should create valid money")
        void shouldCreateValidMoney() {
            // Given & When
            Money money = new Money(new BigDecimal("10.99"), USD);

            // Then
            assertThat(money.getAmount()).isEqualByComparingTo("10.99");
            assertThat(money.getCurrency()).isEqualTo(USD);
        }

        @Test
        @DisplayName("should not allow negative amount")
        void shouldNotAllowNegativeAmount() {
            // Given
            BigDecimal negative = new BigDecimal("-10.00");

            // When & Then
            assertThrows(IllegalArgumentException.class,
                    () -> new Money(negative, USD),
                    "Amount of money cannot be negative"
            );
        }

        @Test
        @DisplayName("should not allow null amount")
        void shouldNotAllowNullAmount() {
            assertThrows(NullPointerException.class,
                    () -> new Money(null, USD)
            );
        }

        @Test
        @DisplayName("should not allow null currency")
        void shouldNotAllowNullCurrency() {
            assertThrows(NullPointerException.class,
                    () -> new Money(new BigDecimal("10"), null)
            );
        }

        @Test
        @DisplayName("should not allow more than 2 decimal places")
        void shouldNotAllowMoreThan2DecimalPlaces() {
            // Given
            BigDecimal threeDecimals = new BigDecimal("10.999");

            // When & Then
            assertThrows(IllegalArgumentException.class,
                    () -> new Money(threeDecimals, USD)
            );
        }

        @Test
        @DisplayName("should not exceed maximum amount")
        void shouldNotExceedMaximumAmount() {
            // Given
            BigDecimal tooLarge = new BigDecimal("9999999999.99");

            // When & Then
            assertThrows(IllegalArgumentException.class,
                    () -> new Money(tooLarge, USD)
            );
        }
    }

    @Nested
    @DisplayName("Arithmetic operations")
    class ArithmeticOperations {

        @Test
        @DisplayName("should add money with same currency")
        void shouldAddMoneyWithSameCurrency() {
            // Given
            Money money1 = new Money(new BigDecimal("10.50"), USD);
            Money money2 = new Money(new BigDecimal("5.25"), USD);

            // When
            Money result = money1.add(money2);

            // Then
            assertThat(result.getAmount()).isEqualByComparingTo("15.75");
            assertThat(result.getCurrency()).isEqualTo(USD);
        }

        @Test
        @DisplayName("should not add money with different currencies")
        void shouldNotAddMoneyWithDifferentCurrencies() {
            // Given
            Money usd = new Money(new BigDecimal("10"), USD);
            Money eur = new Money(new BigDecimal("10"), EUR);

            // When & Then
            assertThrows(IllegalArgumentException.class,
                    () -> usd.add(eur),
                    "Currencies must match"
            );
        }

        @Test
        @DisplayName("should subtract money")
        void shouldSubtractMoney() {
            // Given
            Money money1 = new Money(new BigDecimal("10.50"), USD);
            Money money2 = new Money(new BigDecimal("5.25"), USD);

            // When
            Money result = money1.subtract(money2);

            // Then
            assertThat(result.getAmount()).isEqualByComparingTo("5.25");
        }

        @Test
        @DisplayName("should not allow negative result after subtraction")
        void shouldNotAllowNegativeResultAfterSubtraction() {
            // Given
            Money smaller = new Money(new BigDecimal("5.00"), USD);
            Money larger = new Money(new BigDecimal("10.00"), USD);

            // When & Then
            assertThrows(IllegalArgumentException.class,
                    () -> smaller.subtract(larger)
            );
        }

        @Test
        @DisplayName("should multiply by integer")
        void shouldMultiplyByInteger() {
            // Given
            Money money = new Money(new BigDecimal("10.50"), USD);

            // When
            Money result = money.multiply(3);

            // Then
            assertThat(result.getAmount()).isEqualByComparingTo("31.50");
        }

        @Test
        @DisplayName("should multiply by decimal with rounding")
        void shouldMultiplyByDecimalWithRounding() {
            // Given
            Money money = new Money(new BigDecimal("10.00"), USD);

            // When
            Money result = money.multiply(0.333);

            // Then
            assertThat(result.getAmount()).isEqualByComparingTo("3.33");
        }
    }

    @Nested
    @DisplayName("Comparison operations")
    class ComparisonOperations {

        @Test
        @DisplayName("should correctly compare greater than")
        void shouldCorrectlyCompareGreaterThan() {
            // Given
            Money larger = new Money(new BigDecimal("10.00"), USD);
            Money smaller = new Money(new BigDecimal("5.00"), USD);

            // When & Then
            assertThat(larger.isGreaterThan(smaller)).isTrue();
            assertThat(smaller.isGreaterThan(larger)).isFalse();
        }

        @Test
        @DisplayName("should correctly compare equal amounts")
        void shouldCorrectlyCompareEqualAmounts() {
            // Given
            Money money1 = new Money(new BigDecimal("10.00"), USD);
            Money money2 = new Money(new BigDecimal("10.00"), USD);

            // When & Then
            assertThat(money1.isEqualTo(money2)).isTrue();
            assertThat(money1.isGreaterThan(money2)).isFalse();
            assertThat(money1.isLesserThan(money2)).isFalse();
        }
    }

    @Nested
    @DisplayName("Equality")
    class Equality {

        @Test
        @DisplayName("should be equal with same amount and currency")
        void shouldBeEqualWithSameAmountAndCurrency() {
            // Given
            Money money1 = new Money(new BigDecimal("10.50"), USD);
            Money money2 = new Money(new BigDecimal("10.50"), USD);

            // When & Then
            assertThat(money1).isEqualTo(money2);
            assertThat(money1.hashCode()).isEqualTo(money2.hashCode());
        }

        @Test
        @DisplayName("should not be equal with different currencies")
        void shouldNotBeEqualWithDifferentCurrencies() {
            // Given
            Money usd = new Money(new BigDecimal("10.00"), USD);
            Money eur = new Money(new BigDecimal("10.00"), EUR);

            // When & Then
            assertThat(usd).isNotEqualTo(eur);
        }
    }
}
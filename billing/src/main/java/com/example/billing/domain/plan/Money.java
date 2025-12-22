package com.example.billing.domain.plan;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

public class Money {
    private final BigDecimal amount;
    private final Currency currency;
    private static final BigDecimal MAX_AMOUNT = new BigDecimal("999999999.99");

    public Money(BigDecimal amount, Currency currency) {
        this.amount = Objects.requireNonNull(amount, "Money value cannot be null");
        this.currency = Objects.requireNonNull(currency, "Money currency cannot be null");

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount of money cannot be negative");
        }

        if (amount.compareTo(MAX_AMOUNT) > 0) {
            throw new IllegalArgumentException("Amount exceeds maximum allowed value");
        }

        if (amount.scale() > 2) {
            throw new IllegalArgumentException("Amount scale cannot be more than 2 digits");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;
        Money money = (Money) o;
        return amount.equals(money.amount) && currency.equals(money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return amount + " " + currency.getCurrencyCode();
    }

    private void checkCurrencyMatch(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currencies must match");
        }
    }

    public Money add(Money other) {
        checkCurrencyMatch(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        checkCurrencyMatch(other);
        final BigDecimal newAmount = this.amount.subtract(other.amount);
        if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Resulting amount cannot be negative");
        }
        return new Money(newAmount, this.currency);
    }

    public Money multiply(BigDecimal factor) {
        if (factor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Multiplier must be non-negative");
        }
        BigDecimal newAmount = this.amount.multiply(factor).setScale(2, RoundingMode.HALF_UP);
        return new Money(newAmount, this.currency);
    }

    public Money multiply(double factor) {
        return multiply(new BigDecimal(factor));
    }

    public Money multiply(int factor) {
        return multiply(new BigDecimal(factor));
    }

    public boolean isGreaterThan(Money other) {
        checkCurrencyMatch(other);
        return this.amount.compareTo(other.amount) > 0;
    }

    public boolean isLesserThan(Money other) {
        checkCurrencyMatch(other);
        return this.amount.compareTo(other.amount) < 0;
    }

    public boolean isEqualTo(Money other) {
        checkCurrencyMatch(other);
        return this.amount.compareTo(other.amount) == 0;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }
}

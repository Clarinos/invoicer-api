package fr.invoicer.api.domain.model.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

/**
 * Value Object représentant un montant monétaire immuable.
 * Conforme aux règles comptables : arrondi HALF_UP à 2 décimales.
 */
public record Money(BigDecimal amount, Currency currency) {

    public static final Currency EUR = Currency.getInstance("EUR");

    /**
     * Constructeur compact : valide et normalise le montant.
     */
    public Money {
        Objects.requireNonNull(amount, "Le montant ne peut pas être null");
        Objects.requireNonNull(currency, "La devise ne peut pas être null");

        // Règle comptable : on normalise toujours à 2 décimales avec arrondi commercial (HALF_UP)
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Méthode de fabrique pratique pour créer des euros rapidement.
     */
    public static Money euros(BigDecimal amount) {
        return new Money(amount, EUR);
    }

    public static Money euros(double amount) {
        return euros(BigDecimal.valueOf(amount));
    }

    public static Money zero() {
        return euros(BigDecimal.ZERO);
    }

    /**
     * Additionne deux montants (interdit d'additionner des devises différentes).
     */
    public Money add(Money other) {
        checkSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    /**
     * Soustrait un montant.
     */
    public Money subtract(Money other) {
        checkSameCurrency(other);
        return new Money(this.amount.subtract(other.amount), this.currency);
    }

    /**
     * Multiplie par une quantité (ex: 3 articles à 15.00 €).
     */
    public Money multiply(BigDecimal factor) {
        Objects.requireNonNull(factor, "Le facteur de multiplication ne peut pas être null");
        return new Money(this.amount.multiply(factor), this.currency);
    }

    public Money multiply(long factor) {
        return multiply(BigDecimal.valueOf(factor));
    }

    /**
     * Vérifie qu'on manipule la même devise (ex: pas d'addition EUR + USD).
     */
    private void checkSameCurrency(Money other) {
        Objects.requireNonNull(other, "Le montant à comparer ne peut pas être null");
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                    "Impossible d'opérer sur deux devises différentes : " + this.currency + " et " + other.currency
            );
        }
    }
}
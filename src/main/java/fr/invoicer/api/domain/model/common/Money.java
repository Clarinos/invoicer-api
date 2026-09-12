package fr.invoicer.api.domain.model.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

public record Money(BigDecimal amount, Currency currency) {
    public Money {
        // Code exécuté automatiquement à la création de l'objet
        if (amount == null) {
            throw new IllegalArgumentException("Le montant ne peut pas être null");
        }
        // On force 2 décimales pour la comptabilité (ex: 10 -> 10.00)
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }
}
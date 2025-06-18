package com.ensup.nasstech.controller;

import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import com.ensup.nasstech.entity.Ordinateur;
import com.ensup.nasstech.service.OrdinateurServiceItf;

@RestController
@RequestMapping("/stripe")
public class StripeController {

    private final OrdinateurServiceItf ordinateurService;

    public StripeController(OrdinateurServiceItf ordinateurService) {
        this.ordinateurService = ordinateurService;
    }

    @PostMapping("/create-checkout-session")
    public Map<String, String> checkout(HttpSession session) throws Exception {

        // Vérif authentification
        String role = (String) session.getAttribute("role");
        if (role == null || (!role.equals("utilisateur") && !role.equals("admin"))) {
            throw new IllegalStateException("Vous devez être connecté avec un rôle valide pour effectuer un paiement.");
        }

        List<Long> ids = (List<Long>) session.getAttribute("ordinateurCommanderListId");
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("Aucun ordinateur sélectionné pour le paiement.");
        }

        List<Ordinateur> ordis = ids.stream()
                .map(ordinateurService::getOrdinateurById)
                .filter(Objects::nonNull)
                .toList();

        if (ordis.isEmpty()) {
            throw new IllegalStateException("Aucun ordinateur valide trouvé pour les IDs donnés.");
        }

        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();
        for (Ordinateur ordi : ordis) {
            lineItems.add(
                SessionCreateParams.LineItem.builder()
                    .setQuantity(1L)
                    .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("eur")
                            .setUnitAmount((long) (ordi.getPrix() * 100)) // prix en centimes
                            .setProductData(
                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName(ordi.getDenomination())
                                    .build()
                            )
                            .build()
                    )
                    .build()
            );
        }

        SessionCreateParams params = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl("http://localhost:8080/success")
            .setCancelUrl("http://localhost:8080/cancel")
            .addAllLineItem(lineItems)
            .build();

        Session stripeSession = Session.create(params);
        System.out.println("✅ Stripe session ID : " + stripeSession.getId());

        Map<String, String> response = new HashMap<>();
        response.put("id", stripeSession.getId());
        return response;
    }
}

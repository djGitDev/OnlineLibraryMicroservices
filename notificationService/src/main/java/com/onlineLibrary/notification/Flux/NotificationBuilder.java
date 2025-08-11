//package com.onlineLibrary.notification.Flux;
//
//import com.google.gson.JsonObject;
//
//public class NotificationBuilder {
//
//    public static JsonObject buildPaymentNotification(JsonObject notificationResult, int orderId, int cartId, double totalPrice) throws Exception {
//        String firstName = notificationResult.get("first_name").getAsString();
//        String email = notificationResult.get("email").getAsString();
//        String subject = "Confirmation de paiement";
//        String message = String.format(
//                "Bonjour %s,\n\n" +
//                        "Nous vous confirmons que le paiement de %.2f$ a été effectué avec succès pour votre panier (ID : %d).\n" +
//                        "Votre commande (ID : %d) a été déclenchée et est en cours de traitement.\n\n" +
//                        "Merci pour votre confiance.\n\n",
//                firstName, totalPrice, cartId, orderId
//        );
//        JsonObject notificationJson = new JsonObject();
//        notificationJson.addProperty("to", email);
//        notificationJson.addProperty("subject", subject);
//        notificationJson.addProperty("message", message);
//
//        return notificationJson;
//    }
//}

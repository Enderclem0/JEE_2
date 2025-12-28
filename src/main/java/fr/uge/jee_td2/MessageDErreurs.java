package fr.uge.jee_td2;

import java.util.HashMap;

public class MessageDErreurs {

    private static final HashMap<String, String> message = new HashMap<>();

    static {
        message.put("0", "Code réservé (initialisation)");
        message.put("3", "Problème pour accéder à ce compte client, vérifiez qu'il est bien valide");
        message.put("4", "Le N° de compte doit comporter 4 caractères");
        message.put("5", "Le N° de compte doit être numérique");
        message.put("6", "La valeur de l'opération doit être un nombre valide.");
        message.put("7", "La valeur de l'opération doit être positive.");
        message.put("10", "Code réservé (succès consultation)");
        message.put("11", "Traitement opération réussi");
        message.put("12", "Code réservé (succès retour)");
        message.put("21", "Problème d'accès à la base de données, veuillez le signaler à votre administrateur");
        message.put("22", "Problème après traitement. Le traitement a été effectué correctement mais il y a eu un problème à signaler à votre administrateur");
        message.put("24", "Opération refusée, débit demandé supérieur au crédit du compte");
        message.put("31", "Opération refusée, la date de début est supérieur à la date de fin");
        message.put("32", "Il n'y a eu aucune opération effectuée durant cette période");
    }

    public static String getMessageDerreur(String errorNumber) {
        if (errorNumber == null || errorNumber.isBlank()) {
            return "Un code d'erreur non spécifié est survenu.";
        }

        return message.getOrDefault(errorNumber, "Erreur inconnue (Code: " + errorNumber + ")");
    }
}
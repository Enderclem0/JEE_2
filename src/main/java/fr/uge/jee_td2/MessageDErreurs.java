package fr.uge.jee_td2;

import java.util.HashMap;

public class MessageDErreurs {
  private static HashMap<Integer, String> message = new HashMap<>();

  static {
    message.put(3, "Problème pour accéder à ce compte client, vérifiez qu'il est bien valide");
    message.put(21, "Problème d'accès à la base de données, veuillez le signaler à votre administrateur");
    message.put(22, "Problème après traitement. Le traitement a ét effecté crrectemnt mais il y aeu un problème à signaler à votre administrateur");
    message.put(24, "Opération refusée, débit demandé supérieur au crédit du compte");
  }
  public static String getMessageDerreur(String errorNumber) {
    return message.get(Integer.parseInt(errorNumber));
  }
}

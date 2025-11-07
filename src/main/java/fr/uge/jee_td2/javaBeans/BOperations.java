package fr.uge.jee_td2.javaBeans;


import fr.uge.jee_td2.TraitementException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class BOperations {

    private static final String DB_URL = "jdbc:mariadb://localhost:3306/jee";
    private static final String USER = "jee_admin";
    private static final String PASS = "root";

    private Connection connection;

    private String noDeCompte;
    private String nom;
    private String prenom;
    private BigDecimal solde;
    private BigDecimal ancienSolde;
    private BigDecimal nouveauSolde;
    private BigDecimal valeur;
    private String op;
    private ArrayList<String[]> operationsParDates;
    private String dateInf;
    private String dateSup;

    public String getDateInf() {
        return dateInf;
    }

    public ArrayList<String[]> getOperationsParDates() {
        return operationsParDates;
    }

    public String getDateSup() {
        return dateSup;
    }

    public String getOp() {
        return op;
    }

    public String getValeur() {
        return String.valueOf(valeur.setScale(2, RoundingMode.HALF_UP));
    }

    public BigDecimal getNouveauSolde() {
        return nouveauSolde;
    }

    public BigDecimal getAncienSolde() {
        return ancienSolde;
    }

    public String getNoDeCompte() {
        return noDeCompte;
    }

    public String getNom() {
        return nom;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setValeur(String valeur) {
        this.valeur = new BigDecimal(valeur);
    }

    public void setOp(String op) {
        this.op = op;
    }

    public void setNoDeCompte(String noDeCompte) {
        this.noDeCompte = noDeCompte;
    }

    public void setDateInf(String dateInf) {
        this.dateInf = dateInf;
    }

    public void setDateSup(String dateSup) {
        this.dateSup = dateSup;
    }

    public void ouvrirConnexion() throws TraitementException {
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setAutoCommit(false);
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            // Handle any SQL errors
            System.err.println("SQL Exception: " + e.getMessage());
            throw new TraitementException("21");
        }
    }

    public void fermerConnexion() throws TraitementException {
        try {
            connection.close();
            System.out.println("Connection closed!");
        } catch (SQLException e) {
            // Handle any SQL errors
            System.err.println("SQL Exception: " + e.getMessage());
            throw new TraitementException("22");
        }
    }

    public void consulter() throws TraitementException {
        try {
            var query = """
                    SELECT * FROM Compte WHERE NOCOMPTE = ?
                    """;
            try (var preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, getNoDeCompte());
                var rs = preparedStatement.executeQuery();
                rs.next();
                this.nom = rs.getString("NOM");
                this.prenom = rs.getString("PRENOM");
                this.solde = rs.getBigDecimal("SOLDE");
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            throw new TraitementException("3");
        }
    }

    public void traiter() throws TraitementException, SQLException {
        boolean doCommit = true;
        try {
            var query = """
                    SELECT SOLDE FROM Compte WHERE NOCOMPTE = ?
                    """;
            try (var preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, getNoDeCompte());
                var rs = preparedStatement.executeQuery();
                rs.next();
                this.ancienSolde = rs.getBigDecimal("SOLDE");
                switch (op) {
                    case "+":
                        nouveauSolde = ancienSolde.add(valeur);
                        break;
                    case "-":
                        nouveauSolde = ancienSolde.subtract(valeur);
                        break;
                }
            }
            if (nouveauSolde.compareTo(BigDecimal.ZERO) < 0) {
                System.err.println("Le solde est insuffisant, transaction annulée");
                nouveauSolde = solde;
                throw new TraitementException("24");
            }
            query = """
                    INSERT INTO Operation (NOCOMPTE, DATE, HEURE, OP, VALEUR) VALUES (?, ?, ?, ?, ?)""";
            var queryUpdate = """
                    UPDATE Compte SET SOLDE = ? WHERE NOCOMPTE = ?""";
            try (var preparedInsertion = connection.prepareStatement(query);
                 var preparedUpdate = connection.prepareStatement(queryUpdate)) {
                preparedUpdate.setString(1, getNouveauSolde().toString());
                preparedUpdate.setString(2, getNoDeCompte());
                preparedUpdate.executeUpdate();

                preparedInsertion.setString(1, getNoDeCompte());
                preparedInsertion.setDate(2, Date.valueOf(LocalDate.now()));
                preparedInsertion.setTime(3, Time.valueOf(LocalTime.now()));
                preparedInsertion.setString(4, getOp());
                preparedInsertion.setString(5, getValeur());
                preparedInsertion.executeUpdate();
            }
        } catch (SQLException e) {
            doCommit = false;
            System.err.println("SQL Exception: " + e.getMessage());
            throw new TraitementException("22");
        } finally {
            if (doCommit) {
                connection.commit();
            } else {
                connection.rollback();
            }
        }
    }

    public void listerParDates() throws TraitementException {
        try {
            var query = """
                    SELECT DATE, OP, VALEUR FROM Operation WHERE NOCOMPTE = ? AND DATE BETWEEN ? AND ?
                    """;
            try (var preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, getNoDeCompte());
                preparedStatement.setString(2, getDateInf());
                preparedStatement.setString(3, getDateSup());
                var rs = preparedStatement.executeQuery();
                operationsParDates = new ArrayList<>();
                while (rs.next()) {
                    var date = rs.getDate("DATE");
                    var op = rs.getString("OP");
                    var valeur = rs.getBigDecimal("VALEUR");
                    operationsParDates.add(new String[]{date.toString(), op, valeur.toString()});
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            throw new TraitementException("3");
        }
    }
}

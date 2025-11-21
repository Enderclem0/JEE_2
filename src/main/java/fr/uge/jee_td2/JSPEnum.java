package fr.uge.jee_td2;

public enum JSPEnum {
    JSaisiNoDeCompte("JSaisiNoDeCompte.jsp"),
    JOperations("JOperations.jsp"),
    JListeOperations("JListeOperations.jsp");

    private final String jspPath;   // The private file path (e.g., "/WEB-INF/jsp/login.jsp")
    JSPEnum(String jspPath) {
        this.jspPath = "/WEB-INF/jsps/" + jspPath;
    }

    public String getJspPath() {
        return jspPath;
    }
}

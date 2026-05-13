package tn.iit.entity;

public enum RoleEmploye {
    ADMIN,
    RESPONSABLE,
    AGENT;

    public String getSpringRole() {
        return "ROLE_" + this.name();
    }
}

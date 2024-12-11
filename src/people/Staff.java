package people;

public class Staff extends Person {
    private String role;

    public Staff(String name, String nationalId, String role) {
        super(name, nationalId);
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

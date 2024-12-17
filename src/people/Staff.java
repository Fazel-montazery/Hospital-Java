package people;

public class Staff extends Person {
    private String role;

    public Staff(String name, String nationalId, String role) {
        super(name, nationalId);
        this.role = (role == null) ? "" : role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean isValid() {
        if (role.isBlank() || nationalId.isBlank() || name.isBlank())
            return false;

        return true;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "role='" + role + '\'' +
                ", name='" + name + '\'' +
                ", nationalId='" + nationalId + '\'' +
                '}';
    }
}

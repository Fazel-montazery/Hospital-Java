package people;

public class Patient extends Person {
    private String illness;

    public Patient(String name, String nationalId, String illness) {
        super(name, nationalId);
        this.illness = (illness == null) ? "" : illness;
    }

    public String getIllness() {
        return illness;
    }

    public void setIllness(String illness) {
        this.illness = illness;
    }

    @Override
    public boolean isValid() {
        if (illness.isBlank() || nationalId.isBlank() || name.isBlank())
            return false;

        return true;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "illness='" + illness + '\'' +
                ", name='" + name + '\'' +
                ", nationalId='" + nationalId + '\'' +
                '}';
    }
}

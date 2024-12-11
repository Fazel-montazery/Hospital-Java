package people;

public class Patient extends Person {
    private String illness;

    public Patient(String name, String nationalId, String illness) {
        super(name, nationalId);
        this.illness = illness;
    }

    public String getIllness() {
        return illness;
    }

    public void setIllness(String illness) {
        this.illness = illness;
    }
}

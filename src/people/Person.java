package people;

public abstract class Person {
    protected String name;
    protected String nationalId;

    public Person(String name, String nationalId) {
        this.name = (name == null) ? "" : name;
        this.nationalId = (nationalId == null) ? "" : nationalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", nationalId='" + nationalId + '\'' +
                '}';
    }

    abstract public boolean isValid();
}

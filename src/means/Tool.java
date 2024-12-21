package means;

public class Tool {
    private String name;
    private boolean available;

    public Tool(String name, boolean available) {
        this.name = (name == null) ? "" : name;
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isValid() {
        return !name.isBlank();
    }

    @Override
    public String toString() {
        return "Tool{" +
                "name='" + name + '\'' +
                ", available=" + available +
                '}';
    }
}

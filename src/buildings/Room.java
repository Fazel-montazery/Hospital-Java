package buildings;

public class Room {
    private int number;
    private int capacity;
    private String type;

    public Room(int number, int capacity, String type) {
        this.number = number;
        this.capacity = Math.max(1, capacity);
        this.type = (type == null) ? "Empty Room" : type;
    }

    public int getNumber() {
        return number;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = Math.max(1, capacity);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = (type == null) ? "Empty Room" : type;
    }

    public boolean isValid() {
        if (number < 1 || capacity < 1 || type.isBlank()) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Room{" +
                "number=" + number +
                ", capacity=" + capacity +
                ", type='" + type + '\'' +
                '}';
    }
}

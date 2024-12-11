import buildings.Hospital;
import people.Patient;
import people.Person;
import people.Staff;

import java.sql.SQLException;

public class Test {
    public static void main(String[] args) throws SQLException {
        Hospital mehrgan = Hospital.getInstance();

        for (Person p : mehrgan.searchPatients("ali")) {
            System.out.println(p);
        }
    }
}

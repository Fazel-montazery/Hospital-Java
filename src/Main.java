import java.sql.SQLException;
import javax.swing.SwingUtilities;
import UI.LoginPage;

public class Main {
    public static void main(String[] args) throws SQLException {
        SwingUtilities.invokeLater(() -> {
            LoginPage ui = new LoginPage();
            ui.setVisible(true);
        });
    }
}


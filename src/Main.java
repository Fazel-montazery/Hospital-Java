import java.sql.SQLException;
import javax.swing.SwingUtilities;

import UI.LoginAndSignUpPage;

public class Main {
    public static void main(String[] args) throws SQLException {
        SwingUtilities.invokeLater(() -> {
            LoginAndSignUpPage ui = new LoginAndSignUpPage();
            ui.setVisible(true);
        });
    }
}


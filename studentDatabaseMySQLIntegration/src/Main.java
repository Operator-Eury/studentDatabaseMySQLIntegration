import javaForms.dashboardFrame;
import javax.swing.*;
import com.formdev.flatlaf.FlatIntelliJLaf; 
import java.awt.Font;

public class Main {
    public static void main(String[] args) {
        // Set FlatLaf Look and Feel
        FlatIntelliJLaf.setup();
        

        // Set Global Font
            Font currentFont = UIManager.getFont("defaultFont"); // FlatLaf uses "defaultFont"
            Font newFontSize = currentFont.deriveFont(16f);

            // Apply font settings to specific components (for compatibility)
            UIManager.put("Button.font", newFontSize);
            UIManager.put("Label.font", newFontSize);
            UIManager.put("TextField.font", newFontSize);
            UIManager.put("TextArea.font", newFontSize);
            UIManager.put("ComboBox.font", newFontSize);
            UIManager.put("Table.font", newFontSize);
            UIManager.put("TableHeader.font", newFontSize);
            UIManager.put("TextPane.font", newFontSize);
            UIManager.put("EditorPane.font", newFontSize);
            UIManager.put("PasswordField.font", newFontSize);
            UIManager.put("RadioButton.font", newFontSize);
            UIManager.put("CheckBox.font", newFontSize);
            UIManager.put("List.font", newFontSize);
            UIManager.put("Menu.font", newFontSize);
            UIManager.put("MenuItem.font", newFontSize);
            UIManager.put("ToolTip.font", newFontSize);
            UIManager.put("Tree.font", newFontSize);
            UIManager.put("TabbedPane.font", newFontSize);
            UIManager.put("ScrollPane.font", newFontSize);
            UIManager.put("ToggleButton.font", newFontSize);

        // Force UI Update
        SwingUtilities.invokeLater(() -> {
            dashboardFrame frame = new dashboardFrame();
            SwingUtilities.updateComponentTreeUI(frame); // Forces all components to update
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

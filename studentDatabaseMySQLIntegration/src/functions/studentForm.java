/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package functions;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javaForms.dashboardFrame;
import javaForms.templateForms;
import javax.swing.JToggleButton;

/**
 *
 * @author John-Ronan Beira
 */
public class studentForm {

    templateForms enrollmentForm = new templateForms();

    public void startComponents() {

    }

    public templateForms showForm() {

        dashboardFrame.getInstance().getToggleFormButton().addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                JToggleButton toggleFormButton = (JToggleButton) e.getSource();
                boolean selected = e.getStateChange() == ItemEvent.SELECTED;
                toggleFormButton.setText(selected ? "Update Form" : "Enrollment Form");
                toggleFormButton.setBackground(selected ? new Color(0, 180, 255) : new Color(153, 255, 153));
                toggleFormButton.setForeground(selected ? Color.WHITE : Color.BLACK);

                changeState();

                toggleFormButton.revalidate();
                toggleFormButton.repaint();

            }

        });

        return enrollmentForm;
    }

    public void changeState() {

        String toggleText = dashboardFrame.getInstance().getToggleFormButton().getText();

        if (dashboardFrame.getInstance().getToggleFormButton().isSelected()) {
            System.out.println("Toggle Button says: " + toggleText + "\n");
            enrollmentForm.setFormHeaderTitle("LUPINBRIDGE UNIVERSITY STUDENT UPDATE FORM");
            enrollmentForm.setCreateButton("Finalize Changes");
        } else {
            enrollmentForm.setFormHeaderTitle("LUPINBRIDGE UNIVERSITY ENROLLMENT FORM");
            enrollmentForm.setCreateButton("Confirm Enrollment");
            System.out.println("\nToggle Button says: " + toggleText);
        }
    }
}

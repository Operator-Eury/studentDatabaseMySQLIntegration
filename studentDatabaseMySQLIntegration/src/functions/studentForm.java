/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package functions;
import javaForms.dashboardFrame;
import javaForms.templateForms;

/**
 *
 * @author John-Ronan Beira
 */
public class studentForm {
    
    
    
    public templateForms showForm(){
        templateForms enrollmentForm = new templateForms();
        
        String toggleText = dashboardFrame.getInstance().getToggleFormButton().getText();
        
        
        
        if(dashboardFrame.getInstance().getToggleFormButton().isSelected()) {
            System.out.println("Toggle Button says: " + toggleText + "\n");
            enrollmentForm.getAllowIdEdit().setVisible(true);
            enrollmentForm.setFormHeaderTitle("LUPINBRIDGE UNIVERSITY STUDENT UPDATE FORM");
            enrollmentForm.setCreateButton("Finalize Changes");
            enrollmentForm.getAllowIdEdit().setVisible(true);
        } else {
            System.out.println("Toggle Button says: " + toggleText + "\n");
        }
        
        return enrollmentForm;
    }
}

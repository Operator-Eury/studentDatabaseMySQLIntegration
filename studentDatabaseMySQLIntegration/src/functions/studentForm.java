/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package functions;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javaForms.dashboardFrame;
import javaForms.templateForms;
import javax.swing.JToggleButton;
import mySQLQueries.databaseConnector;

/**
 *
 * @author John-Ronan Beira
 */
public class studentForm {

    templateForms studentForm = new templateForms();
    private HashMap<String, HashMap<String, String>> programsMap;
    private HashMap<String, String> collegesMap;
    Connection connectionAttempt = databaseConnector.getConnection();

    private void startComponents() {
        fillProgramsComboBox();
        fillCollegesComboBox();
    }

    public templateForms showForm() {

        startComponents();

        dashboardFrame.getInstance().getToggleFormButton().addItemListener(e -> {
            JToggleButton toggleFormButton = (JToggleButton) e.getSource();
            boolean selected = e.getStateChange() == ItemEvent.SELECTED;
            toggleFormButton.setText(selected ? "Update Form" : "Enrollment Form");
            toggleFormButton.setBackground(selected ? new Color(0, 180, 255) : new Color(153, 255, 153));
            toggleFormButton.setForeground(selected ? Color.WHITE : Color.BLACK);

            changeState();

            toggleFormButton.revalidate();
            toggleFormButton.repaint();
        });

        studentForm.getCreateButton().addActionListener(e -> {
            if (!dashboardFrame.getInstance().getToggleFormButton().isSelected()) {
                //enrollment state
                System.out.println("Enrollment Button Triggered");
                //popup modal

                enrollStudent();

            } else {
                //update state
                System.out.println("Update Button Triggered");
                //popup modal

                updateStudent();

            }
        });

        studentForm.getProgramComboBox().addItemListener(e -> {

            if (e.getStateChange() == ItemEvent.SELECTED) {

                String selectedProgramName = (String) e.getItem();
                autoSelectCollege(selectedProgramName);
            }
        });

        studentForm.getCollegeComboBox().addItemListener(e -> {

            if (e.getStateChange() == ItemEvent.SELECTED) {

                String selectedCollegeName = (String) e.getItem();
                filterProgramsByCollege(selectedCollegeName);
            }
        });

        return studentForm;
    }

    private void changeState() {

        String toggleText = dashboardFrame.getInstance().getToggleFormButton().getText();

        if (dashboardFrame.getInstance().getToggleFormButton().isSelected()) {
            studentForm.setFormHeaderTitle("LUPINBRIDGE UNIVERSITY STUDENT UPDATE FORM");
            studentForm.setCreateButton("Finalize Changes");
            System.out.println("Toggle Button says: " + toggleText);

            studentForm.getFirstNameField().setText("");
            studentForm.getMiddleNameField().setText("");
            studentForm.getLastNameField().setText("");
            studentForm.getGenderComboBox().setSelectedIndex(0);
            studentForm.getYearLevelComboBox().setSelectedIndex(0);
            studentForm.getProgramComboBox().setSelectedIndex(0);
            studentForm.getCollegeComboBox().setSelectedIndex(0);
            studentForm.getIdNumberField().setText("0000-0000");
        } else {
            studentForm.setFormHeaderTitle("LUPINBRIDGE UNIVERSITY ENROLLMENT FORM");
            studentForm.setCreateButton("Confirm Enrollment");
            System.out.println("Toggle Button says: " + toggleText);

            studentForm.getFirstNameField().setText("");
            studentForm.getMiddleNameField().setText("");
            studentForm.getLastNameField().setText("");
            studentForm.getGenderComboBox().setSelectedIndex(0);
            studentForm.getYearLevelComboBox().setSelectedIndex(0);
            studentForm.getProgramComboBox().setSelectedIndex(0);
            studentForm.getCollegeComboBox().setSelectedIndex(0);
            studentForm.getIdNumberField().setText("0000-0000");
        }
    }

    private void fetchPrograms() {

        programsMap = new HashMap<>();

        String QUERY = "SELECT programCode, programName, collegeCode FROM programsTable";

        try (Statement createStatement = connectionAttempt.createStatement(); ResultSet createResult = createStatement.executeQuery(QUERY)) {

            while (createResult.next()) {
                String programCode = createResult.getString("programCode");
                String programName = createResult.getString("programName");
                String collegeCode = createResult.getString("collegeCode");

                HashMap<String, String> programInfo = new HashMap<>();
                programInfo.put(programName, collegeCode);

                programsMap.put(programCode, programInfo);
            }
        } catch (SQLException error) {
            System.err.println("SQL Error: " + error.getMessage());
        }
    }

    private void fillProgramsComboBox() {

        fetchPrograms();
        studentForm.getProgramComboBox().removeAllItems();
        studentForm.getProgramComboBox().addItem("Select Program");

        if (programsMap != null) {
            for (HashMap<String, String> programInfo : programsMap.values()) {
                for (String programName : programInfo.keySet()) {
                    studentForm.getProgramComboBox().addItem(programName);
                }
            }
        }
    }

    private void autoSelectCollege(String selectedProgramName) {

        if (!selectedProgramName.equals("Select Program")) {
            String collegeCodeToMatch = null;

            ItemListener[] collegeListeners = studentForm.getCollegeComboBox().getItemListeners();
            for (ItemListener listener : collegeListeners) {
                studentForm.getCollegeComboBox().removeItemListener(listener);
            }

            for (HashMap<String, String> programInfo : programsMap.values()) {
                if (programInfo.containsKey(selectedProgramName)) {
                    collegeCodeToMatch = programInfo.get(selectedProgramName);
                    break;
                }
            }

            if (collegeCodeToMatch != null && collegesMap != null) {

                String collegeNameToMatch = collegesMap.get(collegeCodeToMatch);

                if (collegeNameToMatch != null) {
                    studentForm.getCollegeComboBox().setSelectedItem(collegeNameToMatch);
                }
            }

            for (ItemListener listener : collegeListeners) {
                studentForm.getCollegeComboBox().addItemListener(listener);
            }

        } else {
            studentForm.getCollegeComboBox().setSelectedIndex(0);
        }
    }

    private void fetchColleges() {

        collegesMap = new HashMap<>();

        String QUERY = "SELECT collegeCode, collegeName FROM collegesTable";

        try (Statement createStatement = connectionAttempt.createStatement(); ResultSet createResult = createStatement.executeQuery(QUERY)) {

            while (createResult.next()) {
                String collegeCode = createResult.getString("collegeCode");
                String collegeName = createResult.getString("collegeName");
                collegesMap.put(collegeCode, collegeName);
            }
        } catch (SQLException error) {
            System.err.println("SQL Error: " + error.getMessage());
        }
    }

    private void fillCollegesComboBox() {

        fetchColleges();
        studentForm.getCollegeComboBox().removeAllItems();
        studentForm.getCollegeComboBox().addItem("Select College");

        if (collegesMap != null) {
            for (String collegeName : collegesMap.values()) {
                studentForm.getCollegeComboBox().addItem(collegeName);
            }
        }
    }

    private void filterProgramsByCollege(String selectedCollegeName) {
        if (!selectedCollegeName.equals("Select College")) {

            String collegeCodeToMatch = null;

            for (String collegeCode : collegesMap.keySet()) {
                if (collegesMap.get(collegeCode).equals(selectedCollegeName)) {
                    collegeCodeToMatch = collegeCode;
                    break;
                }
            }

            if (collegeCodeToMatch != null) {

                ItemListener[] programListeners = studentForm.getProgramComboBox().getItemListeners();
                for (ItemListener listener : programListeners) {
                    studentForm.getProgramComboBox().removeItemListener(listener);
                }

                studentForm.getProgramComboBox().removeAllItems();
                studentForm.getProgramComboBox().addItem("Select Program");

                for (HashMap<String, String> programInfo : programsMap.values()) {
                    for (String programName : programInfo.keySet()) {
                        String programCollegeCode = programInfo.get(programName);

                        if (programCollegeCode.equals(collegeCodeToMatch)) {
                            studentForm.getProgramComboBox().addItem(programName);
                        }
                    }
                }

                for (ItemListener listener : programListeners) {
                    studentForm.getProgramComboBox().addItemListener(listener);
                }
            }
        } else {
            fillProgramsComboBox();
        }
    }

    private void enrollStudent() {

        String firstName = studentForm.getFirstNameField().getText().strip();
        String middleName = studentForm.getMiddleNameField().getText().strip();
        String lastName = studentForm.getLastNameField().getText().strip();
        String gender = (String) studentForm.getGenderComboBox().getSelectedItem();
        String yearLevel = (String) studentForm.getYearLevelComboBox().getSelectedItem();
        String college = (String) studentForm.getCollegeComboBox().getSelectedItem();
        String program = (String) studentForm.getProgramComboBox().getSelectedItem();
        String idNumber = studentForm.getIdNumberField().getText().strip();

        //String INSERTQUERY = "INSERT INTO studentTable (idNumber, firstName, middleName, lastName, gender, yearLevel, collegeCode, programCode)"
    }

    private void updateStudent() {

        String firstName = studentForm.getFirstNameField().getText().strip();
        String middleName = studentForm.getMiddleNameField().getText().strip();
        String lastName = studentForm.getLastNameField().getText().strip();
        String gender = (String) studentForm.getGenderComboBox().getSelectedItem();
        String yearLevel = (String) studentForm.getYearLevelComboBox().getSelectedItem();
        String college = (String) studentForm.getCollegeComboBox().getSelectedItem();
        String program = (String) studentForm.getProgramComboBox().getSelectedItem();
        String idNumber = studentForm.getIdNumberField().getText().strip();

    }
}

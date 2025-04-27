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
import java.sql.PreparedStatement;
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

    private studentTable studentTableReference;
    templateForms studentForm = new templateForms();
    private HashMap<String, HashMap<String, String>> programsMap;
    private HashMap<String, String> collegesMap;
    Connection connectionAttempt = databaseConnector.getConnection();
    
    //setter
    public void setStudentTableReference(studentTable tableReference) {
    this.studentTableReference = tableReference;
}

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
                evaluateForm();

                //enrollStudent();
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

    private String retrieveProgramCodeFromName(String programName) {

        if (programName.isBlank() || programName.isEmpty() || programName.equalsIgnoreCase("Select Program")) {
            return null;
        }

        for (String programCode : programsMap.keySet()) {

            HashMap<String, String> programInfo = programsMap.get(programCode);

            for (String selectedProgramName : programInfo.keySet()) {
                if (selectedProgramName.equals(programName)) {
                    return programCode;
                }
            }
        }
        return null;
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

                studentForm.getSelectedCollegeLabel().setText(collegeNameToMatch);
                studentForm.getSelectedProgramLabel().setText(selectedProgramName);
            }

            for (ItemListener listener : collegeListeners) {
                studentForm.getCollegeComboBox().addItemListener(listener);
            }

        } else {
            studentForm.getCollegeComboBox().setSelectedIndex(0);
            studentForm.getSelectedCollegeLabel().setText("PLEASE SELECT COLLEGE");
            studentForm.getSelectedProgramLabel().setText("PLEASE SELECT PROGRAM");
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

                studentForm.getSelectedCollegeLabel().setText(selectedCollegeName);
            }
        } else {
            fillProgramsComboBox();
            studentForm.getSelectedCollegeLabel().setText("PLEASE SELECT COLLEGE");
        }
    }

    private String retrieveCollegeCodeFromName(String collegeName) {

        if (collegeName.isBlank() || collegeName.isEmpty() || collegeName.equalsIgnoreCase("Select College")) {
            return null;
        }

        for (String collegeCode : collegesMap.keySet()) {
            if (collegesMap.get(collegeCode).equals(collegeName)) {
                return collegeCode;
            }
        }

        return null;
    }

    private void evaluateForm() {
        StringBuilder errors = new StringBuilder();

        String firstName = studentForm.getFirstNameField().getText().strip();
        String middleName = studentForm.getMiddleNameField().getText().strip();
        String lastName = studentForm.getLastNameField().getText().strip();
        String gender = (String) studentForm.getGenderComboBox().getSelectedItem();
        String yearLevel = (String) studentForm.getYearLevelComboBox().getSelectedItem();
        String college = (String) studentForm.getCollegeComboBox().getSelectedItem();
        String collegeCode = retrieveCollegeCodeFromName(college);
        String program = (String) studentForm.getProgramComboBox().getSelectedItem();
        String programCode = retrieveProgramCodeFromName(program);
        String idNumber = studentForm.getIdNumberField().getText().strip();

        if (firstName.isBlank()) {
            errors.append("First Name cannot be Empty. Who even are you?\n");
        }

        if (lastName.isBlank()) {
            errors.append("Last Name cannot be Empty. I mean you can't be possibly Adam or Eve that doesn't have last names right?\n");
        }

        if (gender.equalsIgnoreCase("Select Gender")) {
            errors.append("You do have a Gender don't you?\n");
        }

        if (yearLevel.equalsIgnoreCase("Select Year Level")) {
            errors.append("Am I seeing this correctly? You're enrolling without Year Level specified!\n");
        }

        if (college.equalsIgnoreCase("Select College")) {

            if (gender.equalsIgnoreCase("Male")) {
                errors.append("Sir, you don't seem to be enrolling to any college?\n");
            } else if (gender.equalsIgnoreCase("Female")) {
                errors.append("Ma'am, you don't seem to be enrolling to any college?\n");
            } else {
                errors.append("..., you don't seem to be enrolling to any college?\n");
            }
        }

        if (program.equalsIgnoreCase("Select Program")) {

            if (gender.equalsIgnoreCase("Male")) {
                errors.append("Sir, you don't seem to be enrolling to any program?\n");
            } else if (gender.equalsIgnoreCase("Female")) {
                errors.append("Ma'am, you don't seem to be enrolling to any program?\n");
            } else {
                errors.append("..., you don't seem to be enrolling to any program?\n");
            }
        }

        if (isIdNumberUnique(idNumber) == false) {
            errors.append("Oooh I see you're trying to steal someone's ID. Can't let you do that!\n");
        }

        if (!errors.isEmpty()) {
            System.err.println(errors.toString());
        } else {
            enrollStudent(idNumber, firstName, middleName, lastName, gender, yearLevel, collegeCode, programCode);
        }
    }

    private boolean isIdNumberUnique(String idNumber) {
        String query = "SELECT COUNT(*) FROM studentTable WHERE idNumber = ?";

        try (PreparedStatement statement = connectionAttempt.prepareStatement(query)) {
            statement.setString(1, idNumber);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count == 0;
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return false;
    }

    private void enrollStudent(String idNumber, String firstName, String middleName,
            String lastName, String gender, String yearLevel,
            String collegeCode, String programCode) {

        String INSERTQUERY = "INSERT INTO studentTable (idNumber, firstName, middleName, lastName, gender, yearLevel, collegeCode, programCode) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement createStatement = connectionAttempt.prepareStatement(INSERTQUERY)) {
            
            createStatement.setString(1, idNumber);
            createStatement.setString(2, firstName);
            createStatement.setString(3, middleName);
            createStatement.setString(4, lastName);
            createStatement.setString(5, gender);
            createStatement.setString(6, yearLevel);
            createStatement.setString(7, collegeCode);
            createStatement.setString(8, programCode);
            
            int rowsAffected = createStatement.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Enrollment Done");
                studentTableReference.refreshTable();
            } else {
                System.err.println("Enrollment Failed");
            }
            
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
        }
    }

    private void updateStudent() {

    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package functions;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import javaForms.dashboardFrame;
import javaForms.templateFeedbackModalForms;
import javaForms.templateForms;
import javax.swing.JToggleButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import mySQLQueries.databaseConnector;

/**
 *
 * @author John-Ronan Beira
 */
public class studentForm {

    templateFeedbackModalForms newFeedbackModal = new templateFeedbackModalForms(dashboardFrame.getInstance(), true);

    private studentTable studentTableReference;
    templateForms studentForm = new templateForms();
    private HashMap<String, HashMap<String, String>> programsMap;
    private HashMap<String, String> collegesMap;
    Connection connectionAttempt = databaseConnector.getConnection();

    //setter
    public void setStudentTableReference(studentTable tableReference) {
        this.studentTableReference = tableReference;
    }

    //getter
    public templateForms getStudentForm() {
        return this.studentForm;
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
                showDialog(e);

            } else {
                //update state
                System.out.println("Update Button Triggered");
                //popup modal

                showDialog(e);

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

        newFeedbackModal.getConfirmButton().addActionListener(e -> {
            newFeedbackModal.dispose();
        });

        studentForm.getIdNumberField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {

                if (dashboardFrame.getInstance().getToggleFormButton().isSelected()) {
                    searchStudentById();
                }

            }

            @Override
            public void removeUpdate(DocumentEvent e) {

                if (dashboardFrame.getInstance().getToggleFormButton().isSelected()) {
                    searchStudentById();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

                if (dashboardFrame.getInstance().getToggleFormButton().isSelected()) {
                    searchStudentById();
                }

            }
        });

        studentForm.getDiscardButton().addActionListener(e -> {
            showDialog(e);
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

    private void showDialog(ActionEvent eventOccur) {

        Object source = eventOccur.getSource();

        if (source == studentForm.getCreateButton()) {

            templateFeedbackModalForms newOptionModal = new templateFeedbackModalForms(dashboardFrame.getInstance(), true);

            newOptionModal.getDeclineButton().addActionListener(e -> {
                newOptionModal.dispose();
            });

            newOptionModal.getConfirmButton().addActionListener(e -> {

                newOptionModal.dispose();
                evaluateForm();

            });

            if (!dashboardFrame.getInstance().getToggleFormButton().isSelected()) {

                newOptionModal.getFeedbackTextPane().setText("Proceed with the enrollment?");
                newOptionModal.setTitle("Confirm Enrollment?");
            } else {
                newOptionModal.getFeedbackTextPane().setText("Proceed with the update?");
                newOptionModal.setTitle("Confirm Update?");
            }

            newOptionModal.getConfirmButton().setText("Yes proceed");
            newOptionModal.getDeclineButton().setText("I'll double check");
            newOptionModal.getDeclineButton().requestFocusInWindow();

            newOptionModal.setLocationRelativeTo(dashboardFrame.getInstance());
            newOptionModal.setVisible(true);

        } else if (source == studentForm.getDiscardButton()) {

            templateFeedbackModalForms newOptionModal = new templateFeedbackModalForms(dashboardFrame.getInstance(), true);

            newOptionModal.getDeclineButton().addActionListener(e -> {
                newOptionModal.dispose();
            });

            newOptionModal.getConfirmButton().addActionListener(e -> {

                newOptionModal.dispose();
                studentForm.getFirstNameField().setText("");
                studentForm.getMiddleNameField().setText("");
                studentForm.getLastNameField().setText("");
                studentForm.getGenderComboBox().setSelectedIndex(0);
                studentForm.getYearLevelComboBox().setSelectedIndex(0);
                studentForm.getProgramComboBox().setSelectedIndex(0);
                studentForm.getCollegeComboBox().setSelectedIndex(0);
                studentForm.getIdNumberField().setText("0000-0000");

            });

            newOptionModal.getFeedbackTextPane().setText("Are you sure to reset your progress?");
            newOptionModal.setTitle("Discard Progress");

            newOptionModal.getConfirmButton().setText("Yes proceed");
            newOptionModal.getDeclineButton().setText("I'll double check");
            newOptionModal.getDeclineButton().requestFocusInWindow();

            newOptionModal.setLocationRelativeTo(dashboardFrame.getInstance());
            newOptionModal.setVisible(true);

        }
    }

    private void searchStudentById() {
        String SEARCHQUERY = "SELECT * FROM studentTable WHERE idNumber = ?";

        String idNumber = studentForm.getIdNumberField().getText();

        try (PreparedStatement createStatement = connectionAttempt.prepareStatement(SEARCHQUERY)) {

            createStatement.setString(1, idNumber);

            try (ResultSet createResult = createStatement.executeQuery()) {

                if (createResult.next()) {

                    studentForm.getFirstNameField().setText(createResult.getString("firstName"));
                    studentForm.getMiddleNameField().setText(createResult.getString("middleName"));
                    studentForm.getLastNameField().setText(createResult.getString("lastName"));
                    studentForm.getGenderComboBox().setSelectedItem(createResult.getString("gender"));
                    studentForm.getYearLevelComboBox().setSelectedItem(createResult.getString("yearLevel"));

                    String collegeCode = createResult.getString("collegeCode");
                    String programCode = createResult.getString("programCode");

                    setCollegeAndProgramComboBox(collegeCode, programCode);

                } else {

                    studentForm.getFirstNameField().setText("");
                    studentForm.getMiddleNameField().setText("");
                    studentForm.getLastNameField().setText("");
                    studentForm.getGenderComboBox().setSelectedIndex(0);
                    studentForm.getYearLevelComboBox().setSelectedIndex(0);
                    studentForm.getCollegeComboBox().setSelectedIndex(0);
                    studentForm.getProgramComboBox().setSelectedIndex(0);

                }
            }
        } catch (SQLException error) {
            System.err.println("SQL Error: " + error.getMessage());
        }
    }

    public void setCollegeAndProgramComboBox(String collegeCode, String programCode) {

        String QUERY = "SELECT cT.collegeName, pT.programName FROM collegesTable cT JOIN programsTable pT ON pT.collegeCode = cT.collegeCode WHERE cT.collegeCode = ? and pT.programCode = ?";

        try (PreparedStatement createStatement = connectionAttempt.prepareStatement(QUERY)) {

            createStatement.setString(1, collegeCode);
            createStatement.setString(2, programCode);

            try (ResultSet createResult = createStatement.executeQuery()) {
                if (createResult.next()) {

                    studentForm.getCollegeComboBox().setSelectedItem(createResult.getString("collegeName"));
                    studentForm.getProgramComboBox().setSelectedItem(createResult.getString("programName"));
                } else {

                    studentForm.getCollegeComboBox().setSelectedIndex(0);
                    studentForm.getProgramComboBox().setSelectedIndex(0);

                }
            }
        } catch (SQLException error) {
            System.err.println("SQL Error: " + error.getMessage());
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
            errors.append("First Name cannot be Empty\n");
        }

        if (lastName.isBlank()) {
            errors.append("Last Name cannot be Empty\n");
        }

        if (gender.equalsIgnoreCase("Select Gender")) {
            errors.append("Supply your Gender\n");
        }

        if (yearLevel.equalsIgnoreCase("Select Year Level")) {
            errors.append("Supply your Year Level\n");
        }

        if (college.equalsIgnoreCase("Select College")) {

            if (gender.equalsIgnoreCase("Male")) {
                errors.append("Sir, you need to have a College\n");
            } else if (gender.equalsIgnoreCase("Female")) {
                errors.append("Ma'am, you need to have a College\n");
            } else {
                errors.append("Hello, you need to have a College\n");
            }
        }

        if (program.equalsIgnoreCase("Select Program")) {

            if (gender.equalsIgnoreCase("Male")) {
                errors.append("Sir, you need your Program\n");
            } else if (gender.equalsIgnoreCase("Female")) {
                errors.append("Ma'am, you need your Program\n");
            } else {
                errors.append("Hello, you need your Program\n");
            }
        }

        if (!dashboardFrame.getInstance().getToggleFormButton().isSelected()) {
            if (isIdNumberUnique(idNumber) == false) {
                errors.append("That ID's taken!\n");
            }
        }

        if (!errors.isEmpty()) {

            System.err.println(errors.toString());

            if (!dashboardFrame.getInstance().getToggleFormButton().isSelected()) {
                String errorConclusion = "Your Enrollment could not be completed due to following error(s):\n\n" + errors.toString();
                newFeedbackModal.setTitle("Enrollment did not proceed due to following error(s)");
                newFeedbackModal.getFeedbackTextPane().setText(errorConclusion);
            } else {
                String errorConclusion = "Update could not be completed due to following error(s):\n\n" + errors.toString();
                newFeedbackModal.setTitle("Student Update did not proceed due to following error(s)");
                newFeedbackModal.getFeedbackTextPane().setText(errorConclusion);
            }

            newFeedbackModal.setLocationRelativeTo(dashboardFrame.getInstance());
            newFeedbackModal.getDeclineButton().setVisible(false);
            newFeedbackModal.setVisible(true);
            newFeedbackModal.getJScrollPane1().revalidate();
            newFeedbackModal.getJScrollPane1().repaint();
            newFeedbackModal.getJScrollPane1().getVerticalScrollBar().setValue(0);

        } else {

            if (!dashboardFrame.getInstance().getToggleFormButton().isSelected()) {
                newFeedbackModal.setTitle("Enrollment Accepted");
                newFeedbackModal.setLocationRelativeTo(dashboardFrame.getInstance());
                newFeedbackModal.getFeedbackTextPane().setText("No invalid fields found, proceeding with the enrollment.");
                newFeedbackModal.getDeclineButton().setVisible(false);
                newFeedbackModal.setVisible(true);

                enrollStudent(idNumber, firstName, middleName, lastName, gender, yearLevel, collegeCode, programCode);
            } else {
                newFeedbackModal.setTitle("Student Update Accepted");
                newFeedbackModal.setLocationRelativeTo(dashboardFrame.getInstance());
                newFeedbackModal.getFeedbackTextPane().setText("No invalid fields found, now updating student.");
                newFeedbackModal.getDeclineButton().setVisible(false);
                newFeedbackModal.setVisible(true);

                updateStudent(idNumber, firstName, middleName, lastName, gender, yearLevel, collegeCode, programCode);
            }
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
                newFeedbackModal.dispose();
                newFeedbackModal.setTitle("Enrollment Finished");
                newFeedbackModal.setLocationRelativeTo(dashboardFrame.getInstance());
                newFeedbackModal.getFeedbackTextPane().setText("Enrollment successfully operated");
                newFeedbackModal.getDeclineButton().setVisible(false);
                newFeedbackModal.setVisible(true);
                studentTableReference.refreshTable();

                studentForm.getFirstNameField().setText("");
                studentForm.getMiddleNameField().setText("");
                studentForm.getLastNameField().setText("");
                studentForm.getGenderComboBox().setSelectedIndex(0);
                studentForm.getYearLevelComboBox().setSelectedIndex(0);
                studentForm.getProgramComboBox().setSelectedIndex(0);
                studentForm.getCollegeComboBox().setSelectedIndex(0);
                studentForm.getIdNumberField().setText("0000-0000");
            } else {
                System.err.println("Enrollment Failed");
            }

        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
        }
    }

    private void updateStudent(String idNumber, String firstName, String middleName,
            String lastName, String gender, String yearLevel,
            String collegeCode, String programCode) {

        String UPDATEQUERY = "UPDATE studentTable SET "
                + "firstName = ?, "
                + "middleName = ?, "
                + "lastName = ?, "
                + "gender = ?, "
                + "yearLevel = ?, "
                + "collegeCode = ?, "
                + "programCode = ? "
                + "WHERE idNumber = ?";

        try (PreparedStatement createStatement = connectionAttempt.prepareStatement(UPDATEQUERY)) {

            createStatement.setString(1, firstName);
            createStatement.setString(2, middleName);
            createStatement.setString(3, lastName);
            createStatement.setString(4, gender);
            createStatement.setString(5, yearLevel);
            createStatement.setString(6, collegeCode);
            createStatement.setString(7, programCode);
            createStatement.setString(8, idNumber);

            int rowsAffected = createStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Student Update Done");
                newFeedbackModal.dispose();
                newFeedbackModal.setTitle("Update Finished");
                newFeedbackModal.setLocationRelativeTo(dashboardFrame.getInstance());
                newFeedbackModal.getFeedbackTextPane().setText("Update successfully operated");
                newFeedbackModal.getDeclineButton().setVisible(false);
                newFeedbackModal.setVisible(true);
                studentTableReference.refreshTable();

                studentForm.getFirstNameField().setText("");
                studentForm.getMiddleNameField().setText("");
                studentForm.getLastNameField().setText("");
                studentForm.getGenderComboBox().setSelectedIndex(0);
                studentForm.getYearLevelComboBox().setSelectedIndex(0);
                studentForm.getProgramComboBox().setSelectedIndex(0);
                studentForm.getCollegeComboBox().setSelectedIndex(0);
                studentForm.getIdNumberField().setText("0000-0000");
            } else {
                System.err.println("Update Failed");
            }

        } catch (SQLException error) {
            System.err.println("SQL Error: " + error.getMessage());
        }
    }
}

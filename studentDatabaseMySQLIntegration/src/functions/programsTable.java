/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package functions;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javaForms.dashboardFrame;
import javaForms.templateFeedbackModalForms;
import javaForms.templatePaginatedTableForms;
import javax.swing.ImageIcon;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import mySQLQueries.databaseConnector;

/**
 *
 * @author John-Ronan Beira
 */
public class programsTable {

    private HashMap<String, String> collegesMap;

    private studentTable studentFormReference;
    private collegesTable collegeTableReference;
    
    public void setStudentFormReference(studentTable form) {
        this.studentFormReference = form;
    }
    
    public void setCollegeTableReference (collegesTable form) {
        this.collegeTableReference = form;
    }

    private int startingPage = 1;
    private static final int rowsPerPage = 45;
    templatePaginatedTableForms programsTable = new templatePaginatedTableForms();

    private String searchText = programsTable.getSearchInputField().getText().trim();

    Connection connectionAttempt = databaseConnector.getConnection();

    private void startComponents() {
        fillTable();
        fillCollegesComboBox();
        sortSettings();
        getTotalPages();
        pageSelectorComboBox();
        programsTable.getTemplateTable().getTableHeader().repaint();
    }

    public templatePaginatedTableForms showTable() {

        programsTable.getNewItem().setText("Register Program");
        programsTable.setUpdateButton("Update Program");
        programsTable.setCreateButton("Register Program");

        programsTable.getGroupOptionsComboBox().removeAllItems();
        programsTable.getGroupOptionsComboBox().addItem("Program Name");
        programsTable.getGroupOptionsComboBox().addItem("Program Code");

        TableColumnModel columnModel = programsTable.getTemplateTable().getColumnModel();

        int columnCount = columnModel.getColumnCount();
        for (int i = columnCount - 1; i >= 3; i--) {
            columnModel.removeColumn(columnModel.getColumn(i));
        }

        columnModel.getColumn(0).setHeaderValue("Program Code");
        columnModel.getColumn(0).setPreferredWidth(10);

        columnModel.getColumn(1).setHeaderValue("Program Name");
        columnModel.getColumn(1).setPreferredWidth(150);

        columnModel.getColumn(2).setHeaderValue("College Name");
        columnModel.getColumn(2).setPreferredWidth(150);

        startComponents();

        programsTable.getSortingArrangements().addItemListener(e -> {
            sortSettings();
        });

        programsTable.getGroupOptionsComboBox().addItemListener(e -> {
            sortSettings();
        });

        programsTable.getPageSelector().addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Object selectedItem = e.getItem();
                if (selectedItem != null) {
                    try {
                        startingPage = Integer.parseInt(selectedItem.toString());

                        if (searchText.isEmpty()) {
                            sortSettings();
                            System.out.println("Activated");
                        } else {
                            fetchSearchPage(searchText, startingPage, rowsPerPage);
                        }

                    } catch (NumberFormatException ex) {
                        System.err.println("Error parsing page number: " + ex.getMessage());
                    }
                }
            }
        });

        programsTable.getSearchInputField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchFieldBar();

            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchFieldBar();

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchFieldBar();

            }
        });

        programsTable.getCreateButton().addActionListener(e -> {
            showFormDialog(e);
        });

        programsTable.getUpdateButton().addActionListener(e -> {

            int selectedRow = programsTable.getTemplateTable().getSelectedRow();

            if (selectedRow != -1) {

                Object collegeCodeObject = programsTable.getTemplateTable().getValueAt(selectedRow, 0);
                String collegeCode = (String) collegeCodeObject;

                programsTable.getItemCode().setText(collegeCode);
                searchProgramNameByCode();
            }

            showFormDialog(e);

        });

        programsTable.getAcceptButton().addActionListener(e -> {

            String programCode = programsTable.getItemCode().getText().toUpperCase().strip();
            String programName = programsTable.getItemName().getText().strip();
            String collegeName = programsTable.getCollegeComboBox().getSelectedItem().toString().strip();
            String collegeCode = retrieveCollegeCodeFromName(collegeName);

            if (programsTable.getAcceptButton().getText().equalsIgnoreCase("Register Program")) {
                evaluateForm(programCode, programName, collegeCode, false);
            } else {
                evaluateForm(programCode, programName, collegeCode, true);
            }

        });

        programsTable.getItemCode().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {

                if (programsTable.getAcceptButton().getText().equalsIgnoreCase("Update Program")) {
                    searchProgramNameByCode();
                }

            }

            @Override
            public void removeUpdate(DocumentEvent e) {

                if (programsTable.getAcceptButton().getText().equalsIgnoreCase("Update Program")) {
                    searchProgramNameByCode();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

                if (programsTable.getAcceptButton().getText().equalsIgnoreCase("Update Program")) {
                    searchProgramNameByCode();
                }

            }
        });

        programsTable.getDeleteItem().addActionListener(e -> {
            showModalDialog();
        });

        programsTable.getNewItem().addActionListener(e -> {
            showFormDialog(e);
        });

        programsTable.getEditItem().addActionListener(e -> {

            int selectedRow = programsTable.getTemplateTable().getSelectedRow();

            if (selectedRow != -1) {

                Object programCodeObject = programsTable.getTemplateTable().getValueAt(selectedRow, 0);
                String programCode = (String) programCodeObject;

                Object collegeNameObject = programsTable.getTemplateTable().getValueAt(selectedRow, 2);
                String collegeName = (String) collegeNameObject;

                programsTable.getItemCode().setText(programCode);
                searchProgramNameByCode();

                System.out.println("College Name: " + collegeName);

                if (collegeName != null) {

                    programsTable.getCollegeComboBox().setSelectedItem(collegeName);

                }

            }

            showFormDialog(e);
        });

        return programsTable;
    }

    public void refreshTable() {
        startingPage = 1;
        startComponents();
    }

    private void evaluateForm(String programCode, String programName, String collegeCode, boolean isUpdate) {

        StringBuilder errors = new StringBuilder();
        boolean hasError;

        programCode = programCode.strip();
        programName = programName.strip();
        collegeCode = collegeCode.strip();

        if (programCode.isBlank()) {
            errors.append("Program Code cannot be Empty\n");
        }

        if (programName.isBlank()) {
            errors.append("Program Name cannot be Empty\n");
        }

        if (collegeCode.equalsIgnoreCase("Select College")) {
            errors.append("You must bound a Program to a College");
        }

        if (!isUpdate) {
            if (isProgramCodeUnique(programCode) == false) {
                errors.append("That Program Code's taken!\n");
            }
        }

        if (!errors.isEmpty()) {

            System.err.println(errors.toString());
            hasError = true;

        } else {

            hasError = false;

        }

        processProgram(programCode, programName, collegeCode, isUpdate, hasError, errors.toString());

    }

    private void processProgram(String programCode, String programName, String collegeCode, boolean isUpdate, boolean hasError, String errorList) {

        templateFeedbackModalForms newFeedback = new templateFeedbackModalForms(dashboardFrame.getInstance(), true);
        newFeedback.getDeclineButton().setVisible(false);
        newFeedback.setLocationRelativeTo(dashboardFrame.getInstance());

        newFeedback.getConfirmButton().addActionListener(e -> {
            if (!hasError) {
                programsTable.getModalMenu().dispose();
                programsTable.getItemCode().setText("");
                programsTable.getItemName().setText("");
            }
            refreshTable();
            studentFormReference.refreshTable();
            collegeTableReference.refreshTable();
            newFeedback.dispose();
        });

        String INSERTQUERY = "INSERT INTO programsTable (programName, collegeCode, programCode)"
                + " VALUES (?, ?, ?)";

        String UPDATEQUERY = "UPDATE programsTable "
                + "SET programName = ?, "
                + "collegeCode = ? "
                + "WHERE programCode = ?";

        String UPDATESTUDENT = "UPDATE studentTable "
                + "SET collegeCode = ? "
                + "WHERE programCode = ?";

        if (!hasError) {

            if (isUpdate == false) {

                try (PreparedStatement createStatement = connectionAttempt.prepareStatement(INSERTQUERY)) {

                    createStatement.setString(1, programName);
                    createStatement.setString(2, collegeCode);
                    createStatement.setString(3, programCode);

                    int rowsAffected = createStatement.executeUpdate();

                    if (rowsAffected > 0) {

                        System.out.println("Registration Done");
                        programsTable.getModalMenu().setTitle("Program Registered");
                        newFeedback.getFeedbackTextPane().setText("Program successfully registered!");

                        newFeedback.setVisible(true);

                    } else {
                        System.err.println("Registration Failed");
                    }

                } catch (SQLException error) {
                    System.err.println("SQL Error: " + error.getMessage());
                }

            } else if (isUpdate == true) {

                try (PreparedStatement createStatement = connectionAttempt.prepareStatement(UPDATEQUERY); PreparedStatement createStudentUpdate = connectionAttempt.prepareStatement(UPDATESTUDENT)) {

                    createStatement.setString(1, programName);
                    createStatement.setString(2, collegeCode);
                    createStatement.setString(3, programCode);

                    int rowsAffected = createStatement.executeUpdate();

                    createStudentUpdate.setString(1, collegeCode);
                    createStudentUpdate.setString(2, programCode);

                    createStudentUpdate.executeUpdate();

                    if (rowsAffected > 0) {

                        System.out.println("Update Done");
                        programsTable.getModalMenu().setTitle("Program Updated");
                        newFeedback.getFeedbackTextPane().setText("Program successfully updated!");

                        newFeedback.setVisible(true);

                    } else {
                        System.err.println("Registration Failed");
                    }

                } catch (SQLException error) {
                    System.err.println("SQL Error: " + error.getMessage());
                }

            }

        } else if (hasError) {

            if (!isUpdate) {

                String errorConclusion = "Program Registration could not be completed due to following error(s):\n\n" + errorList;
                newFeedback.setTitle("Program Registration did not proceed due to following error(s)");
                newFeedback.getFeedbackTextPane().setText(errorConclusion);
                newFeedback.setVisible(true);

            } else {

                String errorConclusion = "Program Update could not be completed due to following error(s):\n\n" + errorList;
                newFeedback.setTitle("Program Update did not proceed due to following error(s)");
                newFeedback.getFeedbackTextPane().setText(errorConclusion);
                newFeedback.setVisible(true);

            }
        }

    }

    private boolean isProgramCodeUnique(String collegeCode) {
        String query = "SELECT COUNT(*) FROM programsTable WHERE programCode = ?";

        try (PreparedStatement statement = connectionAttempt.prepareStatement(query)) {
            statement.setString(1, collegeCode);

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

    private void showModalDialog() {

        int[] selectedRows = programsTable.getTemplateTable().getSelectedRows();

        templateFeedbackModalForms deleteConfirm = new templateFeedbackModalForms(dashboardFrame.getInstance(), true);
        List<String> programCodeList = new ArrayList<>();
        List<String> programNameList = new ArrayList<>();
        StringBuilder feedbackMessage = new StringBuilder();

        deleteConfirm.getDeclineButton().addActionListener(e -> {

            deleteConfirm.dispose();

        });

        deleteConfirm.getConfirmButton().addActionListener(e -> {

            deleteConfirm.dispose();
            deleteOperation(programCodeList);

        });

        if (selectedRows.length != 0) {

            for (int rows : selectedRows) {

                Object collegeCodeObject = programsTable.getTemplateTable().getValueAt(rows, 0);
                Object collegeNameObject = programsTable.getTemplateTable().getValueAt(rows, 1);

                programCodeList.add(collegeCodeObject.toString());
                programNameList.add(collegeNameObject.toString());
            }

            feedbackMessage.append("You are about to delete the following programs(s):\n\n");

            Map<String, Integer> studentCounts = new HashMap<>();

            String studentCheckSQL = "SELECT COUNT(*) FROM studentTable WHERE programCode = ?";

            try (PreparedStatement studentStatement = connectionAttempt.prepareStatement(studentCheckSQL)) {

                for (String programCode : programCodeList) {

                    studentStatement.setString(1, programCode);

                    ResultSet programCheckResult = studentStatement.executeQuery();
                    programCheckResult.next();

                    int studentCount = programCheckResult.getInt(1);

                    studentCounts.put(programCode, studentCount);

                }

            } catch (SQLException error) {
                System.err.println("SQL Error: " + error.getMessage());
            }

            for (String programName : programNameList) {
                feedbackMessage.append("- ").append(programName).append("\n");
            }

            boolean anyDependencies = false;

            for (int i = 0; i < programNameList.size(); i++) {

                String programName = programNameList.get(i);
                String programCode = programCodeList.get(i);

                int lengthBeforeAppend = feedbackMessage.length();

                feedbackMessage.append("- ").append(programName);

                int students = studentCounts.get(programCode);

                if (students > 0) {

                    anyDependencies = true;
                    feedbackMessage.append(" (");

                    if (students > 0) {

                        feedbackMessage.append(students).append(" student(s)");

                    }

                    feedbackMessage.append(")");
                } else {

                    feedbackMessage.setLength(lengthBeforeAppend);
                    continue;

                }

                feedbackMessage.append("\n");
            }

            if (anyDependencies) {

                feedbackMessage.append("\nWARNING: Programs with students cannot be deleted!\n");
                feedbackMessage.append("Please remove all students first.");

                deleteConfirm.getConfirmButton().setEnabled(false);

            } else {

                feedbackMessage.append("\nThese programs can be safely deleted.");

            }

            deleteConfirm.getFeedbackTextPane().setText(feedbackMessage.toString());

        }

        if (selectedRows.length == 0) {

            deleteConfirm.getFeedbackTextPane().setText("No highlighted programs, cannot proceed with the deletion");
            deleteConfirm.setTitle("Delete Program");

            deleteConfirm.getConfirmButton().setText("Yes proceed");
            deleteConfirm.getDeclineButton().setText("I'll double check");
            deleteConfirm.getDeclineButton().requestFocusInWindow();

            deleteConfirm.setLocationRelativeTo(dashboardFrame.getInstance());
            deleteConfirm.setVisible(true);

        } else if (selectedRows.length == 1) {

            deleteConfirm.getFeedbackTextPane().setText("You have highlighted a program for deletion. Confirm deleting? " + feedbackMessage);
            deleteConfirm.setTitle("Delete Program");

            deleteConfirm.getConfirmButton().setText("Yes proceed");
            deleteConfirm.getDeclineButton().setText("I'll double check");
            deleteConfirm.getDeclineButton().requestFocusInWindow();

            deleteConfirm.setLocationRelativeTo(dashboardFrame.getInstance());

            deleteConfirm.setVisible(true);
        } else if (selectedRows.length >= 2) {

            deleteConfirm.getFeedbackTextPane().setText("You have highlighted multiple programs, please double check before deletion. " + feedbackMessage);
            deleteConfirm.setTitle("Delete Multiple Programs");

            deleteConfirm.getConfirmButton().setText("Yes proceed");
            deleteConfirm.getDeclineButton().setText("I'll double check");
            deleteConfirm.getDeclineButton().requestFocusInWindow();

            deleteConfirm.setLocationRelativeTo(dashboardFrame.getInstance());
            deleteConfirm.setVisible(true);

        }

    }

    private void deleteOperation(List<String> programCodeList) {

        templateFeedbackModalForms deleteFeedback = new templateFeedbackModalForms(dashboardFrame.getInstance(), true);

        StringBuilder DELETE = new StringBuilder("DELETE FROM programsTable WHERE programCode IN (");

        for (int i = 0; i < programCodeList.size(); i++) {

            DELETE.append("?");

            if (i < programCodeList.size() - 1) {
                DELETE.append(",");
            }
        }

        DELETE.append(")");

        if (programCodeList.isEmpty()) {

            deleteFeedback.getFeedbackTextPane().setText("Like I said - You. Cannot. Delete. Anything. Empty!");
            deleteFeedback.setTitle("An Empty Void is about to be deleted");

            deleteFeedback.getConfirmButton().setText("Sorry Boss");
            deleteFeedback.getDeclineButton().setVisible(false);
            deleteFeedback.getDeclineButton().requestFocusInWindow();

            deleteFeedback.setLocationRelativeTo(dashboardFrame.getInstance());

            deleteFeedback.getConfirmButton().addActionListener(e -> {
                deleteFeedback.dispose();
            });

            deleteFeedback.setVisible(true);

        } else {

            try (PreparedStatement createStatement = connectionAttempt.prepareStatement(DELETE.toString())) {

                for (int i = 0; i < programCodeList.size(); i++) {
                    createStatement.setString(i + 1, programCodeList.get(i));
                }

                int rowAffected = createStatement.executeUpdate();

                deleteFeedback.getFeedbackTextPane().setText("Successfully deleted " + rowAffected + " programs!");
                deleteFeedback.setTitle("Operation successful");

                deleteFeedback.getConfirmButton().setText("Noted");
                deleteFeedback.getDeclineButton().setVisible(false);
                deleteFeedback.getDeclineButton().requestFocusInWindow();

                deleteFeedback.setLocationRelativeTo(dashboardFrame.getInstance());

                deleteFeedback.getConfirmButton().addActionListener(e -> {
                    deleteFeedback.dispose();
                });

                deleteFeedback.setVisible(true);

            } catch (SQLException error) {
                System.err.println("SQL Error: " + error.getMessage());
            }
        }

        if (!programCodeList.isEmpty()) {
            refreshTable();
        }
    }

    private void showFormDialog(ActionEvent event) {

        Object source = event.getSource();

        programsTable.getDeclineButton().addActionListener(e -> {
            programsTable.getItemCode().setText("");
            programsTable.getItemName().setText("");
            programsTable.getModalMenu().dispose();
        });

        if (source == programsTable.getCreateButton() || source == programsTable.getNewItem()) {

            programsTable.getModalMenu().setLocationRelativeTo(dashboardFrame.getInstance());
            programsTable.getModalMenu().setResizable(false);
            programsTable.getModalMenu().setTitle("Registering a Program");
            programsTable.getItemCodeLabel().setText("Program Code: ");
            programsTable.getItemNameLabel().setText("Program Name");
            programsTable.getCollegeCodeComboBoxLabel().setText("Select College to Bound");
            programsTable.getCollegeCodeComboBoxLabel().setVisible(true);
            programsTable.getCollegeComboBox().setVisible(true);
            programsTable.getHeaderLabel().setText("LUPINBRIDGE PROGRAM REGISTRATION");
            programsTable.getAcceptButton().setText("Register Program");
            programsTable.getAcceptButton().setBackground(new Color(153, 255, 153));
            programsTable.getDeclineButton().setText("Discard Progress");

            ImageIcon newIcon = new ImageIcon(getClass().getResource("/resources/images/add.png"));
            programsTable.getAcceptButton().setIcon(newIcon);

            programsTable.getModalMenu().setVisible(true);

        }

        if (source == programsTable.getUpdateButton() || source == programsTable.getEditItem()) {
            programsTable.getModalMenu().setLocationRelativeTo(dashboardFrame.getInstance());
            programsTable.getModalMenu().setResizable(false);
            programsTable.getModalMenu().setTitle("Updating a Program");
            programsTable.getItemCodeLabel().setText("Program Code: ");
            programsTable.getItemNameLabel().setText("Program Name");
            programsTable.getCollegeCodeComboBoxLabel().setText("Select College to Bound");
            programsTable.getCollegeCodeComboBoxLabel().setVisible(true);
            programsTable.getCollegeComboBox().setVisible(true);
            programsTable.getHeaderLabel().setText("LUPINBRIDGE UPDATE PROGRAM");
            programsTable.getAcceptButton().setText("Update Program");
            programsTable.getAcceptButton().setBackground(new Color(0, 180, 255));
            programsTable.getDeclineButton().setText("Discard Progress");

            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/resources/images/updated.png"));
            programsTable.getAcceptButton().setIcon(originalIcon);

            programsTable.getModalMenu().setVisible(true);
        }

    }

    private void searchProgramNameByCode() {

        String programCode = programsTable.getItemCode().getText().strip();

        String SEARCHQUERY = "SELECT programName FROM programsTable WHERE programCode = ?";

        try (PreparedStatement createStatement = connectionAttempt.prepareStatement(SEARCHQUERY)) {

            createStatement.setString(1, programCode);

            try (ResultSet createResult = createStatement.executeQuery()) {

                if (createResult.next()) {

                    String programName = createResult.getString("programName");
                    System.out.println("Program Name: " + programName);
                    programsTable.getItemName().setText(programName);

                } else {
                    System.out.println("No programs found for code: " + programCode);

                    programsTable.getItemName().setText("");
                }
            }
        } catch (SQLException error) {
            System.err.println("SQL Error: " + error.getMessage());
        }
    }

    private void fillCollegesComboBox() {

        fetchColleges();
        programsTable.getCollegeComboBox().removeAllItems();
        programsTable.getCollegeComboBox().addItem("Select College");

        if (collegesMap != null) {
            for (String collegeName : collegesMap.values()) {
                programsTable.getCollegeComboBox().addItem(collegeName);
            }
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

    private String retrieveCollegeCodeFromName(String collegeName) {

        if (collegeName.isBlank() || collegeName.isEmpty() || collegeName.equalsIgnoreCase("Select College")) {
            return "Select College";
        }

        for (String collegeCode : collegesMap.keySet()) {
            if (collegesMap.get(collegeCode).equals(collegeName)) {
                return collegeCode;
            }
        }

        return null;
    }

    private void fillTable() {
        DefaultTableModel model = (DefaultTableModel) programsTable.getTemplateTable().getModel();
        model.setRowCount(0);

        if (connectionAttempt != null) {
            sortSettings();
        } else {
            System.err.println("Failed to connect to the database.");
        }

    }

    private templatePaginatedTableForms sortSettings() {
        searchText = programsTable.getSearchInputField().getText().trim();
        if (!searchText.isEmpty()) {
            return programsTable;
        }

        String groupOptions = programsTable.getGroupOptionsComboBox().getSelectedItem().toString();
        String sortArrangements = programsTable.getSortingArrangements().getSelectedItem().toString();
        String sortOrder = sortArrangements.equalsIgnoreCase("Ascending") ? "ASC" : "DESC";
        int selectedPage = (startingPage - 1) * rowsPerPage;

        String currentPageQuery = " LIMIT " + rowsPerPage + " OFFSET " + selectedPage;

        String QUERY = "SELECT pT.programCode, pT.programName, cT.collegeName"
                + " FROM programsTable pT"
                + " JOIN collegesTable cT ON pT.collegeCode = cT.collegeCode";

        switch (groupOptions.toUpperCase()) {

            case "PROGRAM CODE" ->
                QUERY += " ORDER BY pT.programCode " + sortOrder + currentPageQuery;

            case "PROGRAM NAME" ->
                QUERY += " ORDER BY pT.programName " + sortOrder + currentPageQuery;

            default ->
                QUERY += " ORDER BY pT.programCode " + sortOrder + currentPageQuery;
        }

        DefaultTableModel model = (DefaultTableModel) programsTable.getTemplateTable().getModel();
        model.setRowCount(0);

        if (connectionAttempt != null) {
            try (Statement createStatement = connectionAttempt.createStatement(); ResultSet createResult = createStatement.executeQuery(QUERY)) {

                while (createResult.next()) {

                    String programCode = createResult.getString("programCode");
                    String programName = createResult.getString("programName");
                    String collegeName = createResult.getString("collegeName");

                    model.addRow(new Object[]{programCode, programName, collegeName});

                }
            } catch (SQLException error) {
                System.err.println("SQL Error: " + error.getMessage());
            }
        }
        setCounter();
        programsTable.getTemplateTable().getTableHeader().repaint();

        return programsTable;
    }

    private void setCounter() {
        String QUERY = "SELECT COUNT(*) FROM programsTable";

        try (Statement createStatement = connectionAttempt.createStatement(); ResultSet createResult = createStatement.executeQuery(QUERY)) {
            if (createResult.next()) {
                int rowCount = createResult.getInt(1);
                programsTable.setCounterLabel("Programs Found: " + String.valueOf(rowCount));
            }
        } catch (SQLException error) {
            System.err.println("SQL Error: " + error.getMessage());
        }
    }

    private void pageSelectorComboBox() {

        searchText = programsTable.getSearchInputField().getText().trim();
        if (searchText.isEmpty()) {
            programsTable.getPageSelector().removeAllItems();
            for (int i = 1; i <= getTotalPages(); i++) {
                programsTable.getPageSelector().addItem(String.valueOf(i));
            }
        }

    }

    private int getTotalPages() {

        searchText = programsTable.getSearchInputField().getText().trim();

        if (!searchText.isEmpty()) {
            // Skip total count if search is active
            return 0;
        }

        int totalRows = getTotalRows();
        int totalPages = (int) Math.ceil(totalRows / (double) rowsPerPage);
        programsTable.setTotalPageLabel("out of " + String.valueOf(totalPages));

        return totalPages;
    }

    private int getTotalRows() {

        String QUERY = "SELECT COUNT(*) FROM programsTable";
        int totalRows = 0;

        try (Statement createStatement = connectionAttempt.createStatement(); ResultSet createResult = createStatement.executeQuery(QUERY)) {
            if (createResult.next()) {
                totalRows = createResult.getInt(1);
            }
        } catch (SQLException error) {
            System.err.println("SQL Error: " + error.getMessage());
        }

        return totalRows;
    }

    private int searchFieldBar() {
        searchText = programsTable.getSearchInputField().getText().trim();
        if (searchText.isEmpty()) {
            sortSettings();
            pageSelectorComboBox();
            return getTotalPages();
        }

        int totalMatches = countSearchResults(searchText);
        programsTable.setCounterLabel("Programs Found: " + totalMatches);

        // 2) Populate the page selector based on match count
        int totalPages = getTotalSearchPages(totalMatches);

        // 3) Fetch and display the current page
        fetchSearchPage(searchText, startingPage, rowsPerPage);

        return totalPages;
    }

    private int getTotalSearchPages(int rows) {
        searchText = programsTable.getSearchInputField().getText().trim();

        if (searchText.isEmpty()) {
            return getTotalPages();
        }

        int totalRows = rows;
        int totalPages = (int) Math.ceil(totalRows / (double) rowsPerPage);

        if (totalPages == 0) {
            totalPages = 1;
        }

        programsTable.setTotalPageLabel("out of " + String.valueOf(totalPages));

        programsTable.getPageSelector().removeAllItems();
        for (int i = 1; i <= totalPages; i++) {
            programsTable.getPageSelector().addItem(String.valueOf(i));
        }

        return totalPages;
    }

    private int countSearchResults(String searchText) {

        String countQuery = "SELECT COUNT(*) FROM programsTable pT "
                + "JOIN collegesTable cT ON pT.collegeCode = cT.collegeCode "
                + "WHERE pT.programName LIKE ? OR pT.programCode LIKE ? OR cT.collegeName LIKE ? "
                + "OR cT.collegeCode LIKE ?";

        try (PreparedStatement createPreparedStatement = connectionAttempt.prepareStatement(countQuery)) {
            String pattern = "%" + searchText + "%";

            // Bind the search text to each of the four fields
            createPreparedStatement.setString(1, pattern); // programName
            createPreparedStatement.setString(2, pattern); // programCode
            createPreparedStatement.setString(3, pattern); // collegeName
            createPreparedStatement.setString(4, pattern); // collegeCode

            try (ResultSet rs = createPreparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException error) {
            System.err.println("SQL Error (count): " + error.getMessage());
        }
        return 0;
    }

    private void fetchSearchPage(String searchText, int page, int rowsPerPage) {
        int offset = (page - 1) * rowsPerPage;

        // Fuzzy search query for programName, programCode, collegeName, and collegeCode
        String query = "SELECT pT.programCode, pT.programName, cT.collegeName "
                + "FROM programsTable pT "
                + "JOIN collegesTable cT ON pT.collegeCode = cT.collegeCode "
                + "WHERE pT.programName LIKE ? OR pT.programCode LIKE ? OR cT.collegeName LIKE ? "
                + "OR cT.collegeCode LIKE ? "
                + "LIMIT " + rowsPerPage + " OFFSET " + offset;

        DefaultTableModel model = (DefaultTableModel) programsTable.getTemplateTable().getModel();
        model.setRowCount(0);

        try (PreparedStatement createPreparedStatement = connectionAttempt.prepareStatement(query)) {
            String pattern = "%" + searchText + "%";

            // Bind the search text to each of the four fields
            createPreparedStatement.setString(1, pattern); // programName
            createPreparedStatement.setString(2, pattern); // programCode
            createPreparedStatement.setString(3, pattern); // collegeName
            createPreparedStatement.setString(4, pattern); // collegeCode

            try (ResultSet rs = createPreparedStatement.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString("programCode"),
                        rs.getString("programName"),
                        rs.getString("collegeName")
                    });
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error (Fetch): " + e.getMessage());
        }
    }

}

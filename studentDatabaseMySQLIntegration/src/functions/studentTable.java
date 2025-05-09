/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package functions;

import java.util.List;
import java.awt.event.ItemEvent;
import javaForms.templatePaginatedTableForms;
import javaForms.templateFeedbackModalForms;
import javaForms.dashboardFrame;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import mySQLQueries.databaseConnector;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author John-Ronan Beira
 */
public class studentTable {

    private studentForm studentFormReference;

    //setter
    public void setStudentFormReference(studentForm form) {
        this.studentFormReference = form;
    }

    templatePaginatedTableForms studentTable = new templatePaginatedTableForms();
    Connection connectionAttempt = databaseConnector.getConnection();
    private int currentPage = 1;
    private static final int rowsPerPage = 45;
    private String searchText = studentTable.getSearchInputField().getText().strip();

    private void startComponents() {
        fillTable();
        setCounter();
        getTotalPages();
        pageSelectorComboBox();
        studentTable.getTemplateTable().getTableHeader().repaint();
    }

    public void refreshTable() {
        currentPage = 1;
        startComponents();
    }

    public templatePaginatedTableForms showTable() {

        studentTable.setUpdateButton("Update Student");
        studentTable.setCreateButton("Enroll Student");

        studentTable.getGroupOptionsComboBox().removeAllItems();
        studentTable.getGroupOptionsComboBox().addItem("ID Number");
        studentTable.getGroupOptionsComboBox().addItem("First Name");
        studentTable.getGroupOptionsComboBox().addItem("Last Name");
        studentTable.getGroupOptionsComboBox().addItem("Gender");
        studentTable.getGroupOptionsComboBox().addItem("Year Level");
        studentTable.getGroupOptionsComboBox().addItem("College");
        studentTable.getGroupOptionsComboBox().addItem("Program");

        TableColumnModel columnModel = studentTable.getTemplateTable().getColumnModel();
        columnModel.getColumn(0).setHeaderValue("ID Number");
        columnModel.getColumn(0).setPreferredWidth(10);

        columnModel.getColumn(1).setHeaderValue("First Name");
        columnModel.getColumn(1).setPreferredWidth(50);

        columnModel.getColumn(2).setHeaderValue("Middle Name");
        columnModel.getColumn(2).setPreferredWidth(50);

        columnModel.getColumn(3).setHeaderValue("Last Name");
        columnModel.getColumn(3).setPreferredWidth(50);

        columnModel.getColumn(4).setHeaderValue("Gender");
        columnModel.getColumn(4).setPreferredWidth(10);

        columnModel.getColumn(5).setHeaderValue("Year Level");
        columnModel.getColumn(5).setPreferredWidth(10);

        columnModel.getColumn(6).setHeaderValue("College Name");
        columnModel.getColumn(6).setPreferredWidth(150);

        columnModel.getColumn(7).setHeaderValue("Program Name");
        columnModel.getColumn(7).setPreferredWidth(250);

        startComponents();

        studentTable.getSortingArrangements().addItemListener(e -> {
            sortSettings();
        });

        studentTable.getGroupOptionsComboBox().addItemListener(e -> {
            sortSettings();
        });

        studentTable.getEditItem().addActionListener(e -> {

            dashboardFrame.getInstance().getDashboardTabs().setSelectedIndex(1);
            dashboardFrame.getInstance().getToggleFormButton().setSelected(true);

            int selectedRow = studentTable.getTemplateTable().getSelectedRow();

            if (selectedRow != -1) {

                Object idNumberObject = studentTable.getTemplateTable().getValueAt(selectedRow, 0);
                String idNumber = (String) idNumberObject;

                studentFormReference.studentForm.getIdNumberField().setText(idNumber);
            }

            studentTable.getTemplateTable().clearSelection();
            studentTable.getTemplateTable().repaint();
            studentTable.getTemplateTable().revalidate();

        });

        studentTable.getUpdateButton().addActionListener(e -> {
            System.out.println("Update Button is Clicked");

            dashboardFrame.getInstance().getDashboardTabs().setSelectedIndex(1);
            dashboardFrame.getInstance().getToggleFormButton().setSelected(true);

            int selectedRow = studentTable.getTemplateTable().getSelectedRow();

            if (selectedRow != -1) {

                Object idNumberObject = studentTable.getTemplateTable().getValueAt(selectedRow, 0);
                String idNumber = (String) idNumberObject;

                studentFormReference.studentForm.getIdNumberField().setText(idNumber);
            }

            studentTable.getTemplateTable().clearSelection();
            studentTable.getTemplateTable().repaint();
            studentTable.getTemplateTable().revalidate();
        });

        studentTable.getCreateButton().addActionListener(e -> {
            System.out.println("Create Button is Clicked");
            dashboardFrame.getInstance().getDashboardTabs().setSelectedIndex(1);
            dashboardFrame.getInstance().getToggleFormButton().setSelected(false);
            studentTable.getTemplateTable().clearSelection();
            studentTable.getTemplateTable().repaint();
            studentTable.getTemplateTable().revalidate();

        });

        studentTable.getNewItem().addActionListener(e -> {
            dashboardFrame.getInstance().getDashboardTabs().setSelectedIndex(1);
            dashboardFrame.getInstance().getToggleFormButton().setSelected(false);
            studentTable.getTemplateTable().clearSelection();
            studentTable.getTemplateTable().repaint();
            studentTable.getTemplateTable().revalidate();
        });

        studentTable.getPageSelector().addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Object selectedItem = e.getItem();
                if (selectedItem != null) {
                    try {
                        currentPage = Integer.parseInt(selectedItem.toString());

                        if (searchText.isEmpty()) {
                            sortSettings();
                            System.out.println("Activated");
                        } else {
                            fetchSearchPage(searchText, currentPage, rowsPerPage);
                        }

                    } catch (NumberFormatException ex) {
                        System.err.println("Error parsing page number: " + ex.getMessage());
                    }
                }
            }
        });

        studentTable.getSearchInputField().getDocument().addDocumentListener(new DocumentListener() {
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

        studentTable.getDeleteItem().addActionListener(e -> {
            showModalDialog();
        });

        return studentTable;

    }

    private void showModalDialog() {
        int[] selectedRows = studentTable.getTemplateTable().getSelectedRows();

        templateFeedbackModalForms deleteConfirm = new templateFeedbackModalForms(dashboardFrame.getInstance(), true);
        List<String> idNumberList = new ArrayList<>();
        List<String> lastNameList = new ArrayList<>();
        StringBuilder feedbackMessage = new StringBuilder();

        deleteConfirm.getDeclineButton().addActionListener(e -> {

            deleteConfirm.dispose();

        });

        deleteConfirm.getConfirmButton().addActionListener(e -> {

            deleteConfirm.dispose();
            deleteOperation(idNumberList);

        });

        if (selectedRows.length != 0) {

            for (int rows : selectedRows) {

                Object idNumberObject = studentTable.getTemplateTable().getValueAt(rows, 0);
                Object lastNameObject = studentTable.getTemplateTable().getValueAt(rows, 3);

                idNumberList.add(idNumberObject.toString());
                lastNameList.add(lastNameObject.toString());
            }

            feedbackMessage.append("You are about to delete the following student(s):\n\n");

            for (String lastName : lastNameList) {
                feedbackMessage.append("- ").append(lastName).append("\n");
            }

            deleteConfirm.getFeedbackTextPane().setText(feedbackMessage.toString());

        }

        if (selectedRows.length == 0) {

            deleteConfirm.getFeedbackTextPane().setText("No highlighted student, cannot proceed with the deletion");
            deleteConfirm.setTitle("Delete Student");

            deleteConfirm.getConfirmButton().setText("Yes proceed");
            deleteConfirm.getDeclineButton().setText("I'll double check");
            deleteConfirm.getDeclineButton().requestFocusInWindow();

            deleteConfirm.setLocationRelativeTo(dashboardFrame.getInstance());
            deleteConfirm.setVisible(true);

        } else if (selectedRows.length == 1) {

            deleteConfirm.getFeedbackTextPane().setText("You have highlighted a student for deletion. Confirm deleting? " + feedbackMessage);
            deleteConfirm.setTitle("Delete Student");

            deleteConfirm.getConfirmButton().setText("Yes proceed");
            deleteConfirm.getDeclineButton().setText("I'll double check");
            deleteConfirm.getDeclineButton().requestFocusInWindow();

            deleteConfirm.setLocationRelativeTo(dashboardFrame.getInstance());

            deleteConfirm.setVisible(true);
        } else if (selectedRows.length >= 2) {

            deleteConfirm.getFeedbackTextPane().setText("You have highlighted multiple students, please double check before deletion. " + feedbackMessage);
            deleteConfirm.setTitle("Delete Multiple Students");

            deleteConfirm.getConfirmButton().setText("Yes proceed");
            deleteConfirm.getDeclineButton().setText("I'll double check");
            deleteConfirm.getDeclineButton().requestFocusInWindow();

            deleteConfirm.setLocationRelativeTo(dashboardFrame.getInstance());
            deleteConfirm.setVisible(true);

        }

    }

    private void deleteOperation(List<String> idNumberList) {

        templateFeedbackModalForms deleteFeedback = new templateFeedbackModalForms(dashboardFrame.getInstance(), true);

        StringBuilder DELETE = new StringBuilder("DELETE FROM studentTable WHERE idNumber IN (");

        for (int i = 0; i < idNumberList.size(); i++) {

            DELETE.append("?");

            if (i < idNumberList.size() - 1) {
                DELETE.append(",");
            }
        }

        DELETE.append(")");

        if (idNumberList.isEmpty()) {

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

                for (int i = 0; i < idNumberList.size(); i++) {
                    createStatement.setString(i + 1, idNumberList.get(i));
                }

                int rowAffected = createStatement.executeUpdate();

                deleteFeedback.getFeedbackTextPane().setText("Successfully deleted " + rowAffected + " students!");
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
        
        if (!idNumberList.isEmpty()) {
             refreshTable();
        }
    }

    private void fillTable() {
        DefaultTableModel model = (DefaultTableModel) studentTable.getTemplateTable().getModel();
        model.setRowCount(0);

        if (connectionAttempt != null) {
            sortSettings();
        } else {
            System.err.println("Failed to connect to the database.");
        }
    }

    private templatePaginatedTableForms sortSettings() {
        searchText = studentTable.getSearchInputField().getText().strip();
        if (!searchText.isEmpty()) {
            return studentTable;
        }

        String groupOptions = studentTable.getGroupOptionsComboBox().getSelectedItem().toString();
        String sortArrangements = studentTable.getSortingArrangements().getSelectedItem().toString();
        String sortOrder = sortArrangements.equalsIgnoreCase("Ascending") ? "ASC" : "DESC";
        int selectedPage = (currentPage - 1) * rowsPerPage;

        String currentPageQuery = " LIMIT " + rowsPerPage + " OFFSET " + selectedPage;

        String QUERY = "SELECT sT.idNumber, sT.firstName, sT.middleName, sT. lastName, sT.gender, sT.yearLevel, cT.collegeName, pT.programName"
                + " FROM studentTable sT"
                + " JOIN collegesTable cT ON sT.collegeCode = cT.collegeCode"
                + " JOIN programsTable pT ON sT.programCode = pT.programCode";

        switch (groupOptions.toUpperCase()) {

            case "ID NUMBER" ->
                QUERY += " ORDER BY sT.idNumber " + sortOrder + currentPageQuery;

            case "FIRST NAME" ->
                QUERY += " ORDER BY sT.firstName " + sortOrder + currentPageQuery;

            case "LAST NAME" ->
                QUERY += " ORDER BY sT.lastName " + sortOrder + currentPageQuery;

            case "GENDER" ->
                QUERY += " ORDER BY sT.gender " + sortOrder + currentPageQuery;

            case "YEAR LEVEL" ->
                QUERY += " ORDER BY sT.yearLevel " + sortOrder + currentPageQuery;

            case "COLLEGE" ->
                QUERY += " ORDER BY cT.collegeName " + sortOrder + currentPageQuery;

            case "PROGRAM" ->
                QUERY += " ORDER BY pT.programName " + sortOrder + currentPageQuery;

            default ->
                QUERY += " ORDER BY sT.idNumber " + sortOrder + currentPageQuery;
        }

        DefaultTableModel model = (DefaultTableModel) studentTable.getTemplateTable().getModel();
        model.setRowCount(0); // Clear existing rows

        if (connectionAttempt != null) {
            try (Statement createStatement = connectionAttempt.createStatement(); ResultSet createResult = createStatement.executeQuery(QUERY)) {

                while (createResult.next()) {

                    String idNumber = createResult.getString("idNumber");
                    String firstName = createResult.getString("firstName");
                    String middleName = createResult.getString("middleName");
                    String lastName = createResult.getString("lastName");
                    String gender = createResult.getString("gender");
                    String yearLevel = createResult.getString("yearLevel");
                    String collegeName = createResult.getString("collegeName");
                    String programName = createResult.getString("programName");

                    model.addRow(new Object[]{idNumber, firstName, middleName, lastName, gender, yearLevel, collegeName, programName});
                }
            } catch (SQLException error) {
                System.err.println("SQL Error: " + error.getMessage());
            }
        }
        setCounter();
        studentTable.getTemplateTable().getTableHeader().repaint();

        return studentTable;
    }

    private void pageSelectorComboBox() {

        searchText = studentTable.getSearchInputField().getText().strip();
        if (searchText.isEmpty()) {
            studentTable.getPageSelector().removeAllItems();
            for (int i = 1; i <= getTotalPages(); i++) {
                studentTable.getPageSelector().addItem(String.valueOf(i));
            }
        }

    }

    private void setCounter() {
        String QUERY = "SELECT COUNT(*) FROM studentTable";

        try (Statement createStatement = connectionAttempt.createStatement(); ResultSet createResult = createStatement.executeQuery(QUERY)) {
            if (createResult.next()) {
                int rowCount = createResult.getInt(1);
                studentTable.setCounterLabel("Students Found: " + String.valueOf(rowCount));
            }
        } catch (SQLException error) {
            System.err.println("SQL Error: " + error.getMessage());
        }
    }

    private int getTotalRows() {

        String QUERY = "SELECT COUNT(*) FROM studentTable";
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

    private int getTotalPages() {

        searchText = studentTable.getSearchInputField().getText().strip();

        if (!searchText.isEmpty()) {
            // Skip total count if search is active
            return 0;
        }

        int totalRows = getTotalRows();
        int totalPages = (int) Math.ceil(totalRows / (double) rowsPerPage);
        studentTable.setTotalPageLabel("out of " + String.valueOf(totalPages));

        return totalPages;
    }

    private int getTotalSearchPages(int rows) {
        searchText = studentTable.getSearchInputField().getText().strip();

        if (searchText.isEmpty()) {
            return getTotalPages();
        }

        int totalRows = rows;
        int totalPages = (int) Math.ceil(totalRows / (double) rowsPerPage);

        if (totalPages == 0) {
            totalPages = 1;
        }

        studentTable.setTotalPageLabel("out of " + String.valueOf(totalPages));

        studentTable.getPageSelector().removeAllItems();
        for (int i = 1; i <= totalPages; i++) {
            studentTable.getPageSelector().addItem(String.valueOf(i));
        }

        return totalPages;
    }

    private int searchFieldBar() {
        searchText = studentTable.getSearchInputField().getText().strip();
        if (searchText.isEmpty()) {
            sortSettings();
            pageSelectorComboBox();
            return getTotalPages();
        }

        int totalMatches = countSearchResults(searchText);
        studentTable.setCounterLabel("Students Found: " + totalMatches);

        // 2) Populate the page selector based on match count
        int totalPages = getTotalSearchPages(totalMatches);

        // 3) Fetch and display the current page
        fetchSearchPage(searchText, currentPage, rowsPerPage);

        return totalPages;
    }

    private int countSearchResults(String searchText) {
        String countQuery
                = "SELECT COUNT(*) FROM studentTable sT "
                + "JOIN collegesTable cT ON sT.collegeCode = cT.collegeCode "
                + "JOIN programsTable pT ON sT.programCode = pT.programCode "
                + "WHERE sT.firstName LIKE ? OR sT.lastName LIKE ? OR sT.idNumber LIKE ? OR "
                + "pT.programName LIKE ? OR cT.collegeName LIKE ? OR sT.collegeCode LIKE ? OR "
                + "sT.programCode LIKE ? OR sT.gender = ?";

        try (PreparedStatement createPreparedStatement = connectionAttempt.prepareStatement(countQuery)) {
            String pattern = "%" + searchText + "%";
            for (int i = 1; i <= 7; i++) {
                createPreparedStatement.setString(i, pattern);
            }
            createPreparedStatement.setString(8, searchText);
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

        boolean isFullId = searchText.matches("\\d{4}-\\d{4}");
        boolean isNumeric = searchText.matches("\\d+");
        boolean isSingleDigit = isNumeric && searchText.length() == 1;

        String query;

        if (isFullId) {
            query = "SELECT sT.idNumber, sT.firstName, sT.middleName, sT.lastName, "
                    + "sT.gender, sT.yearLevel, cT.collegeName, pT.programName "
                    + "FROM studentTable sT "
                    + "JOIN collegesTable cT ON sT.collegeCode = cT.collegeCode "
                    + "JOIN programsTable pT ON sT.programCode = pT.programCode "
                    + "WHERE sT.idNumber = ? "
                    + "LIMIT " + rowsPerPage + " OFFSET " + offset;
        } else if (isSingleDigit) {
            int searchDigit = Integer.parseInt(searchText);

            if (searchDigit > 4) {
                // If the digit is greater than 4, search in idNumber
                query = "SELECT sT.idNumber, sT.firstName, sT.middleName, sT.lastName, "
                        + "sT.gender, sT.yearLevel, cT.collegeName, pT.programName "
                        + "FROM studentTable sT "
                        + "JOIN collegesTable cT ON sT.collegeCode = cT.collegeCode "
                        + "JOIN programsTable pT ON sT.programCode = pT.programCode "
                        + "WHERE sT.idNumber LIKE ? "
                        + "LIMIT " + rowsPerPage + " OFFSET " + offset;
            } else if (searchDigit > 0) {
                // If the digit is 1 to 4, search in yearLevel
                query = "SELECT sT.idNumber, sT.firstName, sT.middleName, sT.lastName, "
                        + "sT.gender, sT.yearLevel, cT.collegeName, pT.programName "
                        + "FROM studentTable sT "
                        + "JOIN collegesTable cT ON sT.collegeCode = cT.collegeCode "
                        + "JOIN programsTable pT ON sT.programCode = pT.programCode "
                        + "WHERE sT.yearLevel = ? "
                        + "LIMIT " + rowsPerPage + " OFFSET " + offset;
            } else {
                // If the digit is 0, treat it as a non-numeric query
                query = "SELECT sT.idNumber, sT.firstName, sT.middleName, sT.lastName, "
                        + "sT.gender, sT.yearLevel, cT.collegeName, pT.programName "
                        + "FROM studentTable sT "
                        + "JOIN collegesTable cT ON sT.collegeCode = cT.collegeCode "
                        + "JOIN programsTable pT ON sT.programCode = pT.programCode "
                        + "WHERE sT.firstName LIKE ? OR sT.lastName LIKE ? OR sT.middleName LIKE ? OR "
                        + "sT.idNumber LIKE ? OR pT.programName LIKE ? OR cT.collegeName LIKE ? OR "
                        + "sT.collegeCode LIKE ? OR sT.programCode LIKE ? OR sT.gender = ? "
                        + "LIMIT " + rowsPerPage + " OFFSET " + offset;
            }
        } else if (isNumeric) {
            query = "SELECT sT.idNumber, sT.firstName, sT.middleName, sT.lastName, "
                    + "sT.gender, sT.yearLevel, cT.collegeName, pT.programName "
                    + "FROM studentTable sT "
                    + "JOIN collegesTable cT ON sT.collegeCode = cT.collegeCode "
                    + "JOIN programsTable pT ON sT.programCode = pT.programCode "
                    + "WHERE sT.yearLevel = ? OR sT.idNumber LIKE ? OR sT.middleName LIKE ? "
                    + "LIMIT " + rowsPerPage + " OFFSET " + offset;
        } else {
            query = "SELECT sT.idNumber, sT.firstName, sT.middleName, sT.lastName, "
                    + "sT.gender, sT.yearLevel, cT.collegeName, pT.programName "
                    + "FROM studentTable sT "
                    + "JOIN collegesTable cT ON sT.collegeCode = cT.collegeCode "
                    + "JOIN programsTable pT ON sT.programCode = pT.programCode "
                    + "WHERE sT.firstName LIKE ? OR sT.lastName LIKE ? OR sT.middleName LIKE ? OR "
                    + "sT.idNumber LIKE ? OR pT.programName LIKE ? OR cT.collegeName LIKE ? OR "
                    + "sT.collegeCode LIKE ? OR sT.programCode LIKE ? OR sT.gender = ? "
                    + "LIMIT " + rowsPerPage + " OFFSET " + offset;
        }

        DefaultTableModel model = (DefaultTableModel) studentTable.getTemplateTable().getModel();
        model.setRowCount(0);

        try (PreparedStatement createPreparedStatement = connectionAttempt.prepareStatement(query)) {
            if (isFullId) {
                createPreparedStatement.setString(1, searchText);
            } else if (isSingleDigit) {
                int searchDigit = Integer.parseInt(searchText);
                if (searchDigit > 4) {
                    createPreparedStatement.setString(1, "%" + searchText + "%");
                } else if (searchDigit > 0) {
                    createPreparedStatement.setInt(1, searchDigit);
                } else {

                    String pattern = "%" + searchText + "%";
                    for (int i = 1; i <= 8; i++) {
                        createPreparedStatement.setString(i, pattern);
                    }
                    createPreparedStatement.setString(9, searchText);
                }
            } else if (isNumeric) {
                createPreparedStatement.setString(1, searchText);
                createPreparedStatement.setString(2, "%" + searchText + "%");
                createPreparedStatement.setString(3, "%" + searchText + "%");
            } else {
                String pattern = "%" + searchText + "%";
                for (int i = 1; i <= 8; i++) {
                    createPreparedStatement.setString(i, pattern);
                }
                createPreparedStatement.setString(9, searchText);
            }

            try (ResultSet rs = createPreparedStatement.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString("idNumber"),
                        rs.getString("firstName"),
                        rs.getString("middleName"),
                        rs.getString("lastName"),
                        rs.getString("gender"),
                        rs.getString("yearLevel"),
                        rs.getString("collegeName"),
                        rs.getString("programName")
                    });
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error (Fetch): " + e.getMessage());
        }
    }

}

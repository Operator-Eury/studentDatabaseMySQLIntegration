/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package functions;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javaForms.templatePaginatedTableForms;
import javaForms.dashboardFrame;
import javaForms.templateFeedbackModalForms;
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
public class collegesTable {

    private int startingPage = 1;
    private static final int rowsPerPage = 45;
    templatePaginatedTableForms collegeTable = new templatePaginatedTableForms();

    private String searchText = collegeTable.getSearchInputField().getText().strip();

    Connection connectionAttempt = databaseConnector.getConnection();

    private void startComponents() {
        fillTable();
        sortSettings();
        getTotalPages();
        pageSelectorComboBox();
        collegeTable.getTemplateTable().getTableHeader().repaint();
    }

    private void refreshTable() {
        startComponents();
    }

    public templatePaginatedTableForms showTable() {

        collegeTable.setUpdateButton("Update College");
        collegeTable.setCreateButton("Register College");

        collegeTable.getNewItem().setText("Register College");

        collegeTable.getGroupOptionsComboBox().removeAllItems();
        collegeTable.getGroupOptionsComboBox().addItem("College Name");
        collegeTable.getGroupOptionsComboBox().addItem("College Code");

        TableColumnModel columnModel = collegeTable.getTemplateTable().getColumnModel();

        int columnCount = columnModel.getColumnCount();
        for (int i = columnCount - 1; i >= 2; i--) {
            columnModel.removeColumn(columnModel.getColumn(i));
        }

        columnModel.getColumn(0).setHeaderValue("College Code");
        columnModel.getColumn(0).setPreferredWidth(10);

        columnModel.getColumn(1).setHeaderValue("College Name");
        columnModel.getColumn(1).setPreferredWidth(150);

        startComponents();

        collegeTable.getSortingArrangements().addItemListener(e -> {
            sortSettings();
        });

        collegeTable.getGroupOptionsComboBox().addItemListener(e -> {
            sortSettings();
        });

        collegeTable.getPageSelector().addItemListener((ItemEvent e) -> {
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

        collegeTable.getSearchInputField().getDocument().addDocumentListener(new DocumentListener() {
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

        collegeTable.getCreateButton().addActionListener(e -> {
            showFormDialog(e);
        });

        collegeTable.getUpdateButton().addActionListener(e -> {
            showFormDialog(e);
        });

        collegeTable.getAcceptButton().addActionListener(e -> {

            String collegeCode = collegeTable.getItemCode().getText().toUpperCase().strip();
            String collegeName = collegeTable.getItemName().getText().strip();

            if (collegeTable.getAcceptButton().getText().equalsIgnoreCase("Register College")) {
                evaluateForm(collegeCode, collegeName, false);
            } else {
                evaluateForm(collegeCode, collegeName, true);
            }

        });

        return collegeTable;

    }

    private void showFormDialog(ActionEvent event) {

        Object source = event.getSource();

        collegeTable.getDeclineButton().addActionListener(e -> {
            collegeTable.getItemCode().setText("");
            collegeTable.getItemName().setText("");
            collegeTable.getModalMenu().dispose();
        });

        if (source == collegeTable.getCreateButton()) {

            collegeTable.getModalMenu().setLocationRelativeTo(dashboardFrame.getInstance());
            collegeTable.getModalMenu().setResizable(false);
            collegeTable.getModalMenu().setTitle("Registering a College");
            collegeTable.getItemCodeLabel().setText("College Code: ");
            collegeTable.getItemNameLabel().setText("College Name");
            collegeTable.getCollegeCodeComboBoxLabel().setVisible(false);
            collegeTable.getCollegeComboBox().setVisible(false);
            collegeTable.getHeaderLabel().setText("LUPINBRIDGE COLLEGE REGISTRATION");
            collegeTable.getAcceptButton().setText("Register College");
            collegeTable.getAcceptButton().setBackground(new Color(153, 255, 153));
            collegeTable.getDeclineButton().setText("Discard Progress");

            ImageIcon newIcon = new ImageIcon(getClass().getResource("/resources/images/add.png"));
            collegeTable.getAcceptButton().setIcon(newIcon);

            collegeTable.getModalMenu().setVisible(true);

        }

        if (source == collegeTable.getUpdateButton()) {
            collegeTable.getModalMenu().setLocationRelativeTo(dashboardFrame.getInstance());
            collegeTable.getModalMenu().setResizable(false);
            collegeTable.getModalMenu().setTitle("Updating a College");
            collegeTable.getItemCodeLabel().setText("College Code: ");
            collegeTable.getItemNameLabel().setText("College Name");
            collegeTable.getCollegeCodeComboBoxLabel().setVisible(false);
            collegeTable.getCollegeComboBox().setVisible(false);
            collegeTable.getHeaderLabel().setText("LUPINBRIDGE UPDATE COLLEGE");
            collegeTable.getAcceptButton().setText("Update College");
            collegeTable.getAcceptButton().setBackground(new Color(0, 180, 255));
            collegeTable.getDeclineButton().setText("Discard Progress");

            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/resources/images/updated.png"));
            collegeTable.getAcceptButton().setIcon(originalIcon);

            collegeTable.getModalMenu().setVisible(true);
        }

    }

    private void evaluateForm(String collegeCode, String collegeName, boolean isUpdate) {

        StringBuilder errors = new StringBuilder();
        boolean hasError;

        collegeCode = collegeCode.strip();
        collegeName = collegeName.strip();

        if (collegeCode.isBlank()) {
            errors.append("College Code cannot be Empty\n");
        }

        if (collegeName.isBlank()) {
            errors.append("College Name cannot be Empty\n");
        }

        if (!isUpdate) {
            if (isCollegeCodeUnique(collegeCode) == false) {
                errors.append("That College Code's taken!\n");
            }
        }

        if (!errors.isEmpty()) {

            System.err.println(errors.toString());
            hasError = true;

        } else {

            hasError = false;

        }

        processCollege(collegeCode, collegeName, isUpdate, hasError, errors.toString());

    }

    private boolean isCollegeCodeUnique(String collegeCode) {
        String query = "SELECT COUNT(*) FROM collegesTable WHERE collegeCode = ?";

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

    private void processCollege(String collegeCode, String collegeName, boolean isUpdate, boolean hasError, String errorList) {

        templateFeedbackModalForms newFeedback = new templateFeedbackModalForms(dashboardFrame.getInstance(), true);
        newFeedback.getDeclineButton().setVisible(false);
        newFeedback.setLocationRelativeTo(dashboardFrame.getInstance());

        newFeedback.getConfirmButton().addActionListener(e -> {
            if(!hasError) {
            collegeTable.getModalMenu().dispose();
            collegeTable.getItemCode().setText("");
            collegeTable.getItemName().setText("");
            }
            refreshTable();
            newFeedback.dispose();
        });

        String INSERTQUERY = "INSERT INTO collegesTable (collegeCode, collegeName)"
                + " VALUES (?, ?)";

        if (!hasError) {

            if (isUpdate == false) {

                try (PreparedStatement createStatement = connectionAttempt.prepareStatement(INSERTQUERY)) {

                    createStatement.setString(1, collegeCode);
                    createStatement.setString(2, collegeName);

                    int rowsAffected = createStatement.executeUpdate();

                    if (rowsAffected > 0) {

                        System.out.println("Registration Done");
                        collegeTable.getModalMenu().setTitle("College Registered");
                        newFeedback.getFeedbackTextPane().setText("College successfully registered!");

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

                String errorConclusion = "College Registration could not be completed due to following error(s):\n\n" + errorList;
                newFeedback.setTitle("College Registration did not proceed due to following error(s)");
                newFeedback.getFeedbackTextPane().setText(errorConclusion);
                newFeedback.setVisible(true);

            } else {

                String errorConclusion = "College Update could not be completed due to following error(s):\n\n" + errorList;
                newFeedback.setTitle("College Update did not proceed due to following error(s)");
                newFeedback.getFeedbackTextPane().setText(errorConclusion);
                newFeedback.setVisible(true);

            }
        }

    }

    private void fillTable() {
        DefaultTableModel model = (DefaultTableModel) collegeTable.getTemplateTable().getModel();
        model.setRowCount(0);

        if (connectionAttempt != null) {
            sortSettings();
        } else {
            System.err.println("Failed to connect to the database.");
        }

    }

    private templatePaginatedTableForms sortSettings() {
        searchText = collegeTable.getSearchInputField().getText().strip();
        if (!searchText.isEmpty()) {
            return collegeTable;
        }

        String groupOptions = collegeTable.getGroupOptionsComboBox().getSelectedItem().toString();
        String sortArrangements = collegeTable.getSortingArrangements().getSelectedItem().toString();
        String sortOrder = sortArrangements.equalsIgnoreCase("Ascending") ? "ASC" : "DESC";
        int selectedPage = (startingPage - 1) * rowsPerPage;

        String currentPageQuery = " LIMIT " + rowsPerPage + " OFFSET " + selectedPage;

        String QUERY = "SELECT cT.collegeCode, cT.collegeName"
                + " FROM collegesTable cT";

        switch (groupOptions.toUpperCase()) {

            case "COLLEGE CODE" ->
                QUERY += " ORDER BY cT.collegeCode " + sortOrder + currentPageQuery;

            case "COLLEGE NAME" ->
                QUERY += " ORDER BY cT.collegeName " + sortOrder + currentPageQuery;

            default ->
                QUERY += " ORDER BY cT.collegeCode " + sortOrder + currentPageQuery;
        }

        DefaultTableModel model = (DefaultTableModel) collegeTable.getTemplateTable().getModel();
        model.setRowCount(0);

        if (connectionAttempt != null) {
            try (Statement createStatement = connectionAttempt.createStatement(); ResultSet createResult = createStatement.executeQuery(QUERY)) {

                while (createResult.next()) {

                    String collegeCode = createResult.getString("collegeCode");
                    String collegeName = createResult.getString("collegeName");

                    model.addRow(new Object[]{collegeCode, collegeName});

                }
            } catch (SQLException error) {
                System.err.println("SQL Error: " + error.getMessage());
            }
        }
        setCounter();
        collegeTable.getTemplateTable().getTableHeader().repaint();

        return collegeTable;
    }

    private void setCounter() {
        String QUERY = "SELECT COUNT(*) FROM collegesTable";

        try (Statement createStatement = connectionAttempt.createStatement(); ResultSet createResult = createStatement.executeQuery(QUERY)) {
            if (createResult.next()) {
                int rowCount = createResult.getInt(1);
                collegeTable.setCounterLabel("Colleges Found: " + String.valueOf(rowCount));
            }
        } catch (SQLException error) {
            System.err.println("SQL Error: " + error.getMessage());
        }
    }

    private void pageSelectorComboBox() {

        searchText = collegeTable.getSearchInputField().getText().strip();
        if (searchText.isEmpty()) {
            collegeTable.getPageSelector().removeAllItems();
            for (int i = 1; i <= getTotalPages(); i++) {
                collegeTable.getPageSelector().addItem(String.valueOf(i));
            }
        }

    }

    private int getTotalPages() {

        searchText = collegeTable.getSearchInputField().getText().strip();

        if (!searchText.isEmpty()) {

            return 0;
        }

        int totalRows = getTotalRows();
        int totalPages = (int) Math.ceil(totalRows / (double) rowsPerPage);
        collegeTable.setTotalPageLabel("out of " + String.valueOf(totalPages));

        return totalPages;
    }

    private int getTotalRows() {

        String QUERY = "SELECT COUNT(*) FROM collegesTable";
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
        searchText = collegeTable.getSearchInputField().getText().strip();
        if (searchText.isEmpty()) {
            sortSettings();
            pageSelectorComboBox();
            return getTotalPages();
        }

        int totalMatches = countSearchResults(searchText);
        collegeTable.setCounterLabel("Colleges Found: " + totalMatches);
        
        int totalPages = getTotalSearchPages(totalMatches);
        
        fetchSearchPage(searchText, startingPage, rowsPerPage);

        return totalPages;
    }

    private int getTotalSearchPages(int rows) {
        searchText = collegeTable.getSearchInputField().getText().strip();

        if (searchText.isEmpty()) {
            return getTotalPages();
        }

        int totalRows = rows;
        int totalPages = (int) Math.ceil(totalRows / (double) rowsPerPage);

        if (totalPages == 0) {
            totalPages = 1;
        }

        collegeTable.setTotalPageLabel("out of " + String.valueOf(totalPages));

        collegeTable.getPageSelector().removeAllItems();
        for (int i = 1; i <= totalPages; i++) {
            collegeTable.getPageSelector().addItem(String.valueOf(i));
        }

        return totalPages;
    }

    private int countSearchResults(String searchText) {

        String countQuery = "SELECT COUNT(*) FROM collegesTable cT "
                + "WHERE cT.collegeName LIKE ? OR cT.collegeCode LIKE ?";

        try (PreparedStatement createPreparedStatement = connectionAttempt.prepareStatement(countQuery)) {
            String pattern = "%" + searchText + "%";

            createPreparedStatement.setString(1, pattern);
            createPreparedStatement.setString(2, pattern);

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

        String query = "SELECT cT.collegeCode, cT.collegeName "
                + "FROM collegesTable cT "
                + "WHERE cT.collegeName LIKE ? OR cT.collegeCode LIKE ? "
                + "LIMIT " + rowsPerPage + " OFFSET " + offset;

        DefaultTableModel model = (DefaultTableModel) collegeTable.getTemplateTable().getModel();
        model.setRowCount(0);

        try (PreparedStatement createPreparedStatement = connectionAttempt.prepareStatement(query)) {
            String pattern = "%" + searchText + "%";

            createPreparedStatement.setString(1, pattern);
            createPreparedStatement.setString(2, pattern);

            try (ResultSet rs = createPreparedStatement.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString("collegeCode"),
                        rs.getString("collegeName"),});
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error (Fetch): " + e.getMessage());
        }
    }
}

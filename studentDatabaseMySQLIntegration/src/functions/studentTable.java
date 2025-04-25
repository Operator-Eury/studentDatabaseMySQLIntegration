/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package functions;

import java.awt.event.ItemEvent;
import javaForms.templatePaginatedTableForms;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import mySQLQueries.databaseConnector;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author John-Ronan Beira
 */
public class studentTable {

    templatePaginatedTableForms studentTable = new templatePaginatedTableForms();
    Connection connectionAttempt = databaseConnector.getConnection();
    private int currentPage = 1;
    private static final int rowsPerPage = 45;
    private String searchText = studentTable.getSearchInputField().getText().trim();

    public void startComponents() {
        fillTable();
        setCounter();
        getTotalPages();
        pageSelectorComboBox();
        studentTable.getTemplateTable().getTableHeader().repaint();
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

        studentTable.getUpdateButton().addActionListener(e -> {
            System.out.println("Update Button is Clicked");
            studentTable.getTemplateTable().clearSelection();
            studentTable.getTemplateTable().repaint();
            studentTable.getTemplateTable().revalidate();
        });

        studentTable.getCreateButton().addActionListener(e -> {
            System.out.println("Create Button is Clicked");
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
        return studentTable;

    }

    public void fillTable() {
        DefaultTableModel model = (DefaultTableModel) studentTable.getTemplateTable().getModel();
        model.setRowCount(0);

        if (connectionAttempt != null) {
            sortSettings();
        } else {
            System.err.println("Failed to connect to the database.");
        }
    }

    public templatePaginatedTableForms sortSettings() {
        searchText = studentTable.getSearchInputField().getText().trim();
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

    public void pageSelectorComboBox() {

        searchText = studentTable.getSearchInputField().getText().trim();
        if (searchText.isEmpty()) {
            studentTable.getPageSelector().removeAllItems();
            for (int i = 1; i <= getTotalPages(); i++) {
                studentTable.getPageSelector().addItem(String.valueOf(i));
            }
        }

    }

    public void setCounter() {
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

    public int getTotalRows() {

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

    public int getTotalPages() {

        searchText = studentTable.getSearchInputField().getText().trim();

        if (!searchText.isEmpty()) {
            // Skip total count if search is active
            return 0;
        }

        int totalRows = getTotalRows();
        int totalPages = (int) Math.ceil(totalRows / (double) rowsPerPage);
        studentTable.setTotalPageLabel("out of " + String.valueOf(totalPages));

        return totalPages;
    }

    public int getTotalSearchPages(int rows) {
        searchText = studentTable.getSearchInputField().getText().trim();

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

    public int searchFieldBar() {
        searchText = studentTable.getSearchInputField().getText().trim();
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

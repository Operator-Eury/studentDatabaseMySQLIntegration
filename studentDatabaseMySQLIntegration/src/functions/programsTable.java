/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package functions;

import java.awt.event.ItemEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import javaForms.templatePaginatedTableForms;
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

    private int startingPage = 1;
    private static final int rowsPerPage = 45;
    templatePaginatedTableForms programsTable = new templatePaginatedTableForms();

    private String searchText = programsTable.getSearchInputField().getText().trim();

    Connection connectionAttempt = databaseConnector.getConnection();

    private void startComponents() {
        fillTable();
        sortSettings();
        getTotalPages();
        pageSelectorComboBox();
        programsTable.getTemplateTable().getTableHeader().repaint();
    }

    public templatePaginatedTableForms showTable() {

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

        return programsTable;
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

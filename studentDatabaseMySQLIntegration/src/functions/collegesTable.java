/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package functions;

import java.awt.event.ItemEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
public class collegesTable {

    private int startingPage = 1;
    private static final int rowsPerPage = 45;
    templatePaginatedTableForms collegeTable = new templatePaginatedTableForms();

    private String searchText = collegeTable.getSearchInputField().getText().trim();

    Connection connectionAttempt = databaseConnector.getConnection();

    public void startComponents() {
        fillTable();
        sortSettings();
        getTotalPages();
        pageSelectorComboBox();
        collegeTable.getTemplateTable().getTableHeader().repaint();
    }

    public templatePaginatedTableForms showTable() {

        collegeTable.setUpdateButton("Update College");
        collegeTable.setCreateButton("Register College");

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

        return collegeTable;

    }

    public void fillTable() {
        DefaultTableModel model = (DefaultTableModel) collegeTable.getTemplateTable().getModel();
        model.setRowCount(0);

        if (connectionAttempt != null) {
            sortSettings();
        } else {
            System.err.println("Failed to connect to the database.");
        }

    }

    public templatePaginatedTableForms sortSettings() {
        searchText = collegeTable.getSearchInputField().getText().trim();
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

    public void setCounter() {
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

    public void pageSelectorComboBox() {

        searchText = collegeTable.getSearchInputField().getText().trim();
        if (searchText.isEmpty()) {
            collegeTable.getPageSelector().removeAllItems();
            for (int i = 1; i <= getTotalPages(); i++) {
                collegeTable.getPageSelector().addItem(String.valueOf(i));
            }
        }

    }

    public int getTotalPages() {

        searchText = collegeTable.getSearchInputField().getText().trim();

        if (!searchText.isEmpty()) {
            // Skip total count if search is active
            return 0;
        }

        int totalRows = getTotalRows();
        int totalPages = (int) Math.ceil(totalRows / (double) rowsPerPage);
        collegeTable.setTotalPageLabel("out of " + String.valueOf(totalPages));

        return totalPages;
    }

    public int getTotalRows() {

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

    public int searchFieldBar() {
        searchText = collegeTable.getSearchInputField().getText().trim();
        if (searchText.isEmpty()) {
            sortSettings();
            pageSelectorComboBox();
            return getTotalPages();
        }

        int totalMatches = countSearchResults(searchText);
        collegeTable.setCounterLabel("Colleges Found: " + totalMatches);

        // 2) Populate the page selector based on match count
        int totalPages = getTotalSearchPages(totalMatches);

        // 3) Fetch and display the current page
        fetchSearchPage(searchText, startingPage, rowsPerPage);

        return totalPages;
    }

    public int getTotalSearchPages(int rows) {
        searchText = collegeTable.getSearchInputField().getText().trim();

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

            // Bind the search text to each of the four fields
            createPreparedStatement.setString(1, pattern); // collegeName
            createPreparedStatement.setString(2, pattern); // collegeCode

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

        // Fuzzy search query for collegeName, and collegeCode
        String query = "SELECT cT.collegeCode, cT.collegeName "
                + "FROM collegesTable cT "
                + "WHERE cT.collegeName LIKE ? OR cT.collegeCode LIKE ? "
                + "LIMIT " + rowsPerPage + " OFFSET " + offset;

        DefaultTableModel model = (DefaultTableModel) collegeTable.getTemplateTable().getModel();
        model.setRowCount(0);

        try (PreparedStatement createPreparedStatement = connectionAttempt.prepareStatement(query)) {
            String pattern = "%" + searchText + "%";

            // Bind the search text to each of the four fields
            createPreparedStatement.setString(1, pattern); // collegeName
            createPreparedStatement.setString(2, pattern); // collegeCode

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

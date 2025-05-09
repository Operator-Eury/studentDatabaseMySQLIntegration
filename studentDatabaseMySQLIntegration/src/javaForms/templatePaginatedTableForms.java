/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package javaForms;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author John-Ronan Beira
 */
public class templatePaginatedTableForms extends javax.swing.JPanel {

    /**
     * Creates new form templatePaginatedTableForm
     */
    public templatePaginatedTableForms() {
        initComponents();
        centerTable();
        setupEscapeKeyToClearSelection(templateTable);

        URL iconUrl = getClass().getResource("/resources/images/tabIcon.png");
        ImageIcon logo = new ImageIcon(iconUrl);
        modalMenu.setIconImage(logo.getImage());

        contextMenu.add(editItem);
        contextMenu.add(deleteItem);

        templateTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handlePopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                handlePopup(e);
            }
        });
    }

    private void handlePopup(MouseEvent event) {
        if (event.isPopupTrigger()) {

            if (templateTable.getRowCount() == 0) {
                return;
            }

            int clickedRow = templateTable.rowAtPoint(event.getPoint());

            if (clickedRow >= 0 && clickedRow < templateTable.getRowCount()) {

                if (templateTable.getSelectedRowCount() == 0) {
                    templateTable.setRowSelectionInterval(clickedRow, clickedRow);
                }

            } else {

                if (templateTable.getSelectedRowCount() == 0) {
                    templateTable.setRowSelectionInterval(0, 0);
                }

            }

            templateTable.requestFocusInWindow();
            contextMenu.show(event.getComponent(), event.getX(), event.getY());
        }
    }

    private void setupEscapeKeyToClearSelection(JTable table) {
        InputMap inputMap = table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap actionMap = table.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "clearSelection");

        actionMap.put("clearSelection", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                table.clearSelection();

                if (table.isEditing()) {
                    table.getCellEditor().cancelCellEditing();
                }

                table.getParent().requestFocusInWindow();

            }
        });
    }

    protected void centerTable() {
        TableCellRenderer centerAndStripeRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                // Center text
                setHorizontalAlignment(CENTER);

                // Apply alternating row colors
                if (!isSelected) {
                    c.setBackground((row % 2 == 0)
                            ? new Color(225, 250, 245)
                            : new Color(220, 235, 250));
                } else {
                    c.setBackground(table.getSelectionBackground());
                }

                return c;
            }
        };

        for (int i = 0; i < templateTable.getColumnCount(); i++) {
            templateTable.getColumnModel().getColumn(i)
                    .setCellRenderer(centerAndStripeRenderer);
        }
    }

    // Setters
    public void setCounterLabel(String text) {
        counterLabel.setText(text);
    }

    public void setTotalPageLabel(String text) {
        totalPageLabel.setText(text);
    }

    public void setUpdateButton(String text) {
        updateButton.setText(text);
    }

    public void setCreateButton(String text) {
        createButton.setText(text);
    }

    // Getters
    public String getCounterLabel() {
        return counterLabel.getText();
    }

    public javax.swing.JLabel getTotalPageLabel() {
        return totalPageLabel;
    }

    public javax.swing.JButton getUpdateButton() {
        return updateButton;
    }

    public javax.swing.JButton getCreateButton() {
        return createButton;
    }

    public javax.swing.JTable getTemplateTable() {
        return templateTable;
    }

    public javax.swing.JComboBox getGroupOptionsComboBox() {
        return groupOptionsComboBox;
    }

    public javax.swing.JComboBox getSortingArrangements() {
        return sortingArrangements;
    }

    public javax.swing.JComboBox getPageSelector() {
        return pageSelector;
    }

    public javax.swing.JTextField getSearchInputField() {
        return searchInputField;
    }

    public javax.swing.JMenuItem getEditItem() {
        return editItem;
    }

    public javax.swing.JMenuItem getDeleteItem() {
        return deleteItem;
    }

    public javax.swing.JMenuItem getNewItem() {
        return newItem;
    }

    public javax.swing.JTextField getItemCode() {
        return itemCode;
    }

    public javax.swing.JLabel getItemCodeLabel() {
        return itemCodeLabel;
    }

    public javax.swing.JTextField getItemName() {
        return itemName;
    }

    public javax.swing.JLabel getItemNameLabel() {
        return itemNameLabel;
    }

    public javax.swing.JComboBox getCollegeComboBox() {
        return collegeComboBox;
    }

    public javax.swing.JLabel getCollegeCodeComboBoxLabel() {
        return collegeCodeComboBoxLabel;
    }

    public javax.swing.JButton getAcceptButton() {
        return acceptButton;
    }

    public javax.swing.JButton getDeclineButton() {
        return declineButton;
    }

    public javax.swing.JDialog getModalMenu() {
        return modalMenu;
    }
    
    public javax.swing.JLabel getHeaderLabel() {
        return headerLabel;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        contextMenu = new javax.swing.JPopupMenu();
        deleteItem = new javax.swing.JMenuItem();
        editItem = new javax.swing.JMenuItem();
        newItem = new javax.swing.JMenuItem();
        modalMenu = new javax.swing.JDialog();
        formPanel = new javax.swing.JPanel();
        itemCode = new javax.swing.JTextField();
        itemName = new javax.swing.JTextField();
        itemCodeLabel = new javax.swing.JLabel();
        itemNameLabel = new javax.swing.JLabel();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        jPanel1 = new javax.swing.JPanel();
        headerLabel = new javax.swing.JLabel();
        // Resize the icon before setting it
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/resources/images/dog.png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImage);
        jPanel2 = new javax.swing.JPanel();
        collegeCodeComboBoxLabel = new javax.swing.JLabel();
        collegeComboBox = new javax.swing.JComboBox<>();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        filler8 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        buttonPanel = new javax.swing.JPanel();
        acceptButton = new javax.swing.JButton();
        declineButton = new javax.swing.JButton();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        operationsPanel = new javax.swing.JPanel();
        sortByOptions = new javax.swing.JLabel();
        groupOptionsComboBox = new javax.swing.JComboBox<>();
        sortOptions = new javax.swing.JLabel();
        sortingArrangements = new javax.swing.JComboBox<>();
        searchInput = new javax.swing.JLabel();
        searchInputField = new javax.swing.JTextField();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        jSeparator9 = new javax.swing.JSeparator();
        jSeparator1 = new javax.swing.JSeparator();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(1, 0), new java.awt.Dimension(1, 0), new java.awt.Dimension(1, 32767));
        jSeparator2 = new javax.swing.JSeparator();
        jTablePanel = new javax.swing.JPanel();
        templateScrollPanel = new javax.swing.JScrollPane();
        templateTable = new javax.swing.JTable();
        contextOperationsPanel = new javax.swing.JPanel();
        counterLabel = new javax.swing.JLabel();
        jSeparator13 = new javax.swing.JSeparator();
        jSeparator14 = new javax.swing.JSeparator();
        jSeparator15 = new javax.swing.JSeparator();
        jSeparator16 = new javax.swing.JSeparator();
        updateButton = new javax.swing.JButton();
        createButton = new javax.swing.JButton();
        currentPageLabel = new javax.swing.JLabel();
        pageSelector = new javax.swing.JComboBox<>();
        totalPageLabel = new javax.swing.JLabel();
        jSeparator17 = new javax.swing.JSeparator();
        jSeparator18 = new javax.swing.JSeparator();
        jSeparator19 = new javax.swing.JSeparator();
        jSeparator20 = new javax.swing.JSeparator();
        jSeparator21 = new javax.swing.JSeparator();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(1, 0), new java.awt.Dimension(1, 0), new java.awt.Dimension(1, 32767));
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(1, 0), new java.awt.Dimension(1, 0), new java.awt.Dimension(1, 32767));

        contextMenu.setFont(contextMenu.getFont().deriveFont(contextMenu.getFont().getStyle() | java.awt.Font.BOLD));
        contextMenu.setComponentPopupMenu(contextMenu);
        contextMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        deleteItem.setFont(deleteItem.getFont().deriveFont(deleteItem.getFont().getStyle() | java.awt.Font.BOLD));
        deleteItem.setForeground(new java.awt.Color(255, 51, 51));
        deleteItem.setText("Delete");
        contextMenu.add(deleteItem);

        editItem.setFont(editItem.getFont().deriveFont(editItem.getFont().getStyle() | java.awt.Font.BOLD));
        editItem.setForeground(new java.awt.Color(0, 180, 255));
        editItem.setText("Edit");
        contextMenu.add(editItem);

        newItem.setFont(newItem.getFont().deriveFont(newItem.getFont().getStyle() | java.awt.Font.BOLD));
        newItem.setForeground(new java.awt.Color(0, 204, 102));
        newItem.setText("Enroll");
        contextMenu.add(newItem);

        modalMenu.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        modalMenu.setIconImage(null);
        modalMenu.setIconImages(null);
        modalMenu.setMinimumSize(new java.awt.Dimension(700, 450));
        modalMenu.setModal(true);
        modalMenu.setPreferredSize(new java.awt.Dimension(700, 450));
        modalMenu.setResizable(false);
        modalMenu.getContentPane().setLayout(new java.awt.GridBagLayout());

        formPanel.setLayout(new java.awt.GridBagLayout());

        itemCode.setColumns(20);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        formPanel.add(itemCode, gridBagConstraints);

        itemName.setColumns(20);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        formPanel.add(itemName, gridBagConstraints);

        itemCodeLabel.setText("jLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        formPanel.add(itemCodeLabel, gridBagConstraints);

        itemNameLabel.setText("jLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        formPanel.add(itemNameLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weighty = 0.1;
        formPanel.add(filler6, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.weighty = 0.1;
        formPanel.add(filler7, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        headerLabel.setFont(headerLabel.getFont().deriveFont(headerLabel.getFont().getStyle() | java.awt.Font.BOLD, headerLabel.getFont().getSize()+12));
        headerLabel.setText("jLabel1");
        headerLabel.setIcon(resizedIcon);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel1.add(headerLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        formPanel.add(jPanel1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        collegeCodeComboBoxLabel.setText("jLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(collegeCodeComboBoxLabel, gridBagConstraints);

        collegeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select College" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(collegeComboBox, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.1;
        jPanel2.add(filler5, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.1;
        jPanel2.add(filler8, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        formPanel.add(jPanel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 0.1;
        modalMenu.getContentPane().add(formPanel, gridBagConstraints);

        buttonPanel.setLayout(new java.awt.GridBagLayout());

        acceptButton.setBackground(new java.awt.Color(153, 255, 153));
        acceptButton.setFont(acceptButton.getFont().deriveFont(acceptButton.getFont().getStyle() | java.awt.Font.BOLD));
        acceptButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/add.png"))); // NOI18N
        acceptButton.setText("jButton1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        buttonPanel.add(acceptButton, gridBagConstraints);

        declineButton.setBackground(new java.awt.Color(255, 153, 153));
        declineButton.setFont(declineButton.getFont().deriveFont(declineButton.getFont().getStyle() | java.awt.Font.BOLD));
        declineButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/delivery-box.png"))); // NOI18N
        declineButton.setText("jButton2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        buttonPanel.add(declineButton, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        buttonPanel.add(filler4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        modalMenu.getContentPane().add(buttonPanel, gridBagConstraints);

        setMinimumSize(new java.awt.Dimension(826, 522));
        setPreferredSize(new java.awt.Dimension(826, 522));
        setLayout(new java.awt.GridBagLayout());

        operationsPanel.setLayout(new java.awt.GridBagLayout());

        sortByOptions.setText("Sorting Options:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        operationsPanel.add(sortByOptions, gridBagConstraints);

        groupOptionsComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Group Options" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        operationsPanel.add(groupOptionsComboBox, gridBagConstraints);

        sortOptions.setText("Sort By:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        operationsPanel.add(sortOptions, gridBagConstraints);

        sortingArrangements.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ascending", "Descending" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        operationsPanel.add(sortingArrangements, gridBagConstraints);

        searchInput.setText("Search:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        operationsPanel.add(searchInput, gridBagConstraints);

        searchInputField.setColumns(9);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.2;
        operationsPanel.add(searchInputField, gridBagConstraints);

        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator5.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        operationsPanel.add(jSeparator5, gridBagConstraints);

        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        operationsPanel.add(jSeparator6, gridBagConstraints);

        jSeparator7.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        operationsPanel.add(jSeparator7, gridBagConstraints);

        jSeparator8.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        operationsPanel.add(jSeparator8, gridBagConstraints);

        jSeparator9.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 0;
        operationsPanel.add(jSeparator9, gridBagConstraints);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        operationsPanel.add(jSeparator1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        operationsPanel.add(filler3, gridBagConstraints);

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        operationsPanel.add(jSeparator2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(operationsPanel, gridBagConstraints);

        jTablePanel.setLayout(new java.awt.GridLayout(1, 0));

        templateScrollPanel.setToolTipText("");
        templateScrollPanel.setDoubleBuffered(true);

        templateTable.setFont(templateTable.getFont());
        templateTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "HEADER 1", "HEADER 2", "HEADER 3", "HEADER 4", "HEADER  5", "HEADER 6", "HEADER 7", "HEADER 8"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        templateTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        templateTable.setFillsViewportHeight(true);
        templateTable.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        templateTable.setShowGrid(true);
        templateTable.getTableHeader().setResizingAllowed(false);
        templateTable.getTableHeader().setReorderingAllowed(false);
        templateScrollPanel.setViewportView(templateTable);
        templateTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        jTablePanel.add(templateScrollPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 1.0;
        add(jTablePanel, gridBagConstraints);

        contextOperationsPanel.setLayout(new java.awt.GridBagLayout());

        counterLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/tally-marks.png"))); // NOI18N
        counterLabel.setText("counterLabel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        contextOperationsPanel.add(counterLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        contextOperationsPanel.add(jSeparator13, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        contextOperationsPanel.add(jSeparator14, gridBagConstraints);

        jSeparator15.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        contextOperationsPanel.add(jSeparator15, gridBagConstraints);

        jSeparator16.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 14;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        contextOperationsPanel.add(jSeparator16, gridBagConstraints);

        updateButton.setBackground(new java.awt.Color(0, 180, 255));
        updateButton.setFont(updateButton.getFont().deriveFont(updateButton.getFont().getStyle() | java.awt.Font.BOLD));
        updateButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/updated.png"))); // NOI18N
        updateButton.setText("updateButton");
        updateButton.setDoubleBuffered(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 1;
        contextOperationsPanel.add(updateButton, gridBagConstraints);

        createButton.setBackground(new java.awt.Color(153, 255, 153));
        createButton.setFont(createButton.getFont().deriveFont(createButton.getFont().getStyle() | java.awt.Font.BOLD));
        createButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/add.png"))); // NOI18N
        createButton.setText("createButton");
        createButton.setDoubleBuffered(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 1;
        contextOperationsPanel.add(createButton, gridBagConstraints);

        currentPageLabel.setText("Page");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        contextOperationsPanel.add(currentPageLabel, gridBagConstraints);

        pageSelector.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        contextOperationsPanel.add(pageSelector, gridBagConstraints);

        totalPageLabel.setText("totalPageLabel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 1;
        contextOperationsPanel.add(totalPageLabel, gridBagConstraints);

        jSeparator17.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        contextOperationsPanel.add(jSeparator17, gridBagConstraints);

        jSeparator18.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        contextOperationsPanel.add(jSeparator18, gridBagConstraints);

        jSeparator19.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        contextOperationsPanel.add(jSeparator19, gridBagConstraints);

        jSeparator20.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 1;
        contextOperationsPanel.add(jSeparator20, gridBagConstraints);

        jSeparator21.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 1;
        contextOperationsPanel.add(jSeparator21, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 0.1;
        contextOperationsPanel.add(filler1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 0.1;
        contextOperationsPanel.add(filler2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(contextOperationsPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JButton acceptButton;
    protected javax.swing.JPanel buttonPanel;
    protected javax.swing.JLabel collegeCodeComboBoxLabel;
    protected javax.swing.JComboBox<String> collegeComboBox;
    protected javax.swing.JPopupMenu contextMenu;
    protected javax.swing.JPanel contextOperationsPanel;
    protected javax.swing.JLabel counterLabel;
    protected javax.swing.JButton createButton;
    protected javax.swing.JLabel currentPageLabel;
    protected javax.swing.JButton declineButton;
    protected javax.swing.JMenuItem deleteItem;
    protected javax.swing.JMenuItem editItem;
    protected javax.swing.Box.Filler filler1;
    protected javax.swing.Box.Filler filler2;
    protected javax.swing.Box.Filler filler3;
    protected javax.swing.Box.Filler filler4;
    protected javax.swing.Box.Filler filler5;
    protected javax.swing.Box.Filler filler6;
    protected javax.swing.Box.Filler filler7;
    protected javax.swing.Box.Filler filler8;
    protected javax.swing.JPanel formPanel;
    protected javax.swing.JComboBox<String> groupOptionsComboBox;
    protected javax.swing.JLabel headerLabel;
    protected javax.swing.JTextField itemCode;
    protected javax.swing.JLabel itemCodeLabel;
    protected javax.swing.JTextField itemName;
    protected javax.swing.JLabel itemNameLabel;
    protected javax.swing.JPanel jPanel1;
    protected javax.swing.JPanel jPanel2;
    protected javax.swing.JSeparator jSeparator1;
    protected javax.swing.JSeparator jSeparator13;
    protected javax.swing.JSeparator jSeparator14;
    protected javax.swing.JSeparator jSeparator15;
    protected javax.swing.JSeparator jSeparator16;
    protected javax.swing.JSeparator jSeparator17;
    protected javax.swing.JSeparator jSeparator18;
    protected javax.swing.JSeparator jSeparator19;
    protected javax.swing.JSeparator jSeparator2;
    protected javax.swing.JSeparator jSeparator20;
    protected javax.swing.JSeparator jSeparator21;
    protected javax.swing.JSeparator jSeparator5;
    protected javax.swing.JSeparator jSeparator6;
    protected javax.swing.JSeparator jSeparator7;
    protected javax.swing.JSeparator jSeparator8;
    protected javax.swing.JSeparator jSeparator9;
    protected javax.swing.JPanel jTablePanel;
    protected javax.swing.JDialog modalMenu;
    protected javax.swing.JMenuItem newItem;
    protected javax.swing.JPanel operationsPanel;
    protected javax.swing.JComboBox<String> pageSelector;
    protected javax.swing.JLabel searchInput;
    protected javax.swing.JTextField searchInputField;
    protected javax.swing.JLabel sortByOptions;
    protected javax.swing.JLabel sortOptions;
    protected javax.swing.JComboBox<String> sortingArrangements;
    protected javax.swing.JScrollPane templateScrollPanel;
    protected javax.swing.JTable templateTable;
    protected javax.swing.JLabel totalPageLabel;
    protected javax.swing.JButton updateButton;
    // End of variables declaration//GEN-END:variables
}

package com.fluent.builder;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class FieldsTableModel extends AbstractTableModel {
    private final String[] columnNames = {"Field", "Type", "Optional"};
    private final List<FieldData> fields = new ArrayList<>();

    public FieldsTableModel(PsiClass psiClass) {
        for (PsiField field : psiClass.getAllFields()) {
            fields.add(new FieldData(field.getName(), field.getType().getPresentableText(), false));
        }
    }

    @Override
    public int getRowCount() {
        return fields.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        FieldData field = fields.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return field.getName();
            case 1:
                return field.getType();
            case 2:
                return field.isOptional();
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 2;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 2) {
            fields.get(rowIndex).setOptional((Boolean) aValue);
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public List<FieldData> getFields() {
        return fields;
    }
}

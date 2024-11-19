package com.fluent.builder.infrastructure.primary;

import com.fluent.builder.application.FluentBuilderApplicationService;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiClass;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FluentBuilderDialog extends DialogWrapper {
    private JPanel contentPanel;
    private JTable fieldsTable;

    private final PsiClass targetClass;

    private final FluentBuilderApplicationService builder = new FluentBuilderApplicationService();

    public FluentBuilderDialog(PsiClass targetClass) {
        super(true);
        this.targetClass = targetClass;
        init();
        setTitle("Configure Fluent Builder");
    }

    @Override
    protected JComponent createCenterPanel() {
        contentPanel = new JPanel(new BorderLayout());

        // Table to configure fields
        fieldsTable = new JBTable(new FieldsTableModel(targetClass));
        JScrollPane scrollPane = new JBScrollPane(fieldsTable);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        return contentPanel;
    }

    public void generateBuilder() {
        // Get data from the table and generate the builder
        FieldsTableModel model = (FieldsTableModel) fieldsTable.getModel();
        List<FieldData> fields = model.getFields();

        builder.generateBuilder(targetClass, fields);
    }
}

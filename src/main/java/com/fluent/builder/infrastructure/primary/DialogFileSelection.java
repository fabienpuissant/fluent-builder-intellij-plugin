package com.fluent.builder.infrastructure.primary;

import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;

import javax.swing.*;

import static com.intellij.openapi.ui.LabeledComponent.create;
import static com.intellij.ui.ToolbarDecorator.createDecorator;

/**
 * Credit <a href="https://github.com/Powershooter83/SealedFluentBuilder/blob/main/src/main/java/me/prouge/sealedfluentbuilder/panels/FieldSelectionPanel.java">...</a>
 */

public class DialogFileSelection extends DialogWrapper {

    final PluginContext context;
    private final JBList<PsiField> fieldList;

    public DialogFileSelection(final PluginContext context) {
        super(context.ownerClass().getProject());

        this.context = context;
        fieldList = loadClassFields(context.ownerClass());
        selectAllFields();
        setSize(600, 400);
        setTitle(Message.SELECT_FIELDS.label());
        init();
    }

    @Override
    protected void init() {
        super.init();
    }

    private void selectAllFields() {
        int[] indices = new int[fieldList.getModel().getSize()];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = i;
        }
        fieldList.setSelectedIndices(indices);
    }

    private JBList<PsiField> loadClassFields(final PsiClass ownerClass) {
        CollectionListModel<PsiField> fields = new CollectionListModel<>(ownerClass.getFields());
        JBList<PsiField> fieldsList = new JBList<>(fields);
        fieldsList.setCellRenderer(new DefaultPsiElementCellRenderer());
        return fieldsList;
    }

    private ToolbarDecorator createToolbar() {
        return createDecorator(fieldList)
                .disableAddAction()
                .disableRemoveAction()
                .disableUpDownActions();
    }

    @Override
    protected void doOKAction() {
        DefaultListModel<PsiField> selectedFields = getFields();

        super.doOKAction();
        new DialogFieldArrangement(context, selectedFields).show();
    }

    @Override
    protected JComponent createCenterPanel() {
        return create(createToolbar().createPanel(), Message.FIELDS_SELECTION_TITLE.label());
    }

    public DefaultListModel<PsiField> getFields() {
        DefaultListModel<PsiField> model = new DefaultListModel<>();
        fieldList.getSelectedValuesList().forEach(model::addElement);
        return model;
    }

}

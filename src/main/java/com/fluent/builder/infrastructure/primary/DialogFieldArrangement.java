package com.fluent.builder.infrastructure.primary;

import com.fluent.builder.application.FluentBuilderApplicationService;
import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiField;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.intellij.ui.ToolbarDecorator.createDecorator;

/**
 * Credit <a href="https://github.com/Powershooter83/SealedFluentBuilder/blob/main/src/main/java/me/prouge/sealedfluentbuilder/panels/FieldArrangementPanel.java">...</a>
 */

public class DialogFieldArrangement extends DialogWrapper {

    final PluginContext context;
    private final JBList<PsiField> fieldList;
    private final JBList<PsiField> optionalFieldList;
    private final DefaultListModel<PsiField> fieldListModel;
    private final DefaultListModel<PsiField> optionalFieldListModel;
    private final FluentBuilderApplicationService builder = new FluentBuilderApplicationService();

    public DialogFieldArrangement(final PluginContext context, DefaultListModel<PsiField> fields) {
        super(context.ownerClass().getProject());
        this.context = context;


        setSize(600, 800);
        setTitle(Message.SELECT_OPTIONAL_FIELDS.label());

        fieldListModel = fields;
        optionalFieldListModel = new DefaultListModel<>();
        fieldList = createPsiFieldList(fieldListModel);
        optionalFieldList = createPsiFieldList(optionalFieldListModel);
        init();
    }

    private JBList<PsiField> createPsiFieldList(final DefaultListModel<PsiField> fieldListModel) {
        final JBList<PsiField> fieldList = new JBList<>(fieldListModel);
        fieldList.setCellRenderer(new DefaultPsiElementCellRenderer());
        fieldList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        return fieldList;
    }

    @Override
    protected void init() {
        super.init();
    }

    private Box createLeftAlignedBoxWithText(String text) {
        Box box = Box.createHorizontalBox();
        box.add(new JLabel(text, SwingConstants.LEFT));
        box.add(Box.createHorizontalGlue());
        return box;
    }

    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(createLeftAlignedBoxWithText(Message.REQUIRED_FIELDS.label()));
        panel.add(Box.createVerticalStrut(10));

        panel.add(createToolbar(fieldList)
                .addExtraAction(new AnAction(Message.MOVE_DOWN.label()) {
                    @Override
                    public void actionPerformed(@NotNull AnActionEvent e) {
                        moveFields(fieldList, fieldListModel, optionalFieldListModel);
                    }
                }).createPanel());

        panel.add(Box.createVerticalStrut(10));
        panel.add(createLeftAlignedBoxWithText(Message.OPTIONAL_FIELDS.label()));
        panel.add(Box.createVerticalStrut(10));

        panel.add(createToolbar(optionalFieldList)
                .addExtraAction(new AnAction(Message.MOVE_UP.label()) {
                    @Override
                    public void actionPerformed(@NotNull AnActionEvent e) {
                        moveFields(optionalFieldList, optionalFieldListModel, fieldListModel);
                    }
                }).createPanel());

        return panel;
    }

    private ToolbarDecorator createToolbar(JList<PsiField> fieldList) {
        return createDecorator(fieldList)
                .disableAddAction()
                .disableRemoveAction();
    }

    @Override
    protected void doOKAction() {
        List<PsiField> selectedFields = getFields();

        super.doOKAction();
        builder.generateBuilder(context, selectedFields, getOptionalFields());
    }

    private void moveFields(JList<PsiField> sourceList, DefaultListModel<PsiField> sourceModel, DefaultListModel<PsiField> targetModel) {
        List<PsiField> selectedFields = sourceList.getSelectedValuesList();
        selectedFields.stream()
                .filter(field -> !targetModel.contains(field))
                .forEach(targetModel::addElement);

        selectedFields.forEach(sourceModel::removeElement);
    }

    private List<PsiField> getSelectedFieldsFromList(JList<PsiField> list) {
        ListModel<PsiField> model = list.getModel();
        return IntStream.range(0, model.getSize())
                .mapToObj(model::getElementAt)
                .collect(Collectors.toList());
    }

    private List<PsiField> getFields() {
        return getSelectedFieldsFromList(fieldList);
    }

    private List<PsiField> getOptionalFields() {
        return getSelectedFieldsFromList(optionalFieldList);
    }

}

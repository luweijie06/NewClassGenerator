package com.dev.gear;

import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class NewClassGeneratorAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        if (project == null || editor == null || psiFile == null) {
            return;
        }

        PsiClass selectedClass = ClassChooserUtil.chooseClass(project,"Type to search for classes");
        if (selectedClass == null) {
            return;
        }



        PsiField[] fields = selectedClass.getAllFields();
        GenerateClassDialog dialog = new GenerateClassDialog(project, fields, selectedClass.getName());


        if (dialog.showAndGet()) {
            List<PsiField> selectedFields = dialog.getSelectedFields();
            boolean useLombok = dialog.useLombok();
            String newClassName = dialog.getNewClassName();

            if (selectedFields.isEmpty() || newClassName == null || newClassName.isEmpty()) {
                return;
            }
            PackageChooserDialog packageChooser = new PackageChooserDialog("Choose Destination Package", project);
            packageChooser.show();
            PsiPackage selectedPackage = packageChooser.getSelectedPackage();

            if (selectedPackage == null) {
                return;
            }
            WriteCommandAction.runWriteCommandAction(project, () -> {
                PsiElementFactory factory = JavaPsiFacade.getElementFactory(project);
                PsiClass newClass = factory.createClass(newClassName);

                if (useLombok) {
                    newClass.getModifierList().addAnnotation("Data");
                }

                for (PsiField field : selectedFields) {
                    PsiField newField = factory.createField(field.getName(), field.getType());
                    copyComments(field, newField);
                    newClass.add(newField);
                }

                if (!useLombok) {
                    for (PsiField field : selectedFields) {
                        addGetterAndSetter(factory, newClass, field);
                    }
                }

                PsiDirectory directory = selectedPackage.getDirectories()[0];
                directory.add(newClass);
            });
        }
    }

    private void addGetterAndSetter(PsiElementFactory factory, PsiClass newClass, PsiField field) {
        // Generate getter without comments
        PsiMethod getter = factory.createMethodFromText(
                "public " + field.getType().getPresentableText() + " get" +
                        capitalize(field.getName()) + "() { return this." + field.getName() + "; }", newClass);
        newClass.add(getter);

        // Generate setter without comments
        PsiMethod setter = factory.createMethodFromText(
                "public void set" + capitalize(field.getName()) + "(" +
                        field.getType().getPresentableText() + " " + field.getName() +
                        ") { this." + field.getName() + " = " + field.getName() + "; }", newClass);
        newClass.add(setter);
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private void copyComments(PsiElement source, PsiElement target) {
        PsiComment[] comments = PsiTreeUtil.getChildrenOfType(source, PsiComment.class);
        if (comments != null) {
            for (PsiComment comment : comments) {
                target.addBefore(comment.copy(), target.getFirstChild());
            }
        }
    }

    private static class GenerateClassDialog extends DialogWrapper {
        private final JBList<PsiField> fieldList;
        private final JCheckBox lombokCheckBox;
        private final JTextField classNameField;

        GenerateClassDialog(Project project, PsiField[] fields, String selectedClassName) {
            super(project);
            setTitle("Generate New Class");

            fieldList = new JBList<>(fields);
            fieldList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            fieldList.setCellRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof PsiField) {
                        PsiField field = (PsiField) value;
                        setText(field.getName() + ": " + field.getType().getPresentableText());
                    }
                    return c;
                }
            });

            lombokCheckBox = new JCheckBox("Use Lombok annotations", false);
            classNameField = new JTextField(selectedClassName, 20);

            init(); // Call this after initializing all fields
        }
        @Override
        protected JComponent createCenterPanel() {
            JPanel panel = new JPanel(new BorderLayout());

            // Add field list
            panel.add(new JBScrollPane(fieldList), BorderLayout.CENTER);

            // Create and add options panel
            JPanel optionsPanel = new JPanel(new GridLayout(2, 1));
            optionsPanel.add(lombokCheckBox);

            JPanel classNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            classNamePanel.add(new JLabel("New Class Name:"));
            classNamePanel.add(classNameField);
            optionsPanel.add(classNamePanel);

            panel.add(optionsPanel, BorderLayout.SOUTH);

            return panel;
        }

        List<PsiField> getSelectedFields() {
            return fieldList.getSelectedValuesList();
        }

        boolean useLombok() {
            return lombokCheckBox.isSelected();
        }

        String getNewClassName() {
            return classNameField.getText();
        }
    }
}
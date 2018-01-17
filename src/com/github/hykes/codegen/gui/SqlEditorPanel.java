package com.github.hykes.codegen.gui;

import com.github.hykes.codegen.model.IdeaContext;
import com.github.hykes.codegen.model.Table;
import com.github.hykes.codegen.parser.DefaultParser;
import com.github.hykes.codegen.parser.Parser;
import com.github.hykes.codegen.provider.filetype.SqlFileType;
import com.github.hykes.codegen.utils.NotifyUtil;
import com.github.hykes.codegen.utils.StringUtils;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.ui.MessageType;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 * @author hehaiyangwork@gmail.com
 * @date 2017/12/19
 */
public class SqlEditorPanel {

    private static final Logger LOGGER = Logger.getInstance(SqlEditorPanel.class);

    private IdeaContext ideaContext;
    private JPanel rootPanel;
    private JButton buttonCancel;
    private JButton buttonOk;
    private Editor sqlTextArea;
    private JPanel sqlPanel;
    private JPanel actionPanel;
    private JScrollPane sqlScrollPane;

    public SqlEditorPanel(IdeaContext ideaContext) {
        this.ideaContext = ideaContext;
        $$$setupUI$$$();

        buttonOk.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String sqls = sqlTextArea.getDocument().getText();
                    if (StringUtils.isBlank(sqls)) {
                        return;
                    }

                    Parser parser = new DefaultParser();
                    List<Table> tables = parser.parseSQLs(sqls);
                    if (tables == null || tables.isEmpty()) {
                        NotifyUtil.notice("CodeGen-SQL", "please check sql format !", MessageType.ERROR);
                        return;
                    }

                    ColumnEditorFrame frame = new ColumnEditorFrame();
                    frame.newColumnEditorBySql(ideaContext, tables);
                    frame.setSize(900, 500);
                    frame.setAlwaysOnTop(false);
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                    frame.setResizable(true);

                    disable();
                } catch (Exception ex) {
                    LOGGER.error(ex.getMessage());
                }
            }
        });
        buttonCancel.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                disable();
            }
        });

        this.rootPanel.registerKeyboardAction(e -> disable(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private void disable() {
        $$$getRootComponent$$$().getRootPane().getParent().setEnabled(false);
        $$$getRootComponent$$$().getRootPane().getParent().setVisible(false);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = new JPanel();
        sqlPanel = new JPanel();
        sqlPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        // 设置 sql text editor
        Document document = EditorFactory.getInstance().createDocument("");
        sqlTextArea = EditorFactory.getInstance().createEditor(document, ideaContext.getProject(), SqlFileType.INSTANCE, false);

        sqlScrollPane = new JBScrollPane();
        sqlScrollPane.setViewportView(sqlTextArea.getComponent());
    }

    public JComponent getRootComponent() {
        return rootPanel;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("CodeGen-SQL");
        frame.setContentPane(new SqlEditorPanel(null).$$$getRootComponent$$$());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        rootPanel.setLayout(new BorderLayout(0, 0));
        actionPanel = new JPanel();
        actionPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.add(actionPanel, BorderLayout.SOUTH);
        buttonOk = new JButton();
        buttonOk.setText("OK");
        actionPanel.add(buttonOk, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        actionPanel.add(buttonCancel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sqlPanel.setLayout(new BorderLayout(0, 0));
        rootPanel.add(sqlPanel, BorderLayout.CENTER);
        sqlPanel.add(sqlScrollPane, BorderLayout.CENTER);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }
}

package com.neoyu.plugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;

public class PyGetterAndSetter extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // 获取到editor和project
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        Project project = e.getData(CommonDataKeys.PROJECT);
        if (editor == null || project == null) {
            return;
        }
        // 获取光标选中文本段对象和doc对象
        SelectionModel selectionModel = editor.getSelectionModel();
        Document document = editor.getDocument();
        // 拿到选中部分字符串
        String selectedText = selectionModel.getSelectedText();
        // 从选中部分得到变量列表
        ArrayList<String> fieldList = getFieldList(selectedText);
        // 得到选中字符串的结束位置
        int endOffset = selectionModel.getSelectionEnd();
        // 得到最大插入字符串（生成的Getter和Setter函数字符串）的位置
        int maxOffset = document.getTextLength() - 1;
        // 计算选中字符串所在的行号，通过行号得到下一行的第一个字符的起始偏移量
        int curLineNumber = document.getLineNumber(endOffset);
        int nextLineStartOffset = document.getLineStartOffset(curLineNumber + 1);
        // 计算字符串的插入位置
        int insertOffset = Math.min(maxOffset, nextLineStartOffset);
        // 对文档进行操作部分代码，需要放入runnable，不然IDEA会卡住
        Runnable runnable = () -> document.insertString(insertOffset, genGetterAndGetter(fieldList));

        // 加入任务，由IDE调度任务
        WriteCommandAction.runWriteCommandAction(project, runnable);

    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) {
            return;
        }
        SelectionModel selectionModel = editor.getSelectionModel();
        // 如果没有字符串被选中，那么不需要显示这个Action
        e.getPresentation().setVisible(selectionModel.hasSelection());
        e.getPresentation().setIcon(MyIcon.GEN_CODE);
    }


    /**
     * 生成字段的Getter和Setter字符串 <br>
     * e.g. 对于 _value,会生成：<br>
     * <code>
     * get_value(self){        <br>
     * return self._value  <br>
     * }                         <br>
     * <br>
     * set_(self, value){      <br>
     * self._value = value  <br>
     * }                        <br>
     * </code>
     *
     * @param fieldList 变量字段的字符串列表
     * @return 对应的GetterAndSetter
     */
    private String genGetterAndGetter(ArrayList<String> fieldList) {
        if (fieldList == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        // 定义Getter和Setter的模板
        String getterTemplate = "    def get_word(self):\n        return self.field\n    ";
        String setterTemplate = "    def set_word(self, word):\n        self.field = word\n    ";
        // 对于 “_value” 类型的变量，在set方法参数中，只需要“value”
        for (String field : fieldList) {
            String tmp = field;
            int i = 0;
            while (tmp.charAt(i) == '_') {
                tmp = tmp.substring(1);
            }
            // 替换掉模板中的变量
            String customGetter = getterTemplate.replaceAll("word", tmp).replaceAll("field", field);
            String customSetter = setterTemplate.replaceAll("word", tmp).replaceAll("field", field);
            stringBuilder.append("\n").append(customGetter).append("\n").append(customSetter);
        }
        return stringBuilder.toString();
    }

    /**
     * 获取选中文本中所有的self.value中的value <br>
     * e.g. self.value = xxx,or self._value = xxx,<br>
     * 可以获取到其中的value
     *
     * @param selectedText 选中文本
     * @return 变量字符串列表
     */
    public ArrayList<String> getFieldList(String selectedText) {
        ArrayList<String> list = new ArrayList<>();
        // 删除所有空格
        selectedText = selectedText.replaceAll(" ", "");
        // 正则匹配获得变量字符串
        String reg = "self.(.*?)=";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(selectedText);
        while (matcher.find()) {
            list.add(matcher.group(1));
        }
        return list;
    }
}

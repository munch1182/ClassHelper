import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiFile;

import javax.swing.JButton;
import javax.swing.JWindow;

/**
 * Created by Mucnch on 2018-12-20.
 */
public class ClassHelper extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        //显示窗口
        showWindow(e);


    }

    private void showWindow(AnActionEvent e) {
        DialogClass dialog = new DialogClass();
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
//        next(e);
    }

    private void next(AnActionEvent e) {
        //获取文件对象
        PsiFile file = getFile(e);
        //获取选择的参数
        ParametersBean bean = getParametersBean();

        //创建文件
        setupAction(file, bean);

        Messages.showMessageDialog(file.getProject().getProjectFilePath(), "TestWord", Messages.getInformationIcon());
    }

    private void setupAction(PsiFile currentFile, ParametersBean bean) {

    }

    private ParametersBean getParametersBean() {
        return null;
    }

    private PsiFile getFile(AnActionEvent e) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        //在代码区域
        if (null != psiFile) {
            return psiFile;
        } else {
            //在非代码区域
            return null;
        }
    }
}

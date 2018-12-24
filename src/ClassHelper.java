import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.pom.Navigatable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.SystemIndependent;

/**
 * Created by Munch on 2018/12/24.
 */
public class ClassHelper extends AnAction {

    private final static String FILE_SETTING_GRADLE = "setting.gradle";

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        Navigatable nav = anActionEvent.getData(CommonDataKeys.NAVIGATABLE);

        if (null == nav) {
            showErrorDialog("不能够获取当前位置");
            return;
        }

        DialogClassHelper dialogClassHelper = new DialogClassHelper();
        dialogClassHelper.pack();
        dialogClassHelper.setLocationRelativeTo(null);
        dialogClassHelper.setCurrentLoc(nav.toString());
        dialogClassHelper.setVisible(true);
    }

    private void showErrorDialog(String reason) {

    }

    private void showDialog(@SystemIndependent String projectFilePath) {

    }


}

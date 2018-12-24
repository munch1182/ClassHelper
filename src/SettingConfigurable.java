import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;

/**
 * Created by Munch on 2018/12/23.
 */
public class SettingConfigurable implements SearchableConfigurable {

    private GuiSetting mGuiSetting;

    @NotNull
    @Override
    public String getId() {
        return "ClassHelper";
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return this.getId();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if (null==mGuiSetting){
            mGuiSetting = new GuiSetting();
        }
        return mGuiSetting.mContentPane;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }
}

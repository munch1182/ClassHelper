package com.laochsm;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.util.SystemIndependent;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;

public class ClassHelper extends AnAction {

    private String srcDir = "";
    private String packName = "";
    private String name = "";

    private final String CONTRIBUTES_CONTENT = "\n" +
            "    @ActivityScoped\n" +
            "    @ContributesAndroidInjector(modules = [($Name$Module::class)])\n" +
            "    abstract fun contributes$Name$Activity(): $Name$Activity";

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        if (project == null) {
            showError("CANNOT READ PROJECT");
            return;
        }
        getSrcDir(project.getBasePath());
        if (srcDir.isEmpty()) {
            showError("CANNOT GET SRC DIR");
            return;
        }
        getPackName();
        if (packName.isEmpty()) {
            showError("CANNOT READ PACK NAME");
            return;
        }
        showInput(event.getProject());
        if (name.isEmpty()) {
            return;
        }
        File parent = createPack(name);
        if (parent == null) {
            return;
        }
        if (name.contains("app")) {
            createApp(parent, name);
            return;
        }
        createPageFile(parent, name);
    }

    private void createPageFile(File parent, String name) {
        File actFile = createFile(parent, getFirstUpperName(name) + "Activity.kt");
        if (actFile == null) {
            return;
        }
        writeFile(actFile, getContent("template/activity/Activity.txt"));
        File contractFile = createFile(parent, getFirstUpperName(name) + "Contract.kt");
        if (contractFile == null) {
            return;
        }
        writeFile(contractFile, getContent("template/activity/Contract.txt"));
        File modelFile = createFile(parent, getFirstUpperName(name) + "Model.kt");
        if (modelFile == null) {
            return;
        }
        writeFile(modelFile, getContent("template/activity/Model.txt"));
        File moduleFile = createFile(parent, getFirstUpperName(name) + "Module.kt");
        if (moduleFile == null) {
            return;
        }
        writeFile(moduleFile, getContent("template/activity/Module.txt"));
        File pFile = createFile(parent, getFirstUpperName(name) + "Presenter.kt");
        if (pFile == null) {
            return;
        }
        writeFile(pFile, getContent("template/activity/Presenter.txt"));
        String pathname = srcDir + "/main/java/" + packName.replace(".", "/") + "/app/Modules.kt";
        File modulesFile = new File(pathname);
        if (!modulesFile.exists()) {
            return;
        }
        append(modulesFile);
    }

    private void append(File modulesFile) {
        try {
            byte[] bytes = readSteam(new FileInputStream(modulesFile));
            if (bytes == null) {
                return;
            }
            String modules = new String(bytes);
            String content = modules.substring(0, modules.length() - 1) + CONTRIBUTES_CONTENT + "\r\n}";
            writeFile(modulesFile, content);
        } catch (FileNotFoundException e) {
            showError(e.getLocalizedMessage());
        }
    }

    private void createApp(File parent, String name) {
        File appFile = createFile(parent, getFirstUpperName(name) + ".kt");
        if (appFile == null) {
            return;
        }
        writeFile(appFile, getContent("template/app/App.txt"));
        File componentFile = createFile(parent, getFirstUpperName(name) + "Component.kt");
        if (componentFile == null) {
            showError("CANNOT CREATE " + name + ".kt");
            return;
        }
        writeFile(componentFile, getContent("template/app/AppComponent.txt"));
        File mFile = createFile(parent, "Modules.kt");
        if (mFile == null) {
            return;
        }
        writeFile(mFile, getContent("template/app/Modules.txt"));
    }

    @NotNull
    private String getFirstUpperName(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private void writeFile(File appFile, String content) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(appFile);
            writer.write(replaceKey(content));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(writer);
        }
    }

    private String replaceKey(String content) {
        return content.replace("$packName$", packName).replace("$name$", name)
                .replace("$Name$", getFirstUpperName(name));
    }

    private String getContent(String res) {
        byte[] bytes = readSteam(this.getClass().getResourceAsStream(res));
        if (bytes == null) {
            showError("CANNOT READ " + res);
            return null;
        }
        return new String(bytes);
    }

    private byte[] readSteam(InputStream is) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        byte[] content;
        try {
            while ((len = is.read(buffer)) != -1) {
                stream.write(buffer, 0, len);
            }
            content = stream.toByteArray();
        } catch (IOException e) {
            content = null;
        } finally {
            close(is);
            close(stream);
        }
        return content;
    }

    private void close(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createFile(File parent, String name) {
        File file = new File(parent, name);
        if (file.exists()) {
            showError(name + ".kt EXISTS");
            return null;
        }
        try {
            if (!file.createNewFile()) {
                showError("CANNOT CREATE " + name + ".kt");
                return null;
            }
        } catch (IOException e) {
            showError("CANNOT CREATE " + name + ".kt");
            return null;
        }
        return file;
    }

    private File createPack(String name) {
        File pFile = new File(srcDir + "/main/java/" + packName.replace(".", "/") + "/" + name);
        if (pFile.exists()) {
            showError(pFile.getName() + " EXISTS");
            return null;
        }
        if (!pFile.mkdirs()) {
            showError("CREATE " + pFile + " FAIL");
            return null;
        }
        return pFile;
    }

    private void showInput(Project project) {
        Messages.InputDialog dialog = new Messages.InputDialog(project, "请输入名称", "NEW",
                Messages.getQuestionIcon(), null, null);
        dialog.show();
        name = dialog.getInputString();
    }

    private void getPackName() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(srcDir + "/main/AndroidManifest.xml");
            NodeList nodeList = document.getElementsByTagName("manifest");
            for (int i = 0; i < nodeList.getLength(); i++) {
                packName = ((Element) nodeList.item(i)).getAttribute("package");
            }
        } catch (Exception e) {
            showError(e.getMessage());
            packName = "";
        }
    }

    private void getSrcDir(@SystemIndependent String path) {
        File pFile = new File(path);
        if (!pFile.exists()) {
            return;
        }
        if (pFile.isDirectory()) {
            if (pFile.getName().equalsIgnoreCase("src")) {
                srcDir = path;
                return;
            }
            File[] files = pFile.listFiles();
            if (files == null) {
                return;
            }
            for (File file : files) {
                //已经找到src文件夹
                if (!srcDir.isEmpty()) {
                    return;
                }
                //不向下遍历android工程中的固定文件夹
                if (file.isDirectory() && (file.getName().contains(".") ||
                        file.getName().equalsIgnoreCase("build") ||
                        file.getName().equalsIgnoreCase("lib"))) {
                    continue;
                }
                //向下遍历文件
                getSrcDir(file.getAbsolutePath());
            }
        }
    }

    private void showError(String error) {
        Messages.showErrorDialog(error, "ERROR");
    }
}

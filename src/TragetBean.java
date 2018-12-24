import java.io.File;

/**
 * Created by Munch on 2018/12/23.
 */
public class TragetBean {

    /**
     * 名称，不带后缀与文件类型
     */
    private String name;
    /**
     * 路径
     */
    private String path;
    /**
     * 包名
     */
    private String pack;
    /**
     * 后缀名
     */
    private String suffix;
    /**
     * 类名
     */
    private String className;
    private File file;
    private String fileType = ".java";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getClassName() {
        return name + suffix;
    }

    public File getFile() {
        return FileHelper.getFile(path, className + fileType);
    }
}

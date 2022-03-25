package cn.reghao.jutil.jdk.http;

import java.io.File;
import java.util.Map;

/**
 * @author reghao
 * @date 2021-12-28 13:06:23
 */
public class UploadParam {
    private final File file;
    private final String mimeType;
    private Map<String, String> textParams;

    public UploadParam(File file, String mimeType) {
        this.file = file;
        this.mimeType = mimeType;
    }

    public File getFile() {
        return file;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setTextParams(Map<String, String> textParams) {
        this.textParams = textParams;
    }

    public Map<String, String> getTextParams() {
        return textParams;
    }
}

package cn.reghao.jutil.jdk.http;

import java.io.InputStream;
import java.util.Map;

/**
 * @author reghao
 * @date 2021-12-28 13:06:23
 */
public class UploadParam {
    private final String filePath;
    private final InputStream inputStream;
    private final String mimeType;
    private Map<String, String> textParams;

    public UploadParam(String filePath, String mimeType) {
        this.filePath = filePath;
        this.inputStream = null;
        this.mimeType = mimeType;
    }

    public UploadParam(InputStream inputStream, String mimeType) {
        this.filePath = null;
        this.inputStream = inputStream;
        this.mimeType = mimeType;
    }

    public String getFilePath() {
        return filePath;
    }

    public InputStream getInputStream() {
        return inputStream;
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

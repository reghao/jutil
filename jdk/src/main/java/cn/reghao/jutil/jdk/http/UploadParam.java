package cn.reghao.jutil.jdk.http;

import java.io.File;
import java.util.Map;

/**
 * @author reghao
 * @date 2021-12-28 13:06:23
 */
public class UploadParam {
    private File file;
    private byte[] bytes;
    private final String mimeType;
    private Map<String, String> textParams;

    public UploadParam(File file, String mimeType) {
        this.file = file;
        this.mimeType = mimeType;
    }

    public UploadParam(byte[] bytes, String mimeType) {
        this.bytes = bytes;
        this.mimeType = mimeType;
    }

    public File getFile() {
        return file;
    }

    public byte[] getBytes() {
        return bytes;
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

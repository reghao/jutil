package cn.reghao.jutil.jdk.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

/**
 * @author reghao
 * @date 2022-05-11 22:29:49
 */
public class AppVersion implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String repo;
    private final String branch;
    private final String commitId;
    private final String commitTime;
    private final String buildTime;

    private AppVersion(String repo, String branch, String commitId, String commitTime, String builtTime) {
        this.repo = repo;
        this.branch = branch;
        this.commitId = commitId;
        this.commitTime = commitTime;
        this.buildTime = builtTime;
    }

    public String getRepo() {
        return repo;
    }

    public String getBranch() {
        return branch;
    }

    public String getCommitId() {
        return commitId;
    }

    public String getCommitTime() {
        return commitTime;
    }

    public String getBuildTime() {
        return buildTime;
    }

    public static AppVersion getVersion() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            // 读取 src/main/resources 目录下的文件
            InputStream inputStream = classLoader.getResourceAsStream("git.properties");
            Properties props = new Properties();
            props.load(inputStream);String repo = props.get("repo").toString();
            String branch = props.get("branch").toString();
            String commitId = props.get("commitId").toString();
            String commitTime = props.get("commitTime").toString();
            String buildTime = props.get("buildTime").toString();
            return new AppVersion(repo, branch, commitId, commitTime, buildTime);
        } catch (IOException ignore) {
        }
        return null;
    }
}

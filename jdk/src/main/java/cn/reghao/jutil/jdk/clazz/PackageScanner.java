package cn.reghao.jutil.jdk.clazz;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 扫描指定包路径下的所有 class 文件
 *
 * @author reghao
 * @date 2020-09-24 15:41:07
 */
public class PackageScanner {
    public Class findClassBySimpleName(Class<?> baseClass, String simpleName, String pkgPath) throws IOException, ClassNotFoundException {
        List<Class<?>> classList = doScan(baseClass, pkgPath);
        for (Class<?> clazz : classList) {
            if (clazz.getSimpleName().equals(simpleName)) {
                return clazz;
            }
        }
        return null;
    }

    /**
     * 扫描指定包中的所有类
     *
     * @param clazz 启动类
     * @param basePackage 待扫描的包路径
     * @return
     * @date 2021-12-16 上午11:54
     */
    @Deprecated
    public List<Class<?>> doScan(Class<?> clazz, String basePackage) throws IOException, ClassNotFoundException {
        List<Class<?>> classList = new ArrayList<>();
        return classList;
    }

    /**
     * 扫描指定包中的所有类
     *
     * @param basePackage 指定的包路径
     * @return
     * @date 2021-12-21 下午3:41
     */
    public List<Class<?>> doScan(String basePackage) throws IOException, ClassNotFoundException {
        List<Class<?>> classList = new ArrayList<>();
        String baseFilePath = basePackage.replace(".", "/");
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL url = cl.getResource(baseFilePath);
        assert url != null;
        File file = new File(rootPath(url));
        String protocol = url.getProtocol();
        if ("jar".equals(protocol)) {
            readFromJarFile(url, basePackage, classList);
        } else if ("file".equals(protocol)) {
            if (file.isDirectory()) {
                readFromDir(file, classList);
            } else {
                addClazz(file.getAbsolutePath(), classList);
            }
        }
        return classList;
    }

    private String rootPath(URL url) {
        String path = url.getPath();
        int pos = path.indexOf("!");
        if (pos == -1) {
            return path.replace("file:", "");
        } else {
            return path.substring(0, pos).replace("file:", "");
        }
    }

    private void readFromDir(File dir, List<Class<?>> classList) {
        File[] files = dir.listFiles();
        assert files != null;
        Arrays.asList(files).forEach(file -> {
            if (file.isDirectory()) {
                readFromDir(file, classList);
            } else {
                String path = file.getPath();
                addClazz(path, classList);
            }
        });
    }

    private void addClazz(String filepath, List<Class<?>> classList) {
        String tmp = filepath.split("target/classes/")[1];
        String className = tmp.replace("/", ".").replace(".class", "");
        try {
            classList.add(Class.forName(className));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取 jar 包中的 class 文件
     *
     * @param
     * @return
     * @date 2021-09-08 下午3:30
     */
    private void readFromJarFile(URL url, String basePackage, List<Class<?>> classList)
            throws IOException, ClassNotFoundException {
        JarFile jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String name = jarEntry.getName();
            String className = name.replace("/", ".");
            if (className.startsWith(basePackage) && className.endsWith(".class")) {
                String clazz = className.replace(".class", "");
                // 使用当前线程的类加载器加载类
                Class<?> loadClass = Thread.currentThread().getContextClassLoader().loadClass(clazz);
                //Class<?> loadClass = Class.forName(clazz);
                classList.add(loadClass);
            }
        }
    }
}

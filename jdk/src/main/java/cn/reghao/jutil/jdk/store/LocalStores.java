package cn.reghao.jutil.jdk.store;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * @author reghao
 * @date 2022-05-23 18:21:22
 */
public class LocalStores {
    private static final Map<String, LocalStore> storeMap = new HashMap<>();
    private static final Map<String, Map<String, StoreDir>> storeDirMap = new HashMap<>();

    public static void init(List<String> diskDirs) throws IOException {
        Map<String, String> map = new HashMap<>();
        for (String diskDir : diskDirs) {
            FileStore fileStore = Files.getFileStore(Path.of(diskDir));
            String logicalDisk = fileStore.name();
            String prevValue = map.putIfAbsent(logicalDisk, diskDir);
            if (prevValue == null) {
                long total = fileStore.getTotalSpace();
                long available = fileStore.getUsableSpace();
                LocalStore localStore = new LocalStore(logicalDisk, diskDir, total, available, 0.9);
                storeMap.put(diskDir, localStore);
                storeDirMap.computeIfAbsent(diskDir, k -> new HashMap<>());
                createSubDirs(diskDir);
            } else {
                String msg = String.format("%s's mounted directory %s has already associated with %s", logicalDisk, diskDir, prevValue);
                System.out.println(msg);
            }
        }
    }

    private static void createSubDirs(String diskDir) throws IOException {
        int total = 128;
        for (int i = 0; i < total; i++) {
            for (int j = 0; j < total; j++) {
                String baseDir = String.format("%s/%s/%s/", diskDir, i, j);
                File dir = new File(baseDir);
                if (!dir.exists() && !dir.mkdirs()) {
                    String msg = String.format("create %s failed", dir);
                    throw new IOException(msg);
                }

                storeDirMap.get(diskDir).put(baseDir, new StoreDir(baseDir));
            }
        }
    }

    public static void initStoreDirs(String diskDir, Map<String, Integer> map) {
        Map<String, StoreDir> map1 = storeDirMap.get(diskDir);
        if (map1 == null) {
            return;
        }

        map.forEach((path, total) -> {
            StoreDir storeDir = map1.get(path);
            if (storeDir != null) {
                storeDir.setTotal(total);
            }
        });
    }

    // TODO 优化算法, 处理异常
    public static LocalStore getMaxStore(long size) {
        Map<String, Long> map = new HashMap<>();
        for (Map.Entry<String, LocalStore> entry : storeMap.entrySet()) {
            String diskDir = entry.getKey();
            LocalStore localStore = entry.getValue();
            long currentAvailable = localStore.getAvailable() - size;
            map.put(diskDir, currentAvailable);
        }

        List<String> diskDirs = new ArrayList<>();
        // diskDirs 中的元素升序排列
        map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEachOrdered(b -> diskDirs.add(b.getKey()));

        String maxDisk = diskDirs.get(diskDirs.size()-1);
        LocalStore localStore = storeMap.get(maxDisk);
        localStore.setCurrentAvailable(map.get(maxDisk));
        return localStore;
    }
    
    public static List<StoreDir> getSubDirs(String diskDir) {
        return new ArrayList<>(storeDirMap.get(diskDir).values());
    }

    public static List<LocalStore> getLocalStores() {
        return new ArrayList<>(storeMap.values());
    }
}

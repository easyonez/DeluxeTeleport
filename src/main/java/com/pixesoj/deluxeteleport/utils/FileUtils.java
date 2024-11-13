package com.pixesoj.deluxeteleport.utils;

import com.pixesoj.deluxeteleport.DeluxeTeleport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static List<String> getFiles(DeluxeTeleport plugin, String type) {
        List<String> list = new ArrayList<>();

        if (type.equalsIgnoreCase("warps")) {
            File folder = new File(plugin.getDataFolder(), "data/warps");

            if (folder.exists() && folder.isDirectory()) {
                File[] files = folder.listFiles();

                if (files != null) {
                    for (File file : files) {
                        if (file.isFile() && file.getName().endsWith(".yml")) {
                            list.add(file.getName().replace(".yml", ""));
                        }
                    }
                }
            }
        }

        return list;
    }
}

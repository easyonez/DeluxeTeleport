package com.pixesoj.deluxeteleport.utils;

import com.pixesoj.deluxeteleport.DeluxeTeleport;
import com.pixesoj.deluxeteleport.managers.filesmanager.MessagesFileManager;

public class TimeUtils {
    public static String formatTime(DeluxeTeleport plugin, int totalTicks) {

        int days = totalTicks / 86400;
        int hours = (totalTicks % 86400) / 3600;
        int minutes = (totalTicks % 3600) / 60;
        int seconds = totalTicks % 60;

        StringBuilder timeFormatted = new StringBuilder();

        MessagesFileManager msg = plugin.getMainMessagesManager();
        String appendDays = msg.getVariablesDays();
        String appendHours = msg.getVariablesHours();
        String appendMinutes = msg.getVariablesMinutes();
        String appendSeconds = msg.getVariablesSeconds();

        if (days > 0) {
            timeFormatted.append(days).append(appendDays).append(" ");
        }
        if (hours > 0) {
            timeFormatted.append(hours).append(appendHours).append(" ");
        }
        if (minutes > 0) {
            timeFormatted.append(minutes).append(appendMinutes).append(" ");
        }
        if (seconds > 0 || timeFormatted.length() == 0) {
            timeFormatted.append(seconds).append(appendSeconds);
        }

        return timeFormatted.toString().trim();
    }

    public static int timerConverter(String unit, String time) {
        if (time == null) {
            return 0;
        }

        if (time.matches("\\d+")) {
            int ticks = Integer.parseInt(time);
            return convertToUnit(unit, ticks);
        }

        int totalTicks = getTotalTicks(time);
        return convertToUnit(unit, totalTicks);
    }

    private static int getTotalTicks(String time) {
        int totalTicks = 0;
        String[] timeUnits = time.split(" ");

        for (String part : timeUnits) {
            if (part.endsWith("t")) {
                totalTicks += Integer.parseInt(part.replace("t", ""));
            } else if (part.endsWith("s")) {
                totalTicks += Integer.parseInt(part.replace("s", "")) * 20;
            } else if (part.endsWith("m")) {
                totalTicks += Integer.parseInt(part.replace("m", "")) * 60 * 20;
            } else if (part.endsWith("h")) {
                totalTicks += Integer.parseInt(part.replace("h", "")) * 3600 * 20;
            } else if (part.endsWith("d")) {
                totalTicks += Integer.parseInt(part.replace("d", "")) * 86400 * 20;
            } else if (part.endsWith("w")) {
                totalTicks += Integer.parseInt(part.replace("w", "")) * 604800 * 20;
            }
        }
        return totalTicks;
    }

    private static int convertToUnit(String unit, int totalTicks) {
        if (unit.equalsIgnoreCase("milliseconds")) {
            return totalTicks * 50;
        } else if (unit.equalsIgnoreCase("seconds")) {
            return totalTicks;
        } else if (unit.equalsIgnoreCase("ticks")) {
            return totalTicks / 20;
        } else {
            return 0;
        }
    }
}

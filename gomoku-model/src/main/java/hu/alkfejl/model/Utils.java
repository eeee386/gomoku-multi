package hu.alkfejl.model;

public class Utils {
    public static String formatDuration(Integer seconds){
        if(seconds == null){
            return null;
        }
        return String.format(
                "%02d:%02d",
                (seconds % 3600) / 60,
                seconds % 60);
    }

    public static Integer getSecondsFromFormattedString(String s){
        System.out.println("Utils");
        System.out.println(s);
        if(s == null){
            return null;
        }
        String[] s2 = s.split(":");
        Integer i = Integer.parseInt(s2[0])*60 + Integer.parseInt(s2[1]);
        return i;
    }
}

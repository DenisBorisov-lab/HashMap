package Amazon;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solution {
    public static void main(String[] args) {
        Solution go = new Solution();
        String oldFile = go.fileReader("test");
        String newFile = go.fileReader("test2");

        Map<String, Data> oldMap = new HashMap<>();
        enrichMapValues(oldFile, oldMap);

        Map<String, Data> newMap = new HashMap<>();
        enrichMapValues(newFile, newMap);

        newPackages(oldMap, newMap);
        System.out.println("--------------------------------------------------");
        IncreasedPackages(oldMap, newMap);
        System.out.println("--------------------------------------------------");
        DowngradedPackages(oldMap, newMap);
        System.out.println("--------------------------------------------------");
        differentCommits(oldMap, newMap);

    }

    @SneakyThrows
    public String fileReader(String firstFile){
        Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource(firstFile)).toURI());

        Stream<String> lines = Files.lines(path);
        String data = lines.collect(Collectors.joining("\n"));
        lines.close();
        return data;
    }

    public static void enrichMapValues(String str, Map<String, Data> map){
        String[] appending = str.split("\n");
        for(int i  =0; i < appending.length; i++){
            String[] line = appending[i].split(";");
            map.put(line[0], new Data(line[1], line[2]));
        }
    }

    public static void newPackages(Map<String, Data> oldMap, Map<String, Data> newMap) {
        for (String key : newMap.keySet()) {
            if (!oldMap.containsKey(key)){
                System.out.println("-->" +  " " + key);
            }
        }
    }

    public static void IncreasedPackages(Map<String, Data> oldMap, Map<String, Data> newMap){
        System.out.println("Packages whose version was increased: ");
        Set<String> newKeys = newMap.keySet();
        String[] arrayNewKeys = newKeys.toArray(new String[newKeys.size()]);
        for (int i = 0; i < newMap.size(); i++){
            if (oldMap.containsKey(arrayNewKeys[i]) && increasing(arrayNewKeys[i], oldMap, newMap)){
                System.out.println("-->" + " " + arrayNewKeys[i]);
            }
        }
    }
    public static void DowngradedPackages(Map<String, Data> oldMap, Map<String, Data> newMap){
        System.out.println("Packages whose version was downgraded: ");
        Set<String> newKeys = newMap.keySet();
        String[] arrayNewKeys = newKeys.toArray(new String[newKeys.size()]);
        for (int i = 0; i < newMap.size(); i++){
            if (oldMap.containsKey(arrayNewKeys[i]) && downing(arrayNewKeys[i], oldMap, newMap)){
                System.out.println("-->" + " " + arrayNewKeys[i]);
            }
        }
    }
    public static boolean increasing(String key, Map<String, Data> oldMap, Map<String, Data> newMap ){
        final Data oldPackages = oldMap.get(key);
        final Data newPackages = newMap.get(key);
        String oldVersion = oldPackages.getVersion();
        String newVersion = newPackages.getVersion();
        oldVersion = replacement(oldVersion);
        newVersion = replacement(newVersion);
        String[] oldVersionArray = oldVersion.split(";");
        String[] newVersionArray = newVersion.split(";");
        boolean result = false;
        for (int i = 0; i < oldVersionArray.length; i++){
            if (Integer.parseInt(newVersionArray[i]) > Integer.parseInt(oldVersionArray[i])){
                result = true;
                break;
            }else if (Integer.parseInt(newVersionArray[i]) < Integer.parseInt(oldVersionArray[i])){
                result =  false;
                break;
            }
        }
        return result;
    }
    public static boolean downing(String key, Map<String, Data> oldMap, Map<String, Data> newMap ){
        final Data oldPackages = oldMap.get(key);
        final Data newPackages = newMap.get(key);
        String oldVersion = oldPackages.getVersion();
        String newVersion = newPackages.getVersion();
        oldVersion = replacement(oldVersion);
        newVersion = replacement(newVersion);
        String[] oldVersionArray = oldVersion.split(";");
        String[] newVersionArray = newVersion.split(";");
        boolean result = false;
        for (int i = 0; i < oldVersionArray.length; i++){
            if (Integer.parseInt(newVersionArray[i]) < Integer.parseInt(oldVersionArray[i])){
                result = true;
                break;
            }else if (Integer.parseInt(newVersionArray[i]) > Integer.parseInt(oldVersionArray[i])){
                result =  false;
                break;
            }
        }
        return result;
    }
    public static void differentCommits(Map<String, Data> oldMap, Map<String, Data> newMap){
        System.out.println("Packages with the same version, but with different commits: ");
        Set<String> newKeys = newMap.keySet();
        String[] arrayNewKeys = newKeys.toArray(new String[newKeys.size()]);
        for (int i = 0; i < newMap.size(); i++){
            if (oldMap.containsKey(arrayNewKeys[i]) && commitDifference(arrayNewKeys[i], oldMap, newMap)){
                System.out.println("-->" + " " + arrayNewKeys[i]);
            }
        }
    }
    public static boolean commitDifference(String key, Map<String, Data> oldMap, Map<String, Data> newMap ){
        final Data oldPackages = oldMap.get(key);
        final Data newPackages = newMap.get(key);
        String oldVersion = oldPackages.getVersion();
        String newVersion = newPackages.getVersion();
        String oldCommit = oldPackages.getCommit();
        String newCommit = newPackages.getCommit();
        if (oldMap.containsKey(key) && oldVersion.equals(newVersion) && !oldCommit.equals(newCommit)){
            return true;
        }else{
            return false;
        }
    }
    public static String replacement(String message){
        String[] intermediate = message.split("");
        StringBuilder fics = new StringBuilder();
        for (int i = 0; i < intermediate.length; i++){
            if (intermediate[i].equals(".")){
                intermediate[i] = ";";
            }
            fics.append(intermediate[i]);
        }
        return fics.toString();
    }
}

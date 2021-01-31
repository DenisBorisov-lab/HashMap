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
        oldMap = toValues(oldFile, oldMap);;
        Map<String, Data> newMap = new HashMap<>();
        newMap = toValues(newFile, newMap);
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

    public static Map<String, Data> toValues(String str, Map<String, Data> map){
        String[] appending = str.split("\n");
        for(int i  =0; i < appending.length; i++){
            String[] line = appending[i].split(";");
            map.put(line[0], new Data(line[1], line[2]));
        }
        return map;
    }

    public static void newPackages(Map<String, Data> oldMap, Map<String, Data> newMap) {
        Set<String> newKeys = newMap.keySet();
        String[] arrayNewKeys = newKeys.toArray(new String[newKeys.size()]);
        System.out.println("New packages: ");
        for (int i = 0; i < newKeys.size(); i++){
            if (!oldMap.containsKey(arrayNewKeys[i])){
                System.out.println("-->" +  " " + arrayNewKeys[i]);
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
        StringBuilder oldNumber = new StringBuilder();
        StringBuilder newNumber = new StringBuilder();
        for (int i = 0; i < oldVersion.length(); i++){
            oldNumber.append(oldVersion.charAt(i));
            newNumber.append(newVersion.charAt(i));
            i++;
        }
        if (Integer.parseInt(String.valueOf(newNumber)) > Integer.parseInt(String.valueOf(oldNumber))){
            return true;
        }else{
            return false;
        }
    }
    public static boolean downing(String key, Map<String, Data> oldMap, Map<String, Data> newMap ){
        final Data oldPackages = oldMap.get(key);
        final Data newPackages = newMap.get(key);
        String oldVersion = oldPackages.getVersion();
        String newVersion = newPackages.getVersion();
        StringBuilder oldNumber = new StringBuilder();
        StringBuilder newNumber = new StringBuilder();
        for (int i = 0; i < oldVersion.length(); i++){
            oldNumber.append(oldVersion.charAt(i));
            newNumber.append(newVersion.charAt(i));
            i++;
        }
        if (Integer.parseInt(String.valueOf(newNumber)) < Integer.parseInt(String.valueOf(oldNumber))){
            return true;
        }else{
            return false;
        }
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
        if (oldMap.containsKey(key) && oldVersion.equals(newVersion) && oldCommit != newCommit){
            return true;
        }else{
            return false;
        }
    }
}

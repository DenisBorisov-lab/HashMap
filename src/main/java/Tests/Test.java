package Tests;

import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        String number = "2;0;1";
        String[] list = number.split(";");
        System.out.println(Arrays.toString(list));
    }
}

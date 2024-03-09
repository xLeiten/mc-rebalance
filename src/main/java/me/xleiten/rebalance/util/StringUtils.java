package me.xleiten.rebalance.util;

public class StringUtils
{
    public static String capitalize(String input) {
        if (input.isEmpty()) return input;
        if (input.length() == 1) return input.toUpperCase();
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}

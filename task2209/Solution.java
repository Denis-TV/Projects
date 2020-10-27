package com.javarush.task.task22.task2209;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/* 
Составить цепочку слов
*/
public class Solution {
    public static void main(String[] args) throws IOException {
        //...
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String fileName = reader.readLine();
        reader.close();
        reader = new BufferedReader(new FileReader(fileName));

        StringBuilder result = getLine(reader.readLine().split(" "));
        System.out.println(result.toString());
        reader.close();
    }

    public static StringBuilder getLine(String... words) {
        StringBuilder sb = new StringBuilder();
        if (words.length==0) return sb;
        for (String s : words) {
            if (found(s, new ArrayList<String>(Arrays.asList(words)), sb)) break;
        }
        return sb;
    }

    public static boolean found(String current, ArrayList<String> possibilities, StringBuilder result) {
        ArrayList<String> nextPossibilities = new ArrayList<>(possibilities);
        if (nextPossibilities.size()== 1) {
            result.append(current);
            return true;
        }
        nextPossibilities.remove(current);

        for (String next: nextPossibilities) {
            if (next.toLowerCase().substring(0,1).equals(current.toLowerCase().substring(current.length()-1))) {
                if  (found(next,nextPossibilities,result)) {
                    result =result.insert(0,current + " ");
                    return true;
                }
            }
        }
        return false;
    }
}

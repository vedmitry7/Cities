package com.vedmitryapps.cities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class Test {
    public static void main(String[] args) {
        String s = null;
        try {
            s = read("countriesToCities.json");
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException e");
            e.printStackTrace();
        }

        System.out.println(s);
    }

    public static String read(String fileName) throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        try {
            BufferedReader in = new BufferedReader(new FileReader( fileName));
            try {
                String s;
                while ((s = in.readLine()) != null) {
                    sb.append(s);
                    sb.append("\n");
                }
            } finally {
                in.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        return sb.toString();
    }
}

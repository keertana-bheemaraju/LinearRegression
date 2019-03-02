package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CsvLoader {

    /*
    Courtesy : https://stackoverflow.com/questions/40074840/reading-a-csv-file-into-a-array
     */
    public List<List<String>> loadCsv(String fileName) {

        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        // this gives you a 2-dimensional array of strings
        List<List<String>> lines = new ArrayList<>();
        FileInputStream inputStream;

        try {
            inputStream = new FileInputStream(file);
            UnicodeBOMInputStream ubis = new UnicodeBOMInputStream(inputStream);

            InputStreamReader isr = new InputStreamReader(ubis);
            BufferedReader br = new BufferedReader(isr);
            ubis.skipBOM();
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                // this adds the currently parsed line to the 2-dimensional string array
                lines.add(Arrays.asList(values));
            }

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lines;
    }

    public List<List<Double>> getDoubleMatrix(List<List<String>> stringMatrix) {

        List<List<Double>> doubleMatrix = new ArrayList<>();

        for (List<String> stringList : stringMatrix) {

            List<Double> doubleList = new ArrayList<>();

            for (String stringVal : stringList) {
                try {

                    Double doubleVal = Double.parseDouble(stringVal.trim());
                    doubleList.add(doubleVal);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            doubleMatrix.add(doubleList);
        }

        return doubleMatrix;
    }
}

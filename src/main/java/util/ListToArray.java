package util;

import java.util.List;

public class ListToArray {

    public static double[][] get2DArrayFromArrayList(List<List<Double>> inputList) {

        double[][] array = new double[inputList.size()][];
        for (int i = 0; i < inputList.size(); i++) {
            List<Double> row = inputList.get(i);
            array[i] = new double[row.size()];
            for (int j = 0; j < array[i].length; j++) {
                array[i][j] = row.get(j);
            }
        }

        return array;
    }

    public static double[] getArrayFromArrayList(List<Double> inputList) {

        double [] array = new double[inputList.size()];
        for(int i=0; i<array.length; i++) {
            array[i] = inputList.get(i);
        }

        return array;
    }


}

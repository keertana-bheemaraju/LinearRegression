package machine_learning_models.linear_regression;

import machine_learning_models.linear_regression.pojo.DataSet;
import machine_learning_models.linear_regression.pojo.TestingDataSet;
import machine_learning_models.linear_regression.pojo.TrainingDataSet;
import org.ejml.simple.SimpleMatrix;
import util.CsvLoader;
import util.ListToArray;

import java.util.ArrayList;
import java.util.List;

public class LinearRegressionModel {

    private final String trainingData;
    private final String testingData;
    private TrainingDataSet trainingDataSet;
    private TestingDataSet testingDataSet;
    private List<Double> w_vector = new ArrayList<>();
    private double L2_training;
    private double L2_validation;

    public LinearRegressionModel(String trainingData, String testingData) {
        this.trainingData = trainingData;
        this.testingData = testingData;
    }

    public double calculateL2(DataSet dataSet) throws Exception {

        List<Double> actualOutput = dataSet.getActualOutput();
        List<Double> calculatedOutput = dataSet.getCalculatedOutput();

        if (actualOutput.size() != calculatedOutput.size()) {
            throw new Exception("actual and calculated outputs don't match");
        }

        double L2 = 0;
        for (int i = 0; i < actualOutput.size(); i++) {
            double delta = calculatedOutput.get(i) - actualOutput.get(i);
            L2 += (delta * delta);
        }

        return  L2;

    }

    public void buildModel() {

        // y = w0 + w1x1 + w2x2 + ...
        List<List<Double>> lists_x = trainingDataSet.getInput();
        List<Double> list_y_calculated = new ArrayList<>();

        // construct w0 + w1x1 + w2x2 + ...
        double w0 = w_vector.get(0);
        for (List<Double> list_x : lists_x) {
            double y_calc = 0;
            // Start from x1, as x0 is always 1
            for (int j = 1; j < list_x.size(); j++) {
                y_calc += w_vector.get(j) * list_x.get(j);
            }
            y_calc += w0;

            list_y_calculated.add(y_calc);
        }

        trainingDataSet.setCalculatedOutput(list_y_calculated);
    }


    public void build_W_Vector() {

        // get x array from training list
        double[][] x_array = ListToArray.get2DArrayFromArrayList(trainingDataSet.getInput());

        // get y array from training list
        double[] y_array = ListToArray.getArrayFromArrayList(trainingDataSet.getActualOutput());

        // get SimpleMatrix for x
        SimpleMatrix simpleMatrix_x = new SimpleMatrix(x_array);

        // get SimpleMatrix for y
        SimpleMatrix simpleMatrix_y = new SimpleMatrix(y_array.length, 1, true, y_array);

        // get Transpose of x
        SimpleMatrix x_transpose = simpleMatrix_x.transpose();

        // multiply xT.x
        SimpleMatrix xxt = x_transpose.mult(simpleMatrix_x);

        // invert xxt
        SimpleMatrix xxt_inverse = xxt.invert();

        // multiply xxt_inverted with xt
        SimpleMatrix a = xxt_inverse.mult(x_transpose);

        // multiply a with y
        SimpleMatrix w = a.mult(simpleMatrix_y);

        for (int i = 0; i < w.getNumElements(); i++) {
            w_vector.add(w.get(i));
        }

    }

    public void createTrainingDataSet(List<List<Double>> doubleMatrix) {

        List<List<Double>> input = new ArrayList<>();

        List<Double> actualOutput = new ArrayList<>();

        buildLists(doubleMatrix, input, actualOutput);

        trainingDataSet = new TrainingDataSet(input, actualOutput);

    }

    public void createTestingDataset(List<List<Double>> doubleMatrix) {

        List<List<Double>> input = new ArrayList<>();

        List<Double> actualOutput = new ArrayList<>();

        buildLists(doubleMatrix, input, actualOutput);

        testingDataSet = new TestingDataSet(input, actualOutput);
    }


    private void buildLists(List<List<Double>> doubleMatrix, List<List<Double>> input, List<Double> actualOutput) {

        for (List<Double> doubleList : doubleMatrix) {

            List<Double> inputRow = new ArrayList<>();
            boolean firstValue = true;

            for (Double doubleVal : doubleList) {
                if (firstValue) {
                    actualOutput.add(doubleVal);
                    firstValue = false;
                } else {
                    inputRow.add(doubleVal);
                }
            }
            input.add(inputRow);
        }

    }


    public void train() {

    }

    public void validate() {

    }

    public void process() throws Exception{

        CsvLoader csvLoader = new CsvLoader();
        List<List<String>> stringMatrix = csvLoader.loadCsv(trainingData);
        List<List<Double>> doubleMatrix = csvLoader.getDoubleMatrix(stringMatrix);

        // create training dataset
        createTrainingDataSet(doubleMatrix);

        // create testing dataset
        createTestingDataset(doubleMatrix);

        // build w-vector
        build_W_Vector();

        // build the linear regression model
        buildModel();

        //calculate L2 for training dataset
        L2_training = calculateL2(trainingDataSet);


    }


}

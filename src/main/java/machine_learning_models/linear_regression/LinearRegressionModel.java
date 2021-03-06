package machine_learning_models.linear_regression;

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

    private final double LAMBDA = -80.0;
    private double costFunction;

    public LinearRegressionModel(String trainingData, String testingData) {
        this.trainingData = trainingData;
        this.testingData = testingData;
    }


    /**
     * The idea of this tuning module is to add the cost function to each calculated y value, in a hope to bring it closer to the expected value
     */
    public void tuneModel() {

        List<Double> calculatedOutput = trainingDataSet.getCalculatedOutput();
        List<Double> tunedOutput = new ArrayList<>();
        for (double val : calculatedOutput) {
            double tunedVal = val + costFunction;
            tunedOutput.add(tunedVal);
        }

        trainingDataSet.setTunedOutput(tunedOutput);
    }


    public double calculateL2ForTraining(TrainingDataSet dataSet, boolean tuned) throws Exception {

        List<Double> actualOutput = dataSet.getActualOutput();
        List<Double> output;

        if(tuned) {
            output = dataSet.getTunedOutput();
        } else {
            output = dataSet.getCalculatedOutput();
        }

        if (actualOutput.size() != output.size()) {
            throw new Exception("actual and calculated outputs don't match");
        }

        double L2 = 0;
        for (int i = 0; i < actualOutput.size(); i++) {
            double delta = Math.abs(output.get(i) - actualOutput.get(i));
            L2 += (delta * delta);
        }

        return L2;

    }

    public double calculateL2ForValidation(TestingDataSet dataSet) throws Exception {

        List<Double> actualOutput = dataSet.getActualOutput();
        List<Double> output;

        output = dataSet.getCalculatedOutput();

        if (actualOutput.size() != output.size()) {
            throw new Exception("actual and calculated outputs don't match");
        }

        double L2 = 0;
        for (int i = 0; i < actualOutput.size(); i++) {
            double delta = Math.abs(output.get(i) - actualOutput.get(i));
            L2 += (delta * delta);
        }

        return L2;

    }

    public void trainModel() {

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

    public void validate() {

        // y = w0 + w1x1 + w2x2 + ...
        List<List<Double>> lists_x = testingDataSet.getInput();
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

        testingDataSet.setCalculatedOutput(list_y_calculated);
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

    public void process() throws Exception {

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
        trainModel();

        //calculate L2 for training dataset
        double L2_training = calculateL2ForTraining(trainingDataSet, false);

        System.out.println("L2training is " + L2_training);

        //calculate L2/N for training dataset
        double L2byN_training = L2_training / trainingDataSet.getInput().size();

        System.out.println("L2/N training is " + L2byN_training);

        // calculate cost function
        costFunction = L2byN_training + (LAMBDA * 5);

        System.out.println("Cost function is " + costFunction);

        // tune the model
        tuneModel();

        //calculate L2 for tuning dataset
        double L2_tuned = calculateL2ForTraining(trainingDataSet, true);

        System.out.println("L2 tuned  is " + L2_tuned);

        //calculate L2/N for tuned dataset
        double L2byN_tuned = L2_tuned / trainingDataSet.getInput().size();

        System.out.println("L2/N tuned is " + L2byN_tuned);

        // validate the linear regression model
        validate();

        //calculate L2 for testing dataset
        double L2_validation = calculateL2ForValidation(testingDataSet);

        System.out.println("L2 Testing is " + L2_validation);

        //calculate validationScore
        double validationScore = L2_validation/ testingDataSet.getInput().size();

        System.out.println("Validation score is " + validationScore);

    }


}

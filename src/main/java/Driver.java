import machine_learning_models.linear_regression.LinearRegressionModel;

public class Driver {

    public static void main(String args[]) {

        LinearRegressionModel machineLearningModel = new LinearRegressionModel("trainingData_truncated.csv", "testingData_truncated.csv");

        try {
            machineLearningModel.process();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

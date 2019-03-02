package machine_learning_models.linear_regression.pojo;

import java.util.List;

public class TrainingDataSet extends DataSet {


    public TrainingDataSet (List<List<Double>> input, List<Double> actualOutput) {
        super(input, actualOutput);
    }
    private double delta;

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }
}

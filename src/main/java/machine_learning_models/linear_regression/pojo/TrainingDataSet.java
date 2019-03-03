package machine_learning_models.linear_regression.pojo;

import java.util.List;

public class TrainingDataSet extends DataSet {


    public TrainingDataSet (List<List<Double>> input, List<Double> actualOutput) {
        super(input, actualOutput);
    }

    private List<Double> tunedOutput;


    public List<Double> getTunedOutput() {
        return tunedOutput;
    }

    public void setTunedOutput(List<Double> tunedOutput) {
        this.tunedOutput = tunedOutput;
    }
}

package machine_learning_models.linear_regression.pojo;

import java.util.List;

public class DataSet {

    protected List<List<Double>> input;
    protected List<Double> actualOutput;
    protected List<Double> calculatedOutput;

    public DataSet(List<List<Double>> input, List<Double> actualOutput) {
        this.input = input;
        this.actualOutput = actualOutput;
    }

    public List<List<Double>> getInput() {
        return input;
    }

    public void setInput(List<List<Double>> input) {
        this.input = input;
    }

    public List<Double> getActualOutput() {
        return actualOutput;
    }

    public void setActualOutput(List<Double> actualOutput) {
        this.actualOutput = actualOutput;
    }

    public List<Double> getCalculatedOutput() {
        return calculatedOutput;
    }

    public void setCalculatedOutput(List<Double> calculatedOutput) {
        this.calculatedOutput = calculatedOutput;
    }
}

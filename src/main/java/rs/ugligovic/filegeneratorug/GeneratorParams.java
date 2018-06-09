/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ugligovic.filegeneratorug;

import java.util.Arrays;

/**
 *
 * @author UrosGligovic
 */
public class GeneratorParams {

    private String path;
    private long goalSize = 1;
    private String sizeUnit = "kb";
    private long sizePrecisionInLines = 1000;
    private long flushFrequency = 1000;

    public GeneratorParams() {
    }

    private int unitMultiplier() {
        switch (sizeUnit.toLowerCase()) {
            case "kb":
                return 1024;
            case "mb":
                return 1024 * 1024;
            case "gb":
                return 1024 * 1024 * 1024;
            default:
                return 1;
        }
    }

    public GeneratorParams(String[] args) {

        path = args[0]; 
        sizeUnit = args.length > 2 ? args[2] : "b";
        goalSize = args.length > 1 ? Long.valueOf(args[1]) : 1;
        goalSize = goalSize * unitMultiplier();
        sizePrecisionInLines = args.length > 3 ? Long.valueOf(args[3]) : 1000;
        flushFrequency = args.length > 4 ? Long.valueOf(args[4]) : 1000;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getGoalSize() {
        return goalSize;
    }

    public void setGoalSize(long goalSize) {
        this.goalSize = goalSize;
    }

    public long getSizePrecision() {
        return sizePrecisionInLines;
    }

    public void setSizePrecision(long sizePrecision) {
        this.sizePrecisionInLines = sizePrecision;
    }

    public long getFlushFrequency() {
        return flushFrequency;
    }

    public void setFlushFrequency(long flushFrequency) {
        this.flushFrequency = flushFrequency;
    }

}

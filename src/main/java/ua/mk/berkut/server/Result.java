package ua.mk.berkut.server;

import java.time.LocalDateTime;
import java.util.List;

public class Result {

    private int correct;
    private int total;
    private int wrong;
    private String fio;
    private String group;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    private List<TriBean> result;

    public Result() {
    }

    public Result correct(int value) {
        this.correct = value;
        return this;
    }

    public Result total(int value) {
        this.total = value;
        return this;
    }

    public Result wrong(int value) {
        this.wrong = value;
        return this;
    }

    public Result fio(String value) {
        this.fio = value;
        return this;
    }

    public Result group(String value) {
        this.group = value;
        return this;
    }

    public Result startTime(LocalDateTime value) {
        this.startTime = value;
        return this;
    }

    public Result finishTime(LocalDateTime value) {
        this.finishTime = value;
        return this;
    }

    public int getCorrect() {
        return correct;
    }

    public int getTotal() {
        return total;
    }

    public int getWrong() {
        return wrong;
    }

    public String getFio() {
        return fio;
    }

    public String getGroup() {
        return group;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    public List<TriBean> getResult() {
        return result;
    }

    public Result result(List<TriBean> result) {
        this.result = result;
        return this;
    }
}

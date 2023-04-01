package EX2.EX2_2;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @authors Dor Yanay, Yevgeny Ivanov
 * this Program was written for Object Orienting Programming Course as an Assignment.
 * Sub Classes:
 * TaskType.java - enum that we were given.
 * for more information and diagrams you can look at the readme in the GitHub page.
 */

public class Task<T> extends FutureTask<T> implements Callable<T>, Comparable<Task<T>> {
    private TaskType type;
    private Callable<T> cable;

    //Constructors
    private Task(Callable<T> cable) {
        this(TaskType.OTHER, cable);
    }

    private Task(TaskType type, Callable<T> cable) {
        super(cable);
        this.cable = cable;
        this.type = type;
    }

    //Getters and Setters
    public TaskType getType() {
        return type;
    }

    public void setType(TaskType tasktype) {
        this.type = type;
    }

    public Callable<T> getCallable() {
        return cable;
    }

    public void setCallable(Callable<T> cable) {
        this.cable = cable;
    }

    //Methods.
    @Override
    public int hashCode() {
        return Objects.hash(type, cable);
    }

    public boolean equals(Task<T> type) {
        if (this.compareTo(type) == 0) {
            return true;
        }
        return false;
    }


    @Override
    public T call() throws Exception {
        return cable.call();
    }

    //Methods that was "inherited" from TaskType
    public TaskType getTaskType() {
        return type.getType();
    }

    @Override
    public int compareTo(Task other) {
        if (this.type.getPriorityValue() > other.type.getPriorityValue())
            return 1;
        if (this.type.getPriorityValue() < other.type.getPriorityValue())
            return -1;
        return 0;
    }

    //Factory methods:
    public static <T> Task<T> createTask(Callable<T> cable) {
        return new Task<T>(cable);
    }

    public static <T> Task<T> createTask(Callable<T> cable, TaskType type) {
        return new Task<T>(type, cable);
    }

}

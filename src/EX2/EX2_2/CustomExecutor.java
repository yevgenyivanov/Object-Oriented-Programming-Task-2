

package EX2.EX2_2;

import java.util.concurrent.*;
import java.util.concurrent.Callable;

/**
 * @authors Dor Yanay, Yevgeny Ivanov
 * this Program was written for Object Orienting Programming Course as an Assignment.
 * CustomExecutor job is to maintain a task queue that arranges the elements according to their priority from low to high at any given moment.
 * for more information and diagrams you can look at the readme in the GitHub page.
 * Sub Classes:
 * Task.java - Task Generator(we use this class to generate the tasks).
 */
public class CustomExecutor extends ThreadPoolExecutor {
    private static int numOfCores = Runtime.getRuntime().availableProcessors();
    private static PriorityBlockingQueue Qt;
    private static int max = 0;
    private static int mid = 0;
    private static int low = 0;

    //Constructor
    public CustomExecutor() {
        super(numOfCores / 2, numOfCores - 1, 300, TimeUnit.MILLISECONDS,
                Qt = new PriorityBlockingQueue<>());
    }
//submit and check priority of the task you're submitting.

    /**
     * @param task - the task we want to submit(get in the queue)
     * @return task.
     */
    public <T> Future<T> submit(Task<T> task) {
        int priority = task.getType().getPriorityValue();
        if (priority == 1) {
            max++;
        } else if (priority == 2) {
            mid++;
        } else if (priority >= 3) {
            low++;
        }
        if (task == null || task.getCallable() == null) throw new NullPointerException();
        execute(task);
        return task;
    }

    public <T> Future<T> submit(Callable<T> cable) {
        Task<T> task = Task.createTask(cable);
        return submit(task);
    }

    public <T> Future<T> submit(Callable<T> cable, TaskType type) {
        Task<T> task = Task.createTask(cable, type);
        return submit(task);
    }

    //notifyThread + afterExecute are using to update the variables so we could check the current max priority in getCurrentMax function.
    public void notifyThread() {
        int priority = getCurrentMax();
        if (priority == 1) {
            max--;
        } else if (priority == 2) {
            mid--;
        } else if (priority == 3) {
            low--;
        }
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        notifyThread();
    }


    public int getCurrentMax() {
        if (max > 0) {
            return 1;
        } else if (mid > 0) {
            return 2;
        } else if (low > 0) {
            return 3;
        }
        return 0;
    }

    //Waiting 3 seconds and then shutting down the program. the wait is to let the active threads finish their tasks.
    public void gracefullyTerminate() {
        try {
            super.awaitTermination(3000, TimeUnit.MILLISECONDS);
            super.shutdown();
        } catch (InterruptedException e) {
            System.err.println(e);
        }

    }
}

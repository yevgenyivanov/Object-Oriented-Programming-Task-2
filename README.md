# Java Object Oriented Programming Task No.2
### Authors:
[Yevgeny Ivanov](https://github.com/yevgenyivanov)
[Dor Yanay](https://github.com/DorYanay)


## Description
This project consists of two parts:
**Part I**	: Ex2_1.java , MyThread.java , MyThreadPoolCallable.java
**Part II**	: Task.java , TaskType.java , CustomExecutor.java
#### Part I
In this part, Ex2_1 generates a specified number of text files, then proceed to iterate over these files using 3 methods:
- Linear
- Single-use Threading
- Fixed Threadpool


###### **Linear**
After generation the getNumOfLines() method receives a String array containing the files’ names, and proceeds to iterate over each file, calculating the number of lines in each file using BufferedReader.
(Note: we chose to use BufferedReader as opposed to Scanner due to its higher efficiency and better time complexity).
This is done by one for() loop and the files are read consecutively. After each file read the total number of lines is added to sumoflines, which is returned after all files have been iterated.

###### **Single-use Threading**
By using two for() loops, the function generates n amount of single-use threads in the first for() loop and joins all these threads to the main thread in the second for() loop.
(Note: n is total number of files in fileNames).
This is achieved by creating MyThread objects (which inherit from Thread) and initiating their run() methods by running currentFileThread[i].start().
After all threads have been initiated, the second for() loop performs a join() of MyThread[i], thus temporarily stopping the main thread execution to allow each run() to complete execution and adding to the totallines read.
(Note: Because run() is void method, each MyThread holds an integer for saving the total lines that specific file contains. This integer is calculated after running and returned using returnLines() after the thread has been joined).



###### **Fixed Threadpool**
By using two for() loops, the function generates a fixed thread pool with size n and using a Future array, submits each file from the received fileNames array to the generated thread pool by creating a new MyThreadPoolCallable.(Note: n is total number of files in fileNames).
The former is type of Callable object whose purpose is to calculate a number of lines in a given text file. This operation is performed by the call() method in MyThreadPoolCallable.
In the second for() loop Integer numOfLinesInFile performs get() method which saves the result of the aforementioned call(). The result is then added to totalLines.
After all threads has returned their values, the thread pool is shutdown and the total lines of all files is returned.

######### Performed Runs and Time Differences

Run 1:
10 files, seed 1 , bound 100,000
Linear calculation:	 62 milliseconds
Regular Threading:	 13 milliseconds
Thread Pool:		 20 milliseconds

Run 2:
100 files, seed: 1, bound: 150,000
Linear calculation:	 572 milliseconds
Regular Threading:	 112 milliseconds
Thread Pool:		 98 milliseconds

Run 3:
1000 files, seed: 1, bound: 200,000
Linear calculation:	 5331 milliseconds
Regular Threading:	 827 milliseconds
Thread Pool:		 620 milliseconds

Run 4:
2000 files, seed: 1, bound: 200,000
Linear calculation:	 10,962 milliseconds
Regular Threading:	 1,387 milliseconds
Thread Pool:		 1,161 milliseconds

######### Explanation for Run-times
After performing many runs, for the vast majority of them(and when rerunning with the same parameters) The run-rime of Thread Pool is faster than Regular Threading.
Based on this, the instantaneous conclusion is that execution times of Regular Threading and Thread Pool are much faster when compared to Linear Calculation ( I.e. calculation of total lines in all files using one thread).
Our assumption for why Thread Pool being faster than Regular Threading is due to:
- Thread Pool calls the call() method which returns the value of lines in a file after finishing the calculations, but since Regular Threading calls the run() method, which doesn’t return any value, another method must be called after performing the calculations. Thus, there’s a total of two operations in opposed to one when using Thread Pool
- Thread Pool requests resources from the operating system for reusable threads, and upon receiving them, sends each operation from the operation queue into an available thread, then all the results are available without halting main thread’s execution. On the other hand, Regular Threading spends time opening n amount of threads and then prevents the main thread from executing by performing join() for each thread.

###### Part I Diagram
![alt text](https://github.com/DorYanay/OOP.Assignment2/blob/main/src/EX2/partAdiagram.png?raw=true)


#### Part II
In this part (in folder EX2_2), CustomExecutor.java is a modifed Executor that executes user-created tasks based on their priority. The priority of the tasks is defined by a task-type which, when inserted in to custom executor’s queue, gets compared to other tasks that are already queued.


###### Task generation
Each task, generated by a user is created by the createTask() method. This method can receive:
- Callable only. In this case a default value of Other is assigned as a TaskType.
- Callable and TaskType.
  *Why is Task a **Generic** that inherits from **FutureTask** and implements the **Callable** as well as **Comparable** interfaces?*
- Making Task a Generic class, allows casting from user’s main() in order to define and receive casted values, which in turn allow callables with varying return statements without restriction.
- Implementing from the Callable and Comparable interfaces allows the task to be performed by the call() method and compared to one another based on their task-type values
- Inhereting from FutureTask makes Task both Runnable and Future type object. Thanks to this, the task can be executed in the threadpool and return value when performing a get() method on a variable in the user’s main().

###### Task Type and
Every task is given a type (or **_priority_**)  either by the user, or by default.
Tasks are of three types:
-Computational
-I/O
-Other

Based on the order above, the priority of each task is compared to other tasks in the CustomExecutor’s priority queue, where Computational are of highest priority and Other are of lowest.
If a task is created without priority, it’s assigned a default value of Other.
Note: When passing a task to CustomExcutor we referred only to its priority value and not string value.
Tasks are compared based on their assigned task-type value by CustomExecutor, allowing execution that is based on priority

###### CustomExecutor
Because Java’s Thread Pool doesn’t support executing threads based on priority, by creating a thread as a Task (and because Task is comparable) the PriorityBlockingQueue, which is created when a new CustomExecutor is created, can perform comparison and give preference to certain tasks in order to execute them before others. Because of this, CustomExecutor requests a large amount of the total processing power that is given towards the JVM by the operating system. Specifically:

- Half of the processors that are dedicated towards JVM by the operating system, as a minimum of available threads that can execute received tasks.
  -The total number of processors dedicated towards JVM minus 1, as a maximum of available threads that can execute received tasks.
  In addition, CustomExecutor keeps track of the top priority which exsits in the queue at any given moment. It does this by three integers:
- max
- mid
- low
  Each in accordance with the priority values of TaskType.
  When a task is submitted to the CustomExecutor, before it’s executed by CustomExecutor’s threadpool, based on it’s task-type value, the according integer is incemented.
  When a task completes excution, based on the executed task’s priority value, the according integer is decremented.
  Because of this, if a user is to request the top priority task that is queued to execute (this can be done by running CustomExecutor’s getCurrentMax() method), they’ll know what task type is at the head of the queue (because the queue is of priorityBlockingQueue type).

###### Part II Diagram
![alt text](https://github.com/DorYanay/OOP.Assignment2/blob/main/src/EX2/EX2_2/Partbdiagram.png?raw=true)



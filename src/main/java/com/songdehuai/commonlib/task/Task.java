package com.songdehuai.commonlib.task;

/**
 * Created by songdehuai on 19/04/12.
 * @author songdehuai
 */
public class Task {
    private static TaskController mTaskController;

    private Task() {
    }

    public static TaskController task() {
        TaskControllerImpl.registerInstance();
        return mTaskController;
    }

    static void setTaskController(TaskController taskController) {
        mTaskController = taskController;
    }
}

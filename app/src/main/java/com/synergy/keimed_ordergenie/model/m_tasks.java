package com.synergy.keimed_ordergenie.model;

/**
 * Created by 1132 on 31-01-2017.
 */

public class m_tasks {

    public Integer getTaskId() {
        return TaskId;
    }

    public void setTaskId(Integer taskId) {
        TaskId = taskId;
    }

    public String getTaskDesc() {
        return TaskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        TaskDesc = taskDesc;
    }

    public Boolean getActive() {
        return Active;
    }

    public void setActive(Boolean active) {
        Active = active;
    }

           Integer TaskId;
           String  TaskDesc;
           Boolean Active;

}

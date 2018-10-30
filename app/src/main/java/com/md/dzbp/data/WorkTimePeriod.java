package com.md.dzbp.data;

import java.io.Serializable;

/**
 * 服务端接受到的作息时间段
 */

public class WorkTimePeriod implements Serializable {

    private String TaskTag;
    private String Name;
    private int Type;
    private String TriggerTime_A;
    private String TriggerTime_B;

    public WorkTimePeriod() {
    }

    public WorkTimePeriod(String taskTag, String name, int type, String triggerTime_A, String triggerTime_B) {
        TaskTag = taskTag;
        Name = name;
        Type = type;
        TriggerTime_A = triggerTime_A;
        TriggerTime_B = triggerTime_B;
    }

    public String getTaskTag() {
        return TaskTag;
    }

    public void setTaskTag(String taskTag) {
        TaskTag = taskTag;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getTriggerTime_A() {
        return TriggerTime_A;
    }

    public void setTriggerTime_A(String triggerTime_A) {
        TriggerTime_A = triggerTime_A;
    }

    public String getTriggerTime_B() {
        return TriggerTime_B;
    }

    public void setTriggerTime_B(String triggerTime_B) {
        TriggerTime_B = triggerTime_B;
    }
}

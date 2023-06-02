/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.http.api.chat;

public class Task {
    private String task;
    private int amount;
    private int initialAmount;
    private String location;

    public String getTask() {
        return this.task;
    }

    public int getAmount() {
        return this.amount;
    }

    public int getInitialAmount() {
        return this.initialAmount;
    }

    public String getLocation() {
        return this.location;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setInitialAmount(int initialAmount) {
        this.initialAmount = initialAmount;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }
        Task other = (Task)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getAmount() != other.getAmount()) {
            return false;
        }
        if (this.getInitialAmount() != other.getInitialAmount()) {
            return false;
        }
        String this$task = this.getTask();
        String other$task = other.getTask();
        if (this$task == null ? other$task != null : !this$task.equals(other$task)) {
            return false;
        }
        String this$location = this.getLocation();
        String other$location = other.getLocation();
        return !(this$location == null ? other$location != null : !this$location.equals(other$location));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Task;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getAmount();
        result = result * 59 + this.getInitialAmount();
        String $task = this.getTask();
        result = result * 59 + ($task == null ? 43 : $task.hashCode());
        String $location = this.getLocation();
        result = result * 59 + ($location == null ? 43 : $location.hashCode());
        return result;
    }

    public String toString() {
        return "Task(task=" + this.getTask() + ", amount=" + this.getAmount() + ", initialAmount=" + this.getInitialAmount() + ", location=" + this.getLocation() + ")";
    }
}


/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.api.events;

import java.util.Arrays;

public final class CommandExecuted {
    private final String command;
    private final String[] arguments;

    public CommandExecuted(String command, String[] arguments) {
        this.command = command;
        this.arguments = arguments;
    }

    public String getCommand() {
        return this.command;
    }

    public String[] getArguments() {
        return this.arguments;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CommandExecuted)) {
            return false;
        }
        CommandExecuted other = (CommandExecuted)o;
        String this$command = this.getCommand();
        String other$command = other.getCommand();
        if (this$command == null ? other$command != null : !this$command.equals(other$command)) {
            return false;
        }
        return Arrays.deepEquals(this.getArguments(), other.getArguments());
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $command = this.getCommand();
        result = result * 59 + ($command == null ? 43 : $command.hashCode());
        result = result * 59 + Arrays.deepHashCode(this.getArguments());
        return result;
    }

    public String toString() {
        return "CommandExecuted(command=" + this.getCommand() + ", arguments=" + Arrays.deepToString(this.getArguments()) + ")";
    }
}


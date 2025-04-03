package org.mvvm.viewmodel.commands;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import java.util.function.Consumer;
public class MemberCommands implements Command{
    public final BooleanProperty trigger = new SimpleBooleanProperty(false); // Initially false
    private final Consumer<String> action; // The actual command action
    public MemberCommands(Consumer<String> action) {
        this.action = action;

        // Automatically execute when canExecute becomes true
        trigger.addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                execute();
            }
        });
    }

    @Override
    public void execute() {
            action.accept(null);
    }

    public void execute(String parameter) {
           action.accept(parameter);
    }
}

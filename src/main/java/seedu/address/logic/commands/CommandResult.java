package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    private final String feedbackToUser;

    /** Help information should be shown to the user. */
    private final boolean showHelp;

    /** The application should exit. */
    private final boolean exit;
    /** Base delete command awaiting Y/N input. */
    private final String pendingDeleteBaseCommand;
    /** Delete criteria awaiting clash index selection. */
    private final String pendingDeleteCriteria;

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit) {
        this(feedbackToUser, showHelp, exit, null, null);
    }

    /**
     * Constructs a {@code CommandResult} with optional delete continuation metadata.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit,
                         String pendingDeleteBaseCommand, String pendingDeleteCriteria) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
        this.pendingDeleteBaseCommand = pendingDeleteBaseCommand;
        this.pendingDeleteCriteria = pendingDeleteCriteria;
    }

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * and other fields set to their default value.
     */
    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, false, false);
    }

    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    public boolean isShowHelp() {
        return showHelp;
    }

    public boolean isExit() {
        return exit;
    }

    public String getPendingDeleteBaseCommand() {
        return pendingDeleteBaseCommand;
    }

    public String getPendingDeleteCriteria() {
        return pendingDeleteCriteria;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CommandResult)) {
            return false;
        }

        CommandResult otherCommandResult = (CommandResult) other;
        return feedbackToUser.equals(otherCommandResult.feedbackToUser)
                && showHelp == otherCommandResult.showHelp
                && exit == otherCommandResult.exit
                && Objects.equals(pendingDeleteBaseCommand, otherCommandResult.pendingDeleteBaseCommand)
                && Objects.equals(pendingDeleteCriteria, otherCommandResult.pendingDeleteCriteria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, showHelp, exit, pendingDeleteBaseCommand, pendingDeleteCriteria);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("feedbackToUser", feedbackToUser)
                .add("showHelp", showHelp)
                .add("exit", exit)
                .add("pendingDeleteBaseCommand", pendingDeleteBaseCommand)
                .add("pendingDeleteCriteria", pendingDeleteCriteria)
                .toString();
    }

}

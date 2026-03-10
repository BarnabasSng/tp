package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Model;
import seedu.address.model.person.PersonHasRolePredicate;

/**
 * Lists all persons of a specific role in the address book to the user.
 */
public class ListRoleCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists all persons of the specified role.\n"
            + "Parameters: players | staff (omit to list all)\n"
            + "Example: " + COMMAND_WORD + " players";

    public static final String MESSAGE_SUCCESS = "Listed all %s";

    private final PersonHasRolePredicate predicate;
    private final String roleDescription;

    /**
     * Creates a ListRoleCommand to list persons with the given {@code PersonHasRolePredicate}.
     */
    public ListRoleCommand(PersonHasRolePredicate predicate, String roleDescription) {
        this.predicate = predicate;
        this.roleDescription = roleDescription;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(String.format(MESSAGE_SUCCESS, roleDescription));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ListRoleCommand)) {
            return false;
        }

        ListRoleCommand otherCommand = (ListRoleCommand) other;
        return predicate.equals(otherCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("predicate", predicate).toString();
    }
}



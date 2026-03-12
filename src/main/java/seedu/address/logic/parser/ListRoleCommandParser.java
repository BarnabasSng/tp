package seedu.address.logic.parser;

import seedu.address.logic.commands.ListRoleCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.PersonHasRolePredicate;
import seedu.address.model.person.Role;

/**
 * Parses input arguments and creates a new ListRoleCommand object.
 */
public class ListRoleCommandParser implements Parser<ListRoleCommand> {

    public static final String VALID_ARG_PLAYER = "players";
    public static final String VALID_ARG_STAFF = "staff";
    public static final String MESSAGE_INVALID_ROLE =
            "Invalid command format. Use 'list' to view all, 'list players' to view players,"
            + " or 'list staff' to view staff.";

    /**
     * Parses the given {@code String} of arguments in the context of the ListRoleCommand
     * and returns a ListRoleCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public ListRoleCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim().toLowerCase();

        switch (trimmedArgs) {
        case VALID_ARG_PLAYER:
            return new ListRoleCommand(new PersonHasRolePredicate(Role.PLAYER), "players");
        case VALID_ARG_STAFF:
            return new ListRoleCommand(new PersonHasRolePredicate(Role.STAFF), "staff");
        default:
            throw new ParseException(MESSAGE_INVALID_ROLE);
        }
    }
}


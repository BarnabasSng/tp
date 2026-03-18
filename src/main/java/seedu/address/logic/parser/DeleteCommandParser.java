package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;
import java.util.Locale;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeleteCommand.DeletionDecision;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteCommand object.
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        String[] tokens = trimmedArgs.split("\\s+");

        Index index = tryParseIndex(tokens[0]);
        if (index != null) {
            return parseIndexBasedDelete(index, tokens);
        }

        return parseCriteriaBasedDelete(tokens);
    }

    private Index tryParseIndex(String token) {
        try {
            return ParserUtil.parseIndex(token);
        } catch (ParseException ignored) {
            return null;
        }
    }

    private DeleteCommand parseIndexBasedDelete(Index index, String[] tokens) throws ParseException {
        if (tokens.length == 1) {
            return new DeleteCommand(index);
        }

        if (tokens.length == 2) {
            String secondToken = tokens[1].toLowerCase(Locale.ROOT);
            if (secondToken.equals(DeleteCommand.CONFIRM_KEYWORD) || secondToken.equals(DeleteCommand.YES_KEYWORD)) {
                return new DeleteCommand(index, DeletionDecision.CONFIRM);
            }

            if (secondToken.equals(DeleteCommand.NO_KEYWORD)) {
                return new DeleteCommand(index, DeletionDecision.CANCEL);
            }
        }

        throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }

    private DeleteCommand parseCriteriaBasedDelete(String[] tokens) throws ParseException {
        int end = tokens.length;
        DeletionDecision decision = DeletionDecision.UNDECIDED;

        if (isYesNoToken(tokens[end - 1])) {
            decision = tokens[end - 1].equalsIgnoreCase(DeleteCommand.YES_KEYWORD)
                    ? DeletionDecision.CONFIRM : DeletionDecision.CANCEL;
            end--;
        }

        Index matchIndex = null;
        if (end > 1 && isPositiveInteger(tokens[end - 1])) {
            matchIndex = ParserUtil.parseIndex(tokens[end - 1]);
            end--;
        }

        if (end <= 0) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        String criteria = String.join(" ", Arrays.copyOfRange(tokens, 0, end));
        return new DeleteCommand(criteria, matchIndex, decision);
    }

    private boolean isYesNoToken(String token) {
        return token.equalsIgnoreCase(DeleteCommand.YES_KEYWORD)
                || token.equalsIgnoreCase(DeleteCommand.NO_KEYWORD);
    }

    private boolean isPositiveInteger(String token) {
        return token.matches("[1-9]\\d*");
    }
}

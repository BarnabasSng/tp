package seedu.address.logic;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.Locale;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data due to the following error: %s";

    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
            "Could not save data to file %s due to insufficient permissions to write to the file or the folder.";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final AddressBookParser addressBookParser;
    private PendingDeleteContext pendingDeleteContext;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        addressBookParser = new AddressBookParser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        String effectiveCommandText = preprocessPendingDeleteInput(commandText.trim());

        CommandResult commandResult;
        Command command = addressBookParser.parseCommand(effectiveCommandText);
        commandResult = command.execute(model);
        updatePendingDeleteContext(commandResult);

        try {
            storage.saveAddressBook(model.getAddressBook());
        } catch (AccessDeniedException e) {
            throw new CommandException(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()), e);
        } catch (IOException ioe) {
            throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
        }

        return commandResult;
    }

    private String preprocessPendingDeleteInput(String trimmedInput) {
        if (pendingDeleteContext == null) {
            return trimmedInput;
        }

        if (pendingDeleteContext.pendingDeleteBaseCommand != null && isYesNo(trimmedInput)) {
            return pendingDeleteContext.pendingDeleteBaseCommand + " " + trimmedInput.toLowerCase(Locale.ROOT);
        }

        if (pendingDeleteContext.pendingDeleteCriteria != null && isPositiveInteger(trimmedInput)) {
            return DeleteCommand.COMMAND_WORD + " " + pendingDeleteContext.pendingDeleteCriteria + " " + trimmedInput;
        }

        pendingDeleteContext = null;
        return trimmedInput;
    }

    private void updatePendingDeleteContext(CommandResult commandResult) {
        String pendingDeleteBaseCommand = commandResult.getPendingDeleteBaseCommand();
        String pendingDeleteCriteria = commandResult.getPendingDeleteCriteria();

        if (pendingDeleteBaseCommand == null && pendingDeleteCriteria == null) {
            pendingDeleteContext = null;
            return;
        }

        pendingDeleteContext = new PendingDeleteContext(pendingDeleteBaseCommand, pendingDeleteCriteria);
    }

    private boolean isYesNo(String input) {
        return input.equalsIgnoreCase(DeleteCommand.YES_KEYWORD)
                || input.equalsIgnoreCase(DeleteCommand.NO_KEYWORD);
    }

    private boolean isPositiveInteger(String input) {
        return input.matches("[1-9]\\d*");
    }

    private static class PendingDeleteContext {
        private final String pendingDeleteBaseCommand;
        private final String pendingDeleteCriteria;

        private PendingDeleteContext(String pendingDeleteBaseCommand, String pendingDeleteCriteria) {
            this.pendingDeleteBaseCommand = pendingDeleteBaseCommand;
            this.pendingDeleteCriteria = pendingDeleteCriteria;
        }
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return model.getAddressBook();
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    @Override
    public Path getAddressBookFilePath() {
        return model.getAddressBookFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }
}

package seedu.address.model.person;

/**
 * Represents the role of a person in the address book.
 */
public enum Role {
    PLAYER, STAFF;
    public static final String MESSAGE_CONSTRAINTS = "Role must be PLAYER or STAFF";

    /**
     * Returns if a given string is a valid role.
     */
    public static boolean isValidRole(String test) {
        for (Role role : Role.values()) {
            if (role.name().equals(test)) {
                return true;
            }
        }
        return false;
    }
}

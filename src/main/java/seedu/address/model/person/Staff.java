package seedu.address.model.person;

import java.util.Set;

import seedu.address.model.tag.Tag;

public class Staff extends Person {
    public Staff(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        super(name, phone, email, address, tags, Role.STAFF);
    }
}
package seedu.address.model.person;

import java.util.Set;

import seedu.address.model.tag.Tag;

public class Player extends Person {
    public Player(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        super(name, phone, email, address, tags, Role.PLAYER);
    }
}

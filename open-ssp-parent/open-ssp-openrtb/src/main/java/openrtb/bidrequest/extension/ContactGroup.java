package openrtb.bidrequest.extension;

import java.util.ArrayList;
import java.util.List;

/**
 * @author André Schmer
 *
 */
public class ContactGroup {

	private final List<Contact> contacts;

	public ContactGroup() {
		contacts = new ArrayList<>();
	}

	public void addContactBuilder(final Contact contactBuilder) {
		contacts.add(contactBuilder);
	}

	public List<Contact> getContacts() {
		return contacts;
	}

}

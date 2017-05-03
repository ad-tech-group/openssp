package openrtb.bidrequest.extension;

/**
 * @author André Schmer
 *
 */

public class ContactGroupBuilder {

	private final ContactGroup contactGroup;

	public ContactGroupBuilder() {
		contactGroup = new ContactGroup();
	}

	public ContactGroupBuilder addContactGroup(final Contact contact) {
		contactGroup.addContactBuilder(contact);
		return this;
	}

	public ContactGroup build() {
		return contactGroup;
	}

}

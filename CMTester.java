
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Calendar;

public class CMTester
{

	@Before
	public void buildCM()
	{
		ContactManager testCM = new ContactManagerImpl();
	}

	@Before
	public void buildmeeting()
	{
		Calendar rightNow = Calendar.getInstance();

		Set<Contact> testParticipants = new HashSet<Contact>();

		String iContact;
		String iNotes;
		for(i=0;i=10;i++)
		{
			iContact = "testContact" + i;
			iNotes = "testNotes" + i;
			Contact testContact = new ContactImpl(iContact,iNotes,i);
			testParticipants.add(testContact);
		}

		Meeting testMeeting = new Meeting(rightNow,testParticipants,1);
	}

	@Before
	public void buildPastMeeting()
	{
		Calendar rightNow = Calendar.getInstance();

		Set<Contact> testParticipants = new HashSet<Contact>();

		String iContact;
		String iNotes;
		for(i=0;i=10;i++)
		{
			iContact = "testContact" + i;
			iNotes = "testNotes" + i;
			Contact testContact = new ContactImpl(iContact,iNotes,i);
			testParticipants.add(testContact);
		}

		PastMeeting testPastMeeting = new PastMeeting(rightNow,testParticipants,"test notes",1);
	}

	@Before
	public void buildContact()
	{
		Contact testContact = new ContactIMPL("Alice", "test notes",1);
	}

	@test
	public void testContactGetID()
	{
		int result = testContact.getID();
		int expected = 1;
		assertEquals(result,expected);
	}

	@test
	public void testContactGetName()
	{
		String result = testContact.getName();
		String expected = "Alice";
		assertEquals(result,expected);
	}

	@test
	public void testContactGetNotes()
	{
		String result = testContact.getNotes();
		String expected = "test notes";
		assertEquals(result,expected);
	}

	@test
	public void testContactAddNotes()
	{
		String eol = System.getProperty("line.seperator");
		testContact.addNotes("additional notes");
		String result = testContact.getNotes();
		String expected = "test notes"+eol+"additional notes";
		assertEquals(result,expected);
	}

	@test
	public void testMeetingGetID()
	{
		int result = testMeeting.getID();
		int expected = 1;
		assertEquals(result,expected);
	}

	@test
	public void testMeetingGetDate()
	{
		Calendar expected = RightNow;
		Calendar result = testMeeting.getDate();
		assertEquals(result, expected);
	}

	@test
	public void getContacts()
	{
		Set<Contact> expected = testParticipants;
		Set<Contact> result = testMeeting.getContacts();
		assertEquals(result, expected);
	}

	@test
	public void testPastMeetingGetNotes()
	{
		String result = testPastMeeting.getNotes();
		String expected = "test notes";
		assertEquals(result,expected);
	}

	@test
	public void testPastMeetingAddNotes()
	{
		String eol = System.getProperty("line.seperator");
		testPastMeeting.addNotes("additional notes");
		String result = testPastMeeting.getNotes();
		String expected = "test notes"+eol+"additional notes";
		assertEquals(result,expected);
	}

	@test
	public void testContactManagerAddFirstContact()
	{
		String name = "Alice";
		String notes = "test notes";
		testCM.addNewContact(name,notes);

		assertTrue(testCM.contactList.contains(testContact));
	}

	@test
	public void testContactManagerAddNewContact()
	{
		String name;
		String notes;
		
		for(i=0;i=1;i++)
		{
			testCM.addNewContact("contact"+i,"notes"+i);
		}

		Contact checkContact0 = new Contact("contact0","notes0",0);
		Contact checkContact1 = new Contact("contact1","notes1",1);
		

		assertTrue(testCM.contactList.contains(checkContact0));
		assertTrue(testCM.contactList.contains(checkContract1));
	}

	@test(expected = NullPointerException.class)
	public void testContactManagerAddNewNullNameContact()
	{
		String name;
		String notes= = "test";
		testCM.addNewContact(name,notes);
	} 

	@test(expected = NullPointerException.class)
	public void testContactManagerAddNewNullNotesContact()
	{
		String name = "test";
		String notes;
		testCM.addNewContact(name,notes);
	} 

	@test
	public void testContactManagerGetContactsID()
	{
		for(i=1;i=10;i++)
		{
			testCM.addNewContact("contact"+i,"notes"+i);
		}

		Set<Contact> result = testCM.getContacts(1,3,7);

		Contact checkContact1 = new Contact("contact1","notes1",1);
		Contact checkContact3 = new Contact("contact3","notes3",3);
		Contact checkContact7 = new Contact("contact7","notes7",7);

		Set<Contact> expected = new HashSet<Contact>();
		expected.add(checkContact1);
		expected.add(checkContact3);
		expected.add(checkContact7);

		assertEquals(result, expected);		
	}

	@test(expected IllegalArgumentException.class)
	public void testContactManagerGetContactsID()
	{
		for(i=1;i=10;i++)
		{
			testCM.addNewContact("contact"+i,"notes"+i);
		}

		Set<Contact> result = testCM.getContacts(1,3,20);

		Contact checkContact1 = new Contact("contact1","notes1",1);
		Contact checkContact3 = new Contact("contact3","notes3",3);

		assertTrue(testCM.result.contains(checkContact1));
		assertTrue(testCM.result.contains(checkContact3));		
	}

	@test
	public void testContactManagerGetContactsString()
	{

		testCM.addNewContact("contact1","notes3");

		for(i=1;i=2;i++)
		{
			testCM.addNewContact("contact"+i,"notes"+i);
		}

		Contact checkContact3 = new Contact("contact1","notes3",1);
		Contact checkContact1 = new Contact("contact1","notes",2);
		Contact checkContact2 = new Contact("contact2","notes2",3);

		Set<Contact> expected = new HashSet<Contact>();
		expected.add(checkContact3);
		expected.add(checkContact1);

		Set<Contact> result = tsetCM.getContacts("Contact1");		


		assertEquals(expected, result);

		result = testCM.getcontacts(C);

		expected.add(checkContact2);

		assertEquals(expected, result);
	}
















































}

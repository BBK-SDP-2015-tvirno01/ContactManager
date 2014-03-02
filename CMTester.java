
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































}

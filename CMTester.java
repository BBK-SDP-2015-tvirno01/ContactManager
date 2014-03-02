
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Calendar;

public class CMTester
{

	@Before
	public void buildup()
	{
		Calendar rightNow = Calendar.getInstance();
		ContactManager testCM = new ContactManagerImpl();
		Contact testContact = new ContactIMPL("Alice", "test notes",1);
		Meeting testMeeting = new Meeting(rightNow,testParticipants,1);
	}

	@After
	public void cleanup()
	{
		
	}
}

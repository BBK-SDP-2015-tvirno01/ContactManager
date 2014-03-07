
import java.util.Calendar;
import java.util.Set;

/**
*Implementation of PastMeeting Interface, extends Meeting to include Meeting notes
*/
public class PastMeetingImpl extends MeetingImpl implements PastMeeting
{
	/**
	*Meeting notes can be added to instances of past Meetings
	*/
	private String meetingNotes;

	/**
	*Constructor creates new instance of PastMeeting
	*@param date Date of the Meeting
	*@param participants Meeting participants
	*@param notes Meeting notes taken by user regarding the Meeting
	*@param ID Unique Meeting ID
	*/
	public PastMeetingImpl(Calendar date,Set<Contact> participants, String notes, int ID)
	{
		super(date, participants, ID);
		String eol = System.getProperty("line.seperator");
		this.meetingNotes = notes;
	}

	public String getNotes()
	{
		String eol = System.getProperty("line.seperator");

		if(this.meetingNotes==null)
		{
			return "";
		}else{
			return 	this.meetingNotes;
		}
	}

	public void addNotes(String text)
	{
		String eol = System.getProperty("line.seperator");
		
		if(this.meetingNotes==null)
		{
			this.meetingNotes = text;
		}else{
			this.meetingNotes = this.meetingNotes + eol + text;
		}
	}

}
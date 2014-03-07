
import java.util.Calendar;
import java.util.Set;

/**
*Implementation of Meeting interface. Used to store the details of a Meeting (Time/Date & Participants)
*/
public class MeetingImpl implements Meeting
{
	/**
	*MeetingID is unique
	*Time/Date & participants for Meeting instance cannot be altered once Meeting is created
	*/
	public final int meetingID;
	public final Calendar meetingDate;
	public final Set<Contact> participants;

	/**
	*Constructor creates new Meeting instance
	*@param date Date of the Meeting
	*@param participants Meeting participants
	*@param ID Unique Meeting ID
	*/
	public MeetingImpl(Calendar date,Set<Contact> participants, int ID)

	{
		this.meetingID = ID;
		this.meetingDate = date;
		this.participants = participants;
	}

	public int getId()
	{
		return this.meetingID;
	}

	public Calendar getDate()
	{
		return this.meetingDate;
	}

	public Set<Contact> getContacts()
	{
		return participants;
	}
}
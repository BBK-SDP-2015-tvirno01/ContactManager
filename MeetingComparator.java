
import java.util.Calendar;
import java.util.Comparator;

public class MeetingComparator<Meeting> implements Comparator<Meeting>
{
	
	public int compare(Meeting m1, Meeting m2)
	{
		MeetingImpl mImpl1 = (MeetingImpl) m1;
		MeetingImpl mImpl2 = (MeetingImpl) m2;

		if(mImpl1.meetingDate.before(mImpl2.meetingDate))
		{
			return 1;
		}else{
			return -1;
		}
	}

}
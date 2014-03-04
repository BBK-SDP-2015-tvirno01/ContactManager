
import java.util.Calendar;
import java.util.Comparator;

public class MeetingComparator implements Comparator<MeetingImpl>
{
	
	public int compare(MeetingImpl m1, MeetingImpl m2)
	{
		if(m1.meetingDate.before(m2.meetingDate))
		{
			return 1;
		}else{
			return -1;
		}
	}

}
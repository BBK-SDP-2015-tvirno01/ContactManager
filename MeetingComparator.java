
import java.util.Comparator;

	/**
	*Interface for classes to compare Meetings
	*no methods, just restricting generics to Meetings
	*/

public interface MeetingComparator<M extends Meeting> extends Comparator<M>
{

}
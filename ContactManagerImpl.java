

import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.File;

public class ContactManagerImpl
{

	private int contactCount;
	private int meetingCount;
	private ArrayList<Contact> contactList;
	private ArrayList<Meeting> meetingList;
	private String contactFile;
	private String meetingFile;
	private String startupFile;

	public ContactManagerImpl()
	{
		contactFile = "."+File.seperator+"contacts.txt";
		this.readLists();
		Writer hookWriter = new Writer(this);
		Thread hook = new Thread(hookWriter);
		addShutdownHook(hook);
	}


	private void readLists()
	{
		File cFile = new File(contactFile);
		BufferedReader cIn = null;
		try
		{			
			cIn = new BufferedReader(new FileReader(cFile));

			String line;

			String[] sFields = new String[1];

			while((line = cIn.readLine()) != "ContactList")
			{
				sFields = line.split("\t");
				this.contactCount = Integer.parseInt(sFields[0]);
				this.meetingCount = Integer.parseInt(sFields[1]);
			}			

			String[] cFields = new String[2];
			String cName;
			int cID;
			String cNotes;

			while((line = cIn.readLine()) != "MeetingList")
			{
				cFields = line.split("\t");
				cID = Integer.parseInt(cFields[0]);
				cName = cFields[1];
				cNotes = cFields[2];
				Contact newContact = new ContactImpl(cName,cNotes,cID);
				contactList.add(newContact);
			}

			String[] mFields = new String[100];
			int[] IDList = new int[97];
			SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd HHmmss");
			Date mDate;
			Calendar mCal = Calendar.getInstance();
			int mID;
			String mNotes;
			
			while((line = cIn.readLine()) != null)
			{
				mFields = line.split("\t");
				mID = Integer.parseInt(mFields[0]);
				mDate = f.parse(mFields[1]);
				mCal = setTime(mDate);
				mNotes = mFields[3];
				
				for(i=3;i=mFields.length();i++)
				{
					if(mFields[i]="")
					{
						break;
					}else{
						IDList[i-3] = mFields[i];
					}
				}

				Set<Contact> participants = getContacts(IDList);

				Meeting newMeeting = new PastMeeting(mCal, participants, mNotes, mID);
				meetingList.add(newMeeting);

				Arrays.fill(mFields,"");
			}
			
		}catch(FileNotFoundException ex){
			System.out.println("MeetingList and ContactList files do not exist");
		}catch(IOException ex){
			ex.printStackTrace();
		}finally{
			closeReader(cIn);
		}
	}

	private void closeReader(Reader reader)
	{
		try
		{
			if(reader != null)
			{
				reader.close();
			}
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}	

	public void writeLists()
	{
		File cFile = new File(contactFile);

		try
		{
			PrintWriter cOut = new PrintWriter(cFile);

			//Clear contents of file prior to archiving
			cOut.write("");	

			String output;

			output = this.contactCount + "\t" + this.meetingCount;
			cOut.println(output);

			cOut.println("ContactList");

			for(Contact c : contactList)
			{
				output = c.contactID + "\t" + c.contactName + "\t" + c.contactNotes;
				cOut.println(output);
			}

			cOut.println("MeetingList");

			SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd HHmmss");

			for(Meeting m : meetingList)
			{
				output = m.meetingID + "\t" + f.format(m.meetingDate);
				
				PastMeeting pM = m; //downcast meeting as past meeting to get notes field
				
				output  = output + "\t" + pM.meetingNotes;

				for(Contact c : m.participants)
				{
					output = output + "\t" + c.contactID;
				}

				cOut.println(output);
			}


		}catch(FileNotFoundException ex){
			System.out.println("Cannot write to file")
		}catch(IOException ex){
			ex.printStackTrace();
		}finally{
			cOut.close();
			mOut.close();
			sOut.close();
		}

	}

	private boolean inPast(Calendar testDate)
	{
		Calendar rightNow = Calendar.getInstance();
		return testDate.before(rightNow);
	}

	private Meeting addNewMeeting(Calendar date, Set<Contact> contacts, String text)
	{
		this.meetingCount = this.meetingCount + 1;
		Meeting nM = new PastMeeting(date, contacts, text, this.meetingCount); //upcast new PastMeeting as Meeting (hence all meetings are instances of eachother)
		Meeting newMeeting = nM;
		return newMeeting;
	}

	public int addFutureMeeting(Set<Contact> contacts, Calendar date)
	{
		try
		{
			if(inPast(date) || !contactList.containsAll(contacts))
			{
				throw IllegalArgumentException;
			}

			FutureMeeting newMeeting = addNewMeeting(date, contacts, ""); //down cast Meeting as FutureMeeting
			this.meetingList.add(newMeeting);
			return newMeeting.getID();

		}catch(IllegalArgumentException ex){
			System.consol.println("Invalid Future Meeting parameters");
		}
	}

	public PastMeeting getPastMeeting(int id)
	{
		try
		{
			Meeting result = this.getMeeting(id);
			if(result.equals(null))
			{
				return null;
			}else{
				if(inPast(result.meetingDate)
				{
					return result;
				}else{
					throw IllegalArgumentException;
				}
			}
		}catch(IllegalArgumentException ex){
			System.out.println("Selected Meeting occurs in the past");
		}
	}

	public FutureMeeting getFutureMeeting(int id)
	{
		try
		{
			Meeting result = this.getMeeting(id);
			if(result.equals(null))
			{
				return null;
			}else{
				if(inPast(result.meetingDate)
				{
					throw IllegalArgumentException;
				}else{
					return result;
				}
			}
		}catch(IllegalArgumentException ex){
			System.out.println("Selected Meeting occurs in the past");
		}
	}

	public Meeting getMeeting(int id)
	{
		for(Meeting m : meetingList)
		{
			if(m.meetingID==int id)
			{
				Meeting result = m;
				return result;
			}
		}

		return null;		
	}	

	public List<Meeting> getFutureMeetingList(Contact contact)
	{
		List<Meeting> result = new ArrayList<Meeting>();
		
		try
		{
			if(!contactList.contains(contact))
			{
				throw IllegalArgumentException;
			}else{
				for(Meeting m : meetingList)
				{
					if(!isPast(m.meetingDate))
					{
						if(m.participants.contains(contact))
						{
							result.add(m.clone());
						}
					}
				}
			}
			
			Comparator<Meeting> cDate = new Comparator<Meeting>
			{
				public int compare(Meeting m1, Meeting m2)
				{
					if(m1.meetingDate.before(m2.meetingDate))
					{
						return 1;
					}else{
						return -1;
					}
				}
			} 
			Collections.sort(result, cDate);

			return result;

		}catch(IllegalArgumentException ex){
			System.out.println("Contact is not a member of contact list");
		}
	}

	public List<Meeting> getFutureMeetingList(Calendar date)
	{
		List<Meeting> result = new ArrayList<Meeting>();
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");

		
		for(Meeting m : meetingList)
		{
			if(f.format(date).equals(f.format(m.meetingDate))
			{
				result.add(m.clone());
			}
		}

			Comparator<Meeting> cDate = new Comparator<Meeting>
			{
				public int compare(Meeting m1, Meeting m2)
				{
					if(m1.meetingDate.before(m2.meetingDate))
					{
						return 1;
					}else{
						return -1;
					}
				}
			} 
			Collections.sort(result, cDate);

			return result;
		
	}	

	public List<PastMeeting> getPastMeetingList(Contact contact)
	{
		List<PastMeeting> result = new ArrayList<PastMeeting>();
		
		try
		{
			if(!contactList.contains(contact))
			{
				throw IllegalArgumentException;
			}else{
				for(Meeting m : meetingList)
				{
					if(isPast(m.meetingDate))
					{
						if(m.participants.contains(contact))
						{
							result.add(m.clone());
						}
					}
				}
			}
			
			Comparator<Meeting> cDate = new Comparator<Meeting>
			{
				public int compare(Meeting m1, Meeting m2)
				{
					if(m1.meetingDate.before(m2.meetingDate))
					{
						return 1;
					}else{
						return -1;
					}
				}
			} 
			Collections.sort(result, cDate);

			return result;

		}catch(IllegalArgumentException ex){
			System.out.println("Contact is not a member of contact list");
		}
	}

	public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text)
	{
		try
		{
			if(contacts.equals(null) || date.equals(null) || text.equals(null))
			{
				throw NullPointerException;
			}

			if(contacts.isEmpty() || !contactList.containsAll(contacts))
			{
				throw IllegalArgumentException;
			}

			this.meetingCount = this.meetingCount + 1;
			PastMeeting newMeeting = new PastMeetingImpl(date, contacts, text, this.meetingCount);

			this.meetingList.add(newMeeting);

		}catch(NullPointerException ex){
			System.out.println("One of the arguments is null");
		}catch(IllegalArgumentException ex){
			System.out.println("Invalid set of contacts");
		}
	}	

	public void addMeetingNotes(int id, String text)
	{
		try
		{
			if(text.equals(null))
			{
				throw NullPointerException;
			}
			
			PastMeeting pMeeting = getMeeting(id); //downcast Meeting as PastMeeting
	
			if(pMeeting.equals(null))
			{
				throw IllegalArgumentException;
			}
	
			if(!isPast(pMeeting.meetingDate)
			{
				throw IllegalStateException;
			}

			pMeeting.addNotes(text);
		}catch(NullPointerException ex){
			System.out.println("Meeting notes are null");
		}catch(IllegalArgumentException ex){
			System.out.println("Meeting ID not recognised");
		}catch(IllegalStateException ex{
			System.out.println("Meeting occurs in the future");
		}
	}

	public void addNewContact(String name, String notes)
	{
		try
		{
			if(name.equals(null) || notes.equals(null))
			{
				throw NullPointerException;
			}
			
			this.contactCount = this.contactCount + 1;
			Contact newContact = new ContactImpl(name,notes,this.contactCount);
			this.contactList.add(newContact);

		}catch(NullPointerException ex){
			System.out.println("An input parameter is null");
		}
	}
	
	public Set<Contact> getContacts(int... ids)
	{
		try
		{
			Set<int> control = new AbstractSet<int>();

			for(int i : ids)
			{
				control.add(i);
			}

			Set<Contact> result = new HashSet<Contact>();

			for(Contact c : this.contactList)
			{
				if(control.contains(c.contactID))
				{
					result.add(c.clone());
					control.remove(c.contactID);
				}
			}

			if(!control.isEmpty())
			{
				throw IllegalArgumentException;
			}

			return result;

		}catch(IllegalArgumentException ex){
			System.out.println("Not all IDs exist in contact list");
		}
	}

	public Set<Contact> getContact(String name)
	{
		try
		{
			if(name.equals(null))
			{
				throw NullPointerException;
			}

			Set<Contact> result = new HashSet<Contact>();

			for(Contact c : this.contactList)
			{
				if(c.contactName.contains(name))
				{
					result.add(c.clone());
				}
			}

			return result;

		}catch(NullPointerException ex){
			System.out.println("Search criteria is null");
		}
	}

	public void flush()
	{
		this.writeLists;
	}

}
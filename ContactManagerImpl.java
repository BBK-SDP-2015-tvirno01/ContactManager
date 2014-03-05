

import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.io.File;
import java.util.Comparator;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class ContactManagerImpl
{

	private int contactCount;
	private int meetingCount;
	private ArrayList<ContactImpl> contactList;
	private ArrayList<MeetingImpl> meetingList;
	private String contactFile;
	private String meetingFile;
	private String startupFile;

	public ContactManagerImpl()
	{
		contactFile = "."+File.separator+"contacts.txt";
		this.readLists();
		Flusher hookWriter = new Flusher(this);
		Thread hook = new Thread(hookWriter);
		Runtime.getRuntime().addShutdownHook(hook);
	}


	private void readLists() throws FileNotFoundException, IOException
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
				ContactImpl newContact = new ContactImpl(cName,cNotes,cID);
				this.contactList.add(newContact);
			}

			String[] mFields = new String[100];
			int[] IDList = new int[97];
			SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd HHmmss");
			Date mDate;
			Calendar mCal = Calendar.getInstance();
			int mID;
			String mNotes;
			int i;
			
			while((line = cIn.readLine()) != null)
			{
				mFields = line.split("\t");
				mID = Integer.parseInt(mFields[0]);
				mDate = f.parse(mFields[1]);
				mCal.setTime(mDate);
				mNotes = mFields[3];
				
				for(i=3;i==99;i++)
				{
					if(mFields[i].equals(""))
					{
						break;
					}else{
						IDList[i-3] = Integer.parseInt(mFields[i]);
					}
				}

				Set<Contact> participants = getContacts(IDList);

				MeetingImpl newMeeting = new PastMeetingImpl(mCal, participants, mNotes, mID);
				this.meetingList.add(newMeeting);

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

	private void writeLists() throws FileNotFoundException, IOException
	{
		File cFile = new File(contactFile);
		PrintWriter cOut = null;
		try
		{
			cOut = new PrintWriter(cFile);
			//Clear contents of file prior to archiving
			cOut.write("");	

			String output;

			output = this.contactCount + "\t" + this.meetingCount;
			cOut.println(output);

			cOut.println("ContactList");

			for(ContactImpl c : contactList)
			{
				output = c.contactID + "\t" + c.contactName + "\t" + c.getNotes();
				cOut.println(output);
			}

			cOut.println("MeetingList");

			SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd HHmmss");

			for(MeetingImpl m : meetingList)
			{
				output = m.meetingID + "\t" + f.format(m.meetingDate);
				
				PastMeetingImpl pM = (PastMeetingImpl) m; //downcast meeting as past meeting to get notes field
				
				output  = output + "\t" + pM.getNotes();

				for(ContactImpl c : m.participants)
				{
					output = output + "\t" + c.contactID;
				}

				cOut.println(output);
			}


		}catch(FileNotFoundException ex){
			System.out.println("Cannot write to file");
		}catch(IOException ex){
			ex.printStackTrace();
		}finally{
			cOut.close();
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
		Meeting nM = new PastMeetingImpl(date, contacts, text, this.meetingCount); 
		Meeting newMeeting = (MeetingImpl) nM;	//upcast new PastMeeting as Meeting (hence all meetings are instances of eachother)
		return newMeeting;
	}

	public int addFutureMeeting(Set<Contact> contacts, Calendar date)
	{
		try
		{
			if(inPast(date) || !contactList.containsAll(contacts))
			{
				throw new IllegalArgumentException();
			}

			MeetingImpl newMeeting = (MeetingImpl) addNewMeeting(date, contacts, ""); //down cast Meeting as FutureMeeting
			this.meetingList.add(newMeeting);
			return newMeeting.getId();

		}catch(IllegalArgumentException ex){
			System.out.println("Invalid Future Meeting parameters");
		}
	}

	public PastMeeting getPastMeeting(int id)
	{
		try
		{
			MeetingImpl queryMeeting = (MeetingImpl) this.getMeeting(id);
			if(queryMeeting.equals(null))
			{
				return null;
			}else{
				if(inPast(queryMeeting.meetingDate))
				{
					PastMeeting result = (PastMeeting) queryMeeting;
					return result;
				}else{
					throw new IllegalArgumentException();
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
			MeetingImpl queryMeeting = (MeetingImpl) this.getMeeting(id);
			if(queryMeeting.equals(null))
			{
				return null;
			}else{
				if(inPast(queryMeeting.meetingDate))
				{
					throw new IllegalArgumentException();
				}else{
					FutureMeeting result = (FutureMeeting) queryMeeting;
					return result;
				}
			}
		}catch(IllegalArgumentException ex){
			System.out.println("Selected Meeting occurs in the past");
		}
	}

	public Meeting getMeeting(int id)
	{
		for(MeetingImpl m : meetingList)
		{
			if(m.meetingID==id)
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
				throw new IllegalArgumentException();
			}else{
				for(MeetingImpl m : meetingList)
				{
					if(!inPast(m.meetingDate))
					{
						if(m.participants.contains(contact))
						{
							result.add(m);
						}
					}
				}
			}
			
			Comparator<MeetingImpl> cDate = new MeetingComparator<MeetingImpl>();

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

		
		for(MeetingImpl m : meetingList)
		{
			if(f.format(date).equals(f.format(m.meetingDate)))
			{
				result.add(m);
			}
		}

			Comparator<MeetingImpl> cDate = new MeetingComparator<MeetingImpl>();

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
				throw new IllegalArgumentException();
			}else{
				for(MeetingImpl m : meetingList)
				{
					if(inPast(m.meetingDate))
					{
						if(m.participants.contains(contact))
						{
							result.add(m);
						}
					}
				}
			}
			
			Comparator<MeetingImpl> cDate = new MeetingComparator<MeetingImpl>();

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
				throw new NullPointerException();
			}

			if(contacts.isEmpty() || !contactList.containsAll(contacts))
			{
				throw new IllegalArgumentException();
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
				throw new NullPointerException();
			}
			
			PastMeetingImpl pMeeting = (PastMeetingImpl) getMeeting(id); //downcast Meeting as PastMeeting
	
			if(pMeeting.equals(null))
			{
				throw new IllegalArgumentException();
			}
	
			if(!inPast(pMeeting.meetingDate))
			{
				throw new IllegalStateException();
			}

			pMeeting.addNotes(text);
		}catch(NullPointerException ex){
			System.out.println("Meeting notes are null");
		}catch(IllegalArgumentException ex){
			System.out.println("Meeting ID not recognised");
		}catch(IllegalStateException ex){
			System.out.println("Meeting occurs in the future");
		}
	}

	public void addNewContact(String name, String notes)
	{
		try
		{
			if(name.equals(null) || notes.equals(null))
			{
				throw new NullPointerException();
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
			Set<int> control = new HashSet<int>();

			for(int i : ids)
			{
				control.add(i);
			}

			Set<Contact> result = new HashSet<Contact>();

			for(ContactImpl c : this.contactList)
			{
				if(control.contains(c.contactID))
				{
					result.add(c);
					control.remove(c.contactID);
				}
			}

			if(!control.isEmpty())
			{
				throw new IllegalArgumentException();
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
				throw new NullPointerException();
			}

			Set<Contact> result = new HashSet<Contact>();

			for(ContactImpl c : this.contactList)
			{
				if(c.contactName.contains(name))
				{
					result.add(c);
				}
			}

			return result;

		}catch(NullPointerException ex){
			System.out.println("Search criteria is null");
		}
	}

	public void flush()
	{
		writeLists();
	}

}
package ua.phonebook.dao.filestorage.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import ua.phonebook.dao.filestorage.FilePhoneBookRecordRepository;
import ua.phonebook.dao.filestorage.comparator.PhoneBookRecordComparatorById;
import ua.phonebook.model.PhoneBookRecord;

@Profile("file")
@Repository
public class FilePhoneBookRecordRepositoryImpl implements FilePhoneBookRecordRepository {

	@Value("${filestorage.phonebookrecords.path}")
	private String filePhoneBookLocation;
	
	private AtomicLong lastSavedId;
	
	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	private final Lock readLock = rwl.readLock();
	private final Lock writeLock = rwl.writeLock();
	
	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	@PostConstruct
	public void createFileIfNotExistentAndAssignLastSavedId(){
		File file = new File(filePhoneBookLocation);
		
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		long numberOfRecords = 0;
		if(file.length()>0){
			numberOfRecords = getAllPhoneBookRecords().size();
		}
		
		lastSavedId= new AtomicLong(numberOfRecords);
	}
	
	@Override
	public Page<PhoneBookRecord> getByUser_Login(String login,Pageable pageable) {
		final int pageNumber = pageable.getPageNumber();
		final int pageSize = pageable.getPageSize();
		
		
		List<PhoneBookRecord> allRecords = getAllPhoneBookRecords();
		
		//Getting all records for particular user
		List<PhoneBookRecord> phoneBookOfUser = 
				allRecords.stream()
							.filter(phoneBookRecord ->
										phoneBookRecord.getUser().getLogin()
												.equals(login))
							.collect(Collectors.toList());
    	Collections.sort(phoneBookOfUser, new PhoneBookRecordComparatorById());
    	
    	List<PhoneBookRecord> paginatedPhoneBook = 
    			getPaginatedPhoneBook(phoneBookOfUser, pageNumber, pageSize);
    	
        Page<PhoneBookRecord> page =
        		new PageImpl<>(paginatedPhoneBook,pageable,phoneBookOfUser.size());
		return page;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PhoneBookRecord save(final PhoneBookRecord phoneBookRecord) {
		boolean itIsUpdating = true;
		if(phoneBookRecord.getId()==0){
			itIsUpdating = false;
			phoneBookRecord.setId(lastSavedId.incrementAndGet());
		}
		
		if(itIsUpdating){
			List<PhoneBookRecord> list = getAllPhoneBookRecords();
			List<PhoneBookRecord> listForSaving= list.stream().filter(record -> record.getId()!=phoneBookRecord.getId())
			.collect(Collectors.toList());
			listForSaving.add(phoneBookRecord);
			writeResultListToFile(listForSaving);
		}else{
			String jsonPhoneBookRecord = gson.toJson(phoneBookRecord);
			writeLock.lock();
			try{
				File file = new File(filePhoneBookLocation);
				
				try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,true))){
					if(file.length()!=0){
						writer.append(",");
					}
					writer.append(jsonPhoneBookRecord);
				}catch (IOException e) {
					e.printStackTrace();
				}
			}finally{
				writeLock.unlock();
			}
		}
		
		return phoneBookRecord;
	}

	@Override
	public void delete(PhoneBookRecord phoneBookRecord) {
	
		List<PhoneBookRecord> list = getAllPhoneBookRecords();
		List<PhoneBookRecord> resultList = 
				list.stream()
				.filter(record -> record.getId()!=phoneBookRecord.getId())
				.collect(Collectors.toList());
		writeResultListToFile(resultList);
		
	}

	@Override
	public PhoneBookRecord findOne(long id) {
		
		Optional<PhoneBookRecord> wrapperRecord = getAllPhoneBookRecords().stream()
								.filter(record -> record.getId() == id)
								.findFirst();
		if(wrapperRecord.isPresent()){
			return wrapperRecord.get();
		}
		return null;
	}

	@Override
	public PhoneBookRecord findByIdAndUser_Login(long id,String login) {
		PhoneBookRecord phoneBookRecord = findOne(id);
		if(phoneBookRecord!=null){
			if(phoneBookRecord.getUser().getLogin().equals(login)){
				return phoneBookRecord;
			}
		}
		return null;
	}
	
	@Override
	public Page<PhoneBookRecord>findFilteredByUserLogin(String login,
														String firstName,
														String lastName, 
														String mobilePhone,
														Pageable pageable) {
		final int pageNumber = pageable.getPageNumber();
		final int pageSize = pageable.getPageSize();
		
		final List<PhoneBookRecord> allRecords = getAllPhoneBookRecords();
		
		//Getting all records for particular user
		List<PhoneBookRecord> phoneBookOfUser = 
				allRecords.stream()
							.filter(phoneBookRecord ->
										phoneBookRecord.getUser().getLogin()
												.equals(login))
							.collect(Collectors.toList());
		
		//Getting filtered user's records
		List<PhoneBookRecord> filteredPhoneBookOfUser = 
				phoneBookOfUser.stream()
						 .filter(record -> record.getFirstName().toUpperCase()
									.contains(firstName.toUpperCase()) &&
									record.getLastName().toUpperCase()
									.contains(lastName.toUpperCase()) &&
									record.getMobilePhone().contains(mobilePhone))
						 .collect(Collectors.toList());

		Collections.sort(filteredPhoneBookOfUser, new PhoneBookRecordComparatorById());
		
	    List<PhoneBookRecord> paginatedPhoneBook = 
	    		getPaginatedPhoneBook(filteredPhoneBookOfUser, pageNumber, pageSize);
        Page<PhoneBookRecord> pageInfo =
        		new PageImpl<>(paginatedPhoneBook,pageable,filteredPhoneBookOfUser.size());		
		return pageInfo;
	}
	
	/**
	 * Returns List of {@code PhoneBookRecord}s, retrieved from 
	 * file, located on {@link #filePhoneBookLocation}
	 */
	private List<PhoneBookRecord> getAllPhoneBookRecords(){
		String wholeFile="";
		readLock.lock();
		try{
			File file = new File(filePhoneBookLocation);
	        if(file.length()==0){
	        	return new ArrayList<>();
	        }
	        
	        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
	        	String str;
	    	    while ((str = reader.readLine()) != null){
	    	    	wholeFile+= str.trim();
	    	    }
	        }catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}finally{
			readLock.unlock();
		} 
	    wholeFile="["+wholeFile+"]";
    	Type type = new TypeToken<List<PhoneBookRecord>>() {}.getType();
    	List<PhoneBookRecord> list = gson.fromJson(wholeFile, type);
		
    	return list;
	}
	
	/**
	 * Replaces contents of File, located on {@link #filePhoneBookLocation},
	 * with converted to json list of {@code PhoneBookRecord}s.
	 * @param resultList list with PhoneBookRecords
	 */
	private void writeResultListToFile(List<PhoneBookRecord> resultList){
		
		writeLock.lock();
		try{
			try(BufferedWriter writer = new BufferedWriter
								(new FileWriter(filePhoneBookLocation,false))){
		    	Type type = new TypeToken<List<PhoneBookRecord>>() {}.getType();
		    	String str = gson.toJson(resultList,type);
		    	str = str.replace('[', ' ')
		    			 .replace(']', ' ')
		    			 .trim();
				writer.write(str);
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}finally{
			writeLock.unlock();
		}
	}

	/**
	 * Gets sublist from {@code initList} for page with number =
	 * {@code pageNumber}(zero-based), if number of elements can't be more than {@code pageSize}
	 * @param initList {@link List}
	 * @param pageNumber - number of page, zero-based
	 * @param pageSize - number of elements on one page
	 */
	private List<PhoneBookRecord> getPaginatedPhoneBook(List<PhoneBookRecord> initList,int pageNumber,int pageSize){
		final int indexOfFirstEl = pageNumber*pageSize;
    	final int indexOfElAfterLast = pageNumber*pageSize + pageSize;
    	
    	List<PhoneBookRecord> paginatedPhoneBook = new ArrayList<>();
    	if(indexOfFirstEl<initList.size()){
    		if(indexOfElAfterLast<initList.size()){
    			paginatedPhoneBook = initList.subList(pageNumber*pageSize, indexOfElAfterLast);
    		}else{
    			paginatedPhoneBook = initList.subList(pageNumber*pageSize, initList.size());
    		}
    	}
    	return paginatedPhoneBook;
	}
}

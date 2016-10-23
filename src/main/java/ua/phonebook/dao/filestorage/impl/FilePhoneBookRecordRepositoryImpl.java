package ua.phonebook.dao.filestorage.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import ua.phonebook.dao.filestorage.FilePhoneBookRecordRepository;
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
		long numberOfRecords = 0;
		
		if(file.length()>0){
			numberOfRecords = getAllPhoneBookRecords().size();
		}
		
		lastSavedId= new AtomicLong(numberOfRecords);
		
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public List<PhoneBookRecord> getByUser_Login(String login) {
		final List<PhoneBookRecord> phoneBookOfUser = new ArrayList<>();
		
		List<PhoneBookRecord> list = getAllPhoneBookRecords();
        	list.forEach(phoneBookRecord ->{
        		if(phoneBookRecord.getUser().getLogin().equals(login)){
        			phoneBookOfUser.add(phoneBookRecord);
        		}
        	});
		return phoneBookOfUser;
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
						writer.append(";");
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
        wholeFile.replace(';', ',');
	    wholeFile="["+wholeFile+"]";
    	Type type = new TypeToken<List<PhoneBookRecord>>() {}.getType();
    	List<PhoneBookRecord> list = gson.fromJson(wholeFile, type);
		
    	return list;
	}
	
	/**
	 * Replaces contents of File, located in {@link #filePhoneBookLocation},
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
		    			 .replaceAll("\\},\\{", "};{")
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


}

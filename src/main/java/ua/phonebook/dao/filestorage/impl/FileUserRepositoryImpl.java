package ua.phonebook.dao.filestorage.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.lang.reflect.Type;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import ua.phonebook.dao.filestorage.FileUserRepository;
import ua.phonebook.model.User;

@Profile("file")
@Repository
public class FileUserRepositoryImpl implements FileUserRepository {

	@Value("${filestorage.users.path}")
	private String fileLocation;

	private AtomicLong lastSavedId;
	
	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	private final Lock readLock = rwl.readLock();
	private final Lock writeLock = rwl.writeLock();
	
	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	@PostConstruct
	public void createFileIfNotExistent() {
		File file = new File(fileLocation);
		try{
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long numberOfUsers = 0;
		if(file.length()>0){
			numberOfUsers = getAllUsersInJson().length;
		}
		
		lastSavedId = new AtomicLong(numberOfUsers);
	}

	@Override
	public User getUserByLogin(String login) {
		String[] jsonUsers = getAllUsersInJson();
		if(jsonUsers==null || jsonUsers.length==0){
			return null;
		}
    	Type type = new TypeToken<User>() {}.getType();
    	for(int i = 0;i<jsonUsers.length;i++){
    		User user = gson.fromJson(jsonUsers[i], type);
    		if(user.getLogin().equals(login)){
    			return user;
    		}
    	}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public User saveAndFlush(User user) {
		user.setId(lastSavedId.incrementAndGet());
		String jsonUser = gson.toJson(user);
		writeLock.lock();
		try{
			File file = new File(fileLocation);
		
			try(FileWriter writer = new FileWriter(file,true)){
				if(file.length()!=0)
					writer.append(";");
				writer.append(jsonUser);
			}catch (IOException e) {
				e.printStackTrace();
			}
		} finally {
			writeLock.unlock();
		}
		return user;
	}

	private String[] getAllUsersInJson(){
		String wholeFile = "";
		readLock.lock();
		try{
	        File file = new File(fileLocation);
	        if(file.length()==0){
	        	return null;
	        }
	        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
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
        String[] jsonUsers = wholeFile.split(";");
    	return jsonUsers;
	}
}

package ua.phonebook.dao.filestorage.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import ua.phonebook.dao.filestorage.FileUserRepository;
import ua.phonebook.model.User;

@Profile("!db")
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

	@Override
	public User saveAndFlush(User user) {
		user.setId(lastSavedId.incrementAndGet());
		String jsonUser = gson.toJson(user);
		writeLock.lock();
		try{
			Path file = Paths.get(fileLocation);
			
			try(BufferedWriter writer = 
					Files.newBufferedWriter(file,StandardOpenOption.WRITE)){
				
				if(new File(fileLocation).length()!=0)
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
	
	/**
	 * Returns array of String, represented {@link User} objects in json.
	 * If file is empty, null is returned.
	 */
	private String[] getAllUsersInJson(){
		List<String> allLines= null;
		readLock.lock();
		try {
			if(new File(fileLocation).length() == 0){
				return null;
			}
			allLines= Files.readAllLines(Paths.get(fileLocation));
		}catch (IOException e1) {
			e1.printStackTrace();
		}finally{
			readLock.unlock();
		}
		StringBuilder resultString = new StringBuilder();
		allLines.forEach(line -> resultString.append(line.trim()));
		
        String[] jsonUsers = resultString.toString().split(";");
    	return jsonUsers;
	}
}

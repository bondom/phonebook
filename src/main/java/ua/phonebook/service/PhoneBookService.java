package ua.phonebook.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import ua.phonebook.model.PhoneBookRecord;
import ua.phonebook.service.exception.InvalidIdentifier;
import ua.phonebook.web.viewbean.FilterPhoneBookRecords;

public interface PhoneBookService {
	
	/**
	 * Returns {@link Page} with {@link List} {@code list} of 
	 * {@link PhoneBookRecord}s and pagination information
	 * according to {@code pageable}.
	 * <br>All elements in {@code list} have relationships with User, login of which
	 * is passed to method.
	 * @param login login of {@link User}, saved in data storage
	 */
	@PreAuthorize("isAuthenticated()")
	public Page<PhoneBookRecord> getPhoneBookByUserLogin(String login,Pageable pageable);
	
	/**
	 * Gets {@link PhoneBookRecord} from data storage, id of which equals to
	 * {@code phoneBookRecordId}. If phoneBookRecord with such id doesn't exist,
	 * exception is thrown
	 * @param phoneBookRecordId id of phoneBookRecord
	 * @throws InvalidIdentifier if phoneBookRecord with such id doesn't exist
	 */
	@PreAuthorize("isAuthenticated()")
	public PhoneBookRecord getPhoneBookRecordById(long phoneBookRecordId) 
									throws InvalidIdentifier;
	
	/**
	 * Saves {@code phoneBookRecord} in data storage, before creating relationships
	 * with {@link User}, login of which is passed to method.
	 * @param login login of User
	 * @param phoneBookRecord {@link PhoneBookRecord}, which must be saved
	 * @return saved {@code PhoneBookRecord}
	 */
	@PreAuthorize("isAuthenticated()")
	public PhoneBookRecord addPhoneBookRecord
						(String login,PhoneBookRecord phoneBookRecord);
	
	/**
	 * Deletes {@link PhoneBookRecord} {@code phoneBookRecord} from data 
	 * storage, id of which equals to {@code phoneBookRecordId}.
	 * <p>{@code phoneBookRecord} must be linked to {@link User}
	 * {@code user} with login, passed to method.
	 * <p>If PhoneBookRecord with such id doesn't exist in data storage,
	 * or it isn't linked to {@code user}, exception is thrown.
	 * @param login login of {@code User}
	 * @param phoneBookRecordId id of {@code PhoneBookRecord}, which must be deleted
	 * @throws InvalidIdentifier if PhoneBookRecord with such id doesn't exist in data storage,
	 * or it isn't linked to {@code user}.
	 */
	@PreAuthorize("isAuthenticated()")
	public void deletePhoneBookRecord(String login, long phoneBookRecordId) 
										throws InvalidIdentifier;
	
	/**
	 * Updates {@link PhoneBookRecord} {@code phoneBookRecord} in data storage, 
	 * if it is linked to {@link User} {@code user},
	 * login of which is being passed to method.
	 * 
	 * If {@code phoneBookRecord} doesn't exist in data storage(retrieved by id), or it isn't
	 * linked to {@code user}, exception is thrown.
	 * @param login login of {@code User}
	 * @param phoneBookRecord {@code PhoneBookRecord}
	 * @throws InvalidIdentifier
	 */
	@PreAuthorize("isAuthenticated()")
	public PhoneBookRecord updatePhoneBookRecord(String login, PhoneBookRecord phoneBookRecord)
										throws InvalidIdentifier;
	
	/**
	 * This method works the same way as
	 * {@link #getPhoneBookByUserLogin(String, Pageable)} with the only difference in that
	 * some String properties of PhoneBookRecords must contain values, retrieved from
	 * properties of {@code filter} with the same names, regardless of registr.
	 * <p> For example:
	 * <pre>{@code filter.firstName}="oh"</pre>
	 * PhoneBookRecords, related to User, have following values of 
	 * {@code phoneBookRecord.firstName}:<pre>"John","Tomas","Ohra".</pre>  
	 * PhoneBookRecords with firstName "John" and "Ohra" will be match to filter.
	 * @param login login of User
	 * @param filter {@link FilterPhoneBookRecords}
	 * @param pageable {@link Pageable}
	 */
	@PreAuthorize("isAuthenticated()")
	public Page<PhoneBookRecord> getFilteredPhoneBookByUserLogin(String login,FilterPhoneBookRecords filter,
														Pageable pageable);
}

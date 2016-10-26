package ua.phonebook.dao.filestorage.comparator;

import java.util.Comparator;

import ua.phonebook.model.PhoneBookRecord;

/**
 * Note: this comparator imposes orderings that are inconsistent with equals.
 * The comparison is primarily based on id, from lowest to biggest
 *
 */
public class PhoneBookRecordComparatorById implements Comparator<PhoneBookRecord>{

	@Override
	public int compare(PhoneBookRecord o1, PhoneBookRecord o2) {
		if(o1.getId()-o2.getId()==0){
			return 0;
		}
		return o1.getId()<o2.getId()?-1:1;
	}

}

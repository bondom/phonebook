package ua.phonebook.web.controller;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ua.phonebook.model.PhoneBookRecord;
import ua.phonebook.service.PhoneBookService;
import ua.phonebook.service.exception.InvalidIdentifier;
import ua.phonebook.support.Message;
import ua.phonebook.web.viewbean.FilterPhoneBookRecords;

@Controller
@RequestMapping("/phonebook")
public class PhoneBookController {
	
	private static final int NUMBER_PHONE_BOOK_RECORDS_ON_PAGE=6;
	
	@Autowired
	private PhoneBookService phoneBookService;
	
	@Autowired
	private Message message;
	
	@GetMapping
	public String phonebook(@Valid @ModelAttribute("filter") FilterPhoneBookRecords filter,
							BindingResult result,
							 HttpServletRequest request,
				 			 Model model,Principal user,
				 			 @RequestParam(name ="page",defaultValue="1") int page) throws UnsupportedEncodingException{
		
		Page<PhoneBookRecord> pageInfo = null;
		PageRequest pageRequest = new PageRequest(page-1,NUMBER_PHONE_BOOK_RECORDS_ON_PAGE);
		boolean filtering = true;
		
		if(result.hasErrors()||
					filter.getFirstName().trim().equals("") &&
					filter.getLastName().trim().equals("")&&
					filter.getMobilePhone().trim().equals("")){
			filtering = false;
			pageInfo = 
					phoneBookService.getPhoneBookByUserLogin(user.getName(),pageRequest);
		}else{
			pageInfo= 
					phoneBookService.getFilteredPhoneBookByUserLogin(user.getName(),filter,pageRequest);
		}
		List<PhoneBookRecord> phoneBook = pageInfo.getContent();
		int totalPages = pageInfo.getTotalPages();
		
		model.addAttribute("phoneBook", phoneBook);
		model.addAttribute("totalPages",totalPages);
		model.addAttribute("filter", filter);
		model.addAttribute("filtering", filtering);
		model.addAttribute("activePage", page);
		
		//Defining of initial id of element, depending on page
		int initialId = (page-1)*NUMBER_PHONE_BOOK_RECORDS_ON_PAGE+1;
		model.addAttribute("initialId", initialId);
		
		PhoneBookRecord phoneBookRecord =(PhoneBookRecord)model.asMap().get("record");
		if(phoneBookRecord == null){
			PhoneBookRecord newRecord = new PhoneBookRecord();
			model.addAttribute("record", newRecord);
		}
		
		if(filter == null){
			FilterPhoneBookRecords freshfilter = new FilterPhoneBookRecords();
			model.addAttribute("filter", freshfilter);
		}
		return "phonebook";
	}
	
	@PostMapping("/addRecord")
	public String addRecord(@Valid @ModelAttribute("record") PhoneBookRecord phoneBookRecord,
							BindingResult result,
							Principal user,RedirectAttributes attr){
		if(result.hasErrors()){
			attr.addFlashAttribute
				("org.springframework.validation.BindingResult.record",result);
			attr.addFlashAttribute("record", phoneBookRecord);
			return "redirect:/phonebook";
		}
		phoneBookService.addPhoneBookRecord(user.getName(), phoneBookRecord);
		attr.addFlashAttribute("success", message.getMessage("phonebookrecord.add.success"));
		return "redirect:/phonebook";
	}
	
	@PostMapping("/deleteRecord")
	public String deleteRecord(@RequestParam("id") long phoneBookRecordId,
							Principal user,RedirectAttributes attr){
		try {
			phoneBookService.deletePhoneBookRecord(user.getName(), phoneBookRecordId);
			attr.addFlashAttribute("success",message.getMessage("phonebookrecord.delete.success"));
		} catch (InvalidIdentifier e) {}
		return "redirect:/phonebook";
	}
	
	

	@GetMapping("/editRecord")
	public String pageForEditing(@RequestParam("id") long phoneBookRecordId,
							Principal user,Model model){
		PhoneBookRecord phoneBookRecord =(PhoneBookRecord)model.asMap().get("record");
		if(phoneBookRecord != null){
			return "editRecord";
		}else{
			try {
				phoneBookRecord = 
						phoneBookService.getPhoneBookRecordById(user.getName(),phoneBookRecordId);
				model.addAttribute("record", phoneBookRecord);
				return "editRecord";
			} catch (InvalidIdentifier e) {
				return "redirect:/phonebook";
			}
		}
	}
	
	@PostMapping("/updateRecord")
	public String updateRecord(@Valid @ModelAttribute("record") PhoneBookRecord phoneBookRecord,
								BindingResult result,
								Principal user,RedirectAttributes attr){
		if(result.hasErrors()){
			attr.addFlashAttribute
				("org.springframework.validation.BindingResult.record",result);
			attr.addFlashAttribute("record", phoneBookRecord);
			return "redirect:/phonebook/editRecord?id="+phoneBookRecord.getId();
		}
		
		try {
			phoneBookService.updatePhoneBookRecord(user.getName(), phoneBookRecord);
			attr.addFlashAttribute("success", 
					message.getMessage("phonebookrecord.update.success"));
		} catch (InvalidIdentifier e) {}
		return "redirect:/phonebook";
	}
	
	
}

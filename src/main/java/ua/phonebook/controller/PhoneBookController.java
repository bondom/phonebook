package ua.phonebook.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import ua.phonebook.model.User;
import ua.phonebook.service.PhoneBookService;
import ua.phonebook.service.exception.InvalidIdentifier;

@Controller
@RequestMapping("/phonebook")
public class PhoneBookController {
	
	@Autowired
	private PhoneBookService phoneBookService;
	
	@GetMapping
	public String phonebook(HttpServletRequest request,
		 			 Model model,Principal user){
		List<PhoneBookRecord> phoneBook = 
				phoneBookService.getPhoneBookByUserLogin(user.getName());
		model.addAttribute("phoneBook", phoneBook);
		
		PhoneBookRecord phoneBookRecord =(PhoneBookRecord)model.asMap().get("record");
		if(phoneBookRecord != null){
			return "phonebook";
		}else{
			PhoneBookRecord newRecord = new PhoneBookRecord();
			model.addAttribute("record", newRecord);
			return "phonebook";
		}
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
		attr.addFlashAttribute("success", "Record is successfully added");
		return "redirect:/phonebook";
	}
	
	@PostMapping("/deleteRecord")
	public String deleteRecord(@RequestParam("id") long phoneBookRecordId,
							Principal user,RedirectAttributes attr){
		try {
			phoneBookService.deletePhoneBookRecord(user.getName(), phoneBookRecordId);
			attr.addFlashAttribute("success", "Record is successfully deleted");
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
						phoneBookService.getPhoneBookRecordById(phoneBookRecordId);
				model.addAttribute("record", phoneBookRecord);
				return "editRecord";
			} catch (InvalidIdentifier e) {
				return "phonebook";
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
			attr.addFlashAttribute("success", "Record is successfully updated");
		} catch (InvalidIdentifier e) {}
		return "redirect:/phonebook";
	}
	
	
}

package ua.phonebook.web.controller.ajax;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ua.phonebook.model.PhoneBookRecord;
import ua.phonebook.service.PhoneBookService;
import ua.phonebook.support.Message;

@RestController
public class AjaxPhoneBookController {
	
	@Autowired
	private PhoneBookService phoneBookService;
	
	@Autowired
	private Message message;
	
	@PostMapping("/phonebook/addRecordajax")
	public String addRecord(@Valid @ModelAttribute("record") PhoneBookRecord phoneBookRecord,
							BindingResult result,
							Principal user,RedirectAttributes attr){
/*		if(result.hasErrors()){
			attr.addFlashAttribute
				("org.springframework.validation.BindingResult.record",result);
			attr.addFlashAttribute("record", phoneBookRecord);
			return "redirect:/phonebook";
		}*/
		phoneBookService.addPhoneBookRecord(user.getName(), phoneBookRecord);
	/*	Map<String,String> resultMap = new LinkedHashMap<>();
		resultMap.put("success", message.getMessage("phonebookrecord.add.success"));*/
		return message.getMessage("phonebookrecord.add.success");
	}
}

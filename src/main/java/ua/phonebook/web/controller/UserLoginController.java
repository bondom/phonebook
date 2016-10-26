package ua.phonebook.web.controller;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ua.phonebook.model.User;
import ua.phonebook.service.UserService;
import ua.phonebook.service.exception.DuplicateLoginException;
import ua.phonebook.support.Message;

@Controller
public class UserLoginController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private Message message;
	
	@GetMapping(value = "/login")
	public String login(HttpServletRequest request,
		 			 Model model){
		User user =(User)model.asMap().get("user");
		if(user == null){
			model.addAttribute("user", new User());
		}
		return "login";
	}
	 
	@PostMapping(value = "/registration")
	public String registration(@Valid @ModelAttribute(name="user") User user,
							   BindingResult result,Model model,
							   RedirectAttributes attr){
		if(result.hasErrors()){
			attr.addFlashAttribute
					("org.springframework.validation.BindingResult.user",result);
			attr.addFlashAttribute("user", user);
			return "redirect:/login";
		}
		
		try {
			userService.registerUser(user);
			attr.addFlashAttribute("success", message.getMessage("user.registration.success"));
			return "redirect:/login";
		} catch (DuplicateLoginException e) {
			attr.addFlashAttribute("error",message.getMessage("user.registration.failure"));
			attr.addFlashAttribute
					("org.springframework.validation.BindingResult.user",result);
			attr.addFlashAttribute("user", user);
			return "redirect:/login";
		}
	}
	
}

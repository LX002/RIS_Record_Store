package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.services.KorisnikService;

import jakarta.servlet.http.HttpSession;
import model.Album;

@Controller
public class LoginController {
	
	@RequestMapping("/")
	public String getHomePage(HttpSession session) {
		List<Album> cartSession = (List<Album>) session.getAttribute("cartSession");
		if(cartSession != null) {
			session.removeAttribute("cartSession");
		}
		return "login";
	}
}

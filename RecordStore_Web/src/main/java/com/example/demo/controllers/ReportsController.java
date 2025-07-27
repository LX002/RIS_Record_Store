package com.example.demo.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.services.KorisnikService;
import com.example.demo.services.NarudzbinaService;
import com.example.demo.services.ReportsService;

import model.Korisnik;
import model.Narudzbina;
import model.Stavkanarudzbina;

@Controller
public class ReportsController {
	
	@Autowired
	ReportsService rs;
	
	@Autowired
	NarudzbinaService ns;
	
	@Autowired
	KorisnikService ks;
	
	@GetMapping("/users/getOrderBill/{idNarudzbina}")
	public ResponseEntity<?> getOrderBill(@PathVariable Integer idNarudzbina) {
		List<Stavkanarudzbina> items = ns.getItems(idNarudzbina);
		Narudzbina order = ns.getNarudzbina(idNarudzbina);
		try {
			byte[] billPdf = rs.createOrderBill(items, order);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.setContentDispositionFormData("inline", "bill-" + idNarudzbina + ".pdf");
			return new ResponseEntity<>(billPdf, headers, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during generation of your bill!\n" + e.getMessage());
		}
	}
	
	@GetMapping("/admin/getProcessedOrdersReport/{idKorisnik}")
	public ResponseEntity<?> getProcessedOrdersReport(@PathVariable Integer idKorisnik) {
		LocalDate today = LocalDate.now();
	    Date todayDate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
		List<Narudzbina> orders = ns.getProcessedOrders(idKorisnik, todayDate);
		if(orders == null || orders.isEmpty()) {
			String backLink = "<a href=\"/RecordStore/users/store\">Go back</a><br>";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(backLink + "\n<div class=\"error\">You haven't processed any orders today!</div>");
		}
		Korisnik korisnik = ks.findById(idKorisnik);
		try {
			byte[] billPdf = rs.createOrdersReport(orders, todayDate, korisnik);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.setContentDispositionFormData("inline", "processed-orders-employee" + idKorisnik + ".pdf");
			return new ResponseEntity<>(billPdf, headers, HttpStatus.OK);
		} catch (Exception e) {
			String backLink = "a href=\"${pageContext.request.contextPath}/users/store\">Go back</a><br>";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(backLink + "\n<div class=\"error\">Error during generation of your orders report!<br>" + e.getMessage() + "</div>");
		}
		
	}
}

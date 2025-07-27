package com.example.demo.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import model.Korisnik;
import model.Narudzbina;
import model.Stavkanarudzbina;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@Service
public class ReportsService {
	
	public byte[] createOrderBill(List<Stavkanarudzbina> items, Narudzbina narudzbina) throws JRException, IOException {
		System.out.println("Number of items: " + items.size());
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(items);
		InputStream inputStream = this.getClass().getResourceAsStream("/jasperreports/bill.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("priceP", narudzbina.getIznos());
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
		inputStream.close();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
		exporter.exportReport();
		return byteArrayOutputStream.toByteArray();
	}
	
	public byte[] createOrdersReport(List<Narudzbina> orders, Date today, Korisnik korisnik) throws JRException, IOException {
		System.out.println("Number of items: " + orders.size());
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(orders);
		InputStream inputStream = this.getClass().getResourceAsStream("/jasperreports/orders.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("todayDate", today);
		params.put("employeeName", korisnik.getIme());
		params.put("employeeSurname", korisnik.getPrezime());
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
		inputStream.close();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
		exporter.exportReport();
		return byteArrayOutputStream.toByteArray();
	}
}

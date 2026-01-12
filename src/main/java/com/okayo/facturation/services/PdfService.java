package com.okayo.facturation.services;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.styledxmlparser.css.media.MediaDeviceDescription;
import com.itextpdf.styledxmlparser.css.media.MediaType;

@Service
public class PdfService {

    @Autowired
    private SpringTemplateEngine templateEngine;

    public byte[] generatePdfFromModel(String templateName, Map<String, Object> modelMap) {
        // 1. Convert Spring Model Map to Thymeleaf Context
        Context context = new Context();
        context.setVariables(modelMap);

        // 2. Process the template into a String
        String renderedHtml = templateEngine.process(templateName, context);

        // 3. Convert HTML to PDF using iText pdfHTML 6.x (Core 9 companion)
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        
		ConverterProperties properties = new ConverterProperties();
		MediaDeviceDescription mediaDeviceDescription = new MediaDeviceDescription(MediaType.SCREEN);
		mediaDeviceDescription.setWidth(1080); 
		properties.setMediaDeviceDescription(mediaDeviceDescription);
		
        HtmlConverter.convertToPdf(renderedHtml, target, properties);

        return target.toByteArray();
    }
    
}
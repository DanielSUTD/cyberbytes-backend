package com.backend.cyberbytes.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    //Denpendencias
    @Autowired
    JavaMailSender javaMailSender;

    //Recupera o email no applications properties
    @Value("${spring.mail.username}")
    private String sender;

    /*
     * Envia emails em formato de texto
     * */
    public String sendTextEmail(String recipient, String mainContent, String message){
        try{
            //Configuração
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(sender);
            simpleMailMessage.setTo(recipient);
            simpleMailMessage.setSubject(mainContent);
            simpleMailMessage.setText(message);
            javaMailSender.send(simpleMailMessage);
            return "Email enviado";
        } catch (Exception e) {
            return "Erro ao enviar email" + e.getLocalizedMessage();
        }
    }

    /*
     * Envia emails em formato html
     * */
    public String sendCodeEmail(String recipient, String mainContent, String contentType) {
        try {
            //Criação do objeto
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            //Configuração
            helper.setFrom(sender);
            helper.setTo(recipient);
            helper.setSubject(mainContent);
            helper.setText(contentType, true);//Indica que ele é html

            //Envia
            javaMailSender.send(mimeMessage);
            return "Email enviado";
        } catch (Exception e) {
            return "Erro ao enviar email" + e.getMessage();
        }
    }
}

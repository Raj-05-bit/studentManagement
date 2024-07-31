package com.google.studentManagement.helper;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.google.studentManagement.dto.MyUser;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class HelperForSendingMail {


@Autowired
JavaMailSender mailSender;

public void sendEmail(MyUser myUser)
{
    MimeMessage mimeMessage= mailSender.createMimeMessage();
    MimeMessageHelper helper =new MimeMessageHelper(mimeMessage);
try{
    helper.setFrom("bg807906@gmail.com","Student Management System");
    helper.setTo(myUser.getEmail());
    helper.setSubject("OTP verification");
    helper.setText("<h1>Hello, "+ myUser.getName() + "your OTP is : "+myUser.getOtp()+"</h1>",true);

}
catch(MessagingException | UnsupportedEncodingException e)
{
    e.printStackTrace();
}

mailSender.send(mimeMessage);
}

}

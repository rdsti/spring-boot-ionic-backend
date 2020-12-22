package com.rdsti.cursomc.services;

import org.springframework.mail.SimpleMailMessage;
import com.rdsti.cursomc.domain.Pedido;


public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido pedido);
	
	void sendEmail(SimpleMailMessage msg);

}

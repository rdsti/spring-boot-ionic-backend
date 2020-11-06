package com.rdsti.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.rdsti.cursomc.domain.TipoCliente;
import com.rdsti.cursomc.dto.ClienteNewDTO;
import com.rdsti.cursomc.resources.exception.FieldMessage;
import com.rdsti.cursomc.services.validation.utils.BR;

public class ClienteInsertValidation implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {

	@Override
	public void initialize(ClienteInsert ann) {
		
	}
	
	@Override
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {

		List<FieldMessage> list = new ArrayList<>();
		
		if (objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidCPF(objDto.getCpfOuCnpj())) {
			
			list.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
			
		}
		
		if (objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidCNPJ(objDto.getCpfOuCnpj())) {
			
			list.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));
			
		}
		
		
		for (FieldMessage e : list) {
			
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName()).addConstraintViolation();
			
		}
		
		return list.isEmpty();
	}
	

}

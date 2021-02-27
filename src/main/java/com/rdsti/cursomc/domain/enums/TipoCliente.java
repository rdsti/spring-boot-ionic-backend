package com.rdsti.cursomc.domain.enums;

public enum TipoCliente {
	
	PESSOAFISICA(1, "Pessoa Fisíca"),
	PESSOAJURIDICA(2, "Pessoa Jurídica");
	
	private int cod;
	private String descricao;
	
	private TipoCliente(int cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}

	public int getCod() {
		return cod;
	}

	public void setCod(int cod) {
		this.cod = cod;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public static TipoCliente toEnum(Integer cod) {
		
		for (TipoCliente x : TipoCliente.values()) {

			if(cod.equals(x.getCod())) {
				return x;
			}
			
		}
		
		return null;
		
		//throw new IllegalAccessException("Id inválido: " + cod);
	}
	
}

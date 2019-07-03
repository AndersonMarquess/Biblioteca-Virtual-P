package com.andersonmarques.bvp.model.enums;

public enum Tipo {
	TWITTER(1), FACEBOOK(2);

	private Integer id;

	private Tipo(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public static Tipo toEnum(Integer id) {
		if (id != null) {
			for (Tipo t : Tipo.values()) {
				if (t.getId().equals(id)) {
					return t;
				}
			}
		}
		throw new IllegalArgumentException(
				"Tipo de contato [ " + id + " ] inválido, use [ 1 ] para TWITTER ou [ 2 ] para FACEBOOK.");
	}
}

package ar.com.gopay.domain.nosis.ws1;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Nosis {

	@JsonProperty("Contenido")
	private Contenido contenido;

	public Contenido getContenido() {
		return contenido;
	}

	public void setContenido(Contenido contenido) {
		this.contenido = contenido;
	}

	@Override
	public String toString() {
		return "Nosis{" +
				"contenido=" + contenido +
				'}';
	}
}

package com.pedrodev.utils;

public class TempoUtils {
	
	public static int converterParaMinutos(String tempo) {
		
		String[] horaMinutos = tempo.split("h");
		
		int hora = Integer.parseInt(horaMinutos[0]);
		
		int minutos = 0;
		
		if(horaMinutos.length > 1) {
	    	 if (!horaMinutos[1].isEmpty()) {
	 			minutos = Integer.parseInt(horaMinutos[1]);
	 		}
	     } 
		
		int tempoMinutos = (hora * 60) + minutos;
		
		return tempoMinutos;
		
	}

}

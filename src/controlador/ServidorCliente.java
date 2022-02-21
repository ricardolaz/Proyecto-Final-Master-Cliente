package controlador;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ServidorCliente {
	private Socket socket;
	private BufferedReader entrada;
	private BufferedWriter salida;
	
	public ServidorCliente() {
		try {
			socket = new Socket("localhost",1001);
			entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			salida = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			ejecutar();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void ejecutar() {
		try {
			String protocolo =entrada.readLine();
			System.out.println(protocolo);
			
		} catch (IOException e) {
			
		}	
	}
	
	/**
	 * Este método recibe las peticiones del controlador del Cliente, se las envía a la clase Hilo y devuelve la respuesta.
	 * @param peticion	El método envía la petición en String pero con formato JSON al hilo.
	 * @return			El método devuelve un JSON con la respuesta del hilo
	 */
	public JsonObject peticion(String peticion) {
		try {
			salida.write(peticion);
			salida.newLine();
			salida.flush();
			String respuesta=entrada.readLine();
			JsonObject json = new JsonParser().parse(respuesta).getAsJsonObject();
			return json;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}

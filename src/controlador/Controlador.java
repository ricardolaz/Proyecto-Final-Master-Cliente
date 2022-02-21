package controlador;

import com.google.gson.JsonObject;


/**
 * Esta clase crea las peticiones usando objetos JSON y llama a m�todos de la clase ServidorCliente
 * Esta a su vez consulta a la clase Hilo del modelo. 
 * Estos m�todos se llaman desde la vista y reciben la informaci�n en formato JSON.
 * @author Ricardo L�zaro
 *
 */
public class Controlador {
	ServidorCliente servidor;
	
	
	public Controlador(ServidorCliente servidor) {
		super();
		this.servidor = servidor;
	}

	public JsonObject loguearse(String usuario, String contrase�a) {
		JsonObject json = new JsonObject();
		json.addProperty("tipo", "logado");
		json.addProperty("usuario", usuario);
		json.addProperty("contrase�a", contrase�a);
		return servidor.peticion(json.toString());
	}
	
	public JsonObject crearUsuario(String usuario, String contrase�a) {
		JsonObject json = new JsonObject();
		json.addProperty("tipo", "registrar");
		json.addProperty("usuario", usuario);
		json.addProperty("contrase�a", contrase�a);
		return servidor.peticion(json.toString());
	}
	
	public JsonObject crearLugar(String nombre) {
		JsonObject json = new JsonObject();
		json.addProperty("tipo", "crear lugar");
		json.addProperty("nombre", nombre);
		return servidor.peticion(json.toString());
	}
	
	public JsonObject publicarOpinion(int idLugar, int valoracion, String critica, int idUsuario) {
		JsonObject json = new JsonObject();
		json.addProperty("tipo", "publicar opinion");
		json.addProperty("lugar", idLugar);
		json.addProperty("valoracion", valoracion);
		json.addProperty("critica", critica);
		json.addProperty("usuario", idUsuario);
		return servidor.peticion(json.toString());
	}
	
	public JsonObject seguirUsuario(int idUsuario, int idASeguir) {
		JsonObject json = new JsonObject();
		json.addProperty("tipo", "seguir usuario");
		json.addProperty("usuario", idUsuario);
		json.addProperty("seguir", idASeguir);
		return servidor.peticion(json.toString());
	}
	
	public JsonObject dejarDeSeguir(int idUsuario, int idDejarSeguir){
		JsonObject json = new JsonObject();
		json.addProperty("tipo", "dejar de seguir");
		json.addProperty("usuario", idUsuario);
		json.addProperty("dejar de seguir", idDejarSeguir);
		return servidor.peticion(json.toString());
	}
	
	public JsonObject listarUsuariosSeguidos(int id) {
		JsonObject json = new JsonObject();
		json.addProperty("tipo", "listar usuarios seguidos");
		json.addProperty("id", id);
		return servidor.peticion(json.toString());
	}

	public JsonObject listarUsuariosTodos() {
		JsonObject json = new JsonObject();
		json.addProperty("tipo", "listar usuarios");
		return servidor.peticion(json.toString());
	}

	public JsonObject listarLugares() {
		JsonObject json = new JsonObject();
		json.addProperty("tipo", "listar lugares");
		return servidor.peticion(json.toString());
	}
	
	public JsonObject buscarLugarID(int id) {
		JsonObject json = new JsonObject();
		json.addProperty("tipo", "buscar lugar");
		json.addProperty("id", id);
		return servidor.peticion(json.toString());
	}
	
	public JsonObject buscarUsuarioID(int id) {
		JsonObject json = new JsonObject();
		json.addProperty("tipo", "buscar usuario");
		json.addProperty("id", id);
		return servidor.peticion(json.toString());
	}
	
	public JsonObject buscarOpinionID(int id) {
		JsonObject json = new JsonObject();
		json.addProperty("tipo", "buscar opinion");
		json.addProperty("id", id);
		return servidor.peticion(json.toString());
	}

	public JsonObject listarOpinionesTodas() {
		JsonObject json = new JsonObject();
		json.addProperty("tipo", "listar opiniones");
		return servidor.peticion(json.toString());	
	}
	
	

}

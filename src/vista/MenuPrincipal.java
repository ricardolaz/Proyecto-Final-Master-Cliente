package vista;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import controlador.Controlador;
import controlador.ServidorCliente;

public class MenuPrincipal extends JFrame{
	private String usuario = "";
	private int id = 0;
	private Controlador controlador;
	
	
	/**
	 * El constructor de la clase que lanza el método menuLogueo() que es el menú con el que se inicia el programa
	 */
	MenuPrincipal(){
		super();
		controlador = new Controlador(new ServidorCliente());
		menuLogueo();
		this.setVisible(false);	
	}
	
	
	/**
	 * El método que modeliza el menú principal y sus diferentes botones
	 */
	public void menu() {
		JFrame menu=new JFrame("Menú principal");
		JPanel principal = new JPanel(new GridLayout(9,0));
		JButton publicar = new JButton("Publicar una opinión");
		publicar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JsonObject json = controlador.listarLugares();
				menu.setVisible(false);
				Map<Integer, String> mapa = new TreeMap<Integer, String>();
				mapa.put(0, "Crear nuevo lugar");
				JsonArray jarray = new JsonArray();
				if(json.has("lugares")) 
					jarray  =json.get("lugares").getAsJsonArray();		
				for (JsonElement ele : jarray) {
					JsonObject jsonAux = ele.getAsJsonObject();
					mapa.put(jsonAux.get("id").getAsInt(), jsonAux.get("nombre").getAsString());
				}
				mapa.put(Integer.MAX_VALUE, "volver");
				listaBotones(mapa, "publicar opinion");
			}
		});
		
		JButton usuariosSeguidos = new JButton("Visualiza la lista de los usuarios seguidos");
		usuariosSeguidos.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JsonObject json = controlador.listarUsuariosSeguidos(id);
				menu.setVisible(false);
				Map<Integer, String> mapa = new TreeMap<Integer, String>();
				JsonArray jarray = new JsonArray();
				if(json.has("usuarios")) 
					jarray = json.get("usuarios").getAsJsonArray();
				for (JsonElement ele : jarray) {
					JsonObject jsonAux = ele.getAsJsonObject();
					mapa.put(jsonAux.get("id").getAsInt(), jsonAux.get("nombre").getAsString());
				}
				if(mapa.isEmpty()) {
					mapa.put(Integer.MAX_VALUE-1, "No sigues a ningún usuario");
					mapa.put(Integer.MAX_VALUE, "volver");
					menuUsuarios(mapa);
				}
				else {
					mapa.put(Integer.MAX_VALUE, "volver");
					menuUsuarios(mapa);
				}
				
			}
		});
		
		JButton opinionesSeguidas = new JButton("Visualiza las opiniones de usuarios seguidos");
		opinionesSeguidas.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				List<Integer> listaUsuarios = new ArrayList<Integer>();
				List<Integer> listaOpiniones = new ArrayList<Integer>();
				List<String> lista = new ArrayList<String>();
				JsonObject jsonUsuario = controlador.buscarUsuarioID(id);
				JsonArray jArray = new JsonArray();
				if(jsonUsuario.has("siguiendo"))jArray = jsonUsuario.get("siguiendo").getAsJsonArray();
				for(JsonElement ele:jArray) listaUsuarios.add(ele.getAsInt());
				for (Integer ele : listaUsuarios) {
					jArray = new JsonArray();
					jsonUsuario =controlador.buscarUsuarioID(ele);
					if(jsonUsuario.has("opiniones"))jArray = jsonUsuario.get("opiniones").getAsJsonArray();
					for(JsonElement ele2:jArray) listaOpiniones.add(ele2.getAsInt());
				}
				for(Integer ele:listaOpiniones) 
					lista.add(controlador.buscarOpinionID(ele).toString());
				menuOpiniones(lista,"usuarios");
				menu.dispose();
			}
		});
		
		JButton todosUsuarios = new JButton("Visualiza todos los usuarios");
		todosUsuarios.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JsonObject json = controlador.listarUsuariosTodos();
				menu.setVisible(false);
				Map<Integer, String> mapa = new TreeMap<Integer, String>();
				JsonArray jarray = new JsonArray();
				if(json.has("usuarios")) 
					jarray = json.get("usuarios").getAsJsonArray();
				for (JsonElement ele : jarray) {
					JsonObject jsonAux = ele.getAsJsonObject();
					mapa.put(jsonAux.get("id").getAsInt(), jsonAux.get("nombre").getAsString());
				}
				mapa.put(Integer.MAX_VALUE, "volver");
				menuUsuarios(mapa);
			}
		});
		JButton todasOpioniones = new JButton("Visualiza todas las opiniones");
		todasOpioniones.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				List<String> lista = new ArrayList<String>();
				JsonObject json = controlador.listarOpinionesTodas();
				JsonArray jArray = new JsonArray();
				if(json.has("opiniones"))jArray = json.get("opiniones").getAsJsonArray();;
				for(JsonElement ele:jArray) {
					lista.add(ele.toString());
				}
				menuOpiniones(lista, "todas");
				menu.setVisible(false);
				
			}
		});
		
		JButton listaLugares = new JButton("Visualiza todos los lugares");
		listaLugares.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JsonObject json = controlador.listarLugares();
				menu.setVisible(false);
				Map<Integer, String> mapa = new TreeMap<Integer, String>();
				JsonArray jarray = new JsonArray();
				if(json.has("lugares")) 
					jarray = json.get("lugares").getAsJsonArray();
				if(jarray.size()==0)
					mapa.put(0, "No hay lugares disponibles");
				for (JsonElement ele : jarray) {
					JsonObject jsonAux=ele.getAsJsonObject();
					mapa.put(jsonAux.get("id").getAsInt(), jsonAux.get("nombre").getAsString());
				}
				mapa.put(Integer.MAX_VALUE, "volver");
				listaBotones(mapa, "lugares");
				
			}
		});
		
		JButton usuarioBoton = new JButton("Visualiza tu cuenta (Usuario:"+usuario+")");
		usuarioBoton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				menuUsuario(id,usuario);
				menu.setVisible(false);
			}
		});
		
		JButton desconectar = new JButton("Cerrar sesión");
		desconectar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				menu.dispose();
				new MenuPrincipal();
			}
		});
		
		JButton salir = new JButton("Salir");
		salir.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				
			}
		});
		
		principal.add(publicar);
		principal.add(usuariosSeguidos);
		principal.add(opinionesSeguidas);
		principal.add(todosUsuarios);
		principal.add(todasOpioniones);
		principal.add(listaLugares);
		principal.add(usuarioBoton);
		principal.add(desconectar);
		principal.add(salir);
		menu.add(principal);
		menu.setPreferredSize(new Dimension(400,600));
		menu.setVisible(true);
		menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menu.pack();
	}
	
	
	/**
	 * 
	 * @param idUsuario 	el id del usuario
	 * @param nombre 		el nombre del usuario
	 */
	public void menuUsuario(int idUsuario, String nombre) {
		JsonObject json = controlador.buscarUsuarioID(idUsuario);
		JsonObject json2 = controlador.buscarUsuarioID(id);
		JsonArray jArray = new JsonArray();
		JsonArray jArray2 = new JsonArray();
		if(json.has("opiniones")) jArray = json.get("opiniones").getAsJsonArray();
		if(json2.has("siguiendo")) jArray2 = json2.get("siguiendo").getAsJsonArray();
		JFrame menuUsuario = new JFrame("Menú usuario:" + nombre);
		JPanel principal = new JPanel(new GridLayout(0,1));
		JPanel panelNombre = new JPanel();
		JLabel nombreLabel = new JLabel(nombre);
		panelNombre.add(nombreLabel);
		if(idUsuario!=id) {
			List<Integer> listaSeguidos = new ArrayList();
			for (JsonElement ele : jArray2) 
				listaSeguidos.add(ele.getAsInt());
			if (listaSeguidos.contains(idUsuario)) {
				JButton dejarSeguir = new JButton("Dejar de seguir");
				dejarSeguir.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						JDialog confirmar = new JDialog();
						confirmar.setTitle("Usuario: "+nombre);
						JPanel principal = new JPanel(new GridLayout(3,0));
						JPanel superior = new JPanel();
						JLabel etiqueta = new JLabel("¿Está seguro que quiere dejar de seguir a este usuario?");
						superior.add(etiqueta);
						principal.add(superior);
						JPanel inferior = new JPanel();
						JButton ok = new JButton("Sí");
						ok.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								JsonObject json2 = controlador.dejarDeSeguir(id,idUsuario);
								if(json2.has("seguido")) System.out.println("usuario seguido");
								if(json2.has("no seguido")) System.out.println("error al seguir a usuario");
								confirmar.dispose();
								menuUsuario.dispose();
								menuUsuario(idUsuario,nombre);
							}
						});
						
						JButton noOk = new JButton("No");
						noOk.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								confirmar.dispose();
								menuUsuario(idUsuario,nombre);
							}
						});
						
						inferior.add(ok);
						inferior.add(noOk);
						principal.add(inferior);
						confirmar.add(principal);
						confirmar.setVisible(true);
						confirmar.setPreferredSize(new Dimension(350,160));
						confirmar.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
						confirmar.pack();
					}
				});
				panelNombre.add(dejarSeguir);
			}else {
				JButton seguir = new JButton("Seguir");
				seguir.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						JsonObject json2=controlador.seguirUsuario(id,idUsuario);
						if(json2.has("seguido")) System.out.println("usuario seguido");
						if(json2.has("no seguido")) System.out.println("error al seguir a usuario");
						menuUsuario.dispose();
						menuUsuario(idUsuario,nombre);
					}
				});
				panelNombre.add(seguir);
			}	
		}
		principal.add(panelNombre);
		if(jArray.size()!=0) {
			for (JsonElement ele : jArray) {
				JPanel panel = new JPanel(new GridLayout(4,0));
				int auxId = ele.getAsInt();
				int idLugar = 0;
				int idUser = 0;
				int valoracion = 0;
				String critica = "",nombreLugar="";
				JsonObject jsonOpinion = controlador.buscarOpinionID(ele.getAsInt());
				if(jsonOpinion.has("idLugar")) idLugar = jsonOpinion.get("idLugar").getAsInt();
				if(jsonOpinion.has("valoracion")) valoracion = jsonOpinion.get("valoracion").getAsInt();
				if(jsonOpinion.has("critica")) critica = jsonOpinion.get("critica").getAsString();
				if(jsonOpinion.has("idUsuario")) idUser = jsonOpinion.get("idUsuario").getAsInt();
				JsonObject jsonLugar = controlador.buscarLugarID(idLugar);
				if(jsonLugar.has("nombre")) nombreLugar = jsonLugar.get("nombre").getAsString();

				JLabel lugarLabel = new JLabel(nombreLugar);
				panel.add(lugarLabel);
				JLabel criticaLabel = new JLabel(critica);
				panel.add(criticaLabel);
				JLabel valoracionLabel = new JLabel("la valoración es de: "+valoracion);
				panel.add(valoracionLabel);
				JLabel espacio = new JLabel("               ");
				principal.add(espacio);
				principal.add(panel);
			}
		}
		JPanel panelVolver= new JPanel();
		JButton volver=new JButton("Volver");
		volver.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				menuUsuario.dispose();
				menu();	
			}
		});
		panelVolver.add(volver);
		principal.add(panelVolver);
		menuUsuario.add(principal);
		menuUsuario.setPreferredSize(new Dimension(400,600));
		menuUsuario.setVisible(true);
		menuUsuario.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menuUsuario.pack();
	}
	
	
	public void menuUsuarios(Map<Integer,String> mapa) {
		listaBotones(mapa, "usuarios");
	}
	
	/**
	 * Este método modeliza el menú de las opiniones
	 * @param lista 	la lista de opiniones en formato JSON
	 * @param aux		el tipo de lista de opiniones
	 */
	public void menuOpiniones(List<String> lista, String aux) {
		JFrame menuOpiniones = new JFrame();
		String[] partes = aux.split(":");
		if(partes[0].equals("lugares")) menuOpiniones.setTitle("Todas las opiniones de "+partes[1]);
		if(aux.equals("todas")) menuOpiniones.setTitle("Todas las opiniones de los usuarios");
		if(aux.equals("usuarios")) menuOpiniones.setTitle("Opiniones de los usuarios seguidos");
		JPanel principal = new JPanel(new GridLayout(0, 1));
		String nombreUsuario = "", nombreLugar="",critica="";
		int valoracion = 0,idLugar = 0,idUsuario = 0;
		
		for(String ele:lista) {
			JPanel panel = new JPanel(new GridLayout(4,1));
			JsonObject json = new JsonParser().parse(ele).getAsJsonObject();
			if(json.has("valoracion")) valoracion = json.get("valoracion").getAsInt();
			if(json.has("critica")) critica = json.get("critica").getAsString();
			if(json.has("idLugar")) idLugar = json.get("idLugar").getAsInt();
			if(json.has("idUsuario")) idUsuario = json.get("idUsuario").getAsInt();
			JsonObject jsonUsuario=controlador.buscarUsuarioID(idUsuario);
			JsonObject jsonLugar=controlador.buscarLugarID(idLugar);
			if(jsonUsuario.has("nombre")) nombreUsuario=jsonUsuario.get("nombre").getAsString();
			if(jsonLugar.has("nombre")) nombreLugar=jsonLugar.get("nombre").getAsString();
			JLabel lugarLabel = new JLabel("Nombre de lugar: "+nombreLugar);
			panel.add(lugarLabel);
			JLabel criticaLabel = new JLabel(critica);
			panel.add(criticaLabel);
			JPanel panel2 = new JPanel(new GridLayout(1,1));
			JLabel usuarioLabel = new JLabel("Usuario: "+nombreUsuario);
			panel2.add(usuarioLabel);
			JLabel valoracionLabel = new JLabel("Valoración: "+valoracion);
			panel2.add(valoracionLabel);
			panel.add(panel2);
			JLabel espacio = new JLabel("-----------------------------------------------------------------------");
			panel.add(espacio);
			principal.add(panel);
		}
		
		menuOpiniones.add(principal);
		JScrollPane scroll = new JScrollPane(principal);
		menuOpiniones.add(scroll);
		JPanel menuVolver = new JPanel();
		JButton volver = new JButton("Volver");
		volver.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				menuOpiniones.dispose();
				menu();	
			}
		});
		
		menuVolver.add(volver);
		principal.add(menuVolver);
		menuOpiniones.setPreferredSize(new Dimension(400,600));
		menuOpiniones.setVisible(true);
		menuOpiniones.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menuOpiniones.pack();
	
	}
	
	
	/**
	 * Este método es el que pinta el menú para publicar una opinión
	 * @param idLugar	el id del lugar que se va a publicar
	 * @param lugar		el nombre del lugar del que se va a publicar
	 */
	public void menuPublicarOpinion(int idLugar, String lugar) {
		JFrame menuPublicarOp = new JFrame();
		JPanel panel = new JPanel(new BorderLayout());
		JPanel panelNorte = new JPanel();
		JLabel etiqueta = new JLabel("Nombre del lugar");
		panelNorte.add(etiqueta);
		JTextField lugarText = new JTextField();
		lugarText.setColumns(20);
		JLabel lugarLabel = new JLabel(lugar);
		if(idLugar==0) panelNorte.add(lugarText);
		else panelNorte.add(lugarLabel);
		panel.add(panelNorte,BorderLayout.NORTH);
		JTextArea opinionArea = new JTextArea();
		panel.add(opinionArea,BorderLayout.CENTER);
		JPanel panelSur = new JPanel(new GridLayout(2,2));
		JLabel puntuacionLabel = new JLabel("  Elige la puntuación(de 0 a 10):");
		JTextField puntuacionText = new JTextField();
		puntuacionText.setColumns(10);
		JButton botonCrear = new JButton("Publicar");
		botonCrear.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int puntuacion = -1;
				try {
					puntuacion = Integer.parseInt(puntuacionText.getText());
					if(puntuacion>=0 && puntuacion<=10) {
						
					}else {
						throw new NumberFormatException();
					}
				}catch(NumberFormatException ex) {
					JDialog puntuacionIncorrecta = new JDialog();
					puntuacionIncorrecta.setTitle("Un campo está mal introducido");
					JPanel panelPrincipal = new JPanel(new GridLayout(2, 1));
					JPanel panelTexto = new JPanel();
					JLabel textoLabel = new JLabel("En el campo de puntuación solamente se permite un número entero entre 0 y 10");
					panelTexto.add(textoLabel);
					panelPrincipal.add(panelTexto);
					JPanel panelBoton = new JPanel();
					JButton ok = new JButton("Vale");
					ok.addActionListener(new ActionListener() {		
						
						@Override
						public void actionPerformed(ActionEvent e) {
							puntuacionIncorrecta.dispose();
						}
					});
					
					panelBoton.add(ok);
					panelPrincipal.add(panelBoton);
					puntuacionIncorrecta.add(panelPrincipal);
					puntuacionIncorrecta.setPreferredSize(new Dimension(500,160));
					puntuacionIncorrecta.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
					puntuacionIncorrecta.pack();
					puntuacionIncorrecta.setVisible(true);
				}
				if(puntuacion>=0 && puntuacion<=10) {
					if(idLugar==0) {
						String lugarCad=lugarText.getText();
						JsonObject json = controlador.crearLugar(lugarCad);	
						if(json.has("id") && json.has("nombre")) {
							int idLugar = json.get("id").getAsInt();
									JsonObject jsonOpinion = controlador.publicarOpinion(idLugar, puntuacion, opinionArea.getText(), id);
									if(jsonOpinion.has("opinion publicada")) {
										menuPublicarOp.dispose();
										menu();
									}else {
										errorPublicacion();
									}
							
						}else {
							JDialog errorLugar = new JDialog();
							errorLugar.setTitle("Ha sucedido un error al crear el lugar");
							JPanel panelPrincipal = new JPanel(new GridLayout(2, 1));
							JPanel panelTexto = new JPanel();
							JLabel textoLabel = new JLabel("No se ha podido crear el lugar correctamente");
							panelTexto.add(textoLabel);
							panelPrincipal.add(panelTexto);
							JPanel panelBoton = new JPanel();
							JButton ok = new JButton("Vale");
							ok.addActionListener(new ActionListener() {		
								
								@Override
								public void actionPerformed(ActionEvent e) {
									errorLugar.dispose();
								}
							});
							
							panelBoton.add(ok);
							panelPrincipal.add(panelBoton);
							errorLugar.add(panelPrincipal);
							errorLugar.setPreferredSize(new Dimension(350,160));
							errorLugar.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
							errorLugar.pack();
							errorLugar.setVisible(true);
						}	
					}else{
						JsonObject json = controlador.publicarOpinion(idLugar, puntuacion,opinionArea.getText(),id);
						if(json.has("opinion publicada")) {
							menuPublicarOp.dispose();
							menu();
						}else {
							errorPublicacion();
						}	
					}
				}	
			}

			private void errorPublicacion() {
				JDialog errorPublicacion = new JDialog();
				errorPublicacion.setTitle("Ha sucedido un error");
				JPanel panelPrincipal = new JPanel(new GridLayout(2, 1));
				JPanel panelTexto = new JPanel();
				JLabel textoLabel = new JLabel("No se ha podido publicar la opinión");
				panelTexto.add(textoLabel);
				panelPrincipal.add(panelTexto);
				JPanel panelBoton = new JPanel();
				JButton ok = new JButton("Vale");
				ok.addActionListener(new ActionListener() {		
					
					@Override
					public void actionPerformed(ActionEvent e) {
						errorPublicacion.dispose();
					}
				});
				
				panelBoton.add(ok);
				panelPrincipal.add(panelBoton);
				errorPublicacion.add(panelPrincipal);
				errorPublicacion.setPreferredSize(new Dimension(350,160));
				errorPublicacion.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				errorPublicacion.pack();
				errorPublicacion.setVisible(true);
			}
		});
		JButton botonVolver= new JButton ("Volver");
		botonVolver.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				menuPublicarOp.dispose();
				menu();
				
			}
		});
		panelSur.add(puntuacionLabel);
		panelSur.add(puntuacionText);
		panelSur.add(botonCrear);
		panelSur.add(botonVolver);
		panel.add(panelSur, BorderLayout.SOUTH);
		menuPublicarOp.add(panel);
		menuPublicarOp.pack();
		menuPublicarOp.setPreferredSize(new Dimension(400,600));
		menuPublicarOp.setVisible(true);
		menuPublicarOp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menuPublicarOp.pack();
	}    
	
	/**
	 * Este método es para preparar el menú de un lugar y que final muestra el método de listaOpiniones.
	 * @param id		id del lugar consultado
	 * @param nombre	nombre del lugar consultado
	 */
	public void menuLugar(int id, String nombre) {
		JsonObject jsonLugar=controlador.buscarLugarID(id);
		List<String> listaOpiniones = new ArrayList<String>();
		JsonArray arrayLugares = new JsonArray();
		if(jsonLugar.has("opiniones")) arrayLugares=jsonLugar.getAsJsonArray("opiniones");
		if(arrayLugares.size()==0)
			System.out.println("no hay lugares");
		for(JsonElement ele:arrayLugares) 
			listaOpiniones.add(controlador.buscarOpinionID(ele.getAsInt()).toString());
		menuOpiniones(listaOpiniones,"lugares:"+nombre);
		
	}
	
	/**
	 * Un método para mostrar menús con diferentes botones. Según los parámetros va a mostrar un menú u otro
	 * @param lista		el texto que se va a mostrar en los botones	
	 * @param tipo		el tipo de botones que se van a mostrar
	 */
	public void listaBotones(Map<Integer,String> lista, String tipo) {
		JFrame menuBotones=new JFrame();
		JPanel panel=new JPanel(new GridLayout(0,1));	
		
		for(Entry<Integer,String> ele: lista.entrySet()) {
			JButton aux = new JButton(ele.getValue());
			aux.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if(ele.getKey()==0 && ele.getValue().equals("Crear nuevo lugar")) {
						menuBotones.dispose();
						menuPublicarOpinion(0,null);
					}
					if(ele.getValue().equals("No sigues a ningún usuario")) {
						menuBotones.dispose();
						menu();
					}
					if(ele.getKey()==Integer.MAX_VALUE) {
						menuBotones.dispose();
						menu();
					}
					if(tipo.equals("publicar opinion") && ele.getKey()!=Integer.MAX_VALUE && ele.getKey()!=0){
						menuBotones.dispose();
						menuPublicarOpinion(ele.getKey(),ele.getValue());
					}
					if(tipo.equals("usuarios") && ele.getKey()!=Integer.MAX_VALUE && ele.getKey()!=0 && ele.getKey()!=Integer.MAX_VALUE-1){
						menuBotones.dispose();
						menuUsuario(ele.getKey(),ele.getValue());
					}
					
					if(tipo.equals("lugares") && ele.getKey()!=Integer.MAX_VALUE && ele.getKey()!=0){
						menuBotones.dispose();
						menuLugar(ele.getKey(),ele.getValue());
					}
				}
			});
			panel.add(aux);
		}
		JScrollPane scroll=new JScrollPane(panel);
		menuBotones.add(scroll);
		menuBotones.setVisible(true);
		menuBotones.pack();
		menuBotones.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	/**
	 * El menú para de log in del usuario
	 */
	public void menuLogueo() {
		JDialog ventanaLogueo=new JDialog();
		ventanaLogueo.setTitle("Acceda con su usuario");
		JPanel principal = new JPanel();
		JPanel datos = new JPanel(new GridLayout(2,2));
		JPanel panel1 = new JPanel();
		panel1.add(new JLabel("Usuario: "));
		JPanel panel2 = new JPanel();
		panel2.add(new JLabel("Contraseña: "));	
		JPanel panel3 = new JPanel();
		JTextField jUsuario= new JTextField();
		jUsuario.setColumns(10);
		panel3.add(jUsuario);
		JPanel panel4 = new JPanel();
		JPasswordField jContraseña= new JPasswordField();
		jContraseña.setColumns(10);
		panel4.add(jContraseña);
		datos.add(panel1);
		datos.add(panel3);
		datos.add(panel2);
		datos.add(panel4);
		JPanel botones = new JPanel();
		JButton acceder = new JButton("Iniciar sesión");
		acceder.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String usuarioTexto = jUsuario.getText();
				char[] aux = jContraseña.getPassword();
				StringBuilder sb = new StringBuilder();
				for (char c : aux) sb.append(c);
				String contraseñaUsuario = sb.toString();
				JsonObject respuesta = controlador.loguearse(usuarioTexto, contraseñaUsuario);
				if(respuesta.has("usuario")) {
					usuario = respuesta.get("usuario").getAsString();
					id = respuesta.get("id").getAsInt();
					menu();
					ventanaLogueo.setVisible(false);
					ventanaLogueo.dispose();
				}else {
					if(respuesta.has("usuario incorrecto")) menuIncorrecto("usuario");
					if(respuesta.has("contraseña incorrecta")) menuIncorrecto("contraseña");	
				}
				
			}

			private void menuIncorrecto(String error) {
				JDialog usuarioIncorrecto = new JDialog();
				usuarioIncorrecto.setTitle("Se ha producido un error");
				JPanel principal = new JPanel(new GridLayout(2,0));
				JPanel norte = new JPanel();
				JLabel mensaje = new JLabel();
				if(error.equals("usuario")) {
					mensaje.setText("Usuario incorrecto");
				}else {
					mensaje.setText("Contraseña incorrecta");
				}
				norte.add(mensaje);
				JPanel sur = new JPanel();
				JButton botonOk = new JButton("ok");
				botonOk.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						usuarioIncorrecto.dispose();
					}
				});
				
				sur.add(botonOk);
				principal.add(norte);
				principal.add(sur);
				usuarioIncorrecto.add(principal);
				usuarioIncorrecto.setPreferredSize(new Dimension(350,160));
				usuarioIncorrecto.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				usuarioIncorrecto.pack();
				usuarioIncorrecto.setVisible(true);
				
			}
	
		});
		JButton crearUsuario = new JButton("Crea una cuenta");
		crearUsuario.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				menuCrearUsuario();
				
			}

		});
		
		botones.add(acceder);
		botones.add(crearUsuario);
		principal.add(datos);
		principal.add(botones);
		ventanaLogueo.add(principal);
		ventanaLogueo.setPreferredSize(new Dimension(350,160));
		ventanaLogueo.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		ventanaLogueo.pack();
		ventanaLogueo.setVisible(true);
	}
	

	/**
	 * El menú para crear un usuario nuevo
	 */
	public void menuCrearUsuario() {
		JDialog ventanaCrearUsuario=new JDialog();
		ventanaCrearUsuario.setTitle("Crea un nuevo usuario");
		JPanel principal = new JPanel(new GridLayout(2,2));
		JPanel datos = new JPanel(new GridLayout(3,2));
		JPanel panel1 = new JPanel();
		panel1.add(new JLabel("Usuario: "));
		JPanel panel2 = new JPanel();
		panel2.add(new JLabel("Contraseña: "));	
		JPanel panel3 = new JPanel();
		JTextField jUsuario= new JTextField();
		jUsuario.setColumns(10);
		panel3.add(jUsuario);
		JPanel panel4 = new JPanel();
		JTextField jContraseña = new JPasswordField();
		jContraseña.setColumns(10);
		panel4.add(jContraseña);
		JPanel panel5 = new JPanel();
		panel5.add(new JLabel("Confirmar contraseña: "));
		JPanel panel6 = new JPanel();
		JTextField jContraseña2= new JPasswordField();
		jContraseña2.setColumns(10);
		panel6.add(jContraseña2);
		datos.add(panel1);
		datos.add(panel3);
		datos.add(panel2);
		datos.add(panel4);
		datos.add(panel5);
		datos.add(panel6);
		JPanel botones = new JPanel();
		JButton crear = new JButton("Crear usuario");
		crear.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String usuarioTexto=jUsuario.getText();
				String usuarioContraseña=jContraseña.getText();
				String usuarioContraseña2=jContraseña2.getText();
				if(!(usuarioContraseña.equals(usuarioContraseña2))) {
					JOptionPane.showMessageDialog(null, "La contraseña y la confirmación de la contraseña no son iguales");
				}else {
					JsonObject respuesta=controlador.crearUsuario(usuarioTexto, usuarioContraseña);
					if(respuesta.has("usuario"))usuarioCreado();
					else usuarioExistente();
				}	
			}

			private void usuarioExistente() {
				JDialog usuarioExistente = new JDialog();
				JPanel panelPrincipal = new JPanel(new GridLayout(2, 1));
				JPanel panelTexto = new JPanel();
				JLabel textoLabel = new JLabel("Este nombre de usuario ya existe");
				panelTexto.add(textoLabel);
				panelPrincipal.add(panelTexto);
				JPanel panelBoton = new JPanel();
				JButton ok = new JButton("Vale");
				ok.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						usuarioExistente.dispose();
					}
				});
				
				panelBoton.add(ok);
				panelPrincipal.add(panelBoton);
				usuarioExistente.add(panelPrincipal);
				usuarioExistente.setPreferredSize(new Dimension(350,160));
				usuarioExistente.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				usuarioExistente.pack();
				usuarioExistente.setVisible(true);
			}

			private void usuarioCreado() {
				JDialog usuarioCreado = new JDialog();
				jUsuario.setText("");
				jContraseña.setText("");
				jContraseña2.setText("");
				JPanel panelPrincipal = new JPanel(new GridLayout(2, 1));
				JPanel panelTexto = new JPanel();
				JLabel labelTexto = new JLabel("El usuario se ha creado correctamente");
				panelTexto.add(labelTexto);
				panelPrincipal.add(panelTexto);
				JPanel panelBoton = new JPanel();
				JButton ok = new JButton("Vale");
				ok.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						usuarioCreado.dispose();
					}
				});
				panelBoton.add(ok);
				panelPrincipal.add(panelBoton);
				usuarioCreado.add(panelPrincipal);
				usuarioCreado.setPreferredSize(new Dimension(350,160));
				usuarioCreado.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				usuarioCreado.pack();
				usuarioCreado.setVisible(true);
				
			}
		});
		JButton cancelar = new JButton("Cancelar");
		cancelar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ventanaCrearUsuario.setVisible(false);
				ventanaCrearUsuario.dispose();	
			}
		});

		botones.add(crear);
		botones.add(cancelar);
		principal.add(datos);
		principal.add(botones);
		ventanaCrearUsuario.add(principal);
		ventanaCrearUsuario.setPreferredSize(new Dimension(380,200));
		ventanaCrearUsuario.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		ventanaCrearUsuario.pack();
		ventanaCrearUsuario.setVisible(true);
	}
	
	public static void main(String[] args) {
		new MenuPrincipal();
	}

}

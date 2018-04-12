package jus.aor.rmi;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Annuaire extends UnicastRemoteObject implements _Annuaire {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<String, Numero> annuaire = new HashMap<String,Numero>();
			
	public Annuaire (String file) throws SAXException, IOException, ParserConfigurationException {
	
		try {
			/* Récupération de l'annuaire dans le fichier xml */
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = docBuilder.parse(new File(file));

			
			NodeList list = document.getElementsByTagName("Telephone");
			NamedNodeMap attrs;
			/* acquisition de toutes les entrées de l'annuaire */
			for(int i =0; i<list.getLength();i++) {
				attrs = list.item(i).getAttributes();
				String name=attrs.getNamedItem("name").getNodeValue();
				Numero numero= new Numero(attrs.getNamedItem("numero").getNodeValue());
				annuaire.put(name, numero);
			}
		}catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Numero get(String abonne) throws RemoteException {
		return annuaire.get(abonne);
	}

}

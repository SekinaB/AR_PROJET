package jus.aor.rmi;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



public class Chaine extends UnicastRemoteObject implements _Chaine {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Hotel> hotelsList = new ArrayList<Hotel>();
	List<Hotel> hotelsLocated = new ArrayList<Hotel>();
	
	/* récupération des hôtels de la chaîne dans le fichier xml passé en argument */
	
	protected Chaine(String file) throws ParserConfigurationException, SAXException, IOException {
		try {
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			org.w3c.dom.Document document = docBuilder.parse(new File(file));

			String name, localisation;
			NodeList list = document.getElementsByTagName("Hotel");
			NamedNodeMap attrs;
			
			/* acquisition de toutes les entrées de la base d'hôtels */
			for(int i =0; i<list.getLength();i++) {
				attrs = list.item(i).getAttributes();
				name=attrs.getNamedItem("name").getNodeValue();
				localisation=attrs.getNamedItem("localisation").getNodeValue();
				
				this.hotelsList.add(new Hotel(name,localisation));
			}
		} catch (SAXException e) {
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
	
	/* récupération des hôtels présent dans une localisation passée en argument */
	@Override
	public List<Hotel> get(String localisation) throws RemoteException {
		
		for (Hotel hotel : this.hotelsList) {
			
			if(hotel.localisation.matches(localisation)) {
				hotelsLocated.add(hotel);
			}
		}
		return hotelsLocated;
	}

}

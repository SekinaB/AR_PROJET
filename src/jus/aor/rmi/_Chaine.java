package jus.aor.rmi;
import  java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface _Chaine extends Remote{
	/**
	 * Restitue la liste des hotels situés dans la localisation.
	 * @param localisation le lieu où l'on recherche des hotels
	 * @return la liste des hotels trouvés
	 */
	public abstract List<Hotel> get(String localisation) throws RemoteException ;
}

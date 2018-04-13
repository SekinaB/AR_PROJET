package jus.aor.mobilagent.obj6;

import java.rmi.registry.LocateRegistry;

public class Annuaire {
	public static void main(String[] args) {
		try {
			_InSigner obj = new InSigner();
			LocateRegistry.createRegistry(2001);
			java.rmi.Naming.bind("//localhost:2001/InSignerCenter", obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

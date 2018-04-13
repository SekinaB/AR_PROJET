package jus.aor.mobilagent.obj6;

import java.rmi.RemoteException;
import java.util.HashMap;

public class InSigner extends java.rmi.server.UnicastRemoteObject implements _InSigner {

	HashMap<jus.aor.mobilagent.kernel.Server, String> annuaire;

	protected InSigner() throws RemoteException {
		super();
		annuaire = new HashMap<>();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void signIn(String service, jus.aor.mobilagent.kernel.Server server) throws RemoteException {
		// TODO Auto-generated method stub
		annuaire.replace(server, service);
	}

	@Override
	public boolean checkout(String service) throws RemoteException {
		return annuaire.containsValue(service);
	}

}

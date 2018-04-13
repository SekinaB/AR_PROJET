package jus.aor.mobilagent.obj6;

import java.rmi.RemoteException;

public interface _InSigner extends java.rmi.Remote {

	// S'enregistrer
	public void signIn(String serverName, jus.aor.mobilagent.kernel.Server server) throws RemoteException;

	// Verifier si un service est dispo
	public boolean checkout(String service) throws RemoteException;
}

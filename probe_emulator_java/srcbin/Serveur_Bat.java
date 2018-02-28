import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;

public class Serveur_Bat implements Runnable{
	
	private static final int PORT = 7539;
	private static String IP; // adresse IP du serveur
	private static boolean Active = false;
	private static String stringR = "";
	private static ObjectOutputStream out;
	private static Socket s;

	private Thread monThread;
	private int BatLevel=-1;
	
	public Serveur_Bat(int level) {
		BatLevel = level;
		monThread = new Thread(this,"Battery Thread");
		monThread.start();
		try {
			monThread.join();
		} catch (InterruptedException e) {
			System.out.println("Erreur de joinction du thread");
		}
	}
	
	@Override
	public void run() {
		
		//detect the Broadcast IP Address of the device
		while (true) {
				Enumeration en;
				try {
				en = NetworkInterface.getNetworkInterfaces();
				while(en.hasMoreElements()){
				    NetworkInterface ni=(NetworkInterface) en.nextElement();
				    Enumeration ee = ni.getInetAddresses();
				    while(ee.hasMoreElements()) {
				        InetAddress ia= (InetAddress) ee.nextElement();
				        if (ia.getHostAddress().contains("192")) {
				        	IP = ia.getHostAddress();
				        	break;
				        }
				    }
				 }
				
				ServerSocket serversocket=new ServerSocket(PORT,1,InetAddress.getByName(IP));
				System.out.println("Server batery ready to connect");
				
				// Wait until a client is connecting
				s = serversocket.accept();
				System.out.println("Client connnected (Batterie)");
				out = new ObjectOutputStream(s.getOutputStream());
				System.out.println("Batery level send : " + String.valueOf(BatLevel));
				System.out.println("sending ... ");
				Active = true;
				
				while (Active) {
					out.writeInt(BatLevel);
					out.flush();
				}
				
				}catch (Exception e) {
					Active = false;
				}
		}
	}	
}

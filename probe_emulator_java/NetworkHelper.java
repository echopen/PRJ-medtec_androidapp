import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class NetworkHelper {

	/**
	 * 
	 * @return
	 */
	
	public static String getCurrentBroadcastIp() {
		Enumeration lEn;
		
		try {
			lEn = NetworkInterface.getNetworkInterfaces();
			while(lEn.hasMoreElements()){
			    NetworkInterface lNi=(NetworkInterface) lEn.nextElement();
			    Enumeration lEe = lNi.getInetAddresses();
			    while(lEe.hasMoreElements()) {
			        InetAddress lIa = (InetAddress) lEe.nextElement();
			        if (lIa.getHostAddress().contains("192")) {
			        	return lIa.getHostAddress();
			        }
			    }
			 }
			return "";
		}
		
		catch (Exception e) {
			return "";
		}
	}
}

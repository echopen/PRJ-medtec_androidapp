import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Serveur implements Runnable{
	
	// for server setup
	private static final int PORT = 7538;
	private static String IP; // adresse IP du serveur
	private static boolean Active = true;
	private static String stringR = "";
	private static ObjectOutputStream out;
	private static Socket s;
	
	//for image setup
	private static float r0 = (float) 80.0;
	private static float rf = (float) 160.0;
	private static int dec = 8;
	private static int Nline = 0;
	private static double sector = 80;
	private static int mode_RP = 0;
	private static int step = 1;
	private static int buffer_length = 0;
	private static short[][] DATA;
	private static byte[][] convert;
	
	private static int HeaderLimit = 0;
	
	private Thread monThread;
	
	public Serveur() {
		monThread = new Thread(this,"Image Thread");
		monThread.start();
		try {
			monThread.join();
		} catch (InterruptedException e) {
			System.out.println("Erreur de joinction du thread");
		}
	}
	
	@Override
	public void run(){
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
				System.out.println("Server Image ready to connect");
				
				// Wait until a client is connecting
				s = serversocket.accept();
				System.out.println("Client connnected (Image)");
				out = new ObjectOutputStream(s.getOutputStream());
				String path = "data/hand"; // folder where picutre are located
				File file = new File(path + "/hand.txt");
				
				FileReader fin = new FileReader(file);
				
				//Reading of the file
				GetConfig(path);
				InitData();
				LoadParams(fin);
				fin.close();
				
				
				while (true) {
					SendImage(1);
					SendImage(2);
				}
				
				}catch (Exception e) {
					
				}
		}
	}
	
	
	// For skipping the header of files
	public static void LoadParams(FileReader in) throws Exception {
		BufferedReader buffer = new BufferedReader(in);
		String Line =  "";
		Pattern pattern;
		Matcher matcher;
		
		for (int i=0;i<8;i++) {
			Line = buffer.readLine();
		}
		
		int j;
		for (int i = 0;i<Nline;i++) {
			j=1;
			Line=buffer.readLine();
			pattern = Pattern.compile("[0-9]{1,3}[ \\t\\n\\x0B\\f\\r]");
			matcher = pattern.matcher(Line);
			while(matcher.find()) {
				DATA[i][j]=Short.valueOf(matcher.group().substring(0, matcher.group().length()-1));
				j++;		
			}
		}
		
		Convert();
	}
	
	public static void SendImage(int sens) throws Exception{
		if (sens==1) {
			for (int i=0;i<Nline;i++) {
				//send sens 1
				out.write(convert[i]);
				out.flush();
				Thread.sleep(1);
				
			}

		}else {
			for (int i=Nline;i>0;i--) {
				//send sens 2
				out.write(convert[i-1]);
				out.flush();
				Thread.sleep(1);
			}
	
		}
	}
	
	public static void GetConfig(String path) throws Exception{
			
		File file = new File(path + "/settings.txt");
		FileReader fin = new FileReader(file);
		BufferedReader buffer = new BufferedReader(fin);
		
		r0 = Float.valueOf(buffer.readLine()); //lr0
		rf = Float.valueOf(buffer.readLine()); //lrf
		dec = Integer.valueOf(buffer.readLine()); //ldecimation
		Nline = Integer.valueOf(buffer.readLine()); //llineperimage
		sector = Double.valueOf(buffer.readLine()); //LprobeSectorAngle
		mode_RP = Integer.valueOf(buffer.readLine()); //Lmode
				
		System.out.println("Config : OK");
		
		//send of the config
		char[] config = new char[6];
		config[0]=(char)r0;
		config[1]=(char)rf;
		config[2]=(char)dec;
		config[3]=(char)Nline;
		config[4]=(char)sector;
		config[5]=(char)mode_RP;
		
		byte[] byteConfig = toByteArray(config);
		
		out.write(byteConfig);
		out.flush();
		
		System.out.println("config send to client : OK");
	}
	
	public static void InitData() {
		float scale = (float) (2.0*125.0/1.48/((float)dec));
		buffer_length=(int)(scale*(rf-r0));
		if (buffer_length>16384) {
			buffer_length=16384;
		}
		
		DATA = new short[Nline][buffer_length+1];
		for (int i=0;i<Nline;i++) {
			DATA[i][0]= (short)(i+1);
		}
	}
	
	
	public static void test() {
		System.out.println("image format : " + DATA.length + "x" + (DATA[0].length-1));
		for (int j=0;j<DATA[0].length;j++) {
			System.out.print(DATA[0][j]);
			System.out.print(" ");
		}
		System.out.println("");
		for (int j=0;j<DATA[0].length;j++) {
			System.out.print(DATA[6][j]);
			System.out.print(" ");
		}
	}
	
	
	public static void Convert() {
		convert = new byte[Nline][2*(buffer_length+1)];
		for (int i = 0;i<Nline;i++) {
			for (int j =0;j<buffer_length+1;j++) {
				convert[i][2*j] = (byte)(DATA[i][j] & 0xff);
				convert[i][2*j+1] = (byte)((DATA[i][j]>> 8) & 0xff);
			}
		}
	}
	
	public static byte[] toByteArray(char[] array) {
		return toByteArray(array, Charset.defaultCharset());
	}
 
	public static byte[] toByteArray(char[] array, Charset charset) {
		CharBuffer cbuf = CharBuffer.wrap(array);
		ByteBuffer bbuf = charset.encode(cbuf);
		return bbuf.array();
	}
}

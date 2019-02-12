package echopen.emulator.probe;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.CommandLine;

public class ProbeEmulator {

	public static void main(String[] args) throws ParseException{

		// handle input parameters
		Options lOptions = new Options();
		lOptions.addOption("t", "transducer", true, "select the transducer you want to use (3_5MHz, 5MHz, 8MHz)");

		HelpFormatter lFormatter = new HelpFormatter();
		lFormatter.printHelp("Java Probe Emulator", lOptions);

		// process command line
		CommandLineParser lParser = new DefaultParser();
		CommandLine lCmd = lParser.parse( lOptions, args);

		String lTransducerOption = lCmd.getOptionValue("t");
		System.out.println(lTransducerOption);

		if(lTransducerOption == null ||
			 (!lTransducerOption.equals("3_5MHz") &&
			 !lTransducerOption.equals("5MHz"))){
			System.out.println("Missing or invalid transducer option");
			return;
		}

		// start probe emulator
		ProbeServer lProbeServer = new ProbeServer();
		lProbeServer.start();
	}
}

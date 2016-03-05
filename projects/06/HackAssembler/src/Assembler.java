import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;


public class Assembler {
	private HashMap<String, String> addressMap = new HashMap<String, String>();
	
	private HashMap<String, String> destMap = new HashMap<String, String>();
	
	private HashMap<String, String> compMap = new HashMap<String, String>();
	
	private HashMap<String, String> jmpMap = new HashMap<String, String>();
	
	public Assembler() {
		addressMap.put("R0", "000000000000000");
		addressMap.put("R1", "000000000000001");
		addressMap.put("R2", "000000000000010");
		addressMap.put("R3", "000000000000011");
		addressMap.put("R4", "000000000000100");
		addressMap.put("R5", "000000000000101");
		addressMap.put("R6", "000000000000110");
		addressMap.put("R7", "000000000000111");
		addressMap.put("R8", "000000000001000");
		addressMap.put("R9", "000000000001001");
		addressMap.put("R10", "000000000001010");
		addressMap.put("R11", "000000000001011");
		addressMap.put("R12", "000000000001100");
		addressMap.put("R13", "000000000001101");
		addressMap.put("R14", "000000000001110");
		addressMap.put("R15", "000000000001111");
		addressMap.put("SCREEN", "100000000000000");
		addressMap.put("KBD", "110000000000000");
		addressMap.put("SP", "000000000000000");
		addressMap.put("LCL", "000000000000001");
		addressMap.put("ARG", "000000000000010");
		addressMap.put("THIS", "000000000000011");
		addressMap.put("THAT", "000000000000100");
		
		compMap.put("0", "0101010");
		compMap.put("1", "0111111");
		compMap.put("-1", "0111010");
		compMap.put("D", "0001100");
		compMap.put("A", "0110000");
		compMap.put("!D", "0001101");
		compMap.put("!A", "0110001");
		compMap.put("-D", "0001111");
		compMap.put("-A", "0110011");
		compMap.put("D+1", "0011111");
		compMap.put("A+1", "0110111");
		compMap.put("D-1", "0001110");
		compMap.put("A-1", "0110010");
		compMap.put("D+A", "0000010");
		compMap.put("D-A", "0010011");
		compMap.put("A-D", "0000111");
		compMap.put("D&A", "0000000");
		compMap.put("D|A", "0010101");
		
		compMap.put("M", "1110000");
		compMap.put("!M", "1110001");
		compMap.put("-M", "1110011");
		compMap.put("M+1", "1110111");
		compMap.put("M-1", "1110010");
		compMap.put("D+M", "1000010");
		compMap.put("D-M", "1010011");
		compMap.put("M-D", "1000111");
		compMap.put("D&M", "1000000");
		compMap.put("D|M", "1010101");
		
		destMap.put("", "000");
		destMap.put("M", "001");
		destMap.put("D", "010");
		destMap.put("MD", "011");
		destMap.put("A", "100");
		destMap.put("AM", "101");
		destMap.put("AD", "110");
		destMap.put("AMD", "111");
		
		jmpMap.put("", "000");
		jmpMap.put("JGT", "001");
		jmpMap.put("JEQ", "010");
		jmpMap.put("JGE", "011");
		jmpMap.put("JLT", "100");
		jmpMap.put("JNE", "101");
		jmpMap.put("JLE", "110");
		jmpMap.put("JMP", "111");
		
	}
	
	public void assemble(String path) {
		int pc= -1;
		
		int ramCount = 16;
		
		String binaries = "";
		
		BufferedReader br = null;
		
		// First iteration of reading the file to find all the labels and their respective address, and insert them into the Address Map. 
		try {

			String line;

			br = new BufferedReader(new FileReader(path));

			while ((line = br.readLine()) != null) {
				int index = line.indexOf("/"); // Search for the start of a comment.
				
				// If index > -1, ge the part of the string before the comment.
				if (index > -1) {
					line = line.substring(0, index);
				}
				
				line = line.trim(); // Remove the string off white spaces.
				
				//System.out.println(line);
				
				// If length of String is bigger than zero.
				if (line.length() > 0) {
					if (line.charAt(0) == '(' && line.charAt(line.length()-1) == ')') { // If this is a label line, check whether if this label exists inside the Address Map.
						String labelStr = line.substring(1, line.length()-1);
						String labelValue = addressMap.get(labelStr);
						
						if (labelValue == null) {
							addressMap.put(labelStr, String.format("%15s", Integer.toBinaryString(pc+1)).replace(' ', '0'));
						}
						
						continue;
					}
					
					pc++;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		
		// Second iteration of reading the file, translating the assembly codes to machine codes. 
		try {
			String line;

			br = new BufferedReader(new FileReader(path));

			while ((line = br.readLine()) != null) {
				int index = line.indexOf("/"); // Search for the start of a comment.
				
				// If index > -1, ge the part of the string before the comment.
				if (index > -1) {
					line = line.substring(0, index);
				}
				
				line = line.trim(); // Remove the string off white spaces.
				
				//System.out.println(line);
				
				// If length of String is bigger than zero.
				if (line.length() > 0) {
					if (line.charAt(0) == '(' && line.charAt(line.length()-1) == ')') // If this is a label line, ignore this line.
						continue;
					
					// If first character of a String is '@', then this is an 'A' instruction.
					if (line.charAt(0) == '@') {
						binaries += "0";
						
						int address;
						
						String decimalStr = line.substring(1, line.length());
						
						try {
							address = Integer.parseInt(decimalStr);
						} catch (NumberFormatException e) {
							address = Integer.MIN_VALUE;
						}
						
						// If address is not an Integer value, then it might be a label or variable.
						if (address == Integer.MIN_VALUE) {
							// Check to see if symbol exists inside the HashMap.
							String value = addressMap.get(decimalStr);
							
							// If the symbol exists inside the HashMap, then concatenate it to the machine code String. 
							if (value != null) {
								binaries += value;
							} else { // The symbol is not inside the HashMap, it is a new variable.
								binaries += String.format("%15s", Integer.toBinaryString(ramCount)).replace(' ', '0'); // Concatenate the available RAM address to the machine code String.
								
								addressMap.put(decimalStr, String.format("%15s", Integer.toBinaryString(ramCount)).replace(' ', '0')); // Add this new variable and address to the address Map.
								
								ramCount++; // Increment the available RAM count;
							}
						} else { // Else convert it to binary and concatenate it to the machine code String.
							binaries += String.format("%15s", Integer.toBinaryString(address)).replace(' ', '0');
						}
					} else { // Else it is a 'C' instruction.
						binaries += "111";
						
						int equalIndex = line.indexOf("="); // Get the index of the equal sign. 
						int colonIndex = line.indexOf(";"); // Get the index of the semi-colon sign. 
						
						if (equalIndex > -1) { // there is an equal sign.
							String destStr = line.substring(0, equalIndex); // Get the destination part of the command string.
							String destValue = destMap.get(destStr);
							
							String compStr = "";
							String compValue = "";
							
							if (colonIndex > -1) {
								compStr = line.substring(equalIndex, colonIndex); // Get the compute part of the command string.
								compValue = compMap.get(compStr);
								
								String jmpStr = line.substring(colonIndex, line.length()-1); // Get the jump part of the command string.
								String jmpValue = jmpMap.get(jmpStr);
								
								binaries += compValue + destValue + jmpValue;
							} else {
								compStr = line.substring(equalIndex+1, line.length()); // Get the compute part of the command string.
								compValue = compMap.get(compStr);
								
								binaries += compValue + destValue + "000";
							}
							
						} else {
							String compStr = line.substring(0, colonIndex); // Get the compute part of the command string.
							String compValue = compMap.get(compStr);
							
							String jmpStr = line.substring(colonIndex+1, line.length()); // Get the jump part of the command string.
							String jmpValue = jmpMap.get(jmpStr);
							
							binaries += compValue + "000" + jmpValue;
							
						}
					}
					
					binaries += "\n";
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		//System.out.print(binaries);
		
		String filename = FileWrapper.getFileName(path); // Get the file name from the path provided.
		String route = FileWrapper.getRoute(path);
		FileWrapper.writeFile(route + "\\" + filename+".hack", binaries); // Write the translated machine code to a file.
	}
	
	
}

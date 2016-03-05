import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class FileWrapper {
	public static String getFileName(String path) {
		String filename = new File(path).getName();
		int index = filename.indexOf(".asm");
		
		return filename.substring(0, index);
	}
	
	public static String getRoute(String path) {
		return new File(path).getParent();
	}
	
	public static void writeFile(String filename, String content) {
		try {
			File file = new File(filename);

			// If file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();

			System.out.println("File written.");

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}

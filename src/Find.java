import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class Find {
	
	public static void main(String[] args) {
		String nome = "Vinicius";
		String token = "EAACEdEose0cBANlqsLknLXzKWcgtOqu7pEKqJ4yPndztqiNnprZBGVB7ZCHc6kDCJFGRPWxI20nH4DC5J627V3oqOE9HeWuVdpn6ciM81mqRg5ZBGXGtiLhCRL8BnmOL0TrLImLu5xEBS72jyNVOtpn1PtJLm5xxgjSbys9krA440bLO3isUYMWjV4ZCPkDygs2eWHCxZAAZDZD";
		String eventId = "882121291963635";
		URL url;
		File arquivoFinal = new File("arquivoFinal");
		PrintWriter writer = null;
		String regex = "\\bnext.*\\b";
		String regex2 = "\\{([^}]*\\b(" + nome + ")\\b[^}]*)\\}";
		String linha;
		BufferedReader br = null;
		Matcher m = null;
		Pattern p = Pattern.compile(regex);
		FileOutputStream fos;
		FileInputStream fis;
		byte[] fileBytes;
		int bytesRead = 0;
		List<File> list = new ArrayList<File>();
		int idArquivo = 1;
		
		try {
			url = new URL("https://graph.facebook.com/v2.0/" + eventId + "/attending?access_token=" + token);
			File arquivo = new File("arquivo" + idArquivo);
			idArquivo++;
			FileUtils.copyURLToFile(url, arquivo);
			list.add(arquivo);
			br = new BufferedReader(new FileReader(arquivo));
			
			while ((linha = br.readLine()) != null) {
			    m = p.matcher(linha);
			    
			    if(m.find(0))
			    	url = new URL(m.group(0).split("\"")[2].replace("\\", ""));
			}
			
			while(m.find(0)){
				arquivo = new File("arquivo" + idArquivo);
				idArquivo++;
				FileUtils.copyURLToFile(url, arquivo);
				list.add(arquivo);
				br = new BufferedReader(new FileReader(arquivo));
				
				while ((linha = br.readLine()) != null) {
				    m = p.matcher(linha);
				    
				    if(m.find(0))
				    	url = new URL(m.group(0).split("\"")[2].replace("\\", ""));
				}
			}
			
			fos = new FileOutputStream(arquivoFinal,true);
			
            for (File file : list) {
                fis = new FileInputStream(file);
                fileBytes = new byte[(int) file.length()];
                bytesRead = fis.read(fileBytes, 0,(int)  file.length());
                assert(bytesRead == fileBytes.length);
                assert(bytesRead == (int) file.length());
                fos.write(fileBytes);
                fos.flush();
                fileBytes = null;
                fis.close();
                fis = null;
            }
            
            fos.close();
            fos = null;
			
			writer = new PrintWriter("profiles", "UTF-8");
			br = new BufferedReader(new FileReader(arquivoFinal));
			p = Pattern.compile(regex2);
			
			while ((linha = br.readLine()) != null) {
			    m = p.matcher(linha);
			    
			    while(m.find())
			    	writer.println(m.group(0).split("\"")[3] + ": " + "https://www.facebook.com/" + m.group(0).split("\"")[7]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

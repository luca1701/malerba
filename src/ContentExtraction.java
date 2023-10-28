
import java.io.IOException;
import java.rmi.AccessException;

import javax.servlet.annotation.MultipartConfig;

import javax.servlet.http.Part;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

@MultipartConfig
public class ContentExtraction {
	
	public static void printMetadata(Part p) throws IOException, AccessException, TikaException, SAXException{
		long start = System.currentTimeMillis();
		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		//FileInputStream content = new FileInputStream(fileName);
		Parser parser = new AutoDetectParser();
		parser.parse(p.getInputStream(), handler, metadata, new ParseContext());
		for(String name:metadata.names()) {
			System.out.println(name + ":\t" + metadata.get(name));
		}
		System.out.println("...extracting file..." + (System.currentTimeMillis()-start));
	}
	
	public static int checkImagetType(Part p) throws IOException, AccessException, TikaException, SAXException{
		long start = System.currentTimeMillis();
		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		//FileInputStream content = new FileInputStream(fileName);
		Parser parser = new AutoDetectParser();
		parser.parse(p.getInputStream(), handler, metadata, new ParseContext());
		for(String name:metadata.names()) {
			if(name.equals("Content-Type")){
				System.out.println(name + ":\t" + metadata.get(name));
				switch(metadata.get(name)) {
				case "image/png":
					return 0;
				case "image/jpeg":
					return 0;
				}
			}
		}
		System.out.println("...extracting file..." + (System.currentTimeMillis()-start));
		return 1;
	}

	public static int checkFileType(Part p) throws IOException, AccessException, TikaException, SAXException{
		long start = System.currentTimeMillis();
		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		//FileInputStream content = new FileInputStream(fileName);
		Parser parser = new AutoDetectParser();
		parser.parse(p.getInputStream(), handler, metadata, new ParseContext());
		for(String name:metadata.names()) {
			if(name.equals("Content-Type")){
				System.out.println(name + ":\t" + metadata.get(name));
				if(metadata.get(name).contains("text/plain") || metadata.get(name).contains("text/html")) {
					return 0;
				}
			}
		}
		System.out.println("...extracting file..." + (System.currentTimeMillis()-start));
		return 1;
	}
	
	public static void getName(Part p) throws IOException, SAXException, TikaException {
		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		Parser parser = new AutoDetectParser();
		parser.parse(p.getInputStream(), handler, metadata, new ParseContext());
		for(String name:metadata.names()) {
			if(name.equals("dc:title")){
				System.out.println(metadata.get(name));
			}
		}
	}
	
	public static void getSize(Part p) throws IOException, SAXException, TikaException {
		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		Parser parser = new AutoDetectParser();
		parser.parse(p.getInputStream(), handler, metadata, new ParseContext());
		for(String size:metadata.names()) {
			if(size.equals("tiff:ImageLength")){
				System.out.println(metadata.get(size));
			}
		}
	}
}

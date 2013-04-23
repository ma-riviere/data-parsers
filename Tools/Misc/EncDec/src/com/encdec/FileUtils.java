package com.encdec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtils {

	protected static List<File> files = new ArrayList<File>();
	
	public static List<File> collect(String sPath, String extension) {		
		Path path = Paths.get(sPath);
		try {explore(path, extension, files);}
		catch (IOException io) {System.out.println(io);}
		return files;
	}
	
	protected static void explore(Path directory, String extension, List<File> files) throws IOException {
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(directory)) {
			for (Path dirOrFile : ds) {
				System.out.println("Reading file : " + dirOrFile.toFile().getName());
				if (Files.isDirectory(dirOrFile))
					explore(dirOrFile, extension, files);
				else if (!dirOrFile.toFile().getName().toLowerCase().contains(".bat") && !dirOrFile.toFile().getName().toLowerCase().contains(".jar"))
					files.add(dirOrFile.toFile());
			}
		}
	}
	
	public static byte[] read(File file) {
		try {
			byte[] result = new byte[(int) file.length()];
			FileInputStream in = new FileInputStream(file);
			in.read(result);
			in.close();
			return result;
		}
		catch (Exception e) {
			System.out.println("Probleme lors de la lecture du fichier : "  + e.getMessage());
			return null;
		}
	}
 
	public static void write(byte[] data, String fn, String extension) {
		try {
			FileOutputStream out = new FileOutputStream(fn + extension);
			out.write(data);
			out.close();
		}
		catch (Exception e) {
			System.out.println("Probleme lors de la sauvegarde du fichier : " + e.getMessage());
		}
	}
	
	/*
	public static byte[] readZip() {
		Charset utf8 = Charset.forName("utf8");
		ZipFile zf = new ZipFile(f, utf8, utf8);
		try {
			ZipDirectoryEntry d = (ZipDirectoryEntry) zf.get(new AbsoluteLocation("/d"));
			System.out.println("Contents of /d: " + d.getChildEntries().keySet());
			ZipFileEntry df = (ZipFileEntry) d.getChildEntries().get("f");
		  
		}
		finally	{
		  zf.close();
		}
	}*/
	
	List<String> fileList;
    private static final String OUTPUT_ZIP_FILE = "C:\\MyFile.zip";
    private static final String SOURCE_FOLDER = "C:\\testzip";
	
	 public static void zipFile() {
    	AppZip appZip = new AppZip();
    	appZip.generateFileList(new File(SOURCE_FOLDER));
    	appZip.zipIt(OUTPUT_ZIP_FILE);
    }

    public static void zipDir(String zipFile) {
 
     byte[] buffer = new byte[1024];
 
     try{
 
    	FileOutputStream fos = new FileOutputStream(zipFile);
    	ZipOutputStream zos = new ZipOutputStream(fos);
 
    	System.out.println("Output to Zip : " + zipFile);
 
    	for(String file : this.fileList){
 
    		System.out.println("File Added : " + file);
    		ZipEntry ze = new ZipEntry(file);
        	zos.putNextEntry(ze);
 
        	FileInputStream in = new FileInputStream(SOURCE_FOLDER + File.separator + file);
 
        	int len;
        	while ((len = in.read(buffer)) > 0) {
        		zos.write(buffer, 0, len);
        	}
 
        	in.close();
    	}
 
    	zos.closeEntry();
    	//remember close it
    	zos.close();
 
    	System.out.println("Done");
    }catch(IOException ex){
       ex.printStackTrace();   
    }
   }
 
    /**
     * Traverse a directory and get all files,
     * and add the file into fileList  
     * @param node file or directory
     */
    public void generateFileList(File node){
 
    	//add file only
		if(node.isFile()){
			fileList.add(generateZipEntry(node.getAbsoluteFile().toString()));
		}
	 
		if(node.isDirectory()){
			String[] subNote = node.list();
			for(String filename : subNote){
				generateFileList(new File(node, filename));
			}
		}
 
    }
 
    /**
     * Format the file path for zip
     * @param file file path
     * @return Formatted file path
     */
    private String generateZipEntry(String file){
    	return file.substring(SOURCE_FOLDER.length()+1, file.length());
    }
}

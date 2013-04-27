package com.encdec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class FileUtils {

	protected static List<File> files = new ArrayList<File>();
	protected FileOutputStream fos = null;
	protected ZipOutputStream zos = null;
	
	protected String targetDir = null;
	protected String zip = null;
	
	public FileUtils(String targetDir) {
		this.targetDir = targetDir;
		zip = targetDir + "\\" + new File(targetDir).getName() + ".zip";
	}
	
	public List<File> collect(String extension) {		
		Path path = Paths.get(targetDir);
		try {explore(path, extension, files);}
		catch (IOException io) {System.out.println(io);}
		return files;
	}
	
	protected void explore(Path directory, String extension, List<File> files) throws IOException {
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(directory)) {
			for (Path dirOrFile : ds) {
				if (Files.isDirectory(dirOrFile))
					explore(dirOrFile, extension, files);
				else if (dirOrFile.toFile().getName().toLowerCase().contains(extension) && !dirOrFile.toFile().getName().toLowerCase().contains(".bat") && !dirOrFile.toFile().getName().toLowerCase().contains(".jar")) {
					files.add(dirOrFile.toFile());
				}
			}
		}
	}
	
	public byte[] read(File file) {
		try {
			byte[] result = new byte[(int) file.length()];
			FileInputStream in = new FileInputStream(file);
			in.read(result);
			in.close();
			return result;
		}
		catch (Exception e) {
			System.out.println("[READING] Probleme lors de la lecture du fichier : "  + e.getMessage());
			return null;
		}
	}
 
	public void write(byte[] data, String fn, String extension) {
		try {
			FileOutputStream out = new FileOutputStream(fn + extension);
			out.write(data);
			out.close();
		}
		catch (Exception e) {
			System.out.println("[WRITING] Probleme lors de la sauvegarde du fichier : " + e.getMessage());
		}
	}
   
	public File getArchive(String extension) {
		return new File(zip + extension);
	}
	
	public String getRelativePath(File file) {
		return new File(targetDir).toURI().relativize(file.toURI()).getPath();
	}
	
	public String getRelativePathWithoutFN(File file) {
		return new File(targetDir).toURI().relativize(file.getParentFile().toURI()).getPath();
	}
 
    /**
     * Traverse a directory and get all files,
     * and add the file into fileList  
     * @param node file or directory
     *
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
     *
    private String generateZipEntry(String file){
    	return file.substring(SOURCE_FOLDER.length()+1, file.length());
    }
	*/
}

package com.plateandpic.factory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.plateandpic.constants.ConstantsProperties;
import com.plateandpic.constants.MessageConstants;
import com.plateandpic.exceptions.PlateAndPicException;
import com.plateandpic.utils.GetPropertiesValues;

/**
 * @author gonzalo
 *
 */
public class FileFactory {
	
	private static final Logger log = LoggerFactory.getLogger(FileFactory.class);
	
	public static final String JPG = ".jpg";
	private static final String BASE_64 = "data:image/png;base64,";
	private static final String systemPath = "user.dir";
	private static final String DOT = ".";
	
	/**
	 * @param image
	 * @param userId
	 * @return
	 * @throws IOException
	 */
	public static String uploadProfilePicture(MultipartFile image, Long userId) throws IOException{
		
		File newImage = null;
		String newImageName = null;
		String realPath = "";
		GetPropertiesValues properties = null;
		String path = "";
		
		try{
			
			properties = new GetPropertiesValues(ConstantsProperties.APPLICATION_PROPERTIES);
			
			path = properties.getValue(ConstantsProperties.USER_PROFILE_PICTURE_PATH);
			
			realPath = System.getProperty(systemPath) + path;
			
			newImage = new File(realPath + userId + JPG);
			
			if(!newImage.exists()){
				newImage.createNewFile();
			}
			
			image.transferTo(newImage);
			
			newImageName = newImage.getName();
			
		}
		catch (IOException e){
			
			throw e;
		}
		
		return newImageName;
		
	}
	
	/**
	 * @param path
	 * @param profilePictureName
	 * @return
	 * @throws PlateAndPicException 
	 * @throws IOException
	 */
	public static String getBase64FromProfilePictureName(String path, String profilePictureName) throws PlateAndPicException {
		
		byte[] imageBytes = null;
		String uriBase64 = null;
		
		try{
			
			imageBytes = getBytesFromImage(path, profilePictureName);
			
			uriBase64 = getBase64FromBytes(imageBytes);
			
		} catch(IOException e){
			log.error(e.toString());
			throw new PlateAndPicException(MessageConstants.PLATEPICTURE_CANT_LOAD);
		}
		
		return uriBase64;
	}

	/**
	 * @param path
	 * @param profilePictureName
	 * @return
	 * @throws IOException
	 */
	public static byte[] getBytesFromImage(String path, String profilePictureName) throws IOException {
		
		byte[] imageBytes;
		File fi;
		String realPath;
		
		realPath = System.getProperty(systemPath);
		fi = new File(realPath + path + profilePictureName);
		imageBytes = Files.readAllBytes(fi.toPath());
			
		return imageBytes;
	}
	
	/**
	 * @param imageBytes
	 * @return
	 */
	public static String getBase64FromBytes(byte[] imageBytes){
		
		String uriBase64 = null;
		
		StringBuilder sb = new StringBuilder();
		sb.append(BASE_64);
		sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(imageBytes, false)));
		uriBase64 = sb.toString();
		
		return uriBase64;
	}
	
	/**
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 */
	public static File getFileFromFileSystem(String path) throws FileNotFoundException{
		
		String realPath;
		File file = null;
		
		realPath = System.getProperty(systemPath);
		
		file = new File(realPath + path);
		
		if(!file.exists())
			throw new FileNotFoundException();
		
		return file;
		
	}
	
	/**
	 * @param relativeFolderPath
	 * @param fileName
	 * @param fileToUpload
	 * @return
	 * @throws IOException
	 */
	public static String uploadFile(String relativeFolderPath, String fileName, MultipartFile fileToUpload) throws IOException{
		
		String newFileName = "";
		String projectPath = "";
		File newFile = null;
		
		try{
			
			projectPath = System.getProperty(systemPath);
			
			newFile = new File(projectPath + relativeFolderPath + fileName);
			
			if(!newFile.exists())
				newFile.createNewFile();
			
			fileToUpload.transferTo(newFile);
			
			newFileName = newFile.getName();
			
			
		} catch(IOException e){
			throw e;
		}
		
		return newFileName;
	}
	
	/**
	 * @param relativeFolderPath
	 * @return
	 */
	public static Integer getFileCount(String relativeFolderPath){
		
		String projectPath = "";
		File folder = null;
		Integer counter = 0;
		
		projectPath = System.getProperty(systemPath);
		
		folder = new File(projectPath + relativeFolderPath);
		
		if(folder.exists() && folder.isDirectory()){
			counter = folder.list().length;
		}
			
		return counter;		
	}

}

package so.zudui.space;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.ContentResolver;
import android.net.Uri;

public class SerializePicturesUtil {
	
	private byte[] bitmapBuffer;
	
	public byte[] getByteArrayByUri(Uri uri, ContentResolver resolver) {
		try {
			bitmapBuffer = getByteArrayFromStream(resolver.openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmapBuffer;
	}
	
	public byte[] getByteArrayByFile(File file) {
		FileInputStream fis = null;
        try {  
            fis = new FileInputStream(file);  
            getByteArrayFromStream(fis);
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
        return bitmapBuffer;
	}
	
	private byte[] getByteArrayFromStream(InputStream inputStream) throws IOException { 
        byte[] buffer = new byte[1024]; 
        int len = -1; 
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
        while ((len = inputStream.read(buffer)) != -1) { 
        	baos.write(buffer, 0, len); 
        } 
        bitmapBuffer = baos.toByteArray(); 
        baos.close(); 
        inputStream.close(); 
        return bitmapBuffer; 

   } 
	
}

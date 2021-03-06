package so.zudui.space;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

public class SerializePicturesUtil {
	// 序列化方式要修改，太慢了
	private byte[] bitmapBuffer;
	
	public byte[] getByteArrayByUri(Uri uri, ContentResolver resolver) {
		try {
			Bitmap bitmap = BitmapFactory.decodeStream(resolver.openInputStream(uri));
			getByteArrayFromBitMap(bitmap);
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
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            getByteArrayFromBitMap(bitmap);
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
	
	private byte[] getByteArrayFromBitMap(Bitmap bitmap) throws IOException { 
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
        bitmap.compress(Bitmap.CompressFormat.JPEG, 35, baos);
        bitmapBuffer = baos.toByteArray();
        baos.close();
        return bitmapBuffer; 

   } 
	
}

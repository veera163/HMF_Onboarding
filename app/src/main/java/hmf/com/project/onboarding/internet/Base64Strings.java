package hmf.com.project.onboarding.internet;

import android.graphics.Bitmap;
import android.util.Base64;

import hmf.com.project.onboarding.ESurvey;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by saurabh.singh on 4/20/2016.
 */
public class Base64Strings {
    public static String encodeToBase64(int type) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream fis;
        if(type == 0){
            fis = new FileInputStream(new File(ESurvey.DIR_PATH+"/"+ESurvey.FILE_NAME));
        }//
        else{
            fis = new FileInputStream(new File(ESurvey.DIR_PATH+"/"+ESurvey.FILE_NAME_PHOTO));
        }//
        byte[] buf = new byte[1024];
        int n;
        while (-1 != (n = fis.read(buf)))
            baos.write(buf, 0, n);
        fis.close();
         String result = Base64.encodeToString(baos.toByteArray(),
                Base64.DEFAULT);
//         byte[] temp = Base64.decode(result, Base64.DEFAULT);
//        FileOutputStream out =  new FileOutputStream(ESurvey.DIR_PATH+"/"+"temp.mp3");
//        out.write(temp);
//        out.close();temp

        return result;
    }
    public static String encodeToBase64(String path) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream fis;

            fis = new FileInputStream(new File(path));

        byte[] buf = new byte[1024];
        int n;
        while (-1 != (n = fis.read(buf)))
            baos.write(buf, 0, n);
        fis.close();
        String result = Base64.encodeToString(baos.toByteArray(),
                Base64.DEFAULT);
//         byte[] temp = Base64.decode(result, Base64.DEFAULT);
//        FileOutputStream out =  new FileOutputStream(ESurvey.DIR_PATH+"/"+"temp.mp3");
//        out.write(temp);
//        out.close();temp

        return result;
    }

    public static String encodeToBase64(Bitmap bit) throws IOException {
        if(bit == null){
            return "";
        }//
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG ,100 , baos);

        String result = Base64.encodeToString(baos.toByteArray(),
                Base64.DEFAULT);
//         byte[] temp = Base64.decode(result, Base64.DEFAULT);
//        FileOutputStream out =  new FileOutputStream(ESurvey.DIR_PATH+"/"+"temp.mp3");
//        out.write(temp);
//        out.close();temp

        return result;
    }//encodeToBase64
}

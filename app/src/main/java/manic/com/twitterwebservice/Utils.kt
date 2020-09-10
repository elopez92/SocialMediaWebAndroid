package manic.com.twitterwebservice

import android.util.Log
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception

class Utils {

    fun ConvertStreamToString(inputStream: InputStream):String{
        val bufferReader = BufferedReader(InputStreamReader(inputStream));
        var line:String;
        var AllString:String="";

        try{
            do{
                line = bufferReader.readLine();
                if(line != null){
                    AllString += line
                }
            }while (line != null)
            with(inputStream) { close() }
        }catch (e: Exception) {
            Log.e("Convert toString ", e.localizedMessage);
        }

        return AllString;
    }
}
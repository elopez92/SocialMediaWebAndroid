package manic.com.twitterwebservice

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import kotlinx.android.synthetic.main.activity_login.*
import manic.com.twitterwebservice.model.UserViewModel
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun loginEvent(view: View) {
        val email = emailET.text.toString();
        val password = passwordET.text.toString();
        val url = "http://10.0.2.2/TwitterAndroidServer/Login.php?email=$email&password=$password";
        MyAsyncTask().execute(url);
    }

    fun registerEvent(view: View) {
        val intent = Intent(this, RegisterActivity::class.java);
        startActivity(intent);
    }

    // CALL HTTP
    inner class MyAsyncTask: AsyncTask<String, String, String>(){
        override fun doInBackground(vararg p0: String?): String {
            try{
                val url = URL(p0[0])
                val urlConnect = url.openConnection() as HttpURLConnection
                urlConnect.connectTimeout = 7000;

                val utils = Utils();
                var inString = utils.ConvertStreamToString(urlConnect.inputStream);
                publishProgress(inString);
            }catch(ex: Exception){
                Log.e("Do In Background: ", ex.localizedMessage)
            }
            return "";
        }

        override fun onProgressUpdate(vararg values: String?) {
            try{
                var json = JSONObject(values[0]);
                if(json.getString("msg") == "pass login"){
                    val userCredentials = JSONObject(json.getString("info"));

                    Toast.makeText(applicationContext, userCredentials.getString("first_name"), Toast.LENGTH_LONG).show();

                    val user_id = userCredentials.getString("user_id");
                    val saveSettings = SaveSettings(applicationContext);
                    saveSettings.saveSettings(user_id);
                    finish();
                }else{
                    Toast.makeText(applicationContext, json.getString("msg"), Toast.LENGTH_LONG).show();
                }
            }catch (ex: Exception){
                Log.e("Async: " , ex.localizedMessage)
            }
        }
    }
}
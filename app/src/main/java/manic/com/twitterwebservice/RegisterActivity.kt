package manic.com.twitterwebservice

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {

    val READIMAGE:Int = 253;
    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()
        signInAnonymously();

        ivUserImage.setOnClickListener{
            checkPermission()
        }

    }

    fun registerEvent(view: View) {
        registerButton.isEnabled = false
        SaveImageInFirebase();
    }

    fun signInAnonymously(){
        mAuth.signInAnonymously().addOnCompleteListener(this) { task ->
            Log.d("LoginInfo: ", task.toString())
        };
    }

    fun checkPermission(){
        if(Build.VERSION.SDK_INT>=23){
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)!=
                    PackageManager.PERMISSION_GRANTED){

                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), READIMAGE);
                return;
            }
        }
        loadImage();
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            READIMAGE->{
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    loadImage()
                }else{
                    Toast.makeText(applicationContext, "Cannot access your images", Toast.LENGTH_LONG).show();
                }
            }
            else->super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    val PICK_IMAGE_CODE=123
    fun loadImage(){
        var intent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_CODE);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==PICK_IMAGE_CODE && data!=null && resultCode == RESULT_OK){
            val selectedImage = data.data;
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA);
            val cursor = contentResolver.query(selectedImage!!, filePathColumn, null, null, null);
            cursor?.moveToFirst()
            val columnIndex = cursor?.getColumnIndex(filePathColumn[0]);
            val picturePath = cursor?.getString(columnIndex!!);
            cursor?.close();
            ivUserImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    fun SaveImageInFirebase(){

        if(ivUserImage.drawable != null) {

            var currentUser = mAuth!!.currentUser;
            val email: String = currentUser!!.email.toString();
            val storage = FirebaseStorage.getInstance();
            val storageRef = storage.getReferenceFromUrl("gs://twitter-3faca.appspot.com");
            val df = SimpleDateFormat("ddMMyyHHmmss");
            val dataObj = Date();
            val imagePath = SplitString(email) + "." + df.format(dataObj) + ".jpg";
            val ImageRef = storageRef.child("images/" + imagePath);
            ivUserImage.isDrawingCacheEnabled = true;
            ivUserImage.buildDrawingCache()

            val drawable = ivUserImage.drawable as BitmapDrawable
            val bitmap = drawable.bitmap;
            val baos = ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            val data = baos.toByteArray();
            val uploadTask = ImageRef.putBytes(data);
            var DownloadURL = "";
            uploadTask.addOnFailureListener {
                Toast.makeText(applicationContext, "failed to upload", Toast.LENGTH_LONG).show();
            }.addOnSuccessListener { taskSnapshot ->

                DownloadURL = taskSnapshot.storage.downloadUrl!!.toString();

                /*myRef.child("Users").child(currentUser.uid).child("email").setValue(currentUser.email);
            myRef.child("Users").child(currentUser.uid).child("ProfileImage").setValue(DownloadURL);
            loadTweets();*/

                val name = URLEncoder.encode(nameET.text.toString(), "utf-8");
                val email = URLEncoder.encode(emailET.text.toString(), "utf-8");
                val password = URLEncoder.encode(passwordET.text.toString(), "utf-8");
                DownloadURL = URLEncoder.encode(DownloadURL, "utf-8");

                val url =
                    "http://10.0.2.2/TwitterAndroidServer/Register.php?first_name=$name&email=$email&password=$password&picture_path=$DownloadURL";
                MyAsyncTask().execute(url);

            }
        }
    }

    fun SplitString(email:String):String{
        val split = email.split("@");
        return split[0];
    }

    fun loadTweets(){
        var currentUser = mAuth!!.currentUser

        if(currentUser != null){
            var intent = Intent(this, MainActivity::class.java);
            intent.putExtra("email", currentUser.email)
            intent.putExtra("uid", currentUser.uid);

            startActivity(intent);
        }
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
            }catch(ex:Exception){
                Log.e("Do In Background: ", ex.localizedMessage)
            }
            return "";
        }

        override fun onProgressUpdate(vararg values: String?) {
            try{
                var json = JSONObject(values[0]);
                Toast.makeText(applicationContext, json.getString("msg"), Toast.LENGTH_LONG).show();
                Log.i("Async: ", json.getString("msg"));
                if(json.getString("msg") == "user is added"){
                    finish();
                }else{
                    registerButton.isEnabled = true;
                }
            }catch (ex:Exception){
                Log.e("Async: " , ex.localizedMessage)
            }
        }
    }

}
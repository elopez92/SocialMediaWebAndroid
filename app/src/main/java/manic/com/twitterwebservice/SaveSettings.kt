package manic.com.twitterwebservice

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

class SaveSettings {
    var context: Context? = null;
    var sharedPref:SharedPreferences ?= null;

    companion object {
        var userID = "";
    }

    constructor(context: Context){
        this.context = context;
        sharedPref = context.getSharedPreferences("myPref", Context.MODE_PRIVATE);
    }

    fun saveSettings(userID:String){
        val editor = sharedPref!!.edit();
        editor.putString("userID", userID);
        editor.apply();
        loadSettings()
    }

    fun loadSettings(){
        userID = sharedPref!!.getString("userID", "0").toString();

        if(userID == "0"){
            val intent = Intent(context, LoginActivity::class.java);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context!!.startActivity(intent);
        } else{

        }
    }
}
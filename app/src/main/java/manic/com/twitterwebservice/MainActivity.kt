package manic.com.twitterwebservice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import manic.com.twitterwebservice.model.UserViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val userVM: UserViewModel by viewModels();
        //userVM.user.observe(viewLifecyclerOwner){

        //}

        val saveSettings = SaveSettings(this);
        saveSettings.loadSettings();
    }
}
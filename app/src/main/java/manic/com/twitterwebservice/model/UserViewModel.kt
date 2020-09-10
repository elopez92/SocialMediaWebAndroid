package manic.com.twitterwebservice.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class UserViewModel(private val state: SavedStateHandle) : ViewModel() {
    val userId : String = state["id"] ?: throw IllegalArgumentException("missing user id") //TODO()
    //val user : LiveData<User> = TODO();
}
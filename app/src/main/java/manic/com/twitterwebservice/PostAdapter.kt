package manic.com.twitterwebservice

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import manic.com.twitterwebservice.model.User

class PostAdapter(private val myDataset: Array<String>) : RecyclerView.Adapter<PostAdapter.MyViewHolder>() {

    class MyViewHolder(view : View) : RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflatedView = parent.inflate(R.layout.post_item, false);
        return MyViewHolder(inflatedView);
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return myDataset.size;
    }
}
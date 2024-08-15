package com.example.helo.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.helo.Model.Message
import com.example.helo.R

class MessagesAdapter (userId: String) : RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder>(){

    private var messages: List<Message> = ArrayList<Message>()
    private val currentUserId = userId
    fun setMessages (messages : List<Message>){
        this.messages = messages
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
        val layoutResId =
            if(viewType == VIEW_TYPE_MY_MESSAGE) R.layout.my_message_layout
            else R.layout.other_message_item
        val view = LayoutInflater.from(parent.context)
            .inflate(layoutResId, parent, false)
        return MessagesViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        if(message.senderId == this.currentUserId){
            return VIEW_TYPE_MY_MESSAGE
        }else return VIEW_TYPE_OTHER_MESSAGE

    }

    override fun getItemCount(): Int {
        return messages.size
    }



    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) {
        val message = messages[position]
        holder.textViewMessage.text = message.text
    }

    companion object{
        private const val VIEW_TYPE_MY_MESSAGE = 100
        private const val VIEW_TYPE_OTHER_MESSAGE = 101

    }
    class MessagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textViewMessage : TextView = itemView.findViewById(R.id.textViewMessage)
    }
}
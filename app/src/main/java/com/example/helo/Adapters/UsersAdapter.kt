package com.example.helo.Adapters

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.helo.Model.User
import com.example.helo.R

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.UserViewHolder>(){

    private var users: List<User> = ArrayList<User>()
    private var onUserClickListener : OnUserClickListener? = null

    fun setUsers(userList: List<User>){
        users = userList
        notifyDataSetChanged()
    }

    fun setOnUserClickListener(onUserClickListener: OnUserClickListener){
        this.onUserClickListener = onUserClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        val userName = "${user.name} ${user.lastName}"
        holder.textViewUserName.text = userName
        holder.statusView.background =
            if(user.isOnline){
                ContextCompat.getDrawable(holder.itemView.context, R.drawable.circle_green)
            }
            else {
                ContextCompat.getDrawable(holder.itemView.context, R.drawable.circle_red)
            }
        holder.itemView.setOnClickListener {
            onUserClickListener?.onUserClick(user)
        }

    }

    interface OnUserClickListener{
        fun onUserClick(user: User)
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewUserName : TextView = itemView.findViewById(R.id.textViewUserName)
        val statusView : View = itemView.findViewById(R.id.statusView)
    }
}
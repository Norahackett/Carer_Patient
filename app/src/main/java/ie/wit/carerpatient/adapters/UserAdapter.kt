package ie.wit.carerpatient.adapters

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView

import ie.wit.carerpatient.databinding.CardUserBinding


import ie.wit.carerpatient.models.UserModel


interface UserClickListener {
    fun onUserClick( user: UserModel)

}

class UserAdapter constructor(private var users: ArrayList<UserModel>,

                                      private val listener: UserClickListener ,
                                      private val readOnly: Boolean)
    : RecyclerView.Adapter<UserAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardUserBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding,readOnly)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val user =users[holder.adapterPosition]
        holder.bind(user,listener)
    }

    fun removeAt(position: Int) {
        users.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = users.size

    inner class MainHolder(val binding : CardUserBinding, private val readOnly : Boolean) :
        RecyclerView.ViewHolder(binding.root) {

        val readOnlyRow = readOnly

        fun bind(user: UserModel, listener: UserClickListener) {
            binding.root.tag = user
            binding.user = user


            binding.root.setOnClickListener { listener.onUserClick(user) }
            binding.executePendingBindings()
        }
    }
}
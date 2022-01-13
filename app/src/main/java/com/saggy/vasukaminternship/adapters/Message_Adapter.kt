package com.saggy.vasukaminternship.adapters

import androidx.recyclerview.widget.RecyclerView
import com.saggy.vasukaminternship.adapters.Message_Adapter.MessageViewHolder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import android.view.ViewGroup
import android.view.LayoutInflater
import com.saggy.vasukaminternship.R
import android.content.Intent
import android.net.Uri
import android.widget.TextView
import androidx.cardview.widget.CardView
import android.view.View
import android.widget.ImageView
import com.saggy.vasukaminternship.models.Messages

class Message_Adapter(private val userMessagesList: List<Messages>) :
    RecyclerView.Adapter<MessageViewHolder>() {
    private var mAuth: FirebaseAuth? = null
    private val usersRef: DatabaseReference? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_list_messenges_item, parent, false)
        mAuth = FirebaseAuth.getInstance()
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val messageSenderId = mAuth!!.currentUser!!.uid
        val messages = userMessagesList[position]
        val fromUserID = messages.from
        val fromMessageType = messages.type
        holder.senderView.visibility = View.GONE
        holder.receiverView.visibility = View.GONE
        holder.messageSenderPicture.visibility = View.GONE
        holder.messageReceiverPicture.visibility = View.GONE
        if (fromMessageType == "text") {
            if (fromUserID == messageSenderId) {
                //user send to his known
                holder.senderView.visibility = View.VISIBLE
                //holder.senderMessageText.setText(messages.getMessage() + "\n \n" + messages.getTime() + " - " + messages.getDate());
                holder.senderMessageText.text = messages.message
            } else {
                holder.receiverView.visibility = View.VISIBLE
                //holder.receiverMessageText.setText(messages.getMessage() + "\n \n" + messages.getTime() + " - " + messages.getDate());
                holder.receiverMessageText.text = messages.message
            }
        } else if (fromMessageType == "image") {
            if (fromUserID == messageSenderId) {
                holder.messageSenderPicture.visibility = View.VISIBLE
                //Picasso.get().load(messages.getMessage()).placeholder(R.drawable.profile).into(holder.messageSenderPicture);
            } else {
                holder.messageReceiverPicture.visibility = View.VISIBLE
                //Picasso.get().load(messages.getMessage()).placeholder(R.drawable.profile).into(holder.messageReceiverPicture);
            }
        } else {
            if (fromUserID == messageSenderId) {
                holder.messageSenderPicture.visibility = View.VISIBLE
                holder.messageSenderPicture.setBackgroundResource(R.drawable.ic_baseline_insert_drive_file_24)
                holder.itemView.setOnClickListener { view: View? ->
                    val intent = Intent(
                        Intent.ACTION_VIEW, Uri.parse(
                            userMessagesList[position].message
                        )
                    )
                    holder.itemView.context.startActivity(intent)
                }
            } else {
                holder.messageReceiverPicture.visibility = View.VISIBLE
                holder.messageReceiverPicture.setBackgroundResource(R.drawable.ic_baseline_insert_drive_file_24)
                holder.itemView.setOnClickListener { view: View? ->
                    val intent = Intent(
                        Intent.ACTION_VIEW, Uri.parse(
                            userMessagesList[position].message
                        )
                    )
                    holder.itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return userMessagesList.size
    }

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var senderMessageText: TextView
        var receiverMessageText: TextView
        var messageSenderPicture: ImageView
        var messageReceiverPicture: ImageView
        var senderView: CardView
        var receiverView: CardView

        init {
            senderMessageText = itemView.findViewById(R.id.sender_message_text)
            receiverMessageText = itemView.findViewById(R.id.receiver_message_text)
            messageReceiverPicture = itemView.findViewById(R.id.message_receiver_image_view)
            messageSenderPicture = itemView.findViewById(R.id.message_sender_image_view)
            senderView = itemView.findViewById(R.id.sender_card)
            receiverView = itemView.findViewById(R.id.receiver_card)
        }
    }
}
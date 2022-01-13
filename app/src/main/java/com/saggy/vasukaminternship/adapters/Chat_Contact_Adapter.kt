package com.saggy.vasukaminternship.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.saggy.vasukaminternship.R
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.saggy.vasukaminternship.data.model.user.ProfileInfo
import com.saggy.vasukaminternship.RecyclerTouchListener.ClickListener
import android.view.View
import de.hdodenhof.circleimageview.CircleImageView

class Chat_Contact_Adapter(
    private val context: Context,
    private val profileList: List<ProfileInfo>,
    var onhi: ClickListener
) : RecyclerView.Adapter<Chat_Contact_Adapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_chat_messeges, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val profileInfo = profileList[position]
        holder.username.text = profileInfo.username
        holder.status.text = profileInfo.emailId
        /*

    Picasso.get().load(po.getImage()).placeholder(R.drawable.profile).into(holder.circleImageView);
       holder.status.setText(po.getStatus());
         */
    }

    override fun getItemCount(): Int {
        return profileList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var username: TextView
        var status: TextView
        var userImage: CircleImageView
        var cardView: CardView

        init {
            username = itemView.findViewById(R.id.person_name)
            status = itemView.findViewById(R.id.status)
            userImage = itemView.findViewById(R.id.profile_picture)
            cardView = itemView.findViewById(R.id.cardView)
            itemView.setOnClickListener { v: View? -> onhi.onClick(v, adapterPosition) }
        } //        @Override
        //        public void onClick(View v) {
        //            int position = this.getAdapterPosition();
        //            Intent intent = new Intent(context, Chat_Activity.class);
        //            intent.putExtra("username", profileList.get(position).getUsername());
        //            context.startActivity(intent);
        //        }
    }
}
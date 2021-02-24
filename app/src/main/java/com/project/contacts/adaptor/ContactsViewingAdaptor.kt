package com.project.contacts.adaptor

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.project.contacts.R
import com.project.contacts.activity.ContactDetailsViewActivity
import com.project.contacts.activity.ContactsMainActivity
import com.project.contacts.model.ContactsModel
import kotlinx.android.synthetic.main.contact_item.view.*
import java.io.Serializable
import java.util.ArrayList

class ContactsViewingAdaptor(private val context: ContactsMainActivity, private val contactsList: ArrayList<ContactsModel>):
    RecyclerView.Adapter<ContactsViewingAdaptor.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
       return ViewHolder(
           LayoutInflater.from(context).inflate(
               R.layout.contact_item,
               parent,
               false
           )
       )
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(contactsList[position].contactPhoto!=""){
            holder.contactImageText?.visibility=View.GONE
            Glide.with(context).load(contactsList[position].contactPhoto).apply(RequestOptions().circleCrop()).into(holder.contactImage)
            holder.contactImage.visibility=View.VISIBLE
        }else if(contactsList[position].contactName!=""){
            holder.contactImage.visibility=View.GONE
            holder.contactImageText.text= contactsList[position].contactName.first().toString()
            holder.contactImageText?.visibility = View.VISIBLE
        }

        holder.contactName.text = contactsList[position].contactName
       // Log.e("onBindViewHolder","${contactsList[position].contactName.subSequence(0,0)}")
        holder.itemView.setOnClickListener{
            Toast.makeText(context,"${contactsList[position].contactName}, ${contactsList[position].contactName.first()}",Toast.LENGTH_SHORT).show()
           // val lContactsMainActivity  = ContactsMainActivity()
            val intent = Intent(context,
                ContactDetailsViewActivity::class.java)
            intent.putExtra("DETAILS",contactsList[position] as Serializable)
           // lContactsMainActivity.onClickedOnContact(intent)
            context.startActivity(intent)
        }
    }

//    override fun getItemViewType(position: Int): Int {
//        return super.getItemViewType(position)
//    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contactName = view.contact_name
        val contactImageText = view.contact_ImageText
        val contactImage = view.contact_Image
    }
}
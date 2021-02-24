package com.project.contacts.activity

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.project.contacts.R
import com.project.contacts.database.DatabaseHandler
import com.project.contacts.model.ContactsModel
import com.project.contacts.presentor.ContactsDetailsViewPresenter
import com.project.contacts.presentor.ContactsPresenter
import kotlinx.android.synthetic.main.contact_details_view_activity.*
import java.io.Serializable

class ContactDetailsViewActivity : AppCompatActivity() {
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager
    val mContactsDestailsPresenter =
        ContactsDetailsViewPresenter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contact_details_view_activity)
        val lContact  = intent.getSerializableExtra("DETAILS") as ContactsModel
        mContactsDestailsPresenter.validateContact(lContact)
        delete_layout.setOnClickListener {
                showAlert(lContact)
        }
    }
    fun viewDefaultImage(){
        Glide.with(this).load(R.drawable.camera)
            .apply(RequestOptions().override(400, 400).circleCrop()).into(imageView)
    }
    fun viewContactImage(pImage:String){
        Glide.with(this).load(pImage)
            .apply(RequestOptions().override(400, 400).circleCrop()).into(imageView)
    }
    fun viewContactName(pName:String){
        contact_title?.text=pName
    }
    fun viewContactNumberHome(pNumberHome:String){
        number_home_detail_card_view.visibility= View.VISIBLE
        number_home_detail.text =pNumberHome
    }
    fun viewContactNumberMobile(pNumberMobile:String){
        number_mobile_details_layout.visibility=View.VISIBLE
        number_mobile_detail.text=pNumberMobile
    }
    fun viewContactNumberWork(pNumberWork:String){
        number_work_details_layout.visibility=View.VISIBLE
        number_work_detail.text=pNumberWork
    }
    fun viewContactNumberOther(pNumberOther:String){
        number_other_details_layout.visibility=View.VISIBLE
        number_other_detail.text=pNumberOther
    }
    fun viewContactEmailHome(pEmailHome:String){
        email_home_details_layout.visibility=View.VISIBLE
        email_home_detail.text=pEmailHome
    }
    fun viewContactEmailWork(pEmailWork:String){
        email_work_details_layout.visibility=View.VISIBLE
        email_work_details.text=pEmailWork
    }
    fun viewContactEmailOther(pEmailOther:String){
        email_other_details_layout.visibility=View.VISIBLE
        email_other_detail.text=pEmailOther
    }
    fun viewContactOtherDetails(pOtherDetails:String){
        contact_other_details_layout.visibility=View.VISIBLE
        contact_other_detail.text=pOtherDetails
    }
    private fun showAlert(pContact:ContactsModel){
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("DELETE Contact")
        //set message for alert dialog
        builder.setMessage("Do you want to delete contact..?")

        //performing positive action
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            val intent = Intent(this,
                ContactsMainActivity::class.java)
            intent.putExtra("CONTACT",mContactsDestailsPresenter.proccessDeleteRecord(pContact) as Serializable)
            startActivity(intent)
        }

        //performing negative action
        builder.setNegativeButton("No"){dialogInterface, which ->
            finish()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}
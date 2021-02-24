package com.project.contacts.activity

import android.Manifest
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.facebook.stetho.Stetho
import com.project.contacts.presentor.ContactsPresenter
import com.project.contacts.adaptor.ContactsViewingAdaptor
import com.project.contacts.R
import com.project.contacts.model.ContactsModel
import kotlinx.android.synthetic.main.contacts_main_activity.*
import java.lang.Exception
import kotlin.collections.ArrayList

class ContactsMainActivity : AppCompatActivity() {
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager
    var contactsList: ArrayList<ContactsModel> = ArrayList()
    val PERMISSIONS_REQUEST_READ_CONTACTS = 1
    lateinit var progressBar: ProgressBar
    val mContactsPresenter =
        ContactsPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contacts_main_activity)
        Stetho.initializeWithDefaults(this)
        loadContacts()
        try {

            val bundle = getIntent().getExtras();
            if (bundle != null) {
                val lContact:ContactsModel= bundle.get("CONTACT") as ContactsModel
                refreshContactsList(lContact)
            }
        } catch (e:Exception) {
                e.printStackTrace();
        }

        search_item.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(search_item.text.toString()!=""){
                    searchContacts(search_item.text.toString())
                }else{
                    setContactsInView()
                }
            }

        })
    }
    private fun setContactsInView(){
        linearLayoutManager = LinearLayoutManager(applicationContext)
        recyclerviewcontacts.layoutManager=linearLayoutManager
        recyclerviewcontacts.adapter= ContactsViewingAdaptor(this, contactsList)
    }

    private fun searchContacts(pSearchItem :String){
        if(contactsList.isEmpty()){
            Toast.makeText(this,"No contacts to search",Toast.LENGTH_SHORT).show()
        }else{
            var searchList: ArrayList<ContactsModel> = ArrayList()
            for(item in contactsList){
                if(item.contactName.contains(pSearchItem) ||
                    item.contactNumberModel.contactNumberHome.contains(pSearchItem)||
                    item.contactNumberModel.contactNumberMobile.contains(pSearchItem)||
                    item.contactNumberModel.contactNumberWork.contains(pSearchItem)||
                    item.contactNumberModel.contactNumberOther.contains(pSearchItem)){
                    searchList.add(item)
                }
            }
            if(searchList.isNotEmpty()){
                linearLayoutManager = LinearLayoutManager(this)
                recyclerviewcontacts.layoutManager=linearLayoutManager
                recyclerviewcontacts.adapter= ContactsViewingAdaptor(this, searchList)
            }else{
                Toast.makeText(this,"The contact you searched is not found in list",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadContacts() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS)
            //callback onRequestPermissionsResult m
        } else {
            progress_bar_card_view.visibility=View.GONE
            search_item.visibility=View.VISIBLE
            no_contacts_text_view.visibility=View.GONE
            contactsList=mContactsPresenter.getContacts(contentResolver)
            contacts_count.text="${contactsList.size} contacts"
            setContactsInView()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
      //  super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        Toast.makeText(this,"onRequestPermissionCalled",
//            Toast.LENGTH_LONG).show()
        shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                progressBar = findViewById(R.id.progressBar)
                progressBar.max = contactsList.size
                progressBar.progress = 1
                Handler(Looper.getMainLooper()).postDelayed({
                    progress_bar_card_view.visibility=View.GONE
                    // progressBar.visibility = View.GONE
                },3000)
                search_item.visibility=View.VISIBLE
                no_contacts_text_view.visibility=View.GONE
                loadContacts()
            } else if(shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(this,"grant results = ${grantResults[0]} Permission must be granted in order to display contacts information",
                    Toast.LENGTH_LONG).show()
                //shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)
                val builder = AlertDialog.Builder(this)
                //set title for alert dialog
                builder.setTitle("Contact Permission Needed")
                //set message for alert dialog
                builder.setMessage("This App need to access your contacts.. Please allow")

                //performing positive action
                builder.setPositiveButton("CONTINUE"){dialogInterface, which ->
                   // Toast.makeText(applicationContext,"clicked yes",Toast.LENGTH_LONG).show
                    requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS),
                        PERMISSIONS_REQUEST_READ_CONTACTS)
                }

                //performing negative action
                builder.setNegativeButton("CLOSE APP"){dialogInterface, which ->
                    //Toast.makeText(applicationContext,"clicked No",Toast.LENGTH_LONG).show()
                    finish()
                }
                // Create the AlertDialog
                val alertDialog: AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCancelable(false)
                alertDialog.show()
            }else{
                val builder = AlertDialog.Builder(this)

                //set message for alert dialog
                builder.setMessage("To read contacts, allow Contacts to access your contacts. Tap Settings > Permissions, and turn on contact permission")

                //performing positive action
                builder.setPositiveButton("SETTINGS"){dialogInterface, which ->
                     Toast.makeText(applicationContext,"clicked yes",Toast.LENGTH_LONG).show()
//                    requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS),
//                        PERMISSIONS_REQUEST_READ_CONTACTS)
                    startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+packageName)))
                }

                //performing negative action
                builder.setNegativeButton("NOT NOW"){dialogInterface, which ->
                    //Toast.makeText(applicationContext,"clicked No",Toast.LENGTH_LONG).show()
                    finishActivity(-1)
                }
                // Create the AlertDialog
                val alertDialog: AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
    }
    fun refreshContactsList(pContact:ContactsModel){
        contactsList.remove(pContact)
        setContactsInView()
    }
}
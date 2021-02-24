package com.project.contacts.presentor

import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.project.contacts.R
import com.project.contacts.activity.ContactDetailsViewActivity
import com.project.contacts.appcontract.ContactsDetailsViewPresentorContract
import com.project.contacts.database.DatabaseHandler
import com.project.contacts.model.ContactsModel
import kotlinx.android.synthetic.main.contact_details_view_activity.*

class ContactsDetailsViewPresenter (pView : ContactDetailsViewActivity):
    ContactsDetailsViewPresentorContract.ContactsDetailsContractPresenter {
    val mView =pView
    override fun validateContact(pContact: ContactsModel) {
        if(pContact.contactPhoto =="") {
            mView.viewDefaultImage()
        }else{
            mView.viewContactImage(pContact.contactPhoto)
        }

        if (pContact.contactName !="") {
            mView.viewContactName(pContact.contactName)
        }
        if (pContact.contactNumberModel.contactNumberHome !="") {
            mView.viewContactNumberHome(pContact.contactNumberModel.contactNumberHome)
        }
        if (pContact.contactNumberModel.contactNumberMobile !="") {
            mView.viewContactNumberMobile(pContact.contactNumberModel.contactNumberMobile)
        }
        if (pContact.contactNumberModel.contactNumberWork !="") {
            mView.viewContactNumberWork(pContact.contactNumberModel.contactNumberWork)
        }
        if (pContact.contactNumberModel.contactNumberOther !="") {
            mView.viewContactNumberOther(pContact.contactNumberModel.contactNumberOther)
        }
        if (pContact.contactEmailModel.contactEmailHome !="") {
            mView.viewContactEmailHome(pContact.contactEmailModel.contactEmailHome)
        }
        if (pContact.contactEmailModel.contactEmailWork !="") {
            mView.viewContactEmailWork(pContact.contactEmailModel.contactEmailWork)
        }
        if (pContact.contactEmailModel.contactEmailOther !="") {
            mView.viewContactEmailOther(pContact.contactEmailModel.contactEmailOther)
        }
        if (pContact.contactOtherDetails !="") {
            mView.viewContactOtherDetails(pContact.contactOtherDetails)
        }
    }

    override fun proccessDeleteRecord(pContact:ContactsModel):ContactsModel{
        val databaseHandler: DatabaseHandler = DatabaseHandler(mView)
        if(pContact.contactId.trim()!=""){
            //calling the deleteEmployee method of DatabaseHandler class to delete record
            val status = databaseHandler.deleteContact(pContact)
            if(status){
               return pContact
            }
        }else{
            // mView.showToastMessage("id  cannot be blank")
        }
        return ContactsModel()
    }
}
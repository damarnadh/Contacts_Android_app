package com.project.contacts.appcontract

import android.content.ContentResolver
import com.project.contacts.model.ContactsModel

interface ContactsDetailsViewPresentorContract {
    interface ContactsDetailsContractView{
        fun viewDefaultImage()
        fun viewContactImage(pImage:String)
        fun viewContactName(pName:String)
        fun viewContactNumberHome(pNumberHome:String)
        fun viewContactNumberMobile(pNumberMobile:String)
        fun viewContactNumberWork(pNumberWork:String)
        fun viewContactNumberOther(pNumberOther:String)
        fun viewContactEmailHome(pEmailHome:String)
        fun viewContactEmailWork(pEmailWork:String)
        fun viewContactEmailOther(pEmailOther:String)
        fun viewContactOtherDetails(pOtherDetails:String)
    }
    interface ContactsDetailsContractPresenter{
       fun validateContact(pContact:ContactsModel)
       fun proccessDeleteRecord(pContact:ContactsModel):ContactsModel
    }
}
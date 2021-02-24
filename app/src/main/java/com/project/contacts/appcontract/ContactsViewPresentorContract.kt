package com.project.contacts.appcontract

import android.content.ContentResolver
import com.project.contacts.model.ContactsModel

interface ContactsViewPresentorContract {
    interface ContactsContractView{

    }
    interface ContactsContractPresenter{
        fun getContacts(contentResolver: ContentResolver):List<ContactsModel>
    }
}
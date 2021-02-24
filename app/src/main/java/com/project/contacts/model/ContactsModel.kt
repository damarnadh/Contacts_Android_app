package com.project.contacts.model

import java.io.Serializable

data class ContactsModel(var contactId :String,
                         var contactName :String,
                         var contactNumberModel: ContactNumberModel,
                         var contactEmailModel: ContactEmailModel,
                         var contactPhoto:String,
                         var contactOtherDetails:String):Serializable{
    constructor():this("","", ContactNumberModel(),
        ContactEmailModel(),"","")
}
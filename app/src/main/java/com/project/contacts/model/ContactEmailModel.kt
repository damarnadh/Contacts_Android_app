package com.project.contacts.model

import java.io.Serializable


data class ContactEmailModel(
    var contactEmailWork:String,
    var contactEmailHome:String,
    var contactEmailOther:String):Serializable{
    constructor():this("","","")
}
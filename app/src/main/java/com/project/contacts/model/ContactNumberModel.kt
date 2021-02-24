package com.project.contacts.model

import java.io.Serializable

data class ContactNumberModel(
    var contactNumberWork:String,
    var contactNumberHome:String,
    var contactNumberMobile:String,
    var contactNumberOther:String):Serializable{
    constructor():this("","","","")
}
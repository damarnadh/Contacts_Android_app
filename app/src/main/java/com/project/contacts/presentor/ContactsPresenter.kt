package com.project.contacts.presentor


import android.content.ContentResolver
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import com.project.contacts.activity.ContactsMainActivity
import com.project.contacts.appcontract.ContactsViewPresentorContract
import com.project.contacts.database.DatabaseHandler
import com.project.contacts.model.ContactNumberModel
import com.project.contacts.model.ContactsModel

class ContactsPresenter(pView : ContactsMainActivity):
    ContactsViewPresentorContract.ContactsContractPresenter {
    private val mView =pView

    override fun getContacts(pContentResolver:ContentResolver):ArrayList<ContactsModel> {
        var lContactsList: ArrayList<ContactsModel> = ArrayList()

        val databaseHandler: DatabaseHandler =
            DatabaseHandler(mView)
       // Toast.makeText(mView,"db exists.. retrieving",Toast.LENGTH_SHORT).show()
        Log.d("ContactsPresenter","db exists.. retrieving")
        lContactsList= databaseHandler.viewEmployee()
        if(lContactsList.isEmpty()){
            //Toast.makeText(mView,"db does not exists.. getting from content provider..",Toast.LENGTH_SHORT).show()
            Log.d("ContactsPresenter","db does not exists.. getting from content provider..")
            lContactsList =getContactsFromProvider(pContentResolver)
        }
        lContactsList.sortWith(Comparator {
                o1: ContactsModel, o2: ContactsModel ->
            o1.contactName.compareTo(o2.contactName,ignoreCase = true)
        })
        return lContactsList
    }
    private fun getContactsFromProvider(pContentResolver:ContentResolver):ArrayList<ContactsModel>{
        val lContactsList: ArrayList<ContactsModel> = ArrayList()

        val cursor = pContentResolver.query(
            ContactsContract.Contacts.CONTENT_URI, null, null, null,
            null)

        if (cursor!!.count > 0) {
            while (cursor.moveToNext()) {
                val lContactsModel = ContactsModel()
                val id = getContactId(cursor)
                val phoneNumberCount = getPhoneNumberCount(cursor)
                lContactsModel.contactId=id
                lContactsModel.contactName = getContactName(cursor)
                if (phoneNumberCount > 0) {
                    lContactsModel.contactNumberModel=getPhoneNumbers(pContentResolver,id)
                }
                getContactsEmail(pContentResolver,lContactsModel,id)
                lContactsModel.contactPhoto=getContactPhoto(cursor,pContentResolver,id)
                getContactOtherDetails(pContentResolver,lContactsModel,id)
                Log.e("ContactsPresentor","${lContactsModel}")
                if(lContactsModel.contactName!=""){
                    lContactsList.add(lContactsModel)
                }
            }
        } else {
            //   toast("No contacts available!")
        }
        cursor?.close()
        sendToContactsDatabase(lContactsList)
        return lContactsList
    }
    private fun getContactId(pCursor: Cursor):String{
           return pCursor.getString(pCursor.getColumnIndex(ContactsContract.Contacts._ID))
    }
    private fun getContactName(pCursor: Cursor):String{
        if( pCursor.getString(pCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))==null){
            return ""
        }
        return pCursor.getString(pCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
    }
    private fun getPhoneNumberCount(pCursor: Cursor):Int{
        return (pCursor.getString(
            pCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))).toInt()
    }
    private fun getPhoneNumbers(pContentResolver: ContentResolver,pId:String):ContactNumberModel{
        var cursorPhone =pContentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", arrayOf(pId), null)
        val lContactNumberModel =ContactNumberModel()
        if(cursorPhone!!.count > 0 ) {
            while (cursorPhone.moveToNext()) {
                when(cursorPhone.getInt(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))){
                    ContactsContract.CommonDataKinds.Phone.TYPE_HOME->{
                        lContactNumberModel.contactNumberHome=cursorPhone.getString(cursorPhone.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                        ))
                    }
                    ContactsContract.CommonDataKinds.Phone.TYPE_OTHER->{
                        lContactNumberModel.contactNumberOther=cursorPhone.getString(cursorPhone.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                        ))
                    }
                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE->{
                        lContactNumberModel.contactNumberMobile=cursorPhone.getString(cursorPhone.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                        ))
                    }
                    ContactsContract.CommonDataKinds.Phone.TYPE_WORK->{
                        lContactNumberModel.contactNumberWork=cursorPhone.getString(cursorPhone.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                        ))
                    }
                }
            }
        }
        cursorPhone.close()
        return lContactNumberModel
    }

    private fun getContactsEmail(pContentResolver: ContentResolver,pContactsModel: ContactsModel,pId:String){
        val cursorEmail = pContentResolver.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", arrayOf(pId), null)
        if(cursorEmail!!.count>0){
            while (cursorEmail.moveToNext()){
                when(cursorEmail.getInt(cursorEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE))){
                    ContactsContract.CommonDataKinds.Email.TYPE_HOME->{
                        pContactsModel.contactEmailModel.contactEmailHome=cursorEmail.getString(cursorEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS))
                    }
                    ContactsContract.CommonDataKinds.Email.TYPE_OTHER->{
                        pContactsModel.contactEmailModel.contactEmailOther=cursorEmail.getString(cursorEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS))
                    }
                    ContactsContract.CommonDataKinds.Email.TYPE_WORK->{
                        pContactsModel.contactEmailModel.contactEmailWork=cursorEmail.getString(cursorEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS))
                    }
                }
            }
        }
        cursorEmail.close()
    }

    private fun getContactPhoto(pCursor: Cursor,pContentResolver: ContentResolver,pId:String):String{
        val photoUri = pCursor.getString(pCursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI))
        if(photoUri!=null){
            return photoUri
        }
        return ""
    }
    private fun getContactOtherDetails(pContentResolver: ContentResolver,pContactsModel: ContactsModel,pId:String){
        val cursorOtherDetails = pContentResolver.query(
            ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", arrayOf(pId), null)
        if(cursorOtherDetails!!.count>0){
            while (cursorOtherDetails.moveToNext()){
               when(cursorOtherDetails.getInt(cursorOtherDetails.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE))){
                    ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME->{
                        pContactsModel.contactOtherDetails=cursorOtherDetails.getString(cursorOtherDetails.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS))
                    }
                    ContactsContract.CommonDataKinds.StructuredPostal.TYPE_OTHER->{
                        pContactsModel.contactOtherDetails=cursorOtherDetails.getString(cursorOtherDetails.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS))
                    }
                    ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK->{
                        pContactsModel.contactOtherDetails=cursorOtherDetails.getString(cursorOtherDetails.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS))
                    }
               }
            }
        }
        cursorOtherDetails.close()
    }
    private fun sendToContactsDatabase(pContactList:ArrayList<ContactsModel>){
        var flag =false
        for(lContact in pContactList){
            val databaseHandler: DatabaseHandler =
                DatabaseHandler(mView)
            if(databaseHandler.addContacts(lContact)){
                flag=true
            }
            if(!flag){
                Toast.makeText(mView,"record not inserted",Toast.LENGTH_SHORT).show()
            }
        }
    }
}
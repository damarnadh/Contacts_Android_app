package com.project.contacts.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.project.contacts.model.ContactsModel

class DatabaseHandler(context: Context): SQLiteOpenHelper(context,
    DATABASE_NAME,null,
    DATABASE_VERSION
) {
    companion object {
        private var DATABASE_VERSION = 1
        private val DATABASE_NAME = "ContactsDatabase"
        private val TABLE_CONTACTS = "ContactsTable"
        private val KEY_ID = "id"
        private val KEY_NAME = "name"
        private val KEY_PHONE_NUMBER_HOME ="phone_number_home"
        private val KEY_PHONE_NUMBER_WORK ="phone_number_work"
        private val KEY_PHONE_NUMBER_MOBILE ="phone_number_mobile"
        private val KEY_PHONE_NUMBER_OTHER ="phone_number_other"
        private val KEY_EMAIL_HOME = "email_home"
        private val KEY_EMAIL_WORK = "email_work"
        private val KEY_EMAIL_OTHER = "email_other"
        private val KEY_PHOTO       = "photo"
        private val KEY_OTHER_DETAILS = "other_details"
    }
    var idEx=1
    override fun onCreate(db: SQLiteDatabase?) {
        Log.e("DatabaseHandler","onCreate")
        //creating table with fields
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_PHONE_NUMBER_HOME + " TEXT,"
                + KEY_PHONE_NUMBER_WORK + " TEXT,"
                + KEY_PHONE_NUMBER_MOBILE + " TEXT,"
                + KEY_PHONE_NUMBER_OTHER + " TEXT,"
                + KEY_EMAIL_HOME + " TEXT,"
                + KEY_EMAIL_WORK + " TEXT,"
                + KEY_EMAIL_OTHER + " TEXT,"
                + KEY_PHOTO + " TEXT,"
                + KEY_OTHER_DETAILS + " TEXT"+")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    //method to insert data
    fun addContacts(contactsModel : ContactsModel):Boolean{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, contactsModel.contactId.toInt())
        contentValues.put(KEY_NAME, contactsModel.contactName)
        contentValues.put(KEY_PHONE_NUMBER_HOME,contactsModel.contactNumberModel.contactNumberHome )
        contentValues.put(KEY_PHONE_NUMBER_MOBILE,contactsModel.contactNumberModel.contactNumberMobile )
        contentValues.put(KEY_PHONE_NUMBER_OTHER,contactsModel.contactNumberModel.contactNumberOther )
        contentValues.put(KEY_PHONE_NUMBER_WORK,contactsModel.contactNumberModel.contactNumberWork )
        contentValues.put(KEY_EMAIL_HOME,contactsModel.contactEmailModel.contactEmailHome )
        contentValues.put(KEY_EMAIL_WORK,contactsModel.contactEmailModel.contactEmailWork )
        contentValues.put(KEY_EMAIL_OTHER,contactsModel.contactEmailModel.contactEmailOther )
        contentValues.put(KEY_PHOTO,contactsModel.contactPhoto )
        contentValues.put(KEY_OTHER_DETAILS,contactsModel.contactOtherDetails)

        Log.e("DatabaseHandler","count ")
        val success = db.insert(TABLE_CONTACTS, null, contentValues)

        db.close() // Closing database connection
        return (success > -1)
    }

    //method to read data
    fun viewEmployee():ArrayList<ContactsModel>{
        val contactsList:ArrayList<ContactsModel> = ArrayList<ContactsModel>()
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        if (cursor.moveToFirst()) {
            do {
                val lContactsModel = ContactsModel()
                lContactsModel.contactId = cursor.getString(cursor.getColumnIndex(KEY_ID))
                lContactsModel.contactName = cursor.getString(cursor.getColumnIndex(
                    KEY_NAME
                ))
                lContactsModel.contactNumberModel.contactNumberHome = cursor.getString(cursor.getColumnIndex(
                    KEY_PHONE_NUMBER_HOME
                ))
                lContactsModel.contactNumberModel.contactNumberWork = cursor.getString(cursor.getColumnIndex(
                    KEY_PHONE_NUMBER_WORK
                ))
                lContactsModel.contactNumberModel.contactNumberOther = cursor.getString(cursor.getColumnIndex(
                    KEY_PHONE_NUMBER_OTHER
                ))
                lContactsModel.contactNumberModel.contactNumberMobile = cursor.getString(cursor.getColumnIndex(
                    KEY_PHONE_NUMBER_MOBILE
                ))
                lContactsModel.contactEmailModel.contactEmailHome = cursor.getString(cursor.getColumnIndex(
                    KEY_EMAIL_HOME
                ))
                lContactsModel.contactEmailModel.contactEmailWork = cursor.getString(cursor.getColumnIndex(
                    KEY_EMAIL_WORK
                ))
                lContactsModel.contactPhoto = cursor.getString(cursor.getColumnIndex(
                    KEY_PHOTO
                ))
                lContactsModel.contactOtherDetails = cursor.getString(cursor.getColumnIndex(
                    KEY_OTHER_DETAILS
                ))
                contactsList.add(lContactsModel)
            } while (cursor.moveToNext())
        }
        return contactsList
    }
    //method to delete data
    fun deleteContact(pContact: ContactsModel):Boolean{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, pContact.contactId) // EmpModelClass UserId
        // Deleting Row
        val success = db.delete(TABLE_CONTACTS,"id="+pContact.contactId,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success > -1
    }
}
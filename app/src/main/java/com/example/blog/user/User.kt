package com.example.blog.user

import android.net.Uri
import android.os.Parcel
import com.google.android.gms.internal.firebase_auth.zzcz
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseUserMetadata
import com.google.firebase.auth.UserInfo

class User() : FirebaseUser(){
    private var instance: User? = null

    companion object CREATOR{

    }
    var name: String = ""
    var password: String = ""

    @Synchronized
    private fun createInstance(){
        instance = User()
    }

    fun getInstance(): User{
        if (instance == null) createInstance()
        return instance!!
    }

    override fun getEmail(): String? {
        TODO("Not yet implemented")
    }

    override fun zza(p0: MutableList<out UserInfo>): FirebaseUser {
        TODO("Not yet implemented")
    }

    override fun zza(p0: zzcz) {
        TODO("Not yet implemented")
    }

    override fun zzch(): String {
        TODO("Not yet implemented")
    }

    override fun getProviderData(): MutableList<out UserInfo> {
        TODO("Not yet implemented")
    }

    override fun zzcf(): String? {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        TODO("Not yet implemented")
    }

    override fun zzcc(): FirebaseApp {
        TODO("Not yet implemented")
    }

    override fun getMetadata(): FirebaseUserMetadata? {
        TODO("Not yet implemented")
    }

    override fun isAnonymous(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getPhoneNumber(): String? {
        TODO("Not yet implemented")
    }

    override fun getUid(): String {
        TODO("Not yet implemented")
    }

    override fun isEmailVerified(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getDisplayName(): String? {
        TODO("Not yet implemented")
    }

    override fun zzci(): String {
        TODO("Not yet implemented")
    }

    override fun zzcg(): zzcz {
        TODO("Not yet implemented")
    }

    override fun zzce(): FirebaseUser {
        TODO("Not yet implemented")
    }

    override fun getPhotoUrl(): Uri? {
        TODO("Not yet implemented")
    }

    override fun getProviders(): MutableList<String> {
        TODO("Not yet implemented")
    }

    override fun getProviderId(): String {
        TODO("Not yet implemented")
    }


}
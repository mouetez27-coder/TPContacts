package com.example.tpcontacts.model

import java.io.Serializable

data class Contact(
    var nom: String,
    var prenom: String,
    var telephone: String,
    var email: String,
    var photoResId: Int
) : Serializable

package com.example.tpcontacts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import com.example.tpcontacts.model.Contact

class ContactAdapter(
    private val context: Context,
    private val contacts: List<Contact>
) : BaseAdapter() {

    override fun getCount() = contacts.size

    override fun getItem(position: Int) = contacts[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View {

        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_contact, parent, false)

        val contact = contacts[position]

        view.findViewById<TextView>(R.id.txtNom).text = contact.nom
        view.findViewById<TextView>(R.id.txtPrenom).text = contact.prenom
        view.findViewById<TextView>(R.id.txtTel).text = contact.telephone

        val imgPhoto = view.findViewById<ImageView>(R.id.imgPhoto)
        imgPhoto.setImageDrawable(
            AppCompatResources.getDrawable(context, contact.photoResId)
        )

        return view
    }
}

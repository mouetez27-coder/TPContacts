package com.example.tpcontacts

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ListView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.tpcontacts.model.Contact

class MainActivity : AppCompatActivity() {

    private lateinit var contacts: MutableList<Contact>
    private lateinit var adapter: ContactAdapter
    private var selectedPosition: Int = -1

    // Étape 17 : Récupérer le résultat
    private val detailActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val contactModifie = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.getSerializableExtra("contactModifie", Contact::class.java)
            } else {
                @Suppress("DEPRECATION")
                result.data?.getSerializableExtra("contactModifie") as? Contact
            }

            if (contactModifie != null && selectedPosition != -1 && selectedPosition < contacts.size) {
                contacts[selectedPosition] = contactModifie
            }
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Étape 7 : Charger les contacts
        contacts = mutableListOf(
            Contact(
                "Dupont",
                "Claire",
                "0612345678",
                "claire@gmail.com",
                R.drawable.oip
            ),
            Contact(
                "Martin",
                "Thomas",
                "0711223344",
                "thomas@gmail.com",
                R.drawable.oip2
            ),
            Contact(
                "Bernard",
                "Sophie",
                "0655667788",
                "sophie@gmail.com",
                R.drawable.oip3
            )
        )

        val listView = findViewById<ListView>(R.id.listContacts)

        adapter = ContactAdapter(this, contacts)
        listView.adapter = adapter

        // Étape 10 : Ouvrir DetailActivity
        listView.setOnItemClickListener { _, _, position, _ ->
            selectedPosition = position
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("contact", contacts[position])
            detailActivityResultLauncher.launch(intent)
        }
    }
}

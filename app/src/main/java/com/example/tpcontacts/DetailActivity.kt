package com.example.tpcontacts

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.tpcontacts.model.Contact

class DetailActivity : AppCompatActivity() {

    private lateinit var imgPhoto: ImageView
    private lateinit var edtNom: EditText
    private lateinit var edtPrenom: EditText
    private lateinit var edtTel: EditText
    private lateinit var edtEmail: EditText
    private lateinit var btnCall: Button
    private lateinit var btnMail: Button
    private lateinit var btnGalerie: Button
    private lateinit var btnCamera: Button
    private lateinit var btnEnregistrer: Button
    private lateinit var btnAnnuler: Button

    private var contact: Contact? = null

    // Étape 14 : Launcher Galerie moderne (remplace startActivityForResult / PICK_IMAGE)
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imgPhoto.setImageURI(it)
        }
    }

    // Étape 15 : Launcher Caméra moderne (remplace startActivityForResult / TAKE_PHOTO)
    private val takePhotoLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            imgPhoto.setImageBitmap(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        imgPhoto = findViewById(R.id.imgPhoto)
        edtNom = findViewById(R.id.edtNom)
        edtPrenom = findViewById(R.id.edtPrenom)
        edtTel = findViewById(R.id.edtTel)
        edtEmail = findViewById(R.id.edtEmail)
        btnCall = findViewById(R.id.btnCall)
        btnMail = findViewById(R.id.btnMail)
        btnGalerie = findViewById(R.id.btnGalerie)
        btnCamera = findViewById(R.id.btnCamera)
        btnEnregistrer = findViewById(R.id.btnEnregistrer)
        btnAnnuler = findViewById(R.id.btnAnnuler)

        contact = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("contact", Contact::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("contact") as? Contact
        }

        contact?.let { c ->
            edtNom.setText(c.nom)
            edtPrenom.setText(c.prenom)
            edtTel.setText(c.telephone)
            edtEmail.setText(c.email)
            imgPhoto.setImageResource(c.photoResId)
        }

        // Étape 12 : Bouton Appeler
        btnCall.setOnClickListener {
            if (edtTel.text.isEmpty()) {
                edtTel.error = "Téléphone obligatoire"
                return@setOnClickListener
            }
            val intent = Intent(
                Intent.ACTION_DIAL,
                Uri.parse("tel:${edtTel.text}")
            )
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(this, "Impossible d'effectuer l'appel", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Étape 13 : Bouton E-mail
        btnMail.setOnClickListener {
            if (edtEmail.text.isEmpty()) {
                edtEmail.error = "Email obligatoire"
                return@setOnClickListener
            }
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:${edtEmail.text}")
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(this, "Aucune application e-mail trouvée", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Étape 14 : Galerie
        btnGalerie.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        // Étape 15 : Caméra
        btnCamera.setOnClickListener {
            try {
                takePhotoLauncher.launch(null)
            } catch (e: Exception) {
                Toast.makeText(this, "Impossible d'ouvrir l'appareil photo", Toast.LENGTH_SHORT).show()
            }
        }

        // Étape 16 : Bouton Enregistrer + Étape 19 : Gestion des erreurs
        btnEnregistrer.setOnClickListener {
            var valid = true

            if (edtNom.text.isEmpty()) {
                edtNom.error = "Nom obligatoire"
                valid = false
            }

            if (edtPrenom.text.isEmpty()) {
                edtPrenom.error = "Prénom obligatoire"
                valid = false
            }

            if (edtTel.text.isEmpty()) {
                edtTel.error = "Téléphone obligatoire"
                valid = false
            }

            if (edtEmail.text.isEmpty()) {
                edtEmail.error = "Email obligatoire"
                valid = false
            }

            if (!valid) return@setOnClickListener

            contact?.let { c ->
                c.nom = edtNom.text.toString()
                c.prenom = edtPrenom.text.toString()
                c.telephone = edtTel.text.toString()
                c.email = edtEmail.text.toString()

                val resultIntent = Intent()
                resultIntent.putExtra("contactModifie", c)
                setResult(RESULT_OK, resultIntent)
            }

            finish()
        }

        // Étape 18 : Bouton Annuler
        btnAnnuler.setOnClickListener {
            finish()
        }
    }
}

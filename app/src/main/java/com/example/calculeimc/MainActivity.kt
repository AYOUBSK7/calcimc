package com.example.calculeimc
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var editTextPoids: EditText
    private lateinit var editTextTaille: EditText
    private lateinit var buttonCalculer: Button
    private lateinit var buttonEffacer: Button
    private lateinit var textViewImc: TextView
    private lateinit var textViewCategorie: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextPoids = findViewById(R.id.editTextPoids)
        editTextTaille = findViewById(R.id.editTextTaille)
        buttonCalculer = findViewById(R.id.buttonCalculer)
        buttonEffacer = findViewById(R.id.buttonEffacer)
        textViewImc = findViewById(R.id.textViewImc)
        textViewCategorie = findViewById(R.id.textViewCategorie)

        buttonCalculer.setOnClickListener { calculerImc() }
        buttonEffacer.setOnClickListener { effacer() }
    }

    private fun calculerImc() {
        // 1. Récupérer les saisies (la virgule est acceptée)
        val textePoids = editTextPoids.text.toString().trim().replace(',', '.')
        val texteTaille = editTextTaille.text.toString().trim().replace(',', '.')

        // 2. Vérifier que les champs sont renseignés
        if (textePoids.isEmpty() || texteTaille.isEmpty()) {
            Toast.makeText(this, R.string.erreur_champs_vides, Toast.LENGTH_SHORT).show()
            return
        }

        // 3. Convertir en décimaux (null si saisie invalide, ex. ".")
        val poids = textePoids.toDoubleOrNull()
        val taille = texteTaille.toDoubleOrNull()
        if (poids == null || taille == null) {
            Toast.makeText(this, R.string.erreur_saisie, Toast.LENGTH_SHORT).show()
            return
        }

        // 4. Vérifier que les valeurs sont strictement positives
        if (poids <= 0) {
            editTextPoids.error = getString(R.string.erreur_valeur_positive)
            return
        }
        if (taille <= 0) {
            editTextTaille.error = getString(R.string.erreur_valeur_positive)
            return
        }

        val tailleEnMetres = if (taille > 3) taille / 100 else taille
        val imc = ((poids / (tailleEnMetres * tailleEnMetres)) * 100).roundToInt() / 100.0

        // 7. Catégorie avec if / else if / else
        val categorie: String
        val couleur: Int
        if (imc < 18.5) {
            categorie = getString(R.string.cat_insuffisance)
            couleur = Color.parseColor("#FF9800") // orange
        } else if (imc < 25) {
            categorie = getString(R.string.cat_normale)
            couleur = Color.parseColor("#4CAF50") // vert
        } else if (imc < 30) {
            categorie = getString(R.string.cat_surpoids)
            couleur = Color.parseColor("#FF9800") // orange
        } else if (imc < 35) {
            categorie = getString(R.string.cat_obesite_moderee)
            couleur = Color.parseColor("#F44336") // rouge
        } else if (imc < 40) {
            categorie = getString(R.string.cat_obesite_severe)
            couleur = Color.parseColor("#F44336") // rouge
        } else {
            categorie = getString(R.string.cat_obesite_morbide)
            couleur = Color.parseColor("#B71C1C") // rouge foncé
        }

        // 8. Affichage
        textViewImc.text = getString(R.string.imc_resultat, imc)
        textViewCategorie.text = categorie
        textViewImc.setTextColor(couleur)
        textViewCategorie.setTextColor(couleur)
    }

    private fun effacer() {
        editTextPoids.text.clear()
        editTextTaille.text.clear()
        editTextPoids.error = null
        editTextTaille.error = null
        textViewImc.text = ""
        textViewCategorie.text = ""
        editTextPoids.requestFocus()
    }
}
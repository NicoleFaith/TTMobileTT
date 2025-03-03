package com.example.ttanader.LogSign

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.ttanader.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import android.graphics.PorterDuff
import android.widget.TextView

class SignUpPage : AppCompatActivity() {
    @SuppressLint("MissingInflatedId") // Suppresses missing ID warning (ensure IDs exist in XML)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hide Action Bar for a modern UI
        supportActionBar?.hide()
        setContentView(R.layout.activity_sign_up_page)

        // Initialize UI Components
        val firstNameInput = findViewById<TextInputEditText>(R.id.FirstNameFillUpSign)
        val lastNameInput = findViewById<TextInputEditText>(R.id.LastNameFillUpSign)
        val emailInput = findViewById<TextInputEditText>(R.id.emailFillUpSign)
        val passwordInput = findViewById<TextInputEditText>(R.id.passwordFillUpSign)
        val passwordInputLayout = findViewById<TextInputLayout>(R.id.passwordInputLayout)
        val signUpButton = findViewById<MaterialButton>(R.id.SelSignUpBtn)
        val loginRedirect = findViewById<TextView>(R.id.LoginSignUpPg)

        // Initially hide password toggle icon
        passwordInputLayout.isEndIconVisible = false

        // Apply icon tint effect for text fields
        applyIconTintEffect(firstNameInput, R.drawable.baseline_person_24)
        applyIconTintEffect(lastNameInput, R.drawable.baseline_person_24)
        applyIconTintEffect(emailInput, R.drawable.baseline_email_24)
        applyIconTintEffect(passwordInput, R.drawable.baseline_lock_24)

        // Show password toggle icon only when the user starts typing
        passwordInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Ensure the password toggle icon is always visible when there's text
                passwordInputLayout.isEndIconVisible = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Sign-Up Button - Redirect to LoginPage (You can add logic to store user data)
        signUpButton.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }

        // Already have an account? Login Button - Redirect to LoginPage
        loginRedirect.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }
    }

    /**
     * Function to apply dynamic icon tint effect based on input state.
     *
     * @param editText The TextInputEditText field where the icon should be updated.
     * @param drawableRes The resource ID of the drawable icon.
     */
    private fun applyIconTintEffect(editText: TextInputEditText, drawableRes: Int) {
        // Define default (inactive) and active colors
        val defaultColor = ContextCompat.getColor(this, R.color.darkGray) // Inactive color
        val activeColor = ContextCompat.getColor(this, R.color.primaryBlue) // Active color

        // Get the drawable and apply default color
        val drawable = ContextCompat.getDrawable(this, drawableRes)?.mutate()
        drawable?.setColorFilter(defaultColor, PorterDuff.Mode.SRC_IN)

        // Preserve end icon by modifying only the start icon
        editText.setCompoundDrawablesWithIntrinsicBounds(drawable, null, editText.compoundDrawables[2], null)

        // Add text change listener to update the icon color dynamically
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                drawable?.setColorFilter(
                    if (s.isNullOrEmpty()) defaultColor else activeColor,
                    PorterDuff.Mode.SRC_IN
                )
                // Ensure only the start icon is updated, keeping the end icon intact
                editText.setCompoundDrawablesWithIntrinsicBounds(drawable, null, editText.compoundDrawables[2], null)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}

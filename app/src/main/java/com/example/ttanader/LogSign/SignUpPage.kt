package com.example.ttanader.LogSign

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.ttanader.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import android.graphics.PorterDuff
import android.widget.TextView

class SignUpPage : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // Hide the action bar
        setContentView(R.layout.activity_sign_up_page)

        // Initialize SharedPreferences for storing user email
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        // Initialize UI components
        val firstNameInput = findViewById<TextInputEditText>(R.id.FirstNameFillUpSign)
        val lastNameInput = findViewById<TextInputEditText>(R.id.LastNameFillUpSign)
        val emailInput = findViewById<TextInputEditText>(R.id.emailFillUpSign)
        val passwordInput = findViewById<TextInputEditText>(R.id.passwordFillUpSign)
        val passwordInputLayout = findViewById<TextInputLayout>(R.id.passwordInputLayout)
        val signUpButton = findViewById<MaterialButton>(R.id.SelSignUpBtn)
        val loginRedirect = findViewById<TextView>(R.id.LoginSignUpPg)

        // Initially hide the password toggle icon
        passwordInputLayout.isEndIconVisible = false

        // Apply icon tint effects for text fields
        applyIconTintEffect(firstNameInput, R.drawable.baseline_person_24)
        applyIconTintEffect(lastNameInput, R.drawable.baseline_person_24)
        applyIconTintEffect(emailInput, R.drawable.baseline_email_24)
        applyIconTintEffect(passwordInput, R.drawable.baseline_lock_24)

        // Prevent special characters in name fields
        blockInvalidCharacters(firstNameInput, "First Name", true)
        blockInvalidCharacters(lastNameInput, "Last Name", true)

        // Show password toggle icon only when user starts typing
        passwordInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                passwordInputLayout.isEndIconVisible = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Sign-Up Button Click Listener
        signUpButton.setOnClickListener {
            val firstName = firstNameInput.text.toString().trim()
            val lastName = lastNameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            // Validate form before proceeding
            if (validateInputs(firstName, lastName, email, password)) {
                // Save user email in SharedPreferences
                sharedPreferences.edit().putString("UserEmail", email).apply()

                Toast.makeText(this, "Sign-up successful!", Toast.LENGTH_SHORT).show()

                navigateToWelcomePage() // Redirect to Welcome Page
            }
        }

        // Redirect to Login Page when "Login" is clicked
        loginRedirect.setOnClickListener {
            navigateToLogin()
        }
    }

    /**
     * Prevent users from typing special characters while keeping their already typed text.
     */
    private fun blockInvalidCharacters(editText: TextInputEditText, fieldName: String, isNameField: Boolean) {
        val regex = if (isNameField) "[^a-zA-Z ]" else "[^a-zA-Z0-9@._-]" // Allowed characters

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val filteredText = it.toString().replace(regex.toRegex(), "") // Remove invalid characters
                    if (it.toString() != filteredText) {
                        showToast("$fieldName cannot contain special characters!") // Show warning
                        editText.setText(filteredText) // Set corrected text
                        editText.setSelection(filteredText.length) // Move cursor to the end
                    }
                }
            }
        })
    }

    /**
     * Validate user input before signing up.
     */
    private fun validateInputs(firstName: String, lastName: String, email: String, password: String): Boolean {
        if (firstName.isEmpty()) {
            showToast("First Name is required")
            return false
        }
        if (lastName.isEmpty()) {
            showToast("Last Name is required")
            return false
        }
        if (email.isEmpty()) {
            showToast("Email is required")
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Enter a valid email address")
            return false
        }
        if (password.isEmpty()) {
            showToast("Password is required")
            return false
        }
        if (password.length < 6) {
            showToast("Password must be at least 6 characters")
            return false
        }
        return true
    }

    /**
     * Show a toast message.
     */
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Navigate to Welcome Page after sign-up.
     */
    private fun navigateToWelcomePage() {
        startActivity(Intent(this, WelcomePage::class.java))
        finish() // Prevent back navigation to Sign-Up Page
    }

    /**
     * Navigate to Login Page.
     */
    private fun navigateToLogin() {
        startActivity(Intent(this, LoginPage::class.java))
        finish()
    }

    /**
     * Apply icon tint effect dynamically to EditText fields.
     */
    private fun applyIconTintEffect(editText: TextInputEditText, drawableRes: Int) {
        val defaultColor = ContextCompat.getColor(this, R.color.darkGray) // Inactive color
        val activeColor = ContextCompat.getColor(this, R.color.primaryBlue) // Active color

        val drawable = ContextCompat.getDrawable(this, drawableRes)?.mutate()
        drawable?.setColorFilter(defaultColor, PorterDuff.Mode.SRC_IN)

        editText.setCompoundDrawablesWithIntrinsicBounds(drawable, null, editText.compoundDrawables[2], null)

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                drawable?.setColorFilter(
                    if (s.isNullOrEmpty()) defaultColor else activeColor,
                    PorterDuff.Mode.SRC_IN
                )
                editText.setCompoundDrawablesWithIntrinsicBounds(drawable, null, editText.compoundDrawables[2], null)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}

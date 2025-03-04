package com.example.ttanader.LogSign

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.ttanader.ForgotPassword
import com.example.ttanader.MainActivity
import com.example.ttanader.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.button.MaterialButton
import android.widget.TextView
import android.widget.Toast

class LoginPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // Hides the action bar
        setContentView(R.layout.activity_login_page)

        // for input input
        val emailInput = findViewById<TextInputEditText>(R.id.emailFillUpLog)
        val passwordInput = findViewById<TextInputEditText>(R.id.passwordFillUpLog)
        val passwordInputLayout = findViewById<TextInputLayout>(R.id.passwordInputLayout)
        val loginButton = findViewById<MaterialButton>(R.id.SelLogBtn)
        val signUpButton = findViewById<TextView>(R.id.signUpLogPgBtn)
        val forgotPasswordButton = findViewById<TextView>(R.id.ForgotPassBtn)

        // hides the password toggle icon pero appear din as the user types
        passwordInputLayout.isEndIconVisible = false

        // for tint everytime mag input si user inside the box
        applyIconTintEffect(emailInput, R.drawable.baseline_email_24)
        applyIconTintEffect(passwordInput, R.drawable.baseline_lock_24)

        // Restrict special characters in email field and show warning
        restrictSpecialCharacters(emailInput, "Email")

        // Show password toggle icon only when user types
        passwordInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                passwordInputLayout.isEndIconVisible = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Login button - Validate input and redirect to MainActivity if valid
        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (validateInputs(email, password)) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        // Sign-Up button - Redirects to SignUpPage
        signUpButton.setOnClickListener {
            startActivity(Intent(this, SignUpPage::class.java))
        }

        // Forgot Password button - Redirects to ForgotPassword Page
        forgotPasswordButton.setOnClickListener {
            startActivity(Intent(this, ForgotPassword::class.java))
        }
    }

    /**
     * Restrict special characters in the given input field while allowing only valid characters.
     */
    private fun restrictSpecialCharacters(editText: TextInputEditText, fieldName: String) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val regex = "[^a-zA-Z0-9@._-]".toRegex() // Allowed characters for email
                if (s != null && regex.containsMatchIn(s)) {
                    showToast("$fieldName contains invalid characters!")
                    editText.setText(s.replace(regex, "")) // Remove invalid characters
                    editText.setSelection(editText.text?.length ?: 0) // Keep cursor at end
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    /**
     * Validate user inputs before allowing login.
     */
    private fun validateInputs(email: String, password: String): Boolean {
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
     * Display a short Toast message.
     */
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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

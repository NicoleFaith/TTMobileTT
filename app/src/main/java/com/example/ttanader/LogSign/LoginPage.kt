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

class LoginPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // Hide the action bar for a fullscreen experience
        setContentView(R.layout.activity_login_page)

        // Initialize UI components
        val emailInput = findViewById<TextInputEditText>(R.id.emailFillUpLog)
        val passwordInput = findViewById<TextInputEditText>(R.id.passwordFillUpLog)
        val passwordInputLayout = findViewById<TextInputLayout>(R.id.passwordInputLayout)
        val loginButton = findViewById<MaterialButton>(R.id.SelLogBtn)
        val signUpButton = findViewById<TextView>(R.id.signUpLogPgBtn)
        val forgotPasswordButton = findViewById<TextView>(R.id.ForgotPassBtn)

        // Initially hide the password toggle icon
        passwordInputLayout.isEndIconVisible = false

        // Apply dynamic icon tint effect for email and password fields
        applyIconTintEffect(emailInput, R.drawable.baseline_email_24)
        applyIconTintEffect(passwordInput, R.drawable.baseline_lock_24)

        // Show password toggle icon only when the user starts typing
        passwordInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Show password toggle icon only when the password field is not empty
                passwordInputLayout.isEndIconVisible = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Login button - Redirects user to MainActivity
        loginButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        // Sign-Up button - Redirects user to SignUpPage
        signUpButton.setOnClickListener {
            startActivity(Intent(this, SignUpPage::class.java))
        }

        // Forgot Password button - Redirects user to ForgotPassword Page
        forgotPasswordButton.setOnClickListener {
            startActivity(Intent(this, ForgotPassword::class.java))
        }
    }

    /**
     * Function to dynamically update the tint of the left icon in the input field.
     * The icon changes color based on whether the field is empty or has text.
     *
     * @param editText The TextInputEditText field where the icon should be updated.
     * @param drawableRes The resource ID of the drawable icon to be used.
     */
    private fun applyIconTintEffect(editText: TextInputEditText, drawableRes: Int) {
        // Define default (inactive) and active colors
        val defaultColor = ContextCompat.getColor(this, R.color.darkGray) // Inactive color
        val activeColor = ContextCompat.getColor(this, R.color.primaryBlue) // Active color

        // Get the drawable and apply default color
        val drawable = ContextCompat.getDrawable(this, drawableRes)?.mutate()
        drawable?.setColorFilter(defaultColor, PorterDuff.Mode.SRC_IN)

        // Preserve the right (end) icon by only modifying the left (start) icon
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

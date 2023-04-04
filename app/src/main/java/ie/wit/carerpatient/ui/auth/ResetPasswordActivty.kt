package ie.wit.carerpatient.ui.auth

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import ie.wit.carerpatient.R

class ResetPasswordActivity : AppCompatActivity() {


    private var edtEmail: EditText? = null
    private var btnResetPassword: Button? = null
    private var btnBack: Button? = null
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reset_password)
        supportActionBar?.title = "Reset Password"

        edtEmail = findViewById<View>(R.id.edt_reset_email) as EditText
        btnResetPassword = findViewById<View>(R.id.btn_reset_password) as Button
        btnBack = findViewById<View>(R.id.btn_back) as Button
        mAuth = FirebaseAuth.getInstance()
        btnResetPassword!!.setOnClickListener(View.OnClickListener {


            val email = edtEmail!!.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(applicationContext, "Enter your email!", Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener
            }
            mAuth!!.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@ResetPasswordActivity,
                            "Check email to reset your password!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@ResetPasswordActivity,
                            "Fail to send reset password email!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        })
        btnBack!!.setOnClickListener { finish() }
    }
}
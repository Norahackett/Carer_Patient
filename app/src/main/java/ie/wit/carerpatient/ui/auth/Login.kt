package ie.wit.carerpatient.ui.auth


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ie.wit.carerpatient.databinding.LoginBinding
import androidx.navigation.fragment.findNavController
import timber.log.Timber
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import ie.wit.carerpatient.R
import ie.wit.carerpatient.ui.home.Home
import ie.wit.carerpatient.ui.report.ReportFragmentDirections
import androidx.navigation.findNavController as findNavController1
import androidx.navigation.findNavController as findNavController2

class Login : AppCompatActivity() {

    private lateinit var loginRegisterViewModel: LoginRegisterViewModel
    private lateinit var loginBinding: LoginBinding
    private lateinit var startForResult: ActivityResultLauncher<Intent>
    private lateinit var appBarConfiguration: AppBarConfiguration

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = LoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        loginBinding.emailSignInButton.setOnClickListener {
            signIn(
                loginBinding.fieldEmail.text.toString(),
                loginBinding.fieldPassword.text.toString()
            )
        }
       /// getSupportFragmentManager().beginTransaction()
       //     .add(android.R.id.Register, new RegisterFragment ()).commit();

        //val navController = findNavController(R.id.nav_host_fragment)
        loginBinding.emailCreateAccountButton.setOnClickListener {
        //    supportFragmentManager(). beginTranastio
           createAccount(loginBinding.fieldEmail.text.toString(),
               loginBinding.fieldPassword.text.toString())
        }
        loginBinding.btnForgotPassword.setOnClickListener {
           val intent= Intent (this, ResetPasswordActivity ::class.java)
            startActivity(intent)
        }
           // val action = ReportFragmentDirections.actinLoginActivtyToRegisterFragment()
           // findNavController().navigate(action)
            //val intent = Intent(this, Register::class.java)
            //startActivity(intent)
           // val navController = findNavController2(R.id.nav_host_fragment)
          //  findNavController2().navigate(action)
          //  val navController = findNavController(R.id.nav_host_fragment)
            //val intent = Intent(this, Register::class.java)
            //startActivity(intent)
            // = AppBarConfiguration(
            //(R.id.RegisterFragment)

          //  val navController = findNavController(R.id.nav_host_fragment)

            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.

          //  appBarConfiguration = AppBarConfiguration(
            //    setOf(
               //     R.id.RegisterFragment,

            //    ),
           // )
          //  setupActionBarWithNavController(navController, appBarConfiguration)

            //val navView = loginBinding
            //navView.setupWithNavController(navController)


      //  }

        loginBinding.googleSignInButton.setOnClickListener { googleSignIn() }
        loginBinding.googleSignInButton.setSize(SignInButton.SIZE_WIDE)
        loginBinding.googleSignInButton.setColorScheme(0)
        //setupNavigation()

    }

    private fun findNavController() {

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        loginRegisterViewModel = ViewModelProvider(this).get(LoginRegisterViewModel::class.java)
        loginRegisterViewModel.liveFirebaseUser.observe(this, Observer
        { firebaseUser ->
            if (firebaseUser != null)
                startActivity(Intent(this, Home::class.java))
        })

        loginRegisterViewModel.firebaseAuthManager.errorStatus.observe(this, Observer
        { status -> checkStatus(status) })

        setupGoogleSignInCallback()
    }

    private fun googleSignIn() {
        val signInIntent = loginRegisterViewModel.firebaseAuthManager
            .googleSignInClient.value!!.signInIntent

        startForResult.launch(signInIntent)
    }

    private fun setupGoogleSignInCallback() {
        startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                        try {
                            // Google Sign In was successful, authenticate with Firebase
                            val account = task.getResult(ApiException::class.java)
                            loginRegisterViewModel.authWithGoogle(account!!)
                        } catch (e: ApiException) {
                            // Google Sign In failed
                            Timber.i("Google sign in failed $e")
                            Snackbar.make(
                                loginBinding.loginLayout, "Authentication Failed.",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                        Timber.i("CarerPatient Google Result $result.data")
                    }
                    RESULT_CANCELED -> {

                    }
                    else -> {}
                }
            }
    }

    //Required to exit app from Login Screen - must investigate this further
    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this, "Click again to Close App...", Toast.LENGTH_SHORT).show()
        finish()
    }

    //private fun createAccount( firstname: String, lastname: String, email: String, password: String) {
    //    Timber.d("createAccount:$email")
    //    if (!validateForm()) { return }
    //   loginRegisterViewModel.register(firstname,lastname, email,password)
    // }

    private fun signIn(email: String, password: String) {
        Timber.d("signIn:$email")
        if (!validateForm()) {
            return
        }

        loginRegisterViewModel.login(email, password)
    }

    private fun checkStatus(error: Boolean) {
        if (error)
            Toast.makeText(
                this,
                getString(R.string.auth_failed),
                Toast.LENGTH_LONG
            ).show()
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = loginBinding.fieldEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            loginBinding.fieldEmail.error = "Required."
            valid = false
        } else {
            loginBinding.fieldEmail.error = null
        }

        val password = loginBinding.fieldPassword.text.toString()
        if (TextUtils.isEmpty(password)) {
            loginBinding.fieldPassword.error = "Required."
            valid = false
        } else {
            loginBinding.fieldPassword.error = null
        }
        return valid


    }

    private fun createAccount(email: String, password: String) {
        Timber.d("createAccount:$email")
        if (!validateForm()) { return }

        loginRegisterViewModel.register(email,password)
    }


    // private fun setupNavigation(){
    //     loginBinding.emailCreateAccountButton.setOnClickListener {
    //       findNavController().navigate(R.id.action_welcomeAuthFragment_to_registerFragment)
    //   }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController2(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}

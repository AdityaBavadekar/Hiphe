/*******************************************************************************
 * Copyright (c) 2021. Aditya Bavadekar
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 ******************************************************************************/

package com.adityaamolbavadekar.hiphe.ui.signup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import com.adityaamolbavadekar.hiphe.MainActivity
import com.adityaamolbavadekar.hiphe.R
import com.adityaamolbavadekar.hiphe.interaction.*
import com.adityaamolbavadekar.hiphe.ui.googlesign.GoogleSignInFragment
import com.adityaamolbavadekar.hiphe.ui.login.LoginFragment
import com.adityaamolbavadekar.hiphe.utils.constants
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpFragment : Fragment() {


    private lateinit var passwordE: EditText
    private lateinit var usernameE: EditText
    private lateinit var phoneE: EditText
    private lateinit var nameE: EditText
    private lateinit var cPasswordE: EditText
    private lateinit var signUpButton: MaterialButton
    private lateinit var cancelButton: MaterialButton
    private lateinit var goToLoginButton: MaterialButton
    private lateinit var signUpViewModel: SignUpViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var toast: Toast
    private lateinit var progressBar: ProgressBar
    private lateinit var frameLayout: FrameLayout
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var googleSignInButton: CardView
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        signUpViewModel =
            ViewModelProviders.of(this).get(SignUpViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_signup, container, false)
        passwordE = root.findViewById(R.id.password)
        usernameE = root.findViewById(R.id.username)
        phoneE = root.findViewById(R.id.phone)
        nameE = root.findViewById(R.id.name)
        cPasswordE = root.findViewById(R.id.confirm_password)
        signUpButton = root.findViewById(R.id.signUpButton)
        cancelButton = root.findViewById(R.id.cancelButton)
        goToLoginButton = root.findViewById(R.id.go_to_login_button)
        auth = Firebase.auth
        toast = Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT)
        progressBar = root.findViewById(R.id.progressBar)
        frameLayout = root.findViewById(R.id.hideDataFrameLayout)
        googleSignInButton = root.findViewById(R.id.signup_with_google_btn)


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(constants.defaultWebClientId)//getString(R.string.default_web_client_id))
            .build()
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)


        googleSignInButton.setOnClickListener { signUpWithGoogle() }

        goToLoginButton.setOnClickListener {
            try {
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    .navigate(R.id.action_signUpFragment_to_loginFragment)
            } catch (e: Exception) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment_intro, LoginFragment())
                    .addToBackStack("Login")
                    .commit()
            }
        }


        signUpViewModel.password.observe(viewLifecycleOwner, Observer {
            passwordE.setText(it)
        })

        signUpViewModel.phone.observe(viewLifecycleOwner, Observer {
            phoneE.setText(it)
        })

        signUpViewModel.name.observe(viewLifecycleOwner, Observer {
            nameE.setText(it)
        })

        signUpViewModel.username.observe(viewLifecycleOwner, Observer {
            usernameE.setText(it)
        })


        signUpViewModel.setName(nameE.text.toString())
        signUpViewModel.setPassword(passwordE.text.toString())
        signUpViewModel.setPhone(phoneE.text.toString())
        signUpViewModel.setUsername(usernameE.text.toString())

        signUpButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val name = nameE.text.toString()
            val username = usernameE.text.toString()
            val phone = "+91" + phoneE.text.toString()
            val password = passwordE.text.toString()
            if (name != "" && username != "" && password.length >= 8 && cPasswordE.text.toString() == password) {
                signUpWithEmailPasswords(username, password, name, it, phone)
            } else if (name == "") {
                progressBar.visibility = View.GONE
                val b = MaterialAlertDialogBuilder(requireActivity())//.Builder(requireActivity())
                b.setTitle("Empty Name")
                b.setMessage("The name you entered was empty. Empty name cannot be an account name. Please check your name and Try Again.")
                b.setPositiveButton("Try Again") { _, _ ->
                }
                b.create()
                b.show()
            } else if (username == "") {
                progressBar.visibility = View.GONE
                val b = MaterialAlertDialogBuilder(requireActivity())//.Builder(requireActivity())
                b.setTitle("Invalid Email")
                b.setMessage("The Email Address entered was invalid. Please recheck your Email Address and Try Again.")
                b.setPositiveButton("Try Again") { _, _ ->
                }
                b.create()
                b.show()
            } else if (password.length <= 7) {
                progressBar.visibility = View.GONE
                val b = MaterialAlertDialogBuilder(requireActivity())//.Builder(requireActivity())
                b.setTitle("Password is too Weak")
                b.setMessage("The Password entered was too weak for a hacker to get it. Please check that your Password  follows these guidelines :  \nPassword is longer than 8 lines\nPassword does not contain any obvious info like your name or email\nPassword contains at least one number,one special character and one capital letter.\n\nChek that your password follows these if needed change it and Try Again.")
                b.setPositiveButton("Try Again") { _, _ ->
                }
                b.create()
                b.show()
            } else if (cPasswordE.text.toString() != password) {
                progressBar.visibility = View.GONE
                val b = MaterialAlertDialogBuilder(requireActivity())//.Builder(requireActivity())
                b.setTitle("Confirm Password should same as Password")
                b.setMessage("The Password entered does not match with that entered in Confirm Password box. Please check that your Password matches with Confirm Password and try again.")
                b.setPositiveButton("Try Again") { _, _ ->
                }
                b.create()
                b.show()
            }
        }
        cancelButton.setOnClickListener {
            try {
                val navController =
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                val popBackStack = navController.popBackStack()
                if (!popBackStack) navController.navigate(R.id.action_signUpFragment_to_googleSignInFragment)
            } catch (e: Exception) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment_intro, GoogleSignInFragment())
                    .addToBackStack("")
                    .commit()
            }
        }
        return root
    }

    private fun signUpWithEmailPasswords(
        email: String,
        password: String,
        name: String,
        view: View,
        phone: String?,
    ) {
        view.showSnackbar("Verifying details, Please wait...")
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val user = result.user
                HipheInfoLog(
                    TAG,
                    "signUpWithEmailPasswords >> addOnSuccessListener >> USER : $user "
                )


                if (user != null) {

                    user.sendEmailVerification().addOnSuccessListener {
                        requireContext().showLongToast("Verification mail sent, check it in your inbox or spam")
                    }.addOnFailureListener { exception ->
                        requireContext().showLongToast("Verification mail cannot be sent : ${exception.cause}")
                    }


                    val profileChangeRequest = UserProfileChangeRequest.Builder().apply {
                        photoUri =
                            Uri.parse("https://upload.wikimedia.org/wikipedia/commons/f/f3/Instagram_verifed.png")
                        displayName = name
                    }
                    user.updateProfile(profileChangeRequest.build())
                    if (phone != null && phone != "" && phone.length == 13) {
                        HipheInfoLog(
                            TAG,
                            "User is successfully signed up, Phone number was entered corrctly starting Link EmailPassword User with PhoneNumber Flow throung Dialog Procedure"
                        )
                        startPhoneAuthFlow(phone)
                    }
/*
                    PreferenceManager.getDefaultSharedPreferences(requireContext()).edit {
                        putString(constants.signedInUserEmailPrefKey, user.email)
                        this.putBoolean(constants.checkIsLoggedInPrefKey, true)
                    }
                    startActivity(Intent(requireActivity(), MainActivity::class.java))//TODO()*/
                    updateUI(user)

                }


            }
            .addOnFailureListener {
                HipheInfoLog(
                    TAG,
                    "signUpWithEmailPasswords >> addOnFailureListener >> ERROR : $it "
                )
                when (it) {
                    is FirebaseAuthWeakPasswordException -> {
                        progressBar.visibility = View.GONE
                        view.showSnackbar("The Password entered is too weak!")
                    }
                    is FirebaseAuthUserCollisionException -> {
                        progressBar.visibility = View.GONE
                        view.showSnackbar("An account with same credentials already exists!")
                    }
                    is FirebaseAuthInvalidCredentialsException -> {
                        progressBar.visibility = View.GONE
                        view.showSnackbar("Password and Username are invalid!")
                    }
                    is FirebaseTooManyRequestsException -> {
                        progressBar.visibility = View.GONE
                        view.showSnackbar("Too many atempts please! Please wait few moments...")
                    }
                    is FirebaseNetworkException -> {
                        progressBar.visibility = View.GONE
                        val snack =
                            Snackbar.make(view, "No network connection!", Snackbar.LENGTH_LONG)
                        snack.setAction("Settings") {
                            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                        }
                        snack.show()
                    }

                }
            }
    }

    private fun startPhoneAuthFlow(phone: String) {
//        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//                // This callback will be invoked in two situations:
//                // 1 - Instant verification. In some cases the phone number can be instantly
//                //     verified without needing to send or enter a verification code.
//                // 2 - Auto-retrieval. On some devices Google Play services can automatically
//                //     detect the incoming verification SMS and perform verification without
//                //     user action.
//                Log.d(TAG, "onVerificationCompleted:$credential")
//                signInWithPhoneAuthCredential(credential)//SENDS TWO OTPs
//                requireContext().showToast("Auto retrieving...")
//
//            }
//
//
//            override fun onVerificationFailed(e: FirebaseException) {
//                // This callback is invoked in an invalid request for verification is made,
//                // for instance if the the phone number format is not valid.
//                Log.w(TAG, "onVerificationFailed", e)
//
//                progressBar.visibility = View.GONE
//                otpE.isEnabled = false
//                verifyOTPButton.isEnabled = false
//                sendSMSButton.isEnabled = true
//                phoneE.isEnabled = true
//                tryPassword_AuthButton.isEnabled = true
//                timerTextView.visibility = View.INVISIBLE
//
//                if (e is FirebaseAuthInvalidCredentialsException) {
//                    // Invalid request
//                    requireContext().showLongToast("Verification Failed : $e")
//                    phoneE.showSnackbar("Verification Failed : $e")
//                    /*requireActivity().supportFragmentManager.beginTransaction()
//                        .replace(R.id.nav_host_fragment_intro, GoogleSignInFragment())
//                        .commit()*/
//                } else if (e is FirebaseTooManyRequestsException) {
//                    // The SMS quota for the project has been exceeded
//                    requireContext().showLongToast("Too many atempts, please try again tomorrow")
//                    phoneE.showSnackbar("Verification Failed : $e")
//                    requireActivity().supportFragmentManager.beginTransaction()
//                            .replace(R.id.nav_host_fragment_intro, GoogleSignInFragment())
//                            .commit()
//                } else {
//
//                    phoneE.showSnackbar("Verification Failed : $e")
//                    requireContext().showLongToast("Verification Failed : $e")
//                    /*requireActivity().supportFragmentManager.beginTransaction()
//                        .replace(R.id.nav_host_fragment_intro, GoogleSignInFragment())
//                        .commit()*/
//                }
//
//                // Show a message and update the UI
//            }
//
//            override fun onCodeSent(
//                    verificationId: String,
//                    token: PhoneAuthProvider.ForceResendingToken
//            ) {
//                val time = "OTP sent on" + SimpleDateFormat(
//                        "HH:mm aaa Z, EEE, dd mm yyyy",
//                        Locale.ENGLISH
//                ).format(Date()).toString()
//                // The SMS verification code has been sent to the provided phone number, we
//                // now need to ask the user to enter the code and then construct a credential
//                // by combining the code with a verification ID.
//                Log.d(TAG, "onCodeSent:$verificationId")
//                phoneE.isEnabled = false
//                sendSMSButton.isEnabled = false
//                requireContext().showLongToast("OTP sent successfully")
//                progressBar.visibility = View.GONE
//                otpE.isEnabled = true
//                verifyOTPButton.isEnabled = true
//                val previousText = "${timerTextView.text}\n OTP sent on : "
//                timerTextView.text = "$previousText $time"
//                timerTextView.visibility = View.VISIBLE
//                tryPassword_AuthButton.isEnabled = true
//                // Save verification ID and resending token so we can use them later
//                storedVerificationId = verificationId
//                resendToken = token.toString()
//
//
//                verifyOTPButton.setOnClickListener {
//                    val theOTP = otpE.text.toString()
//                    val credential = PhoneAuthProvider.getCredential(verificationId, theOTP)
//                    signInWithPhoneAuthCredential(credential)
//                    requireContext().showLongToast("verifying OTP please wait...")
//                    progressBar.visibility = View.VISIBLE
//                    otpE.isEnabled = false
//                    verifyOTPButton.isEnabled = false
//
//                }
//
//
//            }
//
//            override fun onCodeAutoRetrievalTimeOut(p0: String) {
//                super.onCodeAutoRetrievalTimeOut(p0)
//                requireContext().showLongToast("please wait some moments and try again, OTP Verified incompletely")
//            }
//        }
    }

    override fun onResume() {
        super.onResume()
        frameLayout.visibility = View.GONE
    }

    override fun onPause() {
        super.onPause()
        frameLayout.visibility = View.VISIBLE
    }
/*


    /////////////GOOGLE SIGN IN CODE START
    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {
            makeToast("Logged in successfully : ${account.email}")
            Log.i(
                TAG,
                "ACTION INITIATED : >> signin_with_google_btn >> setOnClickListener >> signIn >> Activity.RESULT_OK >> GoogleSignIn.getSignedInAccountFromIntent(data) >> .addOnCompleteListener >> handleSignInResult >> try... >> updateUI >> makeToast(${account.email}) >> [ACTION_GOOGLE_SIGN_IN_COMPLETE]"
            )
            val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
            val timestamp = SimpleDateFormat(
                getString(R.string.signin_hasmap_timestamp),
                Locale.ENGLISH
            ).format(Date())
            val user = hashMapOf(
                "name" to account.displayName,
                "email" to account.email,
                "givenName" to " ${account.givenName}",
                "idToken" to " ${account.idToken}",
                "familyName" to " ${account.familyName}",
                "id" to account.id,
                "photoURL" to "${account.photoUrl}",
                "signUp" to timestamp,
                "isVerified" to "Verified User : true"
            )
            prefs.edit {
                putString("USER_ACCOUNT", user.toString())
                putString(constants.signedInUserEmailPrefKey, account.email)
                this.putBoolean(constants.checkIsLoggedInPrefKey, true)
            }
            startActivity(Intent(requireActivity(), MainActivity::class.java))
        }


    }

    private var r =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                Log.i(
                    TAG,
                    "ACTION INITIATED : >> signin_with_google_btn >> setOnClickListener >> signIn >> Activity.RESULT_OK"
                )
                val data = activityResult.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    .addOnCompleteListener {
                        Log.i(
                            TAG,
                            "ACTION INITIATED : >> signin_with_google_btn >> setOnClickListener >> signIn >> Activity.RESULT_OK >> GoogleSignIn.getSignedInAccountFromIntent(data) >> .addOnCompleteListener"
                        )
                        handleSignInResult(it)
                    }
                    .addOnFailureListener {
                        makeToast("0ops! Something went wrong! : Failure")
                        updateUI(null)
                    }
                    .addOnCanceledListener {
                        makeToast("0ops! Something went wrong! : You Canceled")
                        updateUI(null)
                    }
                when {
                    task.isComplete -> {
                        handleSignInResult(task)
                    }
                    task.isCanceled -> {
                        makeToast("0ops! Something went wrong! : ${task.exception}")
                        updateUI(null)
                    }
                    else -> {
                        makeToast("0ops! Something went wrong! : ${task.exception}")
                        updateUI(null)
                    }
                }
            }
        }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            Log.i(
                TAG,
                "ACTION INITIATED : >> signin_with_google_btn >> setOnClickListener >> signIn >> Activity.RESULT_OK >> GoogleSignIn.getSignedInAccountFromIntent(data) >> .addOnCompleteListener >> handleSignInResult >> try..."
            )

            // Signed in successfully, show authenticated UI.
            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            Log.e(
                TAG,
                "ACTION INITIATED : >> signin_with_google_btn >> setOnClickListener >> signIn >> Activity.RESULT_OK >> GoogleSignIn.getSignedInAccountFromIntent(data) >> .addOnCompleteListener >> handleSignInResult >> try... !>> catch !>> error : $e"
            )
            makeToast("0ops! Something went wrong! : ${e.message}")
        }
    }

    private fun makeToast(text: String) {
        return requireContext().showToast(text)
    }
    /////////////GOOGLE SIGN IN CODE END

*/


    /////////////GOOGLE SIGN IN CODE START
    private fun updateUI(account: FirebaseUser?) {
        if (account != null) {
            makeToast("Logged in successfully : ${account.email}")
            HipheInfoLog(
                TAG,
                "ACTION INITIATED : >> signin_with_google_btn >> setOnClickListener >> signIn >> Activity.RESULT_OK >> GoogleSignIn.getSignedInAccountFromIntent(data) >> .addOnCompleteListener >> handleSignInResult >> try... >> updateUI >> makeToast(${account.email}) >> [ACTION_GOOGLE_SIGN_IN_COMPLETE]"
            )


            HipheInfoLog(TAG, "A HAS SUCCESSFULLY SIGNED UP")
            HipheInfoLog(TAG, "USER : $account")
            HipheInfoLog(TAG, "USER METADATA : ${account.metadata}")
            HipheInfoLog(TAG, "USER PROVIDER DATA : ${account.providerData}")
            HipheInfoLog(TAG, "USER EMAIL : ${account.email}")
            HipheInfoLog(TAG, "USER NAME : ${account.displayName}")
            HipheInfoLog(TAG, "USER UID : ${account.uid}")


            val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
            prefs.edit {
                putString("USER_ACCOUNT", account.toString())
                putString(constants.signedInUserEmailPrefKey, account.email)
                this.putBoolean(constants.checkIsLoggedInPrefKey, true)
            }
            val intent = Intent(requireActivity(), MainActivity::class.java)
            intent.putExtra(constants.isUserUnexplored, true)
            startActivity(intent)

            requireActivity().finish()
/*//            val timestamp = SimpleDateFormat(
//                getString(R.string.signin_hasmap_timestamp),
//                Locale.ENGLISH
//            ).format(Date())
//            val user = hashMapOf(
//                "name" to account.displayName,
//                "email" to account.email,
//                "givenName" to " ${account.givenName}",
//                "idToken" to " ${account.idToken}",
//                "familyName" to " ${account.familyName}",
//                "id" to account.id,
//                "photoURL" to "${account.photoUrl}",
//                "signUp" to timestamp,
//                "isVerified" to "Verified User : true"
//            )*/
        }
    }

    private var r =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                HipheInfoLog(
                    TAG,
                    "ACTION INITIATED : >> signin_with_google_btn >> setOnClickListener >> signIn >> Activity.RESULT_OK"
                )
                val data = activityResult.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    .addOnCompleteListener {
                        HipheInfoLog(
                            TAG,
                            "ACTION INITIATED : >> signin_with_google_btn >> setOnClickListener >> signIn >> Activity.RESULT_OK >> GoogleSignIn.getSignedInAccountFromIntent(data) >> .addOnCompleteListener"
                        )
//                        handleSignInResult(it)
                        val idToken = it.result.idToken!!
                        firebaseAuthWithGoogle(idToken)
                    }
                    .addOnFailureListener {
                        makeToast("0ops! Something went wrong! : Failure")
                        updateUI(null)
                    }
                    .addOnCanceledListener {
                        makeToast("0ops! Something went wrong! : You Canceled")
                        updateUI(null)
                    }
            }
        }

/*
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            Log.i(
                TAG,
                "ACTION INITIATED : >> signin_with_google_btn >> setOnClickListener >> signIn >> Activity.RESULT_OK >> GoogleSignIn.getSignedInAccountFromIntent(data) >> .addOnCompleteListener >> handleSignInResult >> try..."
            )

            // Signed in successfully, show authenticated UI.
            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            Log.e(
                TAG,
                "ACTION INITIATED : >> signin_with_google_btn >> setOnClickListener >> signIn >> Activity.RESULT_OK >> GoogleSignIn.getSignedInAccountFromIntent(data) >> .addOnCompleteListener >> handleSignInResult >> try... !>> catch !>> error : $e"
            )
            makeToast("0ops! Something went wrong! : ${e.message}")
        }

*/
    //DISABLED USING FIREBASE-GOOGLE SIGNIN INSTEAD

    private fun makeToast(text: String) {
        return requireContext().showToast(text)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    HipheInfoLog(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    HipheErrorLog(TAG, "signInWithCredential:failure", task.exception.toString())
                    updateUI(null)
                }
            }
    }


    private fun signUpWithGoogle() {
        val signUpIntent = mGoogleSignInClient.signInIntent
        r.launch(signUpIntent)
    }

    /////////////GOOGLE SIGN IN CODE END

    companion object {
        const val TAG = "SignUpFragemtn"
    }

}
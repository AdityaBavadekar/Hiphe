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

package com.adityaamolbavadekar.hiphe.ui.login

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.adityaamolbavadekar.hiphe.*
import com.adityaamolbavadekar.hiphe.R
import com.adityaamolbavadekar.hiphe.interaction.showLongToast
import com.adityaamolbavadekar.hiphe.interaction.showSnackbar
import com.adityaamolbavadekar.hiphe.interaction.showSnackbarWithAction
import com.adityaamolbavadekar.hiphe.interaction.showToast
import com.adityaamolbavadekar.hiphe.ui.googlesign.GoogleSignInFragment
import com.adityaamolbavadekar.hiphe.ui.signup.SignUpFragment
import com.adityaamolbavadekar.hiphe.utils.constants
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class LoginFragment : Fragment() {

    private lateinit var passwordE: EditText
    private lateinit var usernameE: EditText
    private lateinit var phoneE: EditText
    private lateinit var countryCodeE: EditText
    private lateinit var otpE: EditText
    private lateinit var loginButton: MaterialButton
    private lateinit var cancelButton: MaterialButton
    private lateinit var forgotButton: MaterialButton
    private lateinit var sendSMSButton: MaterialButton
    private lateinit var verifyOTPButton: MaterialButton
    private lateinit var tryOTP_AuthButton: MaterialButton
    private lateinit var tryPassword_AuthButton: MaterialButton
    private lateinit var goToSignUpButton: MaterialButton
    private lateinit var progressBar: ProgressBar
    private lateinit var timerTextView: TextView
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var googleSignInButton: CardView
    private lateinit var otpeditLayout: LinearLayout
    private lateinit var otpbtnLayout: LinearLayout


    private lateinit var resendToken: String
    private lateinit var storedVerificationId: String
    private lateinit var auth: FirebaseAuth
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
//    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        loginViewModel =
//            ViewModelProviders.of(this).get(LoginViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_login, container, false)

        passwordE = root.findViewById(R.id.password)
        usernameE = root.findViewById(R.id.username)
        phoneE = root.findViewById(R.id.phone)
        countryCodeE = root.findViewById(R.id.ccode)
        otpE = root.findViewById(R.id.otp)
        loginButton = root.findViewById(R.id.loginBtn)
        cancelButton = root.findViewById(R.id.cancelButton)
        forgotButton = root.findViewById(R.id.forgot_psw)
        sendSMSButton = root.findViewById(R.id.sendSMS_Button)
        verifyOTPButton = root.findViewById(R.id.verifyOTP)
        tryOTP_AuthButton = root.findViewById(R.id.authenticate_with_otp)
        tryPassword_AuthButton = root.findViewById(R.id.authenticate_with_password)
        goToSignUpButton = root.findViewById(R.id.go_to_signup_button)
        progressBar = root.findViewById(R.id.progressBar)
        timerTextView = root.findViewById(R.id.timerTxtView)
        googleSignInButton = root.findViewById(R.id.signin_with_google_btn2)
        otpeditLayout = root.findViewById(R.id.otp_layout)
        otpbtnLayout = root.findViewById(R.id.otp_btn_l)


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(getString(R.string.default_web_client_id))
            .build()
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)


        googleSignInButton.setOnClickListener { signInWithGoogle() }

        goToSignUpButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_intro, SignUpFragment())
                .commit()
        }

        forgotButton.setOnClickListener { view ->
            if (usernameE.text.toString() != "" && usernameE.text.toString()
                    .endsWith("@gmail.com")
            ) {
                auth.sendPasswordResetEmail(usernameE.text.toString())
                    .addOnSuccessListener {
                        view.showSnackbarWithAction(
                            "Reset Password link has been sent to ${usernameE.text.toString()}",
                            "OK"
                        )
                        val gmailIntent = Intent.makeMainSelectorActivity(
                            Intent.ACTION_MAIN,
                            Intent.CATEGORY_APP_EMAIL
                        )
                        gmailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(
                            Intent.createChooser(
                                gmailIntent,
                                "View Reset Password Email"
                            )
                        )

                    }
                    .addOnFailureListener {
                        if (it is FirebaseAuthInvalidCredentialsException) view.showSnackbarWithAction(
                            "0ops! no account with that email found",
                            "OK"
                        )
                        else view.showSnackbarWithAction("0ops! ${it.cause}", "OK")
                    }
            } else {
                view.showSnackbarWithAction(
                    "Then please enter your valid Email Address and we'll send you a link",
                    "OK"
                )
            }
        }

        loginButton.setOnClickListener {
            val username = usernameE.text.toString()
            val password = passwordE.text.toString()
            progressBar.visibility = View.VISIBLE
            if (username != "" && password != "" && password.length >= 8) {
                HipheInfoLog(TAG, "Login Button Clicked >> Alert Dialog >> OK Clicked")
                signInWithEmailPasswords(username, password)
            } else if (username == "" && password == "" && password.length >= 8) {
                requireContext().showToast("Username and Password should not be empty!")
                progressBar.visibility = View.GONE
            } else if (username != "" && password != "" && password.length <= 7) {
                requireContext().showToast("Invalid Password entered!")
                progressBar.visibility = View.GONE
            } else {
                requireContext().showToast("Username and Password should be valid!")
                progressBar.visibility = View.GONE
            }

        }
        cancelButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_intro, GoogleSignInFragment())
                .commit()
        }


        tryPassword_AuthButton.setOnClickListener {
            root.findViewById<LinearLayout>(R.id.emailPasswordLayout).visibility = View.VISIBLE
            root.findViewById<LinearLayout>(R.id.OTP_Layout).visibility = View.GONE

        }

        tryOTP_AuthButton.setOnClickListener {
            root.findViewById<LinearLayout>(R.id.emailPasswordLayout).visibility = View.GONE
            root.findViewById<LinearLayout>(R.id.OTP_Layout).visibility = View.VISIBLE

        }

        sendSMSButton.setOnClickListener {
            val countryCode = countryCodeE.text.toString()
            val phoneNumber = "+" + countryCode + phoneE.text.toString()
            if (phoneNumber.isNotBlank() && phoneNumber.isNotEmpty()) {
                if (phoneNumber.length == 13 || phoneNumber.length == 14) {
                    sendSmsForVerification(phoneNumber)
                    requireContext().showLongToast("Sending SMS please wait...")
                    progressBar.visibility = View.VISIBLE
                } else if (phoneNumber.length in 14..20 || phoneNumber.length in 0..9) {
                    requireContext().showLongToast("Invalid Phone number!")
                }
            } else {
                requireContext().showLongToast("Invalid Phone number!")
            }

        }

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]

        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                HipheInfoLog(TAG, "onVerificationCompleted:$credential")
                signInWithPhoneAuthCredential(credential)//SENDS TWO OTPs
                requireContext().showToast("Auto retrieving...")

            }


            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                HipheErrorLog(TAG, "onVerificationFailed", e.toString())

                progressBar.visibility = View.GONE
                otpE.isEnabled = false
                verifyOTPButton.isEnabled = false
                sendSMSButton.isEnabled = true
                phoneE.isEnabled = true
                otpeditLayout.visibility = View.GONE
                otpbtnLayout.visibility = View.GONE
                tryPassword_AuthButton.isEnabled = true
                timerTextView.visibility = View.INVISIBLE

                when (e) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        // Invalid request
                        requireContext().showLongToast("Verification Failed : $e")
                        phoneE.showSnackbar("Verification Failed : $e")
                        /*requireActivity().supportFragmentManager.beginTransaction()
                                    .replace(R.id.nav_host_fragment_intro, GoogleSignInFragment())
                                    .commit()*/
                    }
                    is FirebaseTooManyRequestsException -> {
                        // The SMS quota for the project has been exceeded
                        requireContext().showLongToast("Too many atempts, please try again tomorrow")
                        phoneE.showSnackbar("Verification Failed : $e")
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.nav_host_fragment_intro, GoogleSignInFragment())
                            .commit()
                    }
                    is FirebaseNetworkException -> {
                        progressBar.visibility = View.GONE
                        val snack =
                            Snackbar.make(phoneE, "No network connection!", Snackbar.LENGTH_LONG)
                        snack.setAction("Settings") {
                            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                        }
                        snack.show()
                    }
                    else -> {

                        phoneE.showSnackbar("Verification Failed : $e")
                        requireContext().showLongToast("Verification Failed : $e")
                        /*requireActivity().supportFragmentManager.beginTransaction()
                                    .replace(R.id.nav_host_fragment_intro, GoogleSignInFragment())
                                    .commit()*/
                    }
                }

                // Show a message and update the UI
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                val time = "OTP sent on" + SimpleDateFormat(
                    "HH:mm aaa Z, EEE, dd mm yyyy",
                    Locale.ENGLISH
                ).format(Date()).toString()
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                HipheInfoLog(TAG, "onCodeSent:$verificationId")
                phoneE.isEnabled = false
                sendSMSButton.isEnabled = false
                requireContext().showLongToast("OTP sent successfully")
                progressBar.visibility = View.GONE
                otpeditLayout.visibility = View.VISIBLE
                otpbtnLayout.visibility = View.VISIBLE
                otpE.isEnabled = true
                verifyOTPButton.isEnabled = true
                val previousText = "${timerTextView.text}\n OTP sent on : "
                timerTextView.text = "$previousText $time"
                timerTextView.visibility = View.VISIBLE
                tryPassword_AuthButton.isEnabled = true
                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
                resendToken = token.toString()


                verifyOTPButton.setOnClickListener {
                    val theOTP = otpE.text.toString()
                    val credential = PhoneAuthProvider.getCredential(verificationId, theOTP)
                    signInWithPhoneAuthCredential(credential)
                    requireContext().showLongToast("verifying OTP please wait...")
                    progressBar.visibility = View.VISIBLE
                    otpE.isEnabled = false
                    verifyOTPButton.isEnabled = false
                }


            }

            override fun onCodeAutoRetrievalTimeOut(p0: String) {
                super.onCodeAutoRetrievalTimeOut(p0)
                requireContext().showLongToast("please wait some moments and try again, OTP Verified incompletely")
            }
        }
        return root
    }


    private fun sendSmsForVerification(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, java.util.concurrent.TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }


    // [START resend_verification]
    private fun resendVerificationCode(//TODO(USE THIS FEATURE)
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, java.util.concurrent.TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
        if (token != null) {
            optionsBuilder.setForceResendingToken(token) // callback's ForceResendingToken
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }
    // [END resend_verification]


    // [START sign_in_with_phone]
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    HipheInfoLog(TAG, "signInWithCredential:success")
                    val user = task.result.user
                    progressBar.visibility = View.GONE
                    if (user != null) {
                        if (user.email == null) {
                            showAlertDialogToGetEmailIdandName(credential, user)
                        } //TODO(HERE)
                        else {
                            updateUiNonNullFirebaseUser(user)
                        }
                    }
                } else {
                    // Sign in failed, display a message and update the UI
                    HipheErrorLog(TAG, "signInWithCredential:failure", task.exception.toString())
                    requireContext().showLongToast("Error occurred : ${task.exception!!.message}")
                    requireActivity().finish()
                    progressBar.visibility = View.GONE
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        progressBar.visibility = View.GONE
                        requireContext().showLongToast("Invalid code entered : ${task.exception!!.message}")
                        requireActivity().recreate()
                    }
                    // Update UI
                }
            }
    }

    private fun showAlertDialogToGetEmailIdandName(
        phoneAuthCredential: PhoneAuthCredential,
        user: FirebaseUser
    ) {

        val d = Dialog(requireActivity())
        d.requestWindowFeature(Window.FEATURE_NO_TITLE)
        d.setContentView(R.layout.custom_dialog_for_name_and_email_input)
        d.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(d.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        val progressBarProgress = d.findViewById<ProgressBar>(R.id.progressBar)
        d.findViewById<Button>(R.id.save_ne_button).setOnClickListener {

            val nameEditTextText = d.findViewById<EditText>(R.id.name).text.toString()
            val emailEditTextText = d.findViewById<EditText>(R.id.username).text.toString()
            val passwordEditTextText = d.findViewById<EditText>(R.id.password).text.toString()
            if (nameEditTextText != "" && passwordEditTextText.length >= 8 && emailEditTextText != "" && emailEditTextText.endsWith(
                    "@gmail.com"
                )
            ) {
                progressBarProgress.visibility = View.VISIBLE
                val profileChangeRequest = UserProfileChangeRequest.Builder().apply {
                    photoUri =
                        Uri.parse("https://upload.wikimedia.org/wikipedia/commons/f/f3/Instagram_verifed.png")
                    displayName = nameEditTextText
                }
                user.updateProfile(profileChangeRequest.build())

                val authCredential =
                    EmailAuthProvider.getCredential(emailEditTextText, passwordEditTextText)
                user.linkWithCredential(authCredential)
                    .addOnSuccessListener {
                        progressBarProgress.visibility = View.INVISIBLE
                        d.dismiss()

                        val linkedUser = it.user
                        if (linkedUser != null) {
                            linkedUser.sendEmailVerification()
                            requireContext().showToast("Successfully updated your profile.")
                            d.dismiss()
                            updateUiNonNullFirebaseUser(linkedUser)
                        }

                    }
                    .addOnFailureListener {
                        user.linkWithCredential(authCredential)
                            .addOnSuccessListener {
                            }
                            .addOnFailureListener {
                                if (it is FirebaseNetworkException) {
                                    progressBar.visibility = View.GONE
                                    val snack =
                                        Snackbar.make(
                                            googleSignInButton,
                                            "No network connection!",
                                            Snackbar.LENGTH_LONG
                                        )
                                    snack.setAction("Settings") {
                                        startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                                    }
                                    snack.show()
                                } else {
                                    requireContext().showToast("There is problem linking your Email and Phone please try signup again later:\n ${it.cause}")
                                    requireActivity().recreate()
                                }
                            }
                    }
                progressBarProgress.visibility = View.INVISIBLE
            } else {
                requireContext().showToast("Please verify all details are completely filled and Password ")
            }


            //d.dismiss()
        }
        d.setCancelable(false)
        d.show()
        d.window!!.attributes = lp

    }
    // [END sign_in_with_phone]

    private fun signInWithEmailPasswords(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val additionalUserInfo = it.additionalUserInfo
                val credential = it.credential
                val user = it.user
                if (user != null) {
                    if (!user.isEmailVerified) {
                        user.sendEmailVerification()
                            .addOnSuccessListener {
                                requireContext().showLongToast("Verification mail sent, check it in your inbox or spam \n $additionalUserInfo $credential")
                                progressBar.visibility = View.GONE
                                updateUiNonNullFirebaseUser(user)
                            }
                            .addOnFailureListener { exception ->
                                requireContext().showLongToast("Verification mail cannot be sent : ${exception.cause}")
                                progressBar.visibility = View.GONE
                                updateUiNonNullFirebaseUser(user)
                            }
                    }
                    progressBar.visibility = View.GONE
                    updateUiNonNullFirebaseUser(user)
                }

            }
            .addOnFailureListener {
                if (it is FirebaseAuthInvalidCredentialsException) {
                    requireContext().showLongToast("Credentials entered were invalid, Please try again with another credentials")
                    progressBar.visibility = View.GONE
                } else {
                    requireContext().showLongToast(
                        "Error occurred : ${it.cause}"
                    )
                    progressBar.visibility = View.GONE
                }
            }
    }


    private fun updateUiNonNullFirebaseUser(user: FirebaseUser) {
        PreferenceManager.getDefaultSharedPreferences(requireContext()).edit {
            putString(constants.signedInUserEmailPrefKey, user.email)
            this.putBoolean(constants.checkIsLoggedInPrefKey, true)
        }
        HipheUserInfoInfoLog(TAG, "USER : ${user.email}")
        HipheUserInfoInfoLog(TAG, "USER : ${user.displayName}")
        startActivity(Intent(requireActivity(), MainActivity::class.java))//TODO(HERE)
        requireActivity().finish()

    }


    /////////////GOOGLE SIGN IN CODE START
    private fun updateUI(account: FirebaseUser?) {
        if (account != null) {
            makeToast("Logged in successfully : ${account.email}")
            HipheInfoLog(
                TAG,
                "ACTION INITIATED : >> signin_with_google_btn >> setOnClickListener >> signIn >> Activity.RESULT_OK >> GoogleSignIn.getSignedInAccountFromIntent(data) >> .addOnCompleteListener >> handleSignInResult >> try... >> updateUI >> makeToast(${account.email}) >> [ACTION_GOOGLE_SIGN_IN_COMPLETE]"
            )
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
            updateUiNonNullFirebaseUser(account)
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

    private fun signInWithGoogle() {
        val signInIntent = mGoogleSignInClient.signInIntent
        r.launch(signInIntent)
    }

    /////////////GOOGLE SIGN IN CODE END


    companion object {
        const val TAG = "LoginFragment"
    }

}
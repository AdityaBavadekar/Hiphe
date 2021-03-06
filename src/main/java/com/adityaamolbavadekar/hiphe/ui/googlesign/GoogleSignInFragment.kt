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

package com.adityaamolbavadekar.hiphe.ui.googlesign

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.adityaamolbavadekar.hiphe.R
import com.adityaamolbavadekar.hiphe.interaction.showToast
import com.adityaamolbavadekar.hiphe.ui.login.LoginFragment
import com.adityaamolbavadekar.hiphe.ui.signup.SignUpFragment
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.CredentialsClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.button.MaterialButton
import com.jama.carouselview.CarouselView
import com.jama.carouselview.enums.IndicatorAnimationType

class GoogleSignInFragment : Fragment() {
    companion object {
        const val TAG: String = "GoogleSignInFragment"
    }

    private val images = arrayListOf(
        R.drawable.clouds,
        R.drawable.ic_round_verified_user_24,
        R.drawable.ic_twotone_important_devices_24
    )//Images to be displayed
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mCredentialsClient: CredentialsClient
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_googlesignin, container, false)

        val carouselView = root.findViewById<CarouselView>(R.id.carouselView)

        try {
            carouselView.apply {
                size = images.size
                autoPlay = true
                resource = R.layout.carousel_item
                setCarouselViewListener { view, position ->
                    val imageView = view.findViewById<ImageView>(R.id.imageView)
                    imageView.setImageDrawable(resources.getDrawable(images[position]))
                }
                indicatorAnimationType = IndicatorAnimationType.SLIDE
                show()
            }
        } catch (e: Exception) {
        }

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        mCredentialsClient = Credentials.getClient(requireActivity())


//        checkIfUserHasGooglePasswordLock()


        root.findViewById<MaterialButton>(R.id.login_btn).setOnClickListener {
            login()
        }
        root.findViewById<MaterialButton>(R.id.signin_btn).setOnClickListener {
            signUp()
        }
//        root.findViewById<CardView>(R.id.signin_with_google_btn).setOnClickListener {
//            requireContext().showToast("Google Sign In")
//            Log.i(TAG, "ACTION INITIATED : >> signin_with_google_btn >> setOnClickListener")
//            val emailID = try {
//                getAccounts()
//            } catch (e: Exception) {
//                "Error : $e"
//            }
//
//            val d = AlertDialog.Builder(requireActivity())
//            d.setTitle("Accounts")
//            d.setMessage("$emailID")
//            d.setIcon(R.drawable.ic_baseline_account_box_24)
//            d.setPositiveButton("OK") { _, _ ->
//                startActivity(Intent(requireActivity(), MainActivity::class.java))
//                requireActivity().finish()
//            }
//            d.setNegativeButton("TRY SIGN IN") { _, _ ->
//                val b = AccountPicker.AccountChooserOptions.Builder()
//                signIn(it)
//            }
//            d.create()
//            d.show()
//        }
        return root
    }
//
//    private fun checkIfUserHasGooglePasswordLock(): Boolean {
//        val credentialRequest = CredentialRequest.Builder()
//            .setPasswordLoginSupported(true)
//            .setAccountTypes(IdentityProviders.GOOGLE, IdentityProviders.FACEBOOK)
//            .build()
//
//        val boolResult = mCredentialsClient.request(credentialRequest)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//
//                    onCredentialRetrieved(task.result.credential!!)
//                }
//            }
//        return boolResult.isComplete
//    }
//
//    private fun onCredentialRetrieved(credential: Credential): Boolean {
//
//        val accountType = credential.accountType
//        return when {
//            accountType != null -> {
//                //            signInWithPassword(credential.id, credential.password)
//                true
//            }
//            else -> {
//                false
//            }
//        }
//
//
//    }
//
//    private fun getAccounts(): String {
//        return if (ContextCompat.checkSelfPermission(
//                requireActivity(),
//                android.Manifest.permission.GET_ACCOUNTS
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            val accountManager =
//                requireContext().getSystemService(Context.ACCOUNT_SERVICE) as AccountManager
//            val accounts = accountManager.accounts
//            var emailID = ""
//            for (account in accounts) {
//                emailID += "\n" + account.name
//            }
//            emailID
//        } else ""
//    }
//
//
//    private fun signIn(view: View) {
//        Log.i(TAG, "ACTION INITIATED : >> signin_with_google_btn >> setOnClickListener >> signIn")
//        val signInIntent: Intent = mGoogleSignInClient.signInIntent
//        //view.isEnabled = false
//        r.launch(signInIntent)
//    }
//
//    private fun updateUI(account: GoogleSignInAccount?) {
//        if (account != null) {
//            makeToast("Logged in successfully : ${account.email}")
//            PreferenceManager.getDefaultSharedPreferences(requireContext()).edit {
//                this.putBoolean(constants.checkIsLoggedInPrefKey, true)
//            }
//
//            Log.i(
//                TAG,
//                "ACTION INITIATED : >> signin_with_google_btn >> setOnClickListener >> signIn >> Activity.RESULT_OK >> GoogleSignIn.getSignedInAccountFromIntent(data) >> .addOnCompleteListener >> handleSignInResult >> try... >> updateUI >> makeToast(${account.email}) >> [ACTION_GOOGLE_SIGN_IN_COMPLETE]"
//            )
//            val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
//            val timestamp = SimpleDateFormat(
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
//            )
//            prefs.edit {
//                putString("USER_ACCOUNT", user.toString())
//            }
//            startActivity(Intent(requireActivity(), MainActivity::class.java))
//        }
//
//
//    }
//
//    private fun updateHasUI(account: GoogleSignInAccount) {
//        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
//        val timestamp = SimpleDateFormat(
//            getString(R.string.signin_hasmap_timestamp),
//            Locale.ENGLISH
//        ).format(Date())
//        val user = hashMapOf(
//            "name" to account.displayName,
//            "email" to account.email,
//            "givenName" to " ${account.givenName}",
//            "idToken" to " ${account.idToken}",
//            "familyName" to " ${account.familyName}",
//            "id" to account.id,
//            "photoURL" to "${account.photoUrl}",
//            "signUp" to timestamp,
//            "isVerified" to "Verified User : true"
//        )
//        prefs.edit {
//            putString("USER_ACCOUNT", user.toString())
//        }
//    }
//
//    private var r =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
//            if (activityResult.resultCode == Activity.RESULT_OK) {
//                Log.i(
//                    TAG,
//                    "ACTION INITIATED : >> signin_with_google_btn >> setOnClickListener >> signIn >> Activity.RESULT_OK"
//                )
//                val data = activityResult.data
//                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//                    .addOnCompleteListener {
//                        Log.i(
//                            TAG,
//                            "ACTION INITIATED : >> signin_with_google_btn >> setOnClickListener >> signIn >> Activity.RESULT_OK >> GoogleSignIn.getSignedInAccountFromIntent(data) >> .addOnCompleteListener"
//                        )
//                        handleSignInResult(it)
//                    }
//                    .addOnFailureListener {
//                        makeToast("0ops! Something went wrong! : Failure")
//                        updateUI(null)
//                    }
//                    .addOnCanceledListener {
//                        makeToast("0ops! Something went wrong! : You Canceled")
//                        updateUI(null)
//                    }
//                when {
//                    task.isComplete -> {
//                        handleSignInResult(task)
//                    }
//                    task.isCanceled -> {
//                        makeToast("0ops! Something went wrong! : ${task.exception}")
//                        updateUI(null)
//                    }
//                    else -> {
//                        makeToast("0ops! Something went wrong! : ${task.exception}")
//                        updateUI(null)
//                    }
//                }
//            }
//        }
//
//    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
//        try {
//            val account = completedTask.getResult(ApiException::class.java)
//            Log.i(
//                TAG,
//                "ACTION INITIATED : >> signin_with_google_btn >> setOnClickListener >> signIn >> Activity.RESULT_OK >> GoogleSignIn.getSignedInAccountFromIntent(data) >> .addOnCompleteListener >> handleSignInResult >> try..."
//            )
//
//            // Signed in successfully, show authenticated UI.
//            updateUI(account)
//        } catch (e: ApiException) {
//            // The ApiException status code indicates the detailed failure reason.
//            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            //Log.w(TAG, "signInResult:failed code=" + e.statusCode)
//            Log.e(
//                TAG,
//                "ACTION INITIATED : >> signin_with_google_btn >> setOnClickListener >> signIn >> Activity.RESULT_OK >> GoogleSignIn.getSignedInAccountFromIntent(data) >> .addOnCompleteListener >> handleSignInResult >> try... !>> catch !>> error : $e"
//            )
//            makeToast("0ops! Something went wrong! : ${e.message}")
//        }
//    }


    private fun makeToast(text: String) {
        return requireContext().showToast(text)
    }


    private fun signUp() {

        try {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                .navigate(R.id.action_googleSignInFragment_to_signUpFragment)
        } catch (e: Exception) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, SignUpFragment())
                .addToBackStack("SignUp")
                .commit()
        }
        //CHANGE ACTIVITY TO `SIGN UP ACTIVITY` `true`

    }

    private fun login() {

        try {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                .navigate(R.id.action_googleSignInFragment_to_loginFragment)
        } catch (e: Exception) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, LoginFragment())
                .addToBackStack("Login")
                .commit()
        }
        //CHANGE ACTIVITY TO `LOGIN ACTIVITY` `true`

    }
}
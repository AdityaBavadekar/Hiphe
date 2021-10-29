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

package com.adityaamolbavadekar.hiphe.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.adityaamolbavadekar.hiphe.LauncherActivity
import com.adityaamolbavadekar.hiphe.MainActivity
import com.adityaamolbavadekar.hiphe.R
import com.adityaamolbavadekar.hiphe.interaction.HipheInfoLog
import com.adityaamolbavadekar.hiphe.utils.constants
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AccountFragment : Fragment() {

    private lateinit var emailIdTextView: TextView
    private lateinit var userIdTextView: TextView
    private lateinit var userNameTextView: TextView
    private lateinit var userNameTextView2: TextView
    private lateinit var phoneTextView: TextView
    private lateinit var createdDateTextView: TextView
    private lateinit var deepLinkTextView: TextView
    private lateinit var userIsEmailIdVerifiedTextView: TextView
    private lateinit var userPhotoImageView: ImageView
    private lateinit var userIsEmailIdVerifiedImageView: ImageButton
    private lateinit var logoutButton: MaterialButton
    private lateinit var editButton: MaterialButton

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.account_info_page_fragment, container, false)
        auth = Firebase.auth
        val user = auth.currentUser

        emailIdTextView = root.findViewById(R.id.emailAddressTextView)
        userIdTextView = root.findViewById(R.id.userIdTextView)
        userNameTextView = root.findViewById(R.id.userNameTextView)
        userNameTextView2 = root.findViewById(R.id.userNameTextView2)
        createdDateTextView = root.findViewById(R.id.creationDateTextView)
        phoneTextView = root.findViewById(R.id.phoneTextView)
        deepLinkTextView = root.findViewById(R.id.deepLinkTextView)
        userPhotoImageView = root.findViewById(R.id.imageView2)
        userIsEmailIdVerifiedImageView = root.findViewById(R.id.isemailverified)
        userIsEmailIdVerifiedTextView = root.findViewById(R.id.isemailverifiedt)
        logoutButton = root.findViewById(R.id.logout_button)

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(getString(R.string.default_web_client_id))
            .build()
        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
//        userIsEmailIdVerifiedImageView = root.findViewById(R.id.isemailverified)

        logoutButton.setOnClickListener {
            auth.signOut()
            try {
                googleSignInClient.revokeAccess()
                auth.signOut()

            } catch (e: Exception) {
            }
            PreferenceManager.getDefaultSharedPreferences(requireContext()).edit {
                putBoolean(constants.checkIsLoggedInPrefKey, false)
            }
            HipheInfoLog(
                MainActivity.TAG,
                "User is logging out without remembrance, googleSignInClient.revokeAccess() auth.signOut()"
            )
            startActivity(Intent(requireActivity(), LauncherActivity::class.java))
            requireActivity().finish()
        }

        val emailId = user?.email.toString()
        val userId = user?.uid
        val userName = user?.displayName
        val userPhotoURL = user?.photoUrl
        val emailIdVerified = user?.isEmailVerified
        val phoneNumber = user?.phoneNumber

        val deepLink = getString(
            R.string.to_share_your_profile_with_other_hiphe_users_use_this_link_nhttps_hiphe_page_link_user_1_s,
            emailId.removeSuffix("gmail.com")
        )
        deepLinkTextView.text = deepLink
        userNameTextView.text = userName
        userNameTextView2.text =
            getString(R.string.user_name_formatted, userName)//"USER NAME : $userName"
        userIdTextView.text = getString(R.string.user_id_formatted, userId) //"USER ID : $userId"
        emailIdTextView.text =
            getString(R.string.email_address_formatted, emailId)//"EMAIL ADDRESS : $emailId"
        val theAccountCreationTimestamp = user?.metadata?.creationTimestamp.toString()
        val theAccountLastSignInTimestamp = user?.metadata?.lastSignInTimestamp.toString()
        createdDateTextView.text =
            getString(R.string.created_on_formatted, theAccountCreationTimestamp)

        if (userPhotoURL != null) {
            Glide.with(requireActivity())
                .load(userPhotoURL)
                .into(userPhotoImageView)
        }

        if (phoneNumber != null) {
            phoneTextView.text = phoneNumber
            phoneTextView.visibility = View.VISIBLE
        }

        if (emailIdVerified == true) {
//            userIsEmailIdVerifiedImageView.visibility = View.VISIBLE
            userIsEmailIdVerifiedTextView.visibility = View.VISIBLE
            userIsEmailIdVerifiedTextView.setOnClickListener {
                val b = MaterialAlertDialogBuilder(requireActivity())
                b.setTitle("iVR Verification truth")
                b.setMessage("This is iVR (isVerified) check mark, it helps users denote if the profile or user is verified. You can this this on your profile only if you have Verified your Email Address. This helps maintaining Privacy and Security.")
                b.setPositiveButton("OK, GOT IT") { ba, _ ->
                    ba.dismiss()
                }
                b.create()
                b.show()
            }
        }

        return root
    }


}
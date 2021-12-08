package com.example.mynailproject.signin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.mynailproject.BasicActivity
import com.example.mynailproject.R
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignupFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    val bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity: BasicActivity = activity as BasicActivity
        activity.supportActionBar?.title = "Регистрация"

        auth = Firebase.auth

        val signup : Button = view.findViewById(R.id.button_signup)
        signup.setOnClickListener {
            val password : TextView = view.findViewById(R.id.password)
            if (password.text.toString().length < 6){
                Toast.makeText(
                    this.context, "Пароль должен содержать не менее 6 символов",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else{
                val login : TextView = view.findViewById(R.id.email)
                createAccount(login.text.toString(), password.text.toString(), view)
            }

        }

    }


    private fun createAccount(email: String, password: String, view: View) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "createUserWithEmail:success")
                    Toast.makeText(
                        this.context, "Вы успешно зарегистрированы",
                        Toast.LENGTH_SHORT
                    ).show()
                    //sendSignInLink(email, buildActionCodeSettings())
                    val user = auth.currentUser
                    updateUI(user, view)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.d("TAG", "Fail", task.exception)
                    Toast.makeText(
                        this.context, "Ошибка регистрации. Попробуйте снова",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null, view)
                }
            }
    }

    fun updateUI(user: FirebaseUser?, view: View){
        if (user==null){
            Log.d("Update", "User null")
            view.findNavController().navigate(R.id.action_global_signupFragment)
        }
        else{
            Log.d("Update", "user")
            //val dialog = VerifyEmaillDialogFragment()
            val activity = view.context as BasicActivity
            val manager = activity.supportFragmentManager
            //dialog.show(manager, "info")
            view.findNavController().navigate(R.id.action_global_infoSignupFragment)
        }
    }

    private fun buildActionCodeSettings(): ActionCodeSettings {
        val actionCodeSettings = actionCodeSettings {
            // URL you want to redirect back to. The domain (www.example.com) for this
            // URL must be whitelisted in the Firebase Console.
            url = "https://mynailproject.page.link/TG78"
            // This must be true
            handleCodeInApp = true
            setAndroidPackageName(
                "com.example.mynailproject",
                true, /* installIfNotAvailable */
                "30" /* minimumVersion */
            )
        }
        return actionCodeSettings
    }

    private fun sendSignInLink(email: String, actionCodeSettings: ActionCodeSettings) {
        Log.d("TAG", "Email")
        Firebase.auth.sendSignInLinkToEmail(email, actionCodeSettings)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "Email sent.")
                } else{
                    Log.d("TAG", "Email don't sent.", task.exception)
                }
            }
    }


}
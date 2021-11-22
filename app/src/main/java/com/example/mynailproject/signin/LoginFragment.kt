package com.example.mynailproject.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.mynailproject.R
import com.example.mynailproject.database.DBCall
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    private lateinit var v:View


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        auth = Firebase.auth
        val currentUser = auth.currentUser
        Log.d("CurrentUser", currentUser.toString())

        //Раскомментировать для сохранения того, что пользователь вошёл в систему при запуске приложения!!!!!!!!!!!!!!!!!!!!!!
        if(currentUser != null){
            reload(currentUser)
        }

        // Регистрация
        val to_signup: Button = view.findViewById(R.id.button_to_signup_frame)
        to_signup.setOnClickListener {
            view.findNavController().navigate(R.id.action_global_signupFragment)
        }

        //Вход через почту и пароль
        val login : Button = view.findViewById(R.id.button_login)
        login.setOnClickListener {
            val password : TextView = view.findViewById(R.id.password)
            if (password.text.toString().length < 6){
                Toast.makeText(this.context, "Пароль должен содержать не менее 6 символов",
                    Toast.LENGTH_SHORT).show()
            }
            else{
                val email : TextView = view.findViewById(R.id.email)
                signIn(email.text.toString(), password.text.toString())
            }
        }

        //Вход через Google
        val google_login : Button = view.findViewById(R.id.button_login_google)
        google_login.setOnClickListener {
            val gso = GoogleSignInOptions
                    .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
            googleSignInClient = GoogleSignIn.getClient(view.context, gso)

            val signInIntent = googleSignInClient.signInIntent
            Log.d("Intent", signInIntent.toString())
            startActivityForResult(signInIntent, 9001)
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user, true)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.d("TAG", "signInWithEmail:failure", task.exception)
                    Toast.makeText(this.context, "Ошибка входа",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null, true)
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //if (requestCode == 9001) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e)
            }
        //}
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("GOOGLE_SIGN_IN", "signInWithCredential:success")
                val user = auth.currentUser
                updateUI(user, false)
            } else {
                // If sign in fails, display a message to the user.
                Log.w("GOOGLE_SIGN_IN", "signInWithCredential:failure", task.exception)
                updateUI(null, false)
            }
        }
    }

    fun updateUI(user:FirebaseUser?, full: Boolean){
        if (user==null){
            Log.d("Update", "User null")
            Toast.makeText(this.context, "Неверный логин или пароль",
                Toast.LENGTH_SHORT).show()
        }
        else{
            Log.d("Update", user.isEmailVerified().toString())
            // Верификация почты
            //if (user.isEmailVerified()){
                Toast.makeText(this.context, "Вы успешно вошли в систему",
                    Toast.LENGTH_SHORT).show()
                if (full) {
                    v.findNavController().navigate(R.id.action_global_myOfficeFragment)
                }
                else{
                    v.findNavController().navigate(R.id.action_global_infoSignupFragment)
                }

            // Верификация почты
            /*}
            else{
                val dialog = VerifyEmaillDialogFragment()
                val activity = view.context as BasicActivity
                val manager = activity.supportFragmentManager
                dialog.show(manager, "info")
            }*/
        }
    }

    fun reload(user: FirebaseUser?){
        Log.d("Reload", "Aaaa")
        v.findNavController().navigate(R.id.action_global_myOfficeFragment)
    }
}
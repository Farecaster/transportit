package com.example.transportit

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.transportit.databinding.FragmentRegistrationBinding

class registrationFragment : Fragment() {
    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        var helper = context?.let { MyHelper(it) }
        var db = helper?.readableDatabase
        val rs = db?.rawQuery("SELECT * FROM USER",null)
        binding.apply {
            btnReg.setOnClickListener {
                val username = etRegUsernaame.text.toString()
                val email = etRegEmail.text.toString()
                val pass = etRegPass.text.toString()
                val rePass = etRegRepeatPass.text.toString()

                if (username.isNotEmpty() &&  (email.isEmpty() || isEmailValid(email)) && pass.isNotEmpty() && pass == rePass) {
                    // Check if the username or email already exists
                    if (!userExists(username, email, db)) {
                        // If not, insert the new user
                        val cv = ContentValues()
                        cv.put("USERNAME", username)
                        cv.put("EMAIL", email)
                        cv.put("PASSWORD", pass)
                        db?.insert("USER", null, cv)
                        Toast.makeText(context, "Successful Registration", Toast.LENGTH_SHORT).show()
                        requireActivity().onBackPressed()
                    } else {
                        Toast.makeText(context, "Username or Email already exists", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Unsuccessful registration", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun userExists(username: String, email: String, db: SQLiteDatabase?): Boolean {
        val query = "SELECT * FROM USER WHERE USERNAME = ? OR EMAIL = ?"
        val cursor = db?.rawQuery(query, arrayOf(username, email))
        val exists = (cursor?.count ?: 0) > 0
        cursor?.close()
        return exists
    }
    private fun isEmailValid(email: String): Boolean {
        // Implement your email validation logic here, e.g., using regular expressions
        // For a basic check, you can use a simple pattern like this:
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }
}




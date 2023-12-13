package com.example.transportit

import android.database.Cursor
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.transportit.databinding.FragmentLoginBinding


class loginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        val helper = context?.let { MyHelper(it) }
        val db = helper?.readableDatabase
        binding.btnLogin.setOnClickListener {
            val username = binding.etEmail.text.toString()
            val pass = binding.etPass.text.toString()
            val columns = arrayOf("_id", "USERNAME", "EMAIL")
            val selection = "USERNAME = ? AND PASSWORD = ?"
            val selectionArgs = arrayOf(username, pass)
            var rs: Cursor? = null // Declare rs outside of the try block

            try {
                rs = db?.query("USER", columns, selection, selectionArgs, null, null, null)


                binding.btnLogin.isEnabled = false

                if (rs != null && rs.moveToFirst()) {
                    // Successful login
                    val userIdColumnIndex = rs.getColumnIndex("_id")
                    val userId = if (userIdColumnIndex != -1) rs.getLong(userIdColumnIndex) else -1
                    val userID = userId.toString()
                    val usernameColumnIndex = rs.getColumnIndex("USERNAME")
                    val username = if (usernameColumnIndex != -1) rs.getString(usernameColumnIndex) else ""
                    val action = loginFragmentDirections.actionLoginFragmentToHomeFragment(userID, username)
                    findNavController().navigate(action)

                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                } else {
                    // Failed login
                    Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "An error occurred during login", Toast.LENGTH_SHORT).show()
            } finally {
                rs?.close() // Close the cursor in the finally block
                binding.btnLogin.isEnabled = true
            }
        }

        binding.btnRegg.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_registrationFragment)
        }
    }


}
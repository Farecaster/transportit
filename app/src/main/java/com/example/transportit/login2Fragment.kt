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
import com.example.transportit.databinding.FragmentLogin2Binding
import com.google.firebase.auth.FirebaseAuth

class login2Fragment : Fragment() {
    private lateinit var binding: FragmentLogin2Binding
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogin2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        navController = Navigation.findNavController(view)

        val helper = context?.let { MyHelper(it) }
        val db = helper?.readableDatabase
        val columns = arrayOf("_id", "USERNAME", "EMAIL")
        val selection = "USERNAME = ? AND PASSWORD = ?"

        binding.btnLogin2.setOnClickListener {
            val username = binding.etEmail2.text.toString()
            val password = binding.Etpass2.text.toString()
            val selectionArgs = arrayOf(username, password)
            var rs: Cursor? = null
            try {
                val rs = db?.query("USER", columns, selection, selectionArgs, null, null, null)


                binding.btnLogin2.isEnabled = false

                if (rs != null && rs.moveToFirst()) {
                    // Successful login
                    val userIdColumnIndex = rs.getColumnIndex("_id")
                    val userId = if (userIdColumnIndex != -1) rs.getLong(userIdColumnIndex) else -1
                    val userID = userId.toString()
                    val action = login2FragmentDirections.actionLogin2FragmentToSellerFragment(userID)
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
                rs?.close()
                binding.btnLogin2.isEnabled = true
            }
        }

        binding.btnGotoRegister.setOnClickListener {
            navController.navigate(R.id.action_login2Fragment_to_registrationFragment)
        }
    }

}
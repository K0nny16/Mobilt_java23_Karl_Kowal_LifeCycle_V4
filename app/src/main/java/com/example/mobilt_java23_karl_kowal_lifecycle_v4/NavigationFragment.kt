package com.example.mobilt_java23_karl_kowal_lifecycle_v4

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class NavigationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflaterar layouten för detta fragment
        return inflater.inflate(R.layout.fragment_navigation, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Hitta knapparna och sätt klickhanterare
        view.findViewById<Button>(R.id.button_loggout)?.setOnClickListener {
            navigateTo(MainActivity::class.java)
        }
        view.findViewById<Button>(R.id.button_form)?.setOnClickListener {
            navigateTo(FormActivity::class.java)
        }
        view.findViewById<Button>(R.id.button_register)?.setOnClickListener {
            navigateTo(RegisterActivity::class.java)
        }
        view.findViewById<Button>(R.id.button_user_data)?.setOnClickListener {
            navigateTo(UserDataActivity::class.java)
        }
    }
    private fun navigateTo(targetActivity: Class<*>) {
        val intent = Intent(activity, targetActivity).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(intent)
    }
}


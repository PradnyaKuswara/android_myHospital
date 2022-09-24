package com.example.ugd3_kelompok15.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ugd3_kelompok15.HomeActivity
import com.example.ugd3_kelompok15.R
import com.example.ugd3_kelompok15.databinding.FragmentEditProfilBinding
import com.example.ugd3_kelompok15.databinding.FragmentProfileBinding
import com.example.ugd3_kelompok15.room.User
import com.example.ugd3_kelompok15.room.UserDB
import com.example.ugd3_kelompok15.ui.home.FragmentHome
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_edit_profil.*
import kotlinx.android.synthetic.main.fragment_edit_profil.tietEmail
import kotlinx.android.synthetic.main.fragment_edit_profil.tietUsername


class FragmentEditProfil : Fragment() {

    private var _binding: FragmentEditProfilBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfilBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var check = true

        binding.btnUpdate.setOnClickListener {
            if(binding.tietnamalengkap.text.toString().isEmpty()) {
                binding.tietnamalengkap.setError("Kosong")
                check =false
            }
            if (binding.tietEmail.text.toString().isEmpty()){
                binding.tietEmail.setError("Kosong")
                check =false
            }
            if(binding.tietNoTelp.text.toString().isEmpty()) {
                binding.tietNoTelp.setError("Kosong")
                check =false
            }
            if(binding.tietUsername.text.toString().isEmpty()) {
                binding.tietUsername.setError("Kosong")
                check =false
            }
            if(!check) {
                check = true
                return@setOnClickListener
            }else {
                updateData()
                transitionFragment(FragmentProfile())
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun updateData() {
        val sharedPreferences = (activity as HomeActivity).getSharedPreferences()

        val db by lazy { UserDB(activity as HomeActivity) }
        val userDao = db.userDao()

        val id = sharedPreferences.getInt("id", 0)

        val getUser = userDao.getUser(id)

        val user = User(id,
            binding.tietnamalengkap.text.toString(),
            binding.tietUsername.text.toString(),
            binding.tietEmail.text.toString(),
            binding.tietNoTelp.text.toString(),
            getUser.password
        )
        userDao.updateUser(user)
    }

    private fun transitionFragment(fragment: Fragment) {
        val transition = requireActivity().supportFragmentManager.beginTransaction()
        transition.replace(R.id.layout_fragment_profil, fragment)
            .addToBackStack(null).commit()
        transition.hide(FragmentEditProfil())
    }

}
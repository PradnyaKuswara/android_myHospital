package com.example.ugd3_kelompok15.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ugd3_kelompok15.R
import com.example.ugd3_kelompok15.databinding.FragmentProfileBinding
import com.example.ugd3_kelompok15.room.UserDB
import com.example.ugd3_kelompok15.ui.home.FragmentHome
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FragmentProfile() : Fragment() {
    // TODO: Rename and change types of parameters

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    val db by lazy { UserDB(requireActivity()) }
    private var userId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
        // Inflate the layout for this fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()

        binding.btnUpdate.setOnClickListener {
            transitionFragment(FragmentEditProfil())
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun setData() {
        userId = 1
        CoroutineScope(Dispatchers.IO).launch {
            val users = db.userDao().getUser(userId)[0]

        }

    }

    private fun transitionFragment(fragment: Fragment) {
        val transition = requireActivity().supportFragmentManager.beginTransaction()
        transition.replace(R.id.layout_fragment_profil, fragment)
            .addToBackStack(null).commit()
        transition.hide(FragmentHome())
    }

}
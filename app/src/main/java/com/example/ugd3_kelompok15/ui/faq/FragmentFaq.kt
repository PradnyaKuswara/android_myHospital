package com.example.ugd3_kelompok15.ui.faq

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ugd3_kelompok15.LoginActivity
import com.example.ugd3_kelompok15.databinding.FragmentEditProfilBinding
import com.example.ugd3_kelompok15.databinding.FragmentFaqBinding
import com.example.ugd3_kelompok15.entity.Question

class FragmentFaq : Fragment() {

    private var _binding: FragmentFaqBinding? = null
    private val binding get() = _binding!!
    lateinit var questionAdapter: RVQuestionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFaqBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        questionAdapter = RVQuestionAdapter(Question.listofQuestion, object: RVQuestionAdapter.OnAdapterListener {
            override fun onClick(question: Question) {
                startActivity(Intent(context, DetailQuestionActivity::class.java)
                    .putExtra("intent_title", question.title)
                    .putExtra("intent_main", question.main)

                )
            }

        })
        binding.layoutfaq.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = questionAdapter
        }
    }
}
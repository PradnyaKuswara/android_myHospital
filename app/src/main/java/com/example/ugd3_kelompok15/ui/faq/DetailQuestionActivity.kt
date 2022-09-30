package com.example.ugd3_kelompok15.ui.faq

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ugd3_kelompok15.R
import com.example.ugd3_kelompok15.databinding.ActivityDetailsQuestionBinding

class DetailQuestionActivity : AppCompatActivity() {
    private var _binding: ActivityDetailsQuestionBinding? = null
    private val binding get() = _binding!!
    lateinit var questionAdapter: RVQuestionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailsQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.tvTitleQuestion.setText(intent.getStringExtra("intent_title"))
        binding.tvMainQuestion.setText(intent.getStringExtra("intent_main"))
    }
}
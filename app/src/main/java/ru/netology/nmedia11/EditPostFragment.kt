package ru.netology.nmedia11

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

//import ru.netology.nmedia11.databinding.ActivityEditPostBinding
import ru.netology.nmedia11.databinding.FragmentEditPostBinding

class EditPostFragment : Fragment() {

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val binding = ActivityEditPostBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        binding.edit.setText(intent.getStringExtra("postContent"))
//        binding.edit.requestFocus()
//
//        binding.ok.setOnClickListener {
//            val intent = Intent()
//            if (binding.edit.text.isNullOrBlank()) {
//                setResult(Activity.RESULT_CANCELED, intent)
//            } else {
//                val content = binding.edit.text.toString()
//                intent.putExtra("postContent", content)
//                setResult(Activity.RESULT_OK, intent)
//            }
//            finish()
//        }
//    }

    companion object {
        var Bundle.textArg: String? by StringArg
    }

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentEditPostBinding = FragmentEditPostBinding.inflate(
            inflater,
            container,
            false
        )

//        binding.edit.setText(intent.getStringExtra("postContent"))
        //arguments?.textArg?.let(binding.edit::setText)
        binding.edit.setText(arguments?.textArg.toString())

        binding.edit.requestFocus()

        binding.ok.setOnClickListener {
//            val intent = Intent()
//            if (binding.edit.text.isNullOrBlank()) {
//                activity?.setResult(Activity.RESULT_CANCELED, intent)
//            } else {
//                val content = binding.edit.text.toString()
//                intent.putExtra("postContent", content)
//                activity?.setResult(Activity.RESULT_OK, intent)
//            }
            viewModel.changeContent(binding.edit.text.toString())
            viewModel.save()
            findNavController().navigateUp()
//            finish()
        }
        //return super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

}

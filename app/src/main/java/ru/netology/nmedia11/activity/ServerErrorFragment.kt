package ru.netology.nmedia11.activity

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia11.PostViewModel
import ru.netology.nmedia11.R
import ru.netology.nmedia11.databinding.FragmentServerErrorBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
///private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ServerErrorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ServerErrorFragment : Fragment() {
    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentServerErrorBinding = FragmentServerErrorBinding.inflate(
            inflater, container, false
        )
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
        val bundle = Bundle()
        var choice = true
        binding.buttonCancel.setOnClickListener {
            choice = false
//            viewModel.errorHappened.value = false
            bundle.putBoolean("ChoiceArg", choice)
            findNavController().navigate(R.id.action_serverErrorFragment_to_feedFragment, bundle)

        }
        binding.buttonRetry.setOnClickListener {
            choice = true
//            viewModel.errorHappened.value = false
            bundle.putBoolean("ChoiceArg", choice)
            findNavController().navigate(R.id.action_serverErrorFragment_to_feedFragment, bundle)
        }

//        return inflater.inflate(R.layout.fragment_server_error, container, false)
        return binding.root
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment ServerErrorFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance() = ServerErrorFragment()
//    }
}
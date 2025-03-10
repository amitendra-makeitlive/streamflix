package com.tanasi.streamflix.fragments.people

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.tanasi.streamflix.adapters.AppAdapter
import com.tanasi.streamflix.databinding.FragmentPeopleBinding
import com.tanasi.streamflix.models.People
import com.tanasi.streamflix.utils.viewModelsFactory

class PeopleFragment : Fragment() {

    private var _binding: FragmentPeopleBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<PeopleFragmentArgs>()
    private val viewModel by viewModelsFactory { PeopleViewModel(args.id) }

    private val appAdapter = AppAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPeopleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializePeople()

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                PeopleViewModel.State.Loading -> binding.isLoading.root.visibility = View.VISIBLE
                is PeopleViewModel.State.SuccessLoading -> {
                    displayPeople(state.people)
                    binding.isLoading.root.visibility = View.GONE
                }
                is PeopleViewModel.State.FailedLoading -> {
                    Toast.makeText(
                        requireContext(),
                        state.error.message ?: "",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun initializePeople() {
        binding.vgvPeople.apply {
            adapter = appAdapter
            setItemSpacing(80)
        }
    }

    private fun displayPeople(people: People) {
        appAdapter.items.apply {
            clear()
            add(people.apply { itemType = AppAdapter.Type.PEOPLE })
        }
        appAdapter.notifyDataSetChanged()
    }
}
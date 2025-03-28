package com.example.framereality.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.framereality.PropertyModel
import com.example.framereality.adapter.PropertyAdapter
import com.example.framereality.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var propertyList: ArrayList<PropertyModel>
    private lateinit var propertyAdapter: PropertyAdapter
    private lateinit var propertiesRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Initialize Firebase Database reference for "Properties" node
        propertiesRef = FirebaseDatabase.getInstance().getReference("Properties")
        propertyList = ArrayList()

        // Set up RecyclerView
        binding.propertyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        propertyAdapter = PropertyAdapter(requireContext(), propertyList) { property ->
            // Handle favorite button click
            Toast.makeText(requireContext(), "${property.title} added to favorites", Toast.LENGTH_SHORT).show()
        }
        binding.propertyRecyclerView.adapter = propertyAdapter

        fetchProperties()
    }

    private fun fetchProperties() {
        propertiesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                propertyList.clear()
                for (propertySnapshot in snapshot.children) {
                    val property = propertySnapshot.getValue(PropertyModel::class.java)
                    if (property != null) {
                        // Extract image URLs from the child node "Images" (if available)
                        val imageUrls = ArrayList<String>()
                        val imagesSnapshot = propertySnapshot.child("Images")
                        for (imageChild in imagesSnapshot.children) {
                            val imageUrl = imageChild.child("imageUrl").getValue(String::class.java)
                            if (!imageUrl.isNullOrEmpty()) {
                                imageUrls.add(imageUrl)
                            }
                        }
                        // Create a new model with the imageUrls list
                        val propertyWithImages = property.copy(imageUrls = imageUrls)
                        propertyList.add(propertyWithImages)
                    }
                }
                propertyAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load properties: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

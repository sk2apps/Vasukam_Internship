package com.saggy.vasukaminternship.presentation.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.saggy.vasukaminternship.data.model.post.Post
import com.saggy.vasukaminternship.databinding.FragmentUserPostBinding
import com.saggy.vasukaminternship.presentation.Adapter.HomePostAdapter
import com.saggy.vasukaminternship.utils.displayToast
import com.saggy.vasukaminternship.utils.invisible
import com.saggy.vasukaminternship.utils.show


class UserPostFragment : Fragment() {

    private lateinit var _binding: FragmentUserPostBinding
    val binding
        get() = _binding

    private lateinit var homePostAdapter: HomePostAdapter
    private lateinit var posts: ArrayList<Post>

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        posts = arrayListOf()

        if (firebaseAuth.currentUser != null) {
            readFireStoreData()
        }

    }

    //    .collection("Posts")
//    .where("creatorUid", "==", "this")
//
    private fun readFireStoreData() {
        val db = FirebaseFirestore.getInstance()

        db.collection("Posts")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val document: QuerySnapshot = it.result
                    document.forEach { data ->

                        val post: Post = data.toObject(Post::class.java)
                        if (post.CreatorUid == firebaseAuth.currentUser!!.uid) {
                            posts.add(post)
                        }


                    }
                    homePostAdapter = HomePostAdapter(posts)
                    binding.postRecView.apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = homePostAdapter
                        setHasFixedSize(false)
                    }
                    binding.ProgressBar.invisible()
                    binding.postRecView.show()
                }
            }
            .addOnFailureListener {
                context!!.displayToast(it.message)
            }

    }

}
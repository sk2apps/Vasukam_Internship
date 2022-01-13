package com.saggy.vasukaminternship.presentation.activity.profile

import android.content.Intent
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.saggy.vasukaminternship.R
import com.saggy.vasukaminternship.data.model.user.ProfileInfo
import com.saggy.vasukaminternship.databinding.ActivityProfileBinding
import com.saggy.vasukaminternship.presentation.fragments.profile.UserPostFragment
import com.saggy.vasukaminternship.utils.displayToast

class ProfileActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityProfileBinding
    val binding get() = _binding

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val blurEffect = RenderEffect.createBlurEffect(50.0F, 50.0F, Shader.TileMode.MIRROR)
            binding.containerProfileSide.setRenderEffect(blurEffect)
        }

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        setupViewPager()
        binding.tabTablayout.setupWithViewPager(binding.tabViewpager)
        setupTabIcons()

        binding.editProfile.setOnClickListener {
            startActivity(Intent(this, ProfileEditActivity::class.java))
        }

        binding.editProfileC.setOnClickListener {
            startActivity(Intent(this, ProfileEditActivity::class.java))
        }

    }

    private fun checkUser() {
        val user = firebaseAuth.currentUser
        if (user == null) {
            this.displayToast("User is Null")
        } else {
            loadMyInfo(user)
        }

    }

    private fun loadMyInfo(user: FirebaseUser) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Users")
            .document(user.uid)
            .get()
            .addOnSuccessListener {
                val user: ProfileInfo? = it.toObject(ProfileInfo::class.java)
                if (user != null) {
                    Glide.with(this).load(user.profile_pic).error(R.drawable.person_user).into(binding.profilePic)
                    binding.name.text = user.name
                    binding.nameC.text = user.name
                    binding.username.text = user.username
                    binding.usernameC.text = user.username
                    binding.follower.text = user.followers.toString()
                    binding.following.text = user.following.toString()
                    binding.totalPost.text = user.total_posts.toString()
                    binding.totalPostC.text = user.total_posts.toString()
                    binding.desc.text = user.description
                    binding.descC.text = user.description


                }
            }
            .addOnFailureListener {
                this.displayToast(it.message)
            }
    }

    private fun setupViewPager() {
        var adapter: ViewPagerAdapter = ViewPagerAdapter(supportFragmentManager)


        adapter.addFragment(UserPostFragment(), "")
        adapter.addFragment(UserPostFragment(), "")
        adapter.addFragment(UserPostFragment(), "")
        adapter.addFragment(UserPostFragment(), "")

        // setting adapter to view pager.
        binding.tabViewpager.adapter = adapter

    }

    private fun setupTabIcons() {
        binding.tabTablayout.getTabAt(0)?.setIcon(R.drawable.ic_cardholder_dark)
        binding.tabTablayout.getTabAt(1)?.setIcon(R.drawable.ic_light)
        binding.tabTablayout.getTabAt(2)?.setIcon(R.drawable.live_outline_figma)
        binding.tabTablayout.getTabAt(3)?.setIcon(R.drawable.store_outline_figma)

    }

    // This "ViewPagerAdapter" class overrides functions which are
    // necessary to get information about which item is selected
    // by user, what is title for selected item and so on.*/
    class ViewPagerAdapter// this is a secondary constructor of ViewPagerAdapter class.
        (supportFragmentManager: FragmentManager) : FragmentPagerAdapter(supportFragmentManager) {

        // objects of arraylist. One is of Fragment type and
        // another one is of String type.*/
        private var fragmentList1: ArrayList<Fragment> = ArrayList()
        private var fragmentTitleList1: ArrayList<String> = ArrayList()

        // returns which item is selected from arraylist of fragments.
        override fun getItem(position: Int): Fragment {
            return fragmentList1.get(position)
        }

        // returns which item is selected from arraylist of titles.
        @Nullable
        override fun getPageTitle(position: Int): CharSequence {
            return fragmentTitleList1.get(position)
        }

        // returns the number of items present in arraylist.
        override fun getCount(): Int {
            return fragmentList1.size
        }

        // this function adds the fragment and title in 2 separate  arraylist.
        fun addFragment(fragment: Fragment, title: String) {
            fragmentList1.add(fragment)
            fragmentTitleList1.add(title)
        }
    }

//
//    private fun loadMyInfo(user: Fires) {
//
//        val db = FirebaseFirestore.getInstance()
//        db.collection("Users")
//            .document(userUid)
//
//
//        val ref = FirebaseDatabase.getInstance().getReference("Users")
//        ref.orderByChild("uid").equalTo(firebaseAuth.uid)
//            .addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    for (ds in snapshot.children) {
//                        binding.name.text = ds.child("name").value.toString()
//                        binding.nameC.text = ds.child("name").value.toString()
//                        binding.username.text = ds.child("username").value.toString()
//                        binding.usernameC.text = ds.child("username").value.toString()
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {}
//            })
//    }
}
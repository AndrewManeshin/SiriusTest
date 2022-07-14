package com.sirius.test_app

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.WindowManager
import androidx.core.view.WindowCompat
import coil.load
import coil.transform.CircleCropTransformation
import com.sirius.test_app.databinding.ActivityMainBinding
import com.sirius.test_app.databinding.ReviewItemBinding
import com.sirius.test_app.databinding.TagItemBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.gameImage.load("https://i.ibb.co/g3YVWD2/img-background.png")
        binding.logoImageView.load("https://i.ibb.co/Pjf2B69/img-logo.png")

        binding.ratingBar1.rating = 4.9f

        val dataModel = DataModel()

        showBaseInfo(dataModel)
        showReviews(dataModel.reviews)
        showAction(dataModel.action)


        if (Build.VERSION.SDK_INT in 21..29) {
            window.statusBarColor = Color.TRANSPARENT
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.decorView.systemUiVisibility =
                SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_STABLE

        } else if (Build.VERSION.SDK_INT >= 30) {
            window.statusBarColor = Color.TRANSPARENT
            // Making status bar overlaps with the activity
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }
    }

    private fun showBaseInfo(dataModel: DataModel) {
        with(binding) {
            gameImage.load(dataModel.image)
            logoImageView.load(dataModel.logo)
            nameTextView.text = dataModel.name
            ratingBar1.rating = dataModel.rating
            ratingBar2.rating = dataModel.rating
            descriptionTextView.text = dataModel.description
            ratingTextView.text = dataModel.rating.toString()
            gradeCntTextView.text = dataModel.gradeCnt
        }
        binding.gradeCntReviewsTextView.text =
            this.getString(R.string.number_of_reviews, dataModel.gradeCnt)
        dataModel.tags.forEach { tag ->
            val tagBinding = TagItemBinding.inflate(layoutInflater)
            tagBinding.tag.text = tag
            binding.tagsLinearLayout.addView(tagBinding.root)

        }
    }

    private fun showReviews(reviews: List<ReviewModel>) {
        reviews.forEach { reviewModel ->
            val bindingReviewItem = ReviewItemBinding.inflate(layoutInflater)
            with(bindingReviewItem) {
                userNameTextView.text = reviewModel.userName
                userImageView.load(reviewModel.userImage) {
                    transformations(CircleCropTransformation())
                    placeholder(R.drawable.ic_baseline_account_circle_24)
                    listener(onError = { _, _ ->
                        userImageView.load(R.drawable.ic_baseline_account_circle_24)
                    })
                }
                dateTextView.text = reviewModel.date
                messageTextView.text = reviewModel.message
                binding.reviewsLinearLayout.addView(bindingReviewItem.root)
            }
        }
    }

    private fun showAction(actionModel: ActionModel) {
        binding.installButton.text = actionModel.name
    }
}
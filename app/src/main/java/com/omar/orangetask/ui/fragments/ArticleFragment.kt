package com.omar.orangetask.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.omar.orangetask.R
import com.omar.orangetask.ui.NewsActivity
import com.omar.orangetask.ui.NewsViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_article.*
import kotlinx.android.synthetic.main.item_article_preview.view.ivArticleImage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ArticleFragment : Fragment(R.layout.fragment_article) {

    lateinit var viewModel: NewsViewModel
    val args: ArticleFragmentArgs by navArgs()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        val article = args.article
        val parsedDate = LocalDateTime.parse(article.publishedAt, DateTimeFormatter.ISO_DATE_TIME)
        val formattedDate = parsedDate.format(DateTimeFormatter.ofPattern("MMM dd,yyyy  h:mm a"))
        textAuthor.setText(article.author)
        textTitle.setText(article.title)
        textDate.setText(formattedDate.format(parsedDate))
        article.urlToImage?.let {
            Glide.with(this).load(it).into(imageView)
        } ?: run {
            imageView.setImageResource(R.drawable.noimage)
        }
        textDesc.setText(article.description)
        floatingWebBrowser.setOnClickListener {


        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
        startActivity(browserIntent)
        }

        fabsave.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view, "Artical saved Successful", Snackbar.LENGTH_SHORT).show()


        }




    }
}
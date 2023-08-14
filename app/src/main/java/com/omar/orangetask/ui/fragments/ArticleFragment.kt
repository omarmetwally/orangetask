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
        val formattedDate = parsedDate.format(DateTimeFormatter.ofPattern("MMM dd,yyyy  H:mm a"))
        textAuthor.setText(article.author)
        textDate.setText(formattedDate.format(parsedDate))
        Glide.with(this).load(article.urlToImage).into(imageView)
        textDesc.setText(article.description)
//        webView.apply {
//            webViewClient = WebViewClient()
//            article.url?.let { loadUrl(it) }
//        }

        fab.setOnClickListener {
           //viewModel.saveArticle(article)
            Snackbar.make(view, article.description+"", Snackbar.LENGTH_SHORT).show()
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
            startActivity(browserIntent)

        }




    }
}
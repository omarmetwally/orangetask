package com.omar.orangetask.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.omar.orangetask.R
import com.omar.orangetask.db.ArticleDatabase
import com.omar.orangetask.repository.NewsRepository
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.fragment_article.fab
import java.util.Locale

class NewsActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel
    var flag=true

    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application, newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)
        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())



       if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darkTheme)
        } else {
            setTheme(R.style.AppTheme)
        }

        flag = isLightMode()
        if (flag) {
            switchtheme.setImageResource(R.drawable.thumbtrue)

        } else {
            switchtheme.setImageResource(R.drawable.thumbfalse)
        }




        switchtheme.setOnClickListener {
            setLocale(this, "ar")

              recreate()




            /* if (flag) {
                 switchtheme.setImageResource(R.drawable.thumbfalse)
                 AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                 setTheme(R.style.darkTheme)
                 flag = false
                 saveFlagState(false)
             } else {
                 switchtheme.setImageResource(R.drawable.thumbtrue)
                 AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                 setTheme(R.style.AppTheme)
                 flag = true
                 saveFlagState(true)
             }*/

        }

     /*   switchtheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                setTheme(R.style.darkTheme)

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                setTheme(R.style.AppTheme)
            }


        }*/


    }


    override fun onResume() {
        super.onResume()
/*
        //  force enabling night mode
       // delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES

        // force disabling night mode
        // delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO

*/

        // Night mode following system settings
      // delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        print("")
//        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
//            setTheme(R.style.darkTheme)
//        } else {
//            setTheme(R.style.AppTheme)
//        }





    }


    private fun saveFlagState(isLightMode: Boolean) {
        val preferences = getSharedPreferences("theme_prefs", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean("is_light_mode", isLightMode)
        editor.apply()
    }

    private fun isLightMode(): Boolean {
        val preferences = getSharedPreferences("theme_prefs", MODE_PRIVATE)
        return preferences.getBoolean("is_light_mode", true) // Assuming default is true (light mode)
    }

    fun setLocale(activity: Activity, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(activity.resources.configuration)
        config.setLocale(locale)

        activity.resources.updateConfiguration(
            config, activity.resources.displayMetrics
        )

        // Store the selected language in Shared Preferences or another persistent storage for subsequent launches
        val sharedPreferences = activity.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("app_language", languageCode)
        editor.apply()

        activity.recreate() // Recreate the activity to apply changes
    }

}

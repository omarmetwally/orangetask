package com.omar.orangetask.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.omar.orangetask.R
import com.omar.orangetask.db.ArticleDatabase
import com.omar.orangetask.repository.NewsRepository
import kotlinx.android.synthetic.main.activity_news.*
import java.util.Locale

class NewsActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel
    var flagMode = true
     var  flagLang="en"

    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        //to get last User Settings for Language
        flagLang=isLang()!!
        if(flagLang=="en")
        {
            setLocale(this, "en",false)
            floatinglang.setImageResource(R.drawable.toarabic)

        }
        else{
            setLocale(this, "ar",false)
            floatinglang.setImageResource(R.drawable.toenglish)

        }

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application, newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)
        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())




        //to get last User Settings for Dark Mode
        when (getThemeChoice()) {
            "SYSTEM" ->{
                // Check current system mode to decide which mode to set
                if (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES) {
                    saveThemeChoice("LIGHT")
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    floatingthemeswitch.setImageResource(R.drawable.thumbtrue)
                    setTheme(R.style.AppTheme)
                } else {
                    saveThemeChoice("DARK")
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    floatingthemeswitch.setImageResource(R.drawable.thumbfalse)
                    setTheme(R.style.darkTheme)
                }
            }
            "LIGHT" -> {
                saveThemeChoice("SYSTEM")
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                floatingthemeswitch.setImageResource(R.drawable.thumbtrue)
                setTheme(R.style.AppTheme)
            }
            "DARK" -> {
                saveThemeChoice("SYSTEM")
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                floatingthemeswitch.setImageResource(R.drawable.thumbfalse)
                setTheme(R.style.darkTheme)
            }
        }


        //For language button(Floating)
        floatinglang.setOnClickListener {
            if (flagLang=="en"){
                floatinglang.setImageResource(R.drawable.toarabic)
                saveFlagLangState("ar")
                setLocale(this, "ar")

            }
            else{
                floatinglang.setImageResource(R.drawable.toarabic)
                saveFlagLangState("en")
                setLocale(this, "en")

            }

        }

        //For Dark&Light mode (Floating & System Setting)
        floatingthemeswitch.setOnClickListener {
            when (getThemeChoice()) {
                "SYSTEM" -> {
                    // Check current system mode to decide which mode to set
                    if (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES) {
                        saveThemeChoice("LIGHT")
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        floatingthemeswitch.setImageResource(R.drawable.thumbtrue)
                        setTheme(R.style.AppTheme)
                    } else {
                        saveThemeChoice("DARK")
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        floatingthemeswitch.setImageResource(R.drawable.thumbfalse)
                        setTheme(R.style.darkTheme)
                    }
                }
                "LIGHT" -> {
                    saveThemeChoice("SYSTEM")
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    floatingthemeswitch.setImageResource(R.drawable.thumbtrue)
                    setTheme(R.style.AppTheme)
                }
                "DARK" -> {
                    saveThemeChoice("SYSTEM")
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    floatingthemeswitch.setImageResource(R.drawable.thumbfalse)
                    setTheme(R.style.darkTheme)
                }
            }
        }






    }





    private fun saveThemeChoice(choice: String) {
        val preferences = getSharedPreferences("theme_prefs", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("theme_choice", choice)
        editor.apply()
    }

    private fun getThemeChoice(): String {
        val preferences = getSharedPreferences("theme_prefs", MODE_PRIVATE)
        return preferences.getString("theme_choice", "SYSTEM") ?: "SYSTEM"
    }

    private fun saveFlagLangState(isLang: String) {
        val sharedPreferences = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("app_language", isLang)
        editor.apply()
    }

    private fun isLang(): String? {
        val sharedPreferences = getSharedPreferences("app_settings", MODE_PRIVATE)
        var lang= sharedPreferences.getString("app_language", "en")
        return lang
    }







    fun setLocale(activity: Activity, languageCode: String,check:Boolean=true) {

        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration(activity.resources.configuration)
        config.setLocale(locale)
        activity.resources.updateConfiguration(
            config, activity.resources.displayMetrics
        )

       if(check){

            finish()
            activity.startActivity(intent)



       }

    }



}

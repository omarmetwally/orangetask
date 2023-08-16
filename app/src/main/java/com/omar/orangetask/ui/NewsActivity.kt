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
        flagLang=isLang()!!
        if(flagLang=="en")
        {
            setLocale(this, "en")
        }
        else{
            setLocale(this, "ar")
        }

        setContentView(R.layout.activity_news)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application, newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)
        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())





        flagMode = isLightMode()
        if (flagMode) {
            floatingthemeswitch.setImageResource(R.drawable.thumbtrue)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            setTheme(R.style.AppTheme)

        } else {
            floatingthemeswitch.setImageResource(R.drawable.thumbfalse)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.darkTheme)
        }



        //For language button(Floating)
        floatinglang.setOnClickListener {
            if (flagLang=="en"){
                setLocale(this, "ar")
                saveFlagLangState("ar")
            }
            else{
                setLocale(this, "en")
                saveFlagLangState("en")
            }

        }

        //For Dark&Light mode button(Floating)
        floatingthemeswitch.setOnClickListener {

            if (flagMode) {
                floatingthemeswitch.setImageResource(R.drawable.thumbfalse)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                setTheme(R.style.darkTheme)
                flagMode = false
                saveFlagState(false)
            } else {
                floatingthemeswitch.setImageResource(R.drawable.thumbtrue)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                setTheme(R.style.AppTheme)
                flagMode = true
                saveFlagState(true)
            }

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
        return preferences.getBoolean(
            "is_light_mode",
            true
        )
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







    fun setLocale(activity: Activity, languageCode: String) {

        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration(activity.resources.configuration)
        config.setLocale(locale)
        activity.resources.updateConfiguration(
            config, activity.resources.displayMetrics
        )
          activity.recreate() // Recreate the activity to apply changes
        //restartApp(activity)


    }

    fun restartApp(context: Context) {
        val intent = context.packageManager
            .getLaunchIntentForPackage(context.packageName)
        val componentName = intent!!.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)
        context.startActivity(mainIntent)
        System.exit(0)  // This is used to kill the current instance of the app
    }

}
